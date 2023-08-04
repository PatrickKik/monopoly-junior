package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;

import java.util.logging.Logger;

public class EatenTooMuchCandyChanceCard extends ChanceCard {
    private static final Logger LOGGER = Logger.getLogger(EatenTooMuchCandyChanceCard.class.getName());

    public EatenTooMuchCandyChanceCard() {
        super(6, "Je hebt te veel snoep gegeten! Betaal M2 aan de bank.");
    }

    @Override
    public void action(Game game, Player player) {
        game.transferCapital(player, game.getBank(), 2);
    }
}
