package Server;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import gui.ServerController;

public class ServerLog {
	static ServerController sc;

	public static void setServerController(ServerController controller) {
		sc = controller;
	}

	public static void writeNewLine(String msg) {
		String timeStamp = new SimpleDateFormat("[HH:mm:ss]  ").format(Calendar.getInstance().getTime());
		sc.AddMsgToLog(timeStamp + msg + "\n");

	}
}
