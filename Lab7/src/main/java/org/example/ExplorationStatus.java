package org.example;

public class ExplorationStatus {
    private volatile boolean explorationComplete = false;

    public boolean isExplorationComplete() {
        return explorationComplete;
    }

    public void setExplorationComplete(boolean explorationComplete) {
        this.explorationComplete = explorationComplete;
    }
}
