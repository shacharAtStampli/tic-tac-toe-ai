package com.tictactoe.model;

public class PlayerConfig {
    private boolean isHuman;
    private String name;
    private String description;

    public PlayerConfig() {}

    public PlayerConfig(boolean isHuman, String name, String description) {
        this.isHuman = isHuman;
        this.name = name;
        this.description = description;
    }

    public boolean isHuman() { return isHuman; }
    public void setHuman(boolean human) { isHuman = human; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
