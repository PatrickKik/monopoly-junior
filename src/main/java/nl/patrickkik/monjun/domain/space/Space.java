package nl.patrickkik.monjun.domain.space;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.patrickkik.monjun.domain.Board;
import nl.patrickkik.monjun.domain.Player;

@Data
@RequiredArgsConstructor
public abstract class Space {
    @NonNull int position;
    @NonNull String name;

    public abstract void action(Board board, Player player, boolean getForFree);
}
