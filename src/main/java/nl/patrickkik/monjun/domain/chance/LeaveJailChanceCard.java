package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;

public class LeaveJailChanceCard extends ChanceCard {
    public LeaveJailChanceCard() {
        super(0, "Verlaat de gevangenis zonder te betalen. Bewaar deze kaart tot je hem nodig hebt.");
    }

    @Override
    public void action(Game game, Player player) {
        player.setHasGetOutOfJailCard(true);
    }

    @Override
    public boolean putBackInDeck() {
        return false;
    }
}
