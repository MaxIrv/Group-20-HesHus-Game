package com.skloch.game.events;

/**
 * The EnergyUpdatedEvent class represents an event that is triggered when the energy level is updated in the game.
 * This event carries the updated energy level.
 */
public class EnergyUpdatedEvent {
    // The updated energy level
    private final int energy;

    /**
     * Constructs a new EnergyUpdatedEvent with the specified updated energy level.
     *
     * @param energy The updated energy level.
     */
    public EnergyUpdatedEvent(int energy) {
        this.energy = energy;
    }

    /**
     * Returns the updated energy level.
     *
     * @return The updated energy level.
     */
    public int getEnergy() {
        return energy;
    }
}