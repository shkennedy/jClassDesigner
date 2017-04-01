package testExport;

import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

public class ThreadExample {

	public static String START_TEXT;
	public static String PAUSE_TEXT;
	private Stage window;
	private BorderPane appPane;
	private FlowPane topPane;
	private Button startButton;
	private Button pauseButton;
	private ScrollPane scrollPane;
	private TextArea textArea;
	private Thread dateThread;
	private Task dateTask;
	private Thread counterThread;
	private Task counterTask;
	private boolean work;

	public void start(Stage primaryStage) {

	}

	public void startWork() {

	}

	public void pauseWork() {

	}

	public boolean doWork() {
		return false;
	}

	public void appendText(String textToAppend) {

	}

	public void sleep(int timeToSleep) {

	}

	public void initLayout() {

	}

	public void initHandlers() {

	}

	public void initWindow(Stage initPrimaryStage) {

	}

	public void initThreads() {

	}

	public static void main(String[] args) {

	}

}