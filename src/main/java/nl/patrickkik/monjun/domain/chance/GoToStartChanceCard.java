package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;

public class GoToStartChanceCard extends ChanceCard {
    public GoToStartChanceCard() {
        super(1, "Ga verder naar START. Je krijgt M2.");
    }

    @Override
    public void action(Game game, Player player) {
        game.getBoard().place(player, game.getBoard().START.getPosition(), false);
    }
}
