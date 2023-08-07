package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;
import nl.patrickkik.monjun.domain.Realtor;
import nl.patrickkik.monjun.domain.space.Amusement;

import java.util.logging.Logger;

public class DoOneStepOrGetChanceCardChanceCard extends ChanceCard {
    private static final Logger LOGGER = Logger.getLogger(DoOneStepOrGetChanceCardChanceCard.class.getName());

    public DoOneStepOrGetChanceCardChanceCard() {
        super(5, "Ga 1 vakje vooruit of pak nog een kanskaart.");
    }

    @Override
    public void action(Game game, Player player) {
        switch (player.getStrategy()) {
            case BUY -> strategyBuy(game, player);
            case SAFE -> strategySafe(game, player);
        }
    }

    private static void strategyBuy(Game game, Player player) {
        Board board = game.getBoard();
        Realtor realtor = board.getRealtor();
        int position = board.getPlayerToPosition().get(player.getToken());
        int nextPosition = position + 1;
        Amusement amusement = (Amusement) board.getSpaces().get(nextPosition);
        LOGGER.info("1 vakje vooruit is %s (eigenaar: %s)".formatted(amusement.getName(), amusement.getOwner() == null ? "beschikbaar" : amusement.getOwner().getToken()));
        if (realtor.isAvailable(amusement) && amusement.getValue() <= player.getCapital()) {
            board.place(player, nextPosition, false);
        } else {
            ChanceDeck chanceDeck = board.getChanceDeck();
            chanceDeck.action(game, player);
        }
    }

    private static void strategySafe(Game game, Player player) {
        LOGGER.info("Speler %s speelt op safe.".formatted(player.getToken()));
        Board board = game.getBoard();
        ChanceDeck chanceDeck = board.getChanceDeck();
        chanceDeck.action(game, player);
    }
}
