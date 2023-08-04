package nl.patrickkik.monjun.domain;

import nl.patrickkik.monjun.domain.space.Amusement;

public class Realtor {
    private final Board board;

    public Realtor(Board board) {
        this.board = board;
    }

    public boolean isAvailable(Amusement amusement) {
        return amusement.getOwner() == null;
    }

    public boolean isMine(Amusement amusement, Player player) {
        return amusement.getOwner() == player;
    }

    public int getRent(Amusement amusement, Player player) {
        if (isAvailable(amusement)) {
            return 0;
        }
        if (isMine(amusement, player)) {
            return 0;
        }
        int rent = amusement.getValue();
        Amusement sibling = (Amusement) board.getSpaces().get(amusement.getMatchingPosition());
        if (sibling.getOwner() == amusement.getOwner()) {
            rent *= 2;
        }
        return rent;
    }
}
