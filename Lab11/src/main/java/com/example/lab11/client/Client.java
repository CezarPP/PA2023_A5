package com.example.lab11.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.example.lab11.game.Player;

@SpringBootApplication
public class Client {

    public static void main(String[] args) {
        SpringApplication.run(Client.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            String baseUrl = "http://localhost:8083/players";

            // Add a new player
            Player newPlayer = new Player();
            newPlayer.setId(1);
            newPlayer.setName("New Player");
            restTemplate.postForObject(baseUrl, newPlayer, Player.class);
            System.out.println("Created player: " + newPlayer);

            // Get all players
            Player[] players = restTemplate.getForObject(baseUrl, Player[].class);
            System.out.println("All players: ");
            assert players != null;
            for (Player player : players) {
                System.out.println(player);
            }

            // Update a player
            newPlayer.setName("Updated Player");
            restTemplate.put(baseUrl + "/" + newPlayer.getId(), newPlayer);

            // Delete a player
            restTemplate.delete(baseUrl + "/" + newPlayer.getId());
            System.out.println("Player deleted");
        };
    }
}
