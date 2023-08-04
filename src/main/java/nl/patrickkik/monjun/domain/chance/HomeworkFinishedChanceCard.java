package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;

public class HomeworkFinishedChanceCard extends ChanceCard {
    public HomeworkFinishedChanceCard() {
        super(2, "Je hebt al je huiswerk af! Je krijgt M2 van de bank.");
    }

    @Override
    public void action(Game game, Player player) {
        game.transferCapital(game.getBank(), player, 2);
    }
}
