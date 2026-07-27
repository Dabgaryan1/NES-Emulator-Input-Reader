package com.speedrunanalyzer;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// This is the main application class for the Speedrun Input Analyzer.
public class App extends Application {

    @Override
    public void start(Stage stage) {
        TableView<FrameInput> table = new TableView<>();

        //column for displaying the frame number
        TableColumn<FrameInput, Number> frameColumn =
                new TableColumn<>("Frame");

        frameColumn.setCellValueFactory(data ->
                new ReadOnlyLongWrapper(data.getValue().getFrame())
        );

        //column for displaying the inputs in a human-readable format
        TableColumn<FrameInput, String> inputColumn =
                new TableColumn<>("Inputs");

        inputColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(formatInputs(data.getValue()))
        );

        // Add the columns to the table
        table.getColumns().addAll(frameColumn, inputColumn);

        // Create an observable list to hold the FrameInput objects
        // which allows the table to automatically update when new frames are added
        ObservableList<FrameInput> frames = FXCollections.observableArrayList();

        table.setItems(frames); // Set the observable list as the items for the table

        //fake frames to simulate input events for testing purposes
        FrameInput[] fakeFrames = {
                new FrameInput(
                        100,
                        false, false, false, true,
                        false, true,
                        false, false
                ),

                new FrameInput(
                        101,
                        false, false, false, true,
                        false, true,
                        false, false
                ),

                new FrameInput(
                        102,
                        false, false, false, true,
                        true, true,
                        false, false
                ),

                new FrameInput(
                        103,
                        false, false, false, true,
                        true, true,
                        false, false
                ),

                new FrameInput(
                        104,
                        false, false, false, true,
                        false, true,
                        false, false
                )
        };

        final int[] index = {0};    // This array is used to keep track of the current index in the fakeFrames array.

        InputAnalyzer analyzer = new InputAnalyzer();
        FrameInput[] previousFrame = {null};

        // This timeline simulates the passage of time in the application,
        // adding a new frame every second and comparing it to the
        // previous frame to detect input events.
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {

                    if (index[0] < fakeFrames.length) {

                        FrameInput current = fakeFrames[index[0]];

                        frames.add(current);

                        if (previousFrame[0] != null) {

                            for (InputEvent inputEvent :
                                    analyzer.compareFrames(previousFrame[0], current)) {

                                System.out.println(inputEvent);
                            }
                        }

                        previousFrame[0] = current;

                        index[0]++;
                    }

                })
        );

        timeline.setCycleCount(fakeFrames.length);
        timeline.play();

        VBox root = new VBox(table);
        Scene scene = new Scene(root, 800, 500);

        stage.setTitle("Speedrun Input Analyzer");
        stage.setScene(scene);
        stage.show();
    }

    // This method formats the inputs of a FrameInput object into a string representation.
    private String formatInputs(FrameInput input) {
        StringBuilder sb = new StringBuilder();
        if (input.isU()) sb.append("U ");
        if (input.isD()) sb.append("D ");
        if (input.isL()) sb.append("L ");
        if (input.isR()) sb.append("R ");
        if (input.isA()) sb.append("A ");
        if (input.isB()) sb.append("B ");
        if (input.isStart()) sb.append("Start ");
        if (input.isSelect()) sb.append("Select ");
        return sb.toString().trim();
    }

    //main method launches the application
    public static void main(String[] args) {
        launch(args);
    }
}