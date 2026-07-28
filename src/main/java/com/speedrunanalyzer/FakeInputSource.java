package com.speedrunanalyzer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.function.Consumer;

// This class simulates an input source
public class FakeInputSource implements InputSource {

    private Timeline timeline;
    private long frame = 100;

    //Starts the timeline and generates fake input data every 250 milliseconds,
    // calling the provided onFrame consumer with the generated FrameInput.
    @Override
    public void start(Consumer<FrameInput> onFrame) {

        timeline = new Timeline(
                new KeyFrame(Duration.millis(250), event -> {

                    FrameInput input = createFakeFrame(frame);

                    onFrame.accept(input);

                    frame++;
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    //Stops the timeline
    @Override
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private FrameInput createFakeFrame(long frame) {

        boolean right = true;

        // Fake A presses every few frames
        boolean a = frame % 6 == 0 || frame % 6 == 1;

        // Fake B held most of the time
        boolean b = frame % 10 < 7;

        //returns a new FrameInput with right always pressed,
        // A and B pressed based on the frame number,
        // and all other buttons not pressed.
        return new FrameInput(
                frame,
                false, // up
                false, // down
                false, // left
                right,
                a,
                b,
                false, // start
                false  // select
        );
    }
}