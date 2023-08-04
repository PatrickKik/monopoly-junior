package nl.patrickkik.monjun.domain.space;

import lombok.Getter;

@Getter
public enum SpaceColor {
    BROWN(1),
    LIGHT_BLUE(1),
    PINK(2),
    ORANGE(2),
    RED(3),
    YELLOW(3),
    GREEN(4),
    DARK_BLUE(5);

    private final int value;

    SpaceColor(int value) {
        this.value = value;
    }
}
