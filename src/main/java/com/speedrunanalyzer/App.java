package com.speedrunanalyzer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.nio.file.Path;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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
        frameColumn.setStyle("-fx-alignment: CENTER;"); // Center align the frame number, repeated for all columns

        //Up button column
        TableColumn<FrameInput, String> upColumn =
                new TableColumn<>("U");

        upColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().isU() ? "U" : ""
                )
        );
        upColumn.setStyle("-fx-alignment: CENTER;");

        //Down button column
        TableColumn<FrameInput, String> downColumn =
                new TableColumn<>("D");

        downColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().isD() ? "D" : ""
                )
        );
        downColumn.setStyle("-fx-alignment: CENTER;");

        //Left button column
        TableColumn<FrameInput, String> leftColumn =
                new TableColumn<>("L");

        leftColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().isL() ? "L" : ""
                )
        );
        leftColumn.setStyle("-fx-alignment: CENTER;");

        //Right button column
        TableColumn<FrameInput, String> rightColumn =
                new TableColumn<>("R");

        rightColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().isR() ? "R" : ""
                )
        );
        rightColumn.setStyle("-fx-alignment: CENTER;");

        //A button column
        TableColumn<FrameInput, String> aColumn =
                new TableColumn<>("A");

        aColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().isA() ? "A" : ""
                )
        );
        aColumn.setStyle("-fx-alignment: CENTER;");

        //B button column
        TableColumn<FrameInput, String> bColumn =
                new TableColumn<>("B");

        bColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().isB() ? "B" : ""
                )
        );
        bColumn.setStyle("-fx-alignment: CENTER;");

        //Start button column
        TableColumn<FrameInput, String> startColumn =
                new TableColumn<>("START");

        startColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().isStart() ? "START" : ""
                )
        );
        startColumn.setStyle("-fx-alignment: CENTER;");

        //Select button column
        TableColumn<FrameInput, String> selectColumn =
                new TableColumn<>("SELECT");

        selectColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(
                        data.getValue().isSelect() ? "SELECT" : ""
                )
        );
        selectColumn.setStyle("-fx-alignment: CENTER;");

        // Add the columns to the table
        table.getColumns().addAll(frameColumn, upColumn, downColumn, leftColumn,
                rightColumn, aColumn, bColumn, startColumn, selectColumn);

        // Create an observable list to hold the FrameInput objects
        // which allows the table to automatically update when new frames are added
        ObservableList<FrameInput> frames = FXCollections.observableArrayList();


        table.setItems(frames); // Set the observable list as the items for the table


        BooleanProperty recording = new SimpleBooleanProperty(false);

        //Buttons to start, stop, and clear frame recordings
        Button startButton = new Button("Start Recording");
        Button stopButton = new Button("Stop Recording");
        Button clearButton = new Button("Clear");

        Label statusLabel = new Label("Stopped");

        //start recording frame inputs when pressed(default)
        startButton.setOnAction(event -> {
            recording.set(true);
            statusLabel.setText("Recording");
            startButton.disableProperty().bind(recording);
        });

        //stop recording frame inputs when pressed
        stopButton.setOnAction(event -> {
            recording.set(false);
            statusLabel.setText("Stopped");
            stopButton.disableProperty().bind(recording.not());
        });

        //clear table when pressed
        clearButton.setOnAction(event -> {
           frames.clear();
        });


        //save button
        Button saveButton = new Button("Save");

        //When save pressed, loop through table and write all inputs in
        //current session to a StringBuilder then save to csv file
        saveButton.setOnAction(event -> {
            StringBuilder sessionData = new StringBuilder();
            sessionData.append("Frame, Up, Down, Left, Right, A, B, Start, Select\n");
            for (FrameInput frame : table.getItems()) {
                sessionData.append(frame.getFrame()).append(",");
                sessionData.append(frame.isU() ? 1 : 0).append(",");
                sessionData.append(frame.isD() ? 1 : 0).append(",");
                sessionData.append(frame.isL() ? 1 : 0).append(",");
                sessionData.append(frame.isR() ? 1 : 0).append(",");
                sessionData.append(frame.isA() ? 1 : 0).append(",");
                sessionData.append(frame.isB() ? 1 : 0).append(",");
                sessionData.append(frame.isStart() ? 1 : 0).append(",");
                sessionData.append(frame.isSelect() ? 1 : 0).append("\n");
            }
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Save Input Session");
            fileChooser.setInitialFileName("input-session.csv");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "CSV files",
                            "*.csv"
                    )
            );

            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile == null) {
                return;
            }

            try {
                Files.writeString(
                        selectedFile.toPath(),
                        sessionData.toString(),
                        StandardCharsets.UTF_8
                );

                System.out.println("Session saved to :" + selectedFile.getAbsolutePath());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });





        // Create an instance of the input source (RealInputSource) to read input data from the CSV file
        //must change the MESEN_INPUT_PATH environment variable to the path of the CSV file generated by Mesen
        String inputPath = System.getenv("MESEN_INPUT_PATH");

        if (inputPath == null || inputPath.isBlank()) {
            throw new IllegalStateException(
                    "MESEN_INPUT_PATH environment variable is not set."
            );
        }

        InputSource inputSource = new RealInputSource(
                Path.of(inputPath)
        );

        //check if recording, if so read frames and display on table
        //scroll table along as frames are added
        inputSource.start(frame -> {
            Platform.runLater(() -> {
                if(recording.get()) {
                    frames.add(frame);
                    table.scrollTo(frames.size() - 1);
                }
            });
        });

        // Ensure that the input source is stopped when the application window is closed
        stage.setOnCloseRequest(event -> inputSource.stop());

        //hbox to display start/stop/clear buttons
        HBox controls = new HBox(
                10,
                startButton,
                stopButton,
                clearButton,
                statusLabel
        );

        //hbox to display save button
        HBox saveDisplay = new HBox(
                10,
                saveButton
        );

        saveDisplay.setAlignment(Pos.CENTER_RIGHT);
        //vbox to display rows with frames + buttons pressed info
        VBox root = new VBox(10, controls, table, saveDisplay);
        root.setPadding(new Insets(10));
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

    public static void main(String[] args) {
        launch(args);
    }
}