package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.*;
import nl.patrickkik.monjun.domain.space.Amusement;

import java.util.Comparator;
import java.util.Optional;
import java.util.logging.Logger;

public class GoToFreeSpaceForPlayerChanceCard extends ChanceCard {
    private static final Logger LOGGER = Logger.getLogger(GoToFreeSpaceForPlayerChanceCard.class.getName());

    private final PlayerToken token;

    public GoToFreeSpaceForPlayerChanceCard(int id, PlayerToken token) {
        super(id, "Geef deze kaart aan %s en pak nog een kanskaart. %s: [ga] in je volgende beurt naar een niet verkocht vakje en koop het. Als alle vakjes verkocht zijn, koop je één vakje van een speler naar keuze!".formatted(token, token));
        this.token = token;
    }

    @Override
    public void action(Game game, Player player) {
        // Geef deze kaart aan (andere) speler
        game.getPlayers().stream()
                .filter(p -> p.getToken() == token)
                .findAny()
                .ifPresent(p -> p.setGoToFreeSpaceForPlayerChanceCard(this));

        // Pak nog een kanskaart
        game.getBoard().getChanceDeck().action(game, player);
    }

    public void nextAction(Game game, Player player) {
        switch (player.getStrategy()) {
            case BUY -> strategyBuy(game, player);
            case SAFE -> strategySafe(game, player);
        }
    }

    private static void strategyBuy(Game game, Player player) {
        // Ga naar een niet verkocht vakje en koop het
        // Als alle vakjes verkocht zijn, koop je één vakje van een speler naar keuze!
        Board board = game.getBoard();
        Realtor realtor = board.getRealtor();

        // Het duurste beschikbaar vakje dat ik kan betalen.
        Optional<Amusement> amusementOpt = board.getSpaces().stream()
                .filter(space -> space instanceof Amusement)
                .map(space -> (Amusement) space)
                .filter(realtor::isAvailable)
                .filter(amusement -> amusement.getValue() <= player.getCapital())
                .sorted(Comparator.comparingInt(Amusement::getValue).reversed())
                .findFirst();
        if (amusementOpt.isPresent()) {
            LOGGER.info("Vakje %s is beschikbaar en te betalen.".formatted(amusementOpt.get().getName()));
            board.place(player, amusementOpt.get().getPosition(), false);
            return;
        }
        LOGGER.info("Er zijn geen beschikbare vakjes");

        // Het duurste vakje van iemand anders dat ik kan betalen.
        amusementOpt = board.getSpaces().stream()
                .filter(space -> space instanceof Amusement)
                .map(space -> (Amusement) space)
                .filter(amusement -> !realtor.isMine(amusement, player))
                .filter(amusement -> amusement.getValue() <= player.getCapital())
                .sorted(Comparator.comparingInt(Amusement::getValue).reversed())
                .findFirst();
        if (amusementOpt.isPresent()) {
            Player otherPlayer = amusementOpt.get().getOwner();
            LOGGER.info("Vakje %s wordt overgekocht van speler %s.".formatted(amusementOpt.get().getName(), otherPlayer.getToken()));
            // Maak place available
            otherPlayer.removePosession(amusementOpt.get());
            amusementOpt.get().setOwner(null);
            board.place(player, amusementOpt.get().getPosition(), false);
            game.transferCapital(game.getBank(), otherPlayer, amusementOpt.get().getValue());
        }
    }

    private void strategySafe(Game game, Player player) {
        // Ga naar een niet verkocht vakje en koop het
        // Als alle vakjes verkocht zijn, koop je één vakje van een speler naar keuze!
        Board board = game.getBoard();
        Realtor realtor = board.getRealtor();

        // Het goedkoopste beschikbaar vakje dat ik kan betalen.
        Optional<Amusement> amusementOpt = board.getSpaces().stream()
                .filter(space -> space instanceof Amusement)
                .map(space -> (Amusement) space)
                .filter(realtor::isAvailable)
                .filter(amusement -> amusement.getValue() <= player.getCapital())
                .sorted(Comparator.comparingInt(Amusement::getValue))
                .findFirst();
        if (amusementOpt.isPresent()) {
            LOGGER.info("Vakje %s is beschikbaar en te betalen.".formatted(amusementOpt.get().getName()));
            board.place(player, amusementOpt.get().getPosition(), false);
            return;
        }
        LOGGER.info("Er zijn geen beschikbare vakjes");

        // Het goedkoopste vakje van iemand anders dat ik kan betalen.
        amusementOpt = board.getSpaces().stream()
                .filter(space -> space instanceof Amusement)
                .map(space -> (Amusement) space)
                .filter(amusement -> !realtor.isMine(amusement, player))
                .filter(amusement -> amusement.getValue() <= player.getCapital())
                .sorted(Comparator.comparingInt(Amusement::getValue))
                .findFirst();
        if (amusementOpt.isPresent()) {
            Player otherPlayer = amusementOpt.get().getOwner();
            LOGGER.info("Vakje %s wordt overgekocht van speler %s.".formatted(amusementOpt.get().getName(), otherPlayer.getToken()));
            // Maak place available
            otherPlayer.removePosession(amusementOpt.get());
            amusementOpt.get().setOwner(null);
            board.place(player, amusementOpt.get().getPosition(), false);
            game.transferCapital(game.getBank(), otherPlayer, amusementOpt.get().getValue());
        }
    }

    @Override
    public boolean putBackInDeck() {
        return false;
    }
}
