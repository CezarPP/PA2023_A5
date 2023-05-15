package com.example.lab11;

import com.example.lab11.game.GameEngine;
import com.example.lab11.game.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayersController {

    @GetMapping("/players")
    public List<Player> getPlayerList() {
        return GameEngine.getInstance().getPlayerList();
    }
/*
    @PostMapping("/players")
    public void addPlayer(@RequestBody Player player) {
        GameEngine.getInstance().addPlayer(player);
    }*/
}
