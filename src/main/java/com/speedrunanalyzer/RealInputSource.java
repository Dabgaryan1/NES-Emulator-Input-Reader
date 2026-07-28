package com.speedrunanalyzer;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

// This class reads input data from a CSV file provided by the emulator through Lua scripting
public class RealInputSource implements InputSource {
    private final Path filePath;
    private RandomAccessFile file;  //allows us to remember our position in the file and read new lines as they are added
    private ScheduledExecutorService executor;
    private long position = 0;

    //constructor that takes a Path to the CSV file
    public RealInputSource(Path filePath) {
        this.filePath = filePath;
    }

    //Starts reading the CSV file at regular intervals (every 10 milliseconds)
    // and calls the provided onFrame consumer with new FrameInput data
    @Override
    public void start(Consumer<FrameInput> onFrame) {
        try {
            file = new RandomAccessFile(filePath.toFile(), "r");

            //skip the csv header
            file.readLine();
            position = file.getFilePointer();   //stores where we stopped reading
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
            return;
        }
        // Schedule a task to read new frames every 10 milliseconds
        executor = Executors.newSingleThreadScheduledExecutor();    //creates a background thread
        executor.scheduleAtFixedRate(
                () -> readNewFrames(onFrame),
                0,
                10,
                TimeUnit.MILLISECONDS
        );
    }

    //Stops reading the CSV file and shuts down the executor
    @Override
    public void stop() {
        if (executor != null) {
            executor.shutdown();
        }
        if (file != null) {
            try {
                file.close();
            } catch (IOException e) {
                System.err.println("Error closing file: " + e.getMessage());
            }
        }
    }

    //Reads new frames from the CSV file and calls
    // the provided onFrame consumer with new FrameInput data
    private void readNewFrames(Consumer<FrameInput> onFrame) {
        try {
            //Lua opened the file with "w" so restarting the script
            //can truncate it. Reset if that happens
            if (file.length() < position) {
                file.seek(0);
                file.readLine(); //skip header again
                position = file.getFilePointer();
            }
            //seek to the last read position and read new lines
            file.seek(position);
            String line;

            while ((line = file.readLine()) != null) {
                FrameInput frameInput = parseLine(line);
                if (frameInput != null) {
                    onFrame.accept(frameInput);
                }
                position = file.getFilePointer();
            }
        }   catch (IOException e) {
            System.err.println("Error reading Emulator Inputs: " + e.getMessage());
        }
    }

    //Parses a line from the CSV file and returns a FrameInput object
    private FrameInput parseLine(String line) {
        String[] parts = line.split(",");

        if (parts.length != 9) {
            return null;
        }

        try {
            long frame = Long.parseLong(parts[0]);
            // Convert "1" to true and "0" to false for button states
            boolean up = parts[1].equals("1");
            boolean down = parts[2].equals("1");
            boolean left = parts[3].equals("1");
            boolean right = parts[4].equals("1");
            boolean a = parts[5].equals("1");
            boolean b = parts[6].equals("1");
            boolean start = parts[7].equals("1");
            boolean select = parts[8].equals("1");

            // Return a new FrameInput object with the parsed data
            return new FrameInput(frame, up, down, left, right, a, b, start, select);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
