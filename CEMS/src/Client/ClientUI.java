package Client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import gui.ClientController;
import gui.ServerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ClientUI extends Application {
	static ServerController sc;
	public static int port1;	
	public static ClientChatController chat; //only one instance
	public static ChatClient ch;


	public static void main(String[] args) {
	try {
			int port = 5555;
		System.out.println("Connecting to (default): " + GetMyIp() + ", port: " + port);
		ChatClient.GetClient(GetMyIp(), port);
		launch(args);
	
		} catch (ConnectException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
				Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Conecttion error");
					alert.setHeaderText(null);
					alert.setContentText("Could not connect to server.");
					alert.showAndWait();
				}
			});
	} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			System.out.println("error1");
		} catch (Exception e) {
			System.out.println("error2");
		}
	}
/*
 *----------------------------------------------------------------------------------
 *                                 Get IP
 * ---------------------------------------------------------------------------------*/

	private static String GetMyIp() {
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			return socket.getLocalAddress().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

/*
 * ---------------------------------------------------------------------------------------
 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		chat= new ClientChatController("localhost", 5555);
	
		ClientController client = new ClientController(); // create server frame
		client.start(primaryStage);

	}
	
	
}
