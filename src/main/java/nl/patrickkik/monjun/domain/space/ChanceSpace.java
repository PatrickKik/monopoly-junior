package nl.patrickkik.monjun.domain.space;

import lombok.EqualsAndHashCode;
import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.chance.ChanceDeck;
import nl.patrickkik.monjun.domain.Player;
import nl.patrickkik.monjun.domain.chance.ChanceCard;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@EqualsAndHashCode(callSuper = true)
public class ChanceSpace extends Space {
    private static final Logger LOGGER = Logger.getLogger(ChanceSpace.class.getName());
    private static final AtomicInteger index = new AtomicInteger(1);
    public ChanceSpace(int position) {
        super(position, "Kans[%s]".formatted(index.getAndIncrement()));
    }

    @Override
    public void action(Board board, Player player, boolean getForFree) {
        ChanceDeck chanceDeck = board.getChanceDeck();
        chanceDeck.action(board.getGame(), player);
    }
}
