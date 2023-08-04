package nl.patrickkik.monjun.domain;

import lombok.Data;
import nl.patrickkik.monjun.domain.chance.ChanceDeck;
import nl.patrickkik.monjun.domain.space.*;

import java.util.*;
import java.util.logging.Logger;

import static nl.patrickkik.monjun.domain.space.SpaceColor.*;

@Data
public class Board {
    private static final Logger LOGGER = Logger.getLogger(Board.class.getName());

    public final Space START = new Start();
    public final Space JUST_VISITING = new JustVisiting();
    public final Space FREE_PARKING = new FreeParking();
    public final Space GO_TO_JAIL = new GoToJail();

    public final Space HAMBURGERTENT = new Amusement(1, "Hamburgertent", BROWN, 2);
    public final Space PIZZERIA = new Amusement(2, "Pizzeria", BROWN, 1);
    public final Space SNOEPWINKEL = new Amusement(4, "Snoepwinkel", LIGHT_BLUE, 5);
    public final Space IJSSALON = new Amusement(5, "IJssalon", LIGHT_BLUE, 4);
    public final Space MUSEUM = new Amusement(7, "Museum", PINK, 8);
    public final Space BIBLIOTHEEK = new Amusement(8, "Bibliotheek", PINK, 7);
    public final Space SKATEBAAN = new Amusement(10, "Skatebaan", ORANGE, 11);
    public final Space ZWEMBAD = new Amusement(11, "Zwembad", ORANGE, 10);
    public final Space GAMEHAL = new Amusement(13, "Gamehal", RED, 14);
    public final Space BIOSCOOP = new Amusement(14, "Bioscoop", RED, 13);
    public final Space SPEELGOEDWINKEL = new Amusement(16, "Speelgoedwinkel", YELLOW, 17);
    public final Space DIERENWINKEL = new Amusement(17, "Dierenwinkel", YELLOW, 16);
    public final Space BOWLINGBAAN = new Amusement(19, "Bowlingbaan", GREEN, 20);
    public final Space DIERENTUIN = new Amusement(20, "Dierentuin", GREEN, 19);
    public final Space ZWEMPARADIJS = new Amusement(22, "Zwemparadijs", DARK_BLUE, 23);

    public final Space KERMIS = new Amusement(23, "Kermis", DARK_BLUE, 22);
    private final Game game;
    private final Realtor realtor;

    private Map<PlayerToken, Integer> playerToPosition = new HashMap<>();

    private List<Space> spaces = List.of(
            START,
            HAMBURGERTENT,
            PIZZERIA,
            new ChanceSpace(3),
            SNOEPWINKEL,
            IJSSALON,
            JUST_VISITING,
            MUSEUM,
            BIBLIOTHEEK,
            new ChanceSpace(9),
            SKATEBAAN,
            ZWEMBAD,
            FREE_PARKING,
            GAMEHAL,
            BIOSCOOP,
            new ChanceSpace(15),
            SPEELGOEDWINKEL,
            DIERENWINKEL,
            GO_TO_JAIL,
            BOWLINGBAAN,
            DIERENTUIN,
            new ChanceSpace(21),
            ZWEMPARADIJS,
            KERMIS
    );

    private Set<Player> jail = new HashSet<>();

    private ChanceDeck chanceDeck = new ChanceDeck();

    public Board(Game game) {
        this.game = game;
        this.realtor = new Realtor(this);
    }

    public void move(Player player, int eyes) {
        int index = playerToPosition.get(player.getToken());
        int newIndex = (index + eyes) % 24;
        place(player, newIndex, false);
    }

    public void place(Player player, int position, boolean getForFree) {
        place(player, position, getForFree, true);
    }

    public void place(Player player, int position, boolean getForFree, boolean viaStart) {
        if (viaStart) {
            int oldPosition = playerToPosition.get(player.getToken());
            if (position < oldPosition) {
                LOGGER.info("Speler %s gaat langs Start en ontvangt salaris.".formatted(player.getToken()));
                game.transferCapital(game.getBank(), player, 2);
            }
        }
        Space space = spaces.get(position);
        LOGGER.info("Speler %s staat nu op %s.".formatted(player.getToken(), space.getName()));
        playerToPosition.put(player.getToken(), position);
        action(player, space, getForFree);
    }

    private void action(Player player, Space space, boolean getForFree) {
        space.action(this, player, getForFree);
    }
}
