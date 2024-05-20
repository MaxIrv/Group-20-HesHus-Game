package com.skloch.game.events;

public class EnergyUpdatedEvent {
    private final int energy;

    public EnergyUpdatedEvent(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }
}
