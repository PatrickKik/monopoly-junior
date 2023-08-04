package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;
import nl.patrickkik.monjun.domain.PlayerToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static nl.patrickkik.monjun.domain.space.SpaceColor.*;

public class ChanceDeck {
    private static final Logger LOGGER = Logger.getLogger(ChanceDeck.class.getName());

    private final List<ChanceCard> deck = new ArrayList<>(List.of(
            new LeaveJailChanceCard(),
            new GoToStartChanceCard(),
            new HomeworkFinishedChanceCard(),
            new ItsYourBirthdayChanceCard(),
            new DoUpToFiveStepsChanceCard(),
            new DoOneStepOrGetChanceCardChanceCard(),
            new EatenTooMuchCandyChanceCard(),
            new GoToKermisChanceCard(),
            new GoToSkatebaanChanceCard(),
            new GoToFreeSpaceChanceCard(9, List.of(RED)),
            new GoToFreeSpaceChanceCard(10, List.of(BROWN, YELLOW)),
            new GoToFreeSpaceChanceCard(11, List.of(ORANGE, GREEN)),
            new GoToFreeSpaceChanceCard(12, List.of(ORANGE)),
            new GoToFreeSpaceChanceCard(13, List.of(PINK, DARK_BLUE)),
            new GoToFreeSpaceChanceCard(14, List.of(LIGHT_BLUE)),
            new GoToFreeSpaceChanceCard(15, List.of(LIGHT_BLUE, RED)),
            new GoToFreeSpaceForPlayerChanceCard(16, PlayerToken.CAR),
            new GoToFreeSpaceForPlayerChanceCard(17, PlayerToken.CAT),
            new GoToFreeSpaceForPlayerChanceCard(18, PlayerToken.BOAT),
            new GoToFreeSpaceForPlayerChanceCard(19, PlayerToken.DOG)
    ));

    public ChanceDeck() {
        Collections.shuffle(deck);
    }

    private ChanceCard get() {
        return deck.remove(0);
    }

    public void put(ChanceCard card) {
        deck.add(card);
    }

    public void action(Game game, Player player) {
        ChanceCard card = get();
        LOGGER.info("Speler %s trekt kanskaart %s.".formatted(player.getToken(), card.getDescription()));
        card.action(game, player);
        if (card.putBackInDeck()) {
            put(card);
        }
    }
}
