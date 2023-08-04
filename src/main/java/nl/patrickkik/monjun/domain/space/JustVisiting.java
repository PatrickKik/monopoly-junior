package nl.patrickkik.monjun.domain.space;

import lombok.EqualsAndHashCode;
import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.Player;

@EqualsAndHashCode(callSuper = true)
public class JustVisiting extends Space {
    public JustVisiting() {
        super(6, "Slechts op bezoek");
    }

    @Override
    public void action(Board board, Player player, boolean getForFree) {
        // Do nothing
    }
}
