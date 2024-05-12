package me.butter.api.game;

public enum GameState {
    LOBBY("Lobby"),
    STARTING("Starting"),
    TELEPORTING("Teleporting"),
    IN_GAME("In Game"),
    ENDING("Ending");

    private final String name;

    GameState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
