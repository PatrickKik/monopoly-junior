package nl.patrickkik.monjun.domain.chance;

import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.Game;
import nl.patrickkik.monjun.domain.Player;
import nl.patrickkik.monjun.domain.Realtor;
import nl.patrickkik.monjun.domain.space.Amusement;
import nl.patrickkik.monjun.domain.space.Space;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class DoUpToFiveStepsChanceCard extends ChanceCard {
    private static final Logger LOGGER = Logger.getLogger(DoUpToFiveStepsChanceCard.class.getName());

    public DoUpToFiveStepsChanceCard() {
        super(4, "Ga tot 5 vakjes verder.");
    }

    @Override
    public void action(Game game, Player player) {
        Board board = game.getBoard();
        Realtor realtor = board.getRealtor();
        int oldPosition = board.getPlayerToPosition().get(player.getToken());
        List<Space> spaces = IntStream.rangeClosed(1, 5)
                .map(i -> (oldPosition + i) % 24)
                .mapToObj(position -> board.getSpaces().get(position))
                .peek(space -> LOGGER.info("Vakje %s %s".formatted(space.getName(), space instanceof Amusement a ? "(%s, eigenaar: %s)".formatted(a.getValue(), a.getOwner() == null ? "beschikbaar" : a.getOwner().getToken()) : "")))
                .toList();

        // TODO alternatief; eerst 'dorp' compleet krijgen

        // Duurste vakje kopen dat ik kan betalen
        Optional<Amusement> amusementOpt = spaces.stream()
                .filter(space -> space instanceof Amusement)
                .map(space -> (Amusement) space)
                .filter(realtor::isAvailable)
                .filter(amusement -> amusement.getValue() <= player.getCapital())
                .sorted(Comparator.comparingInt(Amusement::getValue).reversed())
                .findFirst();
        if (amusementOpt.isPresent()) {
            LOGGER.info("Gekozen voor vakje: %s".formatted(amusementOpt.get().getName()));
            board.place(player, amusementOpt.get().getPosition(), true);
            return;
        }

        // Geen van de vakjes kon ik kopen. Blijft het 2e vakje over.
        // Dat is altijd een van de hoeken en dus relatief veilig.\
        LOGGER.info("Geen vakjes om te kopen.");
        board.place(player, spaces.get(2).getPosition(), false);
    }
}
