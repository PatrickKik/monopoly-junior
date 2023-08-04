package nl.patrickkik.monjun.domain.space;

import lombok.EqualsAndHashCode;
import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.Player;

@EqualsAndHashCode(callSuper = true)
public class FreeParking extends Space {
    public FreeParking() {
        super(12, "Vrij parkeren");
    }

    @Override
    public void action(Board board, Player player, boolean getForFree) {
        // Do nothing
    }
}
