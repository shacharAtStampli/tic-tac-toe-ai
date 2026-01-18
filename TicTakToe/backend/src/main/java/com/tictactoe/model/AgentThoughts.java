package com.tictactoe.model;

import java.util.ArrayList;
import java.util.List;

public class AgentThoughts {
    private List<Thought> thoughts;
    private int chosenMove;
    private String reason;

    public AgentThoughts() {
        this.thoughts = new ArrayList<>();
    }

    public void addThought(String text, String type) {
        thoughts.add(new Thought(text, type));
    }

    public List<Thought> getThoughts() { return thoughts; }
    public void setThoughts(List<Thought> thoughts) { this.thoughts = thoughts; }

    public int getChosenMove() { return chosenMove; }
    public void setChosenMove(int chosenMove) { this.chosenMove = chosenMove; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public static class Thought {
        private String text;
        private String type;

        public Thought() {}

        public Thought(String text, String type) {
            this.text = text;
            this.type = type;
        }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
}
