package com.tictactoe.model;

public class GameConfig {
    private int boardSize;
    private int winLength;
    private int humanPlayers;

    public GameConfig() {
        this.boardSize = 3;
        this.winLength = 3;
        this.humanPlayers = 0;
    }

    public int getBoardSize() { return boardSize; }
    public void setBoardSize(int boardSize) { this.boardSize = boardSize; }

    public int getWinLength() { return winLength; }
    public void setWinLength(int winLength) { this.winLength = winLength; }

    public int getHumanPlayers() { return humanPlayers; }
    public void setHumanPlayers(int humanPlayers) { this.humanPlayers = humanPlayers; }
}
