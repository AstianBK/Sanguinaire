package com.TBK.sanguinaire.server.manager;

public class RegenerationInstance {
    private int regerationTimerRemaining;
    private final int regerationTimer;

    public RegenerationInstance(int regerationTimer) {
        this.regerationTimer = regerationTimer;
        this.regerationTimerRemaining = regerationTimer;
    }

    public RegenerationInstance(int regerationTimer, int regerationTimerRemaining) {
        this.regerationTimer = regerationTimer;
        this.regerationTimerRemaining = regerationTimerRemaining;
    }

    public void decrement() {
        regerationTimerRemaining--;
    }

    public void decrementBy(int amount) {
        regerationTimerRemaining -= amount;
    }

    public int getRegerationTimerRemaining() {
        return regerationTimerRemaining;
    }

    public int getRegerationTimer() {
        return regerationTimer;
    }

    public float getCooldownPercent() {
        if (regerationTimerRemaining == 0) {
            return 0;
        }

        return regerationTimerRemaining / (float) regerationTimer;
    }
    public void resetTimer(){
        this.regerationTimerRemaining=this.regerationTimer;
    }
}
