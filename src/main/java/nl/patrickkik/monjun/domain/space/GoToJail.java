package nl.patrickkik.monjun.domain.space;

import lombok.EqualsAndHashCode;
import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.Player;

import java.util.logging.Logger;

@EqualsAndHashCode(callSuper = true)
public class GoToJail extends Space {
    private static final Logger LOGGER = Logger.getLogger(GoToJail.class.getName());

    public GoToJail() {
        super(18, "Ga naar gevangenis");
    }

    @Override
    public void action(Board board, Player player, boolean getForFree) {
        LOGGER.info("Speler %s gaat naar de gevangenis.".formatted(player.getToken()));
        board.place(player, board.JUST_VISITING.getPosition(), false, false);
        board.getJail().add(player);
    }
}
