package nl.patrickkik.monjun.domain.chance;

import lombok.Getter;
import nl.patrickkik.monjun.domain.*;
import nl.patrickkik.monjun.domain.space.Amusement;
import nl.patrickkik.monjun.domain.space.SpaceColor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Getter
public class GoToFreeSpaceChanceCard extends ChanceCard {
    private final static Logger LOGGER = Logger.getLogger(GoToFreeSpaceChanceCard.class.getName());

    private final List<SpaceColor> spaceColors;

    public GoToFreeSpaceChanceCard(int id, List<SpaceColor> spaceColors) {
        super(id, buildDescription(spaceColors));
        this.spaceColors = spaceColors;
    }

    private static String buildDescription(List<SpaceColor> spaceColors) {
        return "Gratis vakje! Ga verder naar een %s vakje. Als er een onverkocht vakje is, krijg je het gratis! Zo niet, dan moet je de eigenaar huur betalen."
                .formatted(
                        spaceColors.size() == 1
                                ? spaceColors.get(0)
                                : "%s of %s".formatted(spaceColors.get(0), spaceColors.get(1))
                );
    }

    @Override
    public void action(Game game, Player player) {
        Board board = game.getBoard();
        Realtor realtor = board.getRealtor();
        List<Amusement> amusements = board.getSpaces().stream()
                .filter(space -> space instanceof Amusement)
                .map(space -> (Amusement) space)
                .filter(amusement -> spaceColors.contains(amusement.getColor()))
                .peek(amusement -> LOGGER.info("%s (%s, %s) eigenaar: %s".formatted(amusement.getName(), amusement.getColor(), amusement.getValue(), amusement.getOwner() == null ? "beschikbaar" : amusement.getOwner().getToken())))
                .toList();
        Optional<Amusement> amusementOpt = amusements.stream()
                .filter(realtor::isAvailable)
                .filter(amusement -> amusement.getValue() <= player.getCapital())
                .sorted(Comparator.comparingInt(Amusement::getValue).reversed())
                .findFirst();
        if (amusementOpt.isPresent()) {
            LOGGER.info("Gekozen voor gratis vakje: %s".formatted(amusementOpt.get().getName()));
            board.place(player, amusementOpt.get().getPosition(), true);
            return;
        }
        LOGGER.info("Geen beschikbare vakjes.");
        Amusement amusement = amusements.stream()
                .map(a -> Map.entry(a, realtor.getRent(a, player)))
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(amusements.get(0));
        LOGGER.info("Gekozen voor: %s".formatted(amusement.getName()));
        board.place(player, amusement.getPosition(), false);
    }
}
