package nl.patrickkik.monjun.domain;

import lombok.Data;

@Data
public class GameStats {
    private int turns;
    private Player winner;
}
