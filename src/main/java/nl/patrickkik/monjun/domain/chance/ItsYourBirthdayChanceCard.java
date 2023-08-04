package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;

import java.util.logging.Logger;

public class ItsYourBirthdayChanceCard extends ChanceCard {
    private static final Logger LOGGER = Logger.getLogger(ItsYourBirthdayChanceCard.class.getName());

    public ItsYourBirthdayChanceCard() {
        super(3, "Je bent jarig! Alle spelers geven je M1. Gefeliciteerd!");
    }

    @Override
    public void action(Game game, Player player) {
        for (Player otherPlayer : game.getPlayers()) {
            if (player != otherPlayer) {
                game.transferCapital(otherPlayer, player, 1);
            }
        }
    }
}
