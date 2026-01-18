package com.tictactoe.model;

public class MoveRequest {
    private int position;

    public MoveRequest() {}

    public MoveRequest(int position) {
        this.position = position;
    }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}
