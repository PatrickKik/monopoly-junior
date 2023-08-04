package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;

public class GoToKermisChanceCard extends ChanceCard {
    public GoToKermisChanceCard() {
        super(7, "Ga verder naar de Kermis.");
    }

    @Override
    public void action(Game game, Player player) {
        game.getBoard().place(player, game.getBoard().KERMIS.getPosition(), false);
    }
}
