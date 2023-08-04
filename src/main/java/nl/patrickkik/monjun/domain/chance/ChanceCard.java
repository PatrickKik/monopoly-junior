package nl.patrickkik.monjun.domain.chance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;

@Getter
@AllArgsConstructor
public abstract class ChanceCard {
    private int id;
    private String description;

    public abstract void action(Game game, Player player);

    public boolean putBackInDeck() {
        return true;
    }

}
