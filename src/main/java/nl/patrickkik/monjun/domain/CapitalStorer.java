package nl.patrickkik.monjun.domain;

import lombok.Getter;

@Getter
public abstract class CapitalStorer {
    private int capital;

    public void addCapital(int amount) {
        capital += amount;
    }

    public abstract String getName();
}
