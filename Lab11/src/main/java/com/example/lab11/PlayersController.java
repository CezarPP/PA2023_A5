package com.example.lab11;

import com.example.lab11.game.GameEngine;
import com.example.lab11.game.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayersController {

    @GetMapping
    public List<Player> getPlayerList() {
        return GameEngine.getInstance().getPlayerList();
    }

    @PostMapping()
    public void addPlayer(@RequestBody Player player) {
        GameEngine.getInstance().addPlayer(player);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable int id, @RequestBody Player playerDetails) {
        Player player = GameEngine.getInstance().findPlayerById(id);
        if (player != null) {
            player.setName(playerDetails.getName());
            return ResponseEntity.ok().body(player);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable int id) {
        Player player = GameEngine.getInstance().findPlayerById(id);
        if (player != null) {
            GameEngine.getInstance().deletePlayer(player);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
