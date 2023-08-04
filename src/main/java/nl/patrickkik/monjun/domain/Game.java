package nl.patrickkik.monjun.domain;

import lombok.Getter;
import lombok.Setter;
import nl.patrickkik.monjun.domain.chance.GoToFreeSpaceForPlayerChanceCard;
import nl.patrickkik.monjun.domain.chance.LeaveJailChanceCard;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
public class Game {
    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private boolean started = false;
    private List<Player> players;
    private Bank bank;
    private int nextPlayer;
    private Board board;
    private int turns = 1;
    private boolean end = false;
    private Player loser;

    public Game(int nr) {
        LOGGER.info("Nieuw spel wordt gestart: %d".formatted(nr));
    }

    public GameStats start(List<Player> players) {
        // Init domain model
        board = new Board(this);
        this.players = players;
        bank = new Bank();
        bank.addCapital(90);

        // Give players their starting capital
        // Place players on START
        int startCapital = 24 - 2 * players.size();
        players.forEach(player -> {
            transferCapital(bank, player, startCapital);
            board.place(player, 0, true, false);
        });

        // It's the first player
        nextPlayer = 0;

        while (playTurn()) {
            turns++;
        }

        GameStats gameStats = new GameStats();
        gameStats.setTurns(turns);
        LOGGER.info("Er zijn %s beurten gespeeld.".formatted(turns));

        players.forEach(player -> {
            LOGGER.info("Het kapitaal van speler %s is M%s, waarde %s eigendommen: M%d.".formatted(player.getToken(), player.getCapital(), player.getPosessions().size(), player.getValueOfPosessions()));
        });

        Player winner = getWinner();
        LOGGER.info("Winnaar: %s!".formatted(winner.getToken()));
        gameStats.setWinner(winner);

        return gameStats;
    }

    private Player getWinner() {
        return players.stream()
                .filter(p -> p != loser)
                .sorted(
                        Comparator.comparingInt(Player::getCapital)
                                .thenComparing(Player::getValueOfPosessions)
                                .reversed()
                )
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    public boolean playTurn() {
        Player player = players.get(nextPlayer);
        LOGGER.info("Speler %s is aan de beurt.".formatted(player.getToken()));

        // Get out of jail
        if (board.getJail().contains(player)) {
            LOGGER.info("Speler %s zit in de gevangenis.".formatted(player.getToken()));
            if (player.isHasGetOutOfJailCard()) {
                LOGGER.info("Speler %s speelt de kanskaart en verlaat de gevangenis.".formatted(player.getToken()));
                board.getChanceDeck().put(new LeaveJailChanceCard()); // ugly
                player.setHasGetOutOfJailCard(false);
                board.getJail().remove(player);
            } else {
                if (transferCapital(player, bank, 1)) {
                    board.getJail().remove(player);
                    LOGGER.info("Speler %s heeft zijn boete betaald en zit niet meer in de gevangenis. Nieuw kapitaal: M%s.".formatted(player.getToken(), player.getCapital()));
                }
            }
        }

        GoToFreeSpaceForPlayerChanceCard goToFreeSpaceForPlayerChanceCard = player.getGoToFreeSpaceForPlayerChanceCard();
        if (goToFreeSpaceForPlayerChanceCard != null) {
            LOGGER.info("Speler %s speelt zijn 'ga naar een niet verkocht vakje'-kanskaart.".formatted(player.getToken()));
            goToFreeSpaceForPlayerChanceCard.nextAction(this, player);
            board.getChanceDeck().put(goToFreeSpaceForPlayerChanceCard); // ugly
            player.setGoToFreeSpaceForPlayerChanceCard(null);
        } else {
            // Roll die
            int eyes = (int) (Math.random() * 6 + 1);
            LOGGER.info("Speler %s heeft %s gegooid.".formatted(player.getToken(), eyes));

            // Move player to place
            board.move(player, eyes);
        }

        if (end) {
            return false;
        }

        // Next player
        nextPlayer = (nextPlayer + 1) % players.size();

        return true;
    }

    public void endWithBankruptcy(Player player) {
        end = true;
        loser = player;
    }

    private void endWithEmptyBank() {
        // TODO
        end = true;
    }

    public boolean transferCapital(CapitalStorer from, CapitalStorer to, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(Integer.toString(amount));
        }

        if (from.getCapital() < amount) {
            LOGGER.warning("%s kan geen M%s betalen aan %s!".formatted(from.getName(), amount, to.getName()));
            if (from instanceof Player player) {
                endWithBankruptcy(player);
            } else {
                endWithEmptyBank();
            }
            return false;
        }

        from.addCapital(-1 * amount);
        to.addCapital(amount);

        LOGGER.info("Speler %s heeft M%d betaald aan %s.".formatted(from.getName(), amount, to.getName()));
        LOGGER.info("Nieuw kapitaal speler %s: M%d.".formatted(from.getName(), from.getCapital()));
        LOGGER.info("Nieuw kapitaal speler %s: M%d.".formatted(to.getName(), to.getCapital()));

        return true;
    }
}
