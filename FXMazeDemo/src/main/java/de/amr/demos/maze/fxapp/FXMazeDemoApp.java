package de.amr.demos.maze.fxapp;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FXMazeDemoApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = createScene(primaryStage);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Maze Generation & Pathfinding");
		primaryStage.show();

	}

	private Scene createScene(Stage stage) {
		Parent root = new Pane();
		Scene scene = new Scene(root);
		return scene;
	}

}
