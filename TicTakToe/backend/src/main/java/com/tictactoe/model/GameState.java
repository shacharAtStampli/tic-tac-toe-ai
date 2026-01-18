package com.tictactoe.model;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private String[] board;
    private int boardSize;
    private int winLength;
    private String currentPlayer;
    private boolean gameOver;
    private String winner;
    private String status;
    private int humanPlayers;
    private boolean humanGoesFirst;
    private List<Boolean> firstPlayerHistory;
    private PlayerConfig playerX;
    private PlayerConfig playerO;
    private List<int[]> winningPattern;
    private AgentThoughts lastThoughts;

    public GameState() {
        this.boardSize = 3;
        this.winLength = 3;
        this.humanPlayers = 0;
        this.firstPlayerHistory = new ArrayList<>();
        reset();
    }

    public void reset() {
        this.board = new String[boardSize * boardSize];
        for (int i = 0; i < board.length; i++) {
            board[i] = "";
        }
        this.currentPlayer = "X";
        this.gameOver = false;
        this.winner = null;
        this.status = "Game started";
        this.winningPattern = null;
        this.lastThoughts = null;
        configurePlayersForNewGame();
    }

    private void configurePlayersForNewGame() {
        if (humanPlayers == 0) {
            playerX = new PlayerConfig(false, "Strategic Agent", "Thinks ahead, blocks threats");
            playerO = new PlayerConfig(false, "Defensive Agent", "Analyzes board, counters moves");
        } else if (humanPlayers == 1) {
            decideWhoGoesFirst();
            if (humanGoesFirst) {
                playerX = new PlayerConfig(true, "You", "Click a cell to place your mark");
                playerO = new PlayerConfig(false, "AI Opponent", "Strategic AI agent");
            } else {
                playerX = new PlayerConfig(false, "AI Opponent", "Strategic AI agent");
                playerO = new PlayerConfig(true, "You", "Click a cell to place your mark");
            }
        } else {
            playerX = new PlayerConfig(true, "Player 1", "Click a cell to place your mark");
            playerO = new PlayerConfig(true, "Player 2", "Click a cell to place your mark");
        }
    }

    private void decideWhoGoesFirst() {
        List<Boolean> lastTwo = firstPlayerHistory.size() >= 2 
            ? firstPlayerHistory.subList(firstPlayerHistory.size() - 2, firstPlayerHistory.size())
            : firstPlayerHistory;
        
        if (lastTwo.size() == 2 && lastTwo.get(0).equals(lastTwo.get(1))) {
            humanGoesFirst = !lastTwo.get(0);
        } else {
            humanGoesFirst = Math.random() < 0.5;
        }
        
        firstPlayerHistory.add(humanGoesFirst);
        if (firstPlayerHistory.size() > 10) {
            firstPlayerHistory.remove(0);
        }
    }

    // Getters and Setters
    public String[] getBoard() { return board; }
    public void setBoard(String[] board) { this.board = board; }
    
    public int getBoardSize() { return boardSize; }
    public void setBoardSize(int boardSize) { 
        this.boardSize = boardSize; 
        reset();
    }
    
    public int getWinLength() { return winLength; }
    public void setWinLength(int winLength) { 
        this.winLength = Math.min(winLength, boardSize);
        reset();
    }
    
    public String getCurrentPlayer() { return currentPlayer; }
    public void setCurrentPlayer(String currentPlayer) { this.currentPlayer = currentPlayer; }
    
    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    
    public String getWinner() { return winner; }
    public void setWinner(String winner) { this.winner = winner; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getHumanPlayers() { return humanPlayers; }
    public void setHumanPlayers(int humanPlayers) { 
        this.humanPlayers = humanPlayers;
        reset();
    }
    
    public boolean isHumanGoesFirst() { return humanGoesFirst; }
    
    public PlayerConfig getPlayerX() { return playerX; }
    public PlayerConfig getPlayerO() { return playerO; }
    
    public List<int[]> getWinningPattern() { return winningPattern; }
    public void setWinningPattern(List<int[]> winningPattern) { this.winningPattern = winningPattern; }
    
    public AgentThoughts getLastThoughts() { return lastThoughts; }
    public void setLastThoughts(AgentThoughts lastThoughts) { this.lastThoughts = lastThoughts; }
}
