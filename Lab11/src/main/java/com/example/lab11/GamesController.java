package com.example.lab11;

import com.example.lab11.game.Game;
import com.example.lab11.game.GameEngine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GamesController {
    @GetMapping
    List<Game> getGameList() {
        return GameEngine.getInstance().getGameList();
    }
}
