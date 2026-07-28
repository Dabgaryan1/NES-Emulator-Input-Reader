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

        // Create an instance of the input source (in this case, a fake input source for testing)
        InputSource inputSource = new FakeInputSource();

        // Start receiving input frames from the input source. Each new frame is added to the observable list,
        // which automatically updates the table view. The table also scrolls to the newly added frame
        inputSource.start(frame -> {
            frames.add(frame);
            table.scrollTo(frame);
        });

        // Set up the JavaFX scene and stage
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

    public static void main(String[] args) {
        launch(args);
    }
}