package com.speedrunanalyzer;

import java.util.ArrayList;
import java.util.List;

//this class contains the logic to compare two FrameInput objects
// and generate a list of InputEvent objects representing
// the changes in button states between the two frames
public class InputAnalyzer {
    public List<InputEvent> compareFrames(FrameInput previous, FrameInput current) {
        List<InputEvent> events = new ArrayList<>();

        // Calls checkButton to compare the current frame with the previous frame and check if the state has changed.
        // If the state has changed, create a new InputEvent and add it to the events list.
        checkButton(events, current.getFrame(), "Up", previous.isU(), current.isU());
        checkButton(events, current.getFrame(), "Down", previous.isD(), current.isD());
        checkButton(events, current.getFrame(), "Left", previous.isL(), current.isL());
        checkButton(events, current.getFrame(), "Right", previous.isR(), current.isR());
        checkButton(events, current.getFrame(), "A", previous.isA(), current.isA());
        checkButton(events, current.getFrame(), "B", previous.isB(), current.isB());
        checkButton(events, current.getFrame(), "Start", previous.isStart(), current.isStart());
        checkButton(events, current.getFrame(), "Select", previous.isSelect(), current.isSelect());
        return events;
    }

    // This method checks if the state of a button has changed between two frames
    // and adds an InputEvent to the list if it has.
    private void checkButton(List<InputEvent> events,
                             Long frame,
                             String button,
                             boolean previous,
                             boolean current) {
        // If the button state has changed from not pressed to pressed, add a "PRESSED" event.
        if (previous != current) {
            events.add(new InputEvent(frame, button, "PRESSED"));
        }

        // If the button state has changed from pressed to not pressed, add a "RELEASED" event.
        if (previous && !current) {
            events.add(new InputEvent(frame, button, "RELEASED"));
        }
    }
}
