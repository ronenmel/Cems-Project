package Server;

import java.io.IOException;
import java.net.BindException;

import gui.ServerController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerUI extends Application {
	static ServerController sc = new ServerController();
	final public static int DEFAULT_PORT = 5555;
	static EchoServer sv;
	static boolean islistening = false;

	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	public static void runServer(int p) throws IOException {
		int port = 0; // Port to listen on

		try {
			port = p; // set port to 5555 default or other number
		} catch (Throwable t) {
		}

		try {

			sv = new EchoServer(port);

			ServerLog.writeNewLine("Server listening for connections on port " + port);
			sv.listen(); // Start listening for connections
			islistening = true;

		} catch (BindException ex) {
			ServerLog.writeNewLine("port " + port + " already in use");
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerController server = new ServerController(); // create server frame
		server.start(primaryStage);

	}
	
	
}