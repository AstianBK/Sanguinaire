package com.TBK.sanguinaire.server.manager;

public class CooldownInstance {
    private int cooldownRemaining;
    private final int powerCooldown;

    public CooldownInstance(int powerCooldown) {
        this.powerCooldown = powerCooldown;
        this.cooldownRemaining = powerCooldown;
    }

    public CooldownInstance(int powerCooldown, int cooldownRemaining) {
        this.powerCooldown = powerCooldown;
        this.cooldownRemaining = cooldownRemaining;
    }

    public void decrement() {
        cooldownRemaining--;
    }

    public void decrementBy(int amount) {
        cooldownRemaining -= amount;
    }

    public int getCooldownRemaining() {
        return cooldownRemaining;
    }

    public int getPowerCooldown() {
        return powerCooldown;
    }

    public float getCooldownPercent() {
        if (cooldownRemaining == 0) {
            return 0;
        }

        return cooldownRemaining / (float) powerCooldown;
    }
}
