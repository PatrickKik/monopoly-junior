package nl.patrickkik.monjun.domain.space;

import lombok.EqualsAndHashCode;
import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.Player;

@EqualsAndHashCode(callSuper = true)
public class Start extends Space {
    public Start() {
        super(0, "Start");
    }

    @Override
    public void action(Board board, Player player, boolean getForFree) {
        // Do nothing
    }
}
