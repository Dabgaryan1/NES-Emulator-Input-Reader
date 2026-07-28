package com.speedrunanalyzer;

import java.util.function.Consumer;

//interface for input sources, which can be implemented to provide
// input data from different sources (e.g., file, live input, etc.)
public interface InputSource {
    void start(Consumer<FrameInput> onFrame);

    void stop();
}
