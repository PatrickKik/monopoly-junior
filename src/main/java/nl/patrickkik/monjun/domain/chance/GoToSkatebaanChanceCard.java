package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;

public class GoToSkatebaanChanceCard extends ChanceCard {
    public GoToSkatebaanChanceCard() {
        super(8, "Ga verder naar de SKATEBAAN om te trucs te oefenen! Als er een onverkocht vakje is, krijg je het gratis! Zo niet, dan moet je de eigenaar huur betalen.");
    }

    @Override
    public void action(Game game, Player player) {
        game.getBoard().place(player, game.getBoard().SKATEBAAN.getPosition(), true);
    }
}
