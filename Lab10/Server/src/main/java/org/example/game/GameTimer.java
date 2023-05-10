package org.example.game;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {
    private final TimerObserver observer;
    private Timer timer;
    private int seconds;
    private final Player player;

    public GameTimer(int initialSeconds, Player player, TimerObserver observer) {
        this.seconds = initialSeconds;
        this.timer = new Timer();
        this.player = player;
        this.observer = observer;
    }

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                seconds--;
                if (seconds <= 0) {
                    timer.cancel();
                    observer.timeUp(player);
                }
            }
        }, 0, 1000); // 1 second
    }

    public void stop() {
        timer.cancel();
    }
}
