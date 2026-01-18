package com.tictactoe.controller;

import com.tictactoe.model.*;
import com.tictactoe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/state")
    public GameState getGameState() {
        return gameService.getGameState();
    }

    @PostMapping("/new")
    public GameState newGame(@RequestBody GameConfig config) {
        return gameService.newGame(config);
    }

    @PostMapping("/reset")
    public GameState resetGame() {
        return gameService.resetGame();
    }

    @PostMapping("/move")
    public GameState makeMove(@RequestBody MoveRequest move) {
        return gameService.makeMove(move.getPosition());
    }

    @PostMapping("/ai-move")
    public GameState makeAIMove() {
        return gameService.makeAIMove();
    }
}
