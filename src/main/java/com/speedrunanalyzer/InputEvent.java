package com.speedrunanalyzer;

//this class represents a change in the state of a button(pressed or released) at a specific frame
public class InputEvent {
    private final long frame;
    private final String button;
    private final String action; // "pressed" or "released"

    //constructor
    public InputEvent(long frame, String button, String action) {
        this.frame = frame;
        this.button = button;
        this.action = action;
    }

    //getters
    public long getFrame() {
        return frame;
    }

    public String getButton() {
        return button;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "Frame " + frame + ": " + button + " " + action;
    }
}
