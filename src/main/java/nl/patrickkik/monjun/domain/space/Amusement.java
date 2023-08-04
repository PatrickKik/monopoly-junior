package nl.patrickkik.monjun.domain.space;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;
import nl.patrickkik.monjun.domain.Realtor;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Amusement extends Space {
    private static final Logger LOGGER = Logger.getLogger(Amusement.class.getName());

    private final SpaceColor color;
    private final int matchingPosition;
    private final int value;

    private Player owner;

    public Amusement(int position, String name, SpaceColor color, int matchingPosition) {
        super(position, name);
        this.color = color;
        this.matchingPosition = matchingPosition;
        this.value = color.getValue();
    }

    @Override
    public void action(Board board, Player player, boolean getForFree) {
        Realtor realtor = board.getRealtor();
        Game game = board.getGame();

        if (realtor.isAvailable(this)) {
            if (player.getPosessions().size() < 12) {
                // Buy property
                LOGGER.info("Speler %s %s %s.".formatted(player.getToken(), getForFree ? "krijgt" : "koopt", this.getName()));
                int amount = this.getValue() * (getForFree ? 0 : 1);
                if (game.transferCapital(player, game.getBank(), amount)) {
                    player.addPosession(this);
                    this.setOwner(player);
                    LOGGER.info("Speler %s heeft %s gekocht en heeft nu %s eigendommen: %s.".formatted(player.getToken(), this.getName(), player.getPosessions().size(), player.getPosessions().stream().map(Amusement::getName).collect(Collectors.joining(", "))));
                }
            } else {
                LOGGER.info("Speler %s heeft al 12 eigendommen en kan %s niet meer verkrijgen.".formatted(player.getToken(), getName()));
            }
        } else {
            if (!realtor.isMine(this, player)) {
                int rent = realtor.getRent(this, player);
                LOGGER.info("Speler %s moet M%d huur betalen aan speler %s.".formatted(player.getToken(), rent, this.getOwner().getToken()));
                game.transferCapital(player, this.getOwner(), rent);
            } else {
                LOGGER.info("Speler %s is eigenaar van %s en hoeft geen huur te betalen.".formatted(player.getToken(), getName()));
            }
        }

    }
}
