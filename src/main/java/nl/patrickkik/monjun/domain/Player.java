package nl.patrickkik.monjun.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import nl.patrickkik.monjun.domain.chance.GoToFreeSpaceForPlayerChanceCard;
import nl.patrickkik.monjun.domain.space.Amusement;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class Player extends CapitalStorer {

    private @NonNull PlayerToken token;
    private @NonNull Strategy strategy;
    private List<Amusement> posessions = new ArrayList<>();
    private boolean hasGetOutOfJailCard = false;
    private GoToFreeSpaceForPlayerChanceCard goToFreeSpaceForPlayerChanceCard = null;

    public void addPosession(Amusement posession) {
        posessions.add(posession);
    }

    public void removePosession(Amusement amusement) {
        posessions.remove(amusement);
    }

    @Override
    public String getName() {
        return token.toString();
    }

    public int getValueOfPosessions() {
        return posessions.stream()
                .mapToInt(Amusement::getValue)
                .sum();
    }
}
