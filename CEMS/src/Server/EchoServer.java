package Server;
// This file contains material supporting section 3.7 of the textbook:

import java.io.IOException;

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import gui.ServerController;
import logic.Test;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;res
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************
	static Connection conn;
	public ServerController sc;
	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	public void CreateDataBase() {
		String query;
		Statement st;
		try {
			query = ("CREATE TABLE CEMS.Test(ID VARCHAR(7), Profession VARCHAR(45), Course VARCHAR(45), Duration INT, PointsPerQuestion VARCHAR(100), PRIMARY KEY(ID));");
			st = conn.createStatement();
			st.executeUpdate(query);
			ServerLog.writeNewLine("Table created: Test");
			try { // Add some exams to DB
				st = conn.createStatement();
				query = ("INSERT INTO Test (ID, Profession, Course, Duration, PointsPerQuestion) VALUES('020301', 'Math (02)', 'Algebra1 (03)', 120, '25, 25, 15, 15, 20');");
				st.executeUpdate(query);
				query = ("INSERT INTO Test (ID, Profession, Course, Duration, PointsPerQuestion) VALUES('020302', 'Math (02)', 'Algebra1 (03)', 120, '10, 10, 10, 20, 20, 30');");
				st.executeUpdate(query);
				query = ("INSERT INTO Test (ID, Profession, Course, Duration, PointsPerQuestion) VALUES('020403', 'Math (02)', 'Algebra2 (04)', 150, '25, 25, 15, 15, 20');");
				st.executeUpdate(query);
				query = ("INSERT INTO Test (ID, Profession, Course, Duration, PointsPerQuestion) VALUES('020504', 'Math (02)', 'Hedva1 (05)', 180, '12, 18, 17, 13, 20, 20');");
				st.executeUpdate(query);
				query = ("INSERT INTO Test (ID, Profession, Course, Duration, PointsPerQuestion) VALUES('010305', 'Software (01)', 'Data structures (03)', 120, '25, 35, 25, 15');");
				st.executeUpdate(query);
				query = ("INSERT INTO Test (ID, Profession, Course, Duration, PointsPerQuestion) VALUES('010406', 'Software (01)', 'Algorithms(04)', 180, '25, 25, 15, 15, 20');");
				st.executeUpdate(query);
				query = ("INSERT INTO Test (ID, Profession, Course, Duration, PointsPerQuestion) VALUES('010107', 'Software (01)', 'Malam (01)', 100, '33, 34, 33');");
				st.executeUpdate(query);
				query = ("INSERT INTO Test (ID, Profession, Course, Duration, PointsPerQuestion) VALUES('010208', 'Software (01)', 'Matam (02)', 120, '33, 34, 20, 13');");
				st.executeUpdate(query);
				query = ("INSERT INTO Test (ID, Profession, Course, Duration, PointsPerQuestion) VALUES('030109', 'Physics (03)', 'Physics1 (01)', 180, '10, 10, 10, 10, 20, 20, 20');");
				st.executeUpdate(query);
				query = ("INSERT INTO Test (ID, Profession, Course, Duration, PointsPerQuestion) VALUES('030210', 'Physics (03)', 'Physics2 (02)', 180, '20, 20, 20, 20, 10, 10');");
				st.executeUpdate(query);
			} catch (SQLException sqlException) {
				if (sqlException.getErrorCode() == 1050) { // Table already exists
					// error
					ServerLog.writeNewLine("Table already exist: Test");
				} else {
					ServerLog.writeNewLine(sqlException.getMessage());
				}
			}

		} catch (SQLException sqlException) {
			if (sqlException.getErrorCode() == 1050) { // Table already exists
				// error
				ServerLog.writeNewLine("Table already exist: Test");
			} else {
				ServerLog.writeNewLine(sqlException.getMessage());
			}
		}
	}
	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String str = (String) msg;
		List<Test> test=null;
		String[] s = str.split(" ");
		if (s[0].equals("Get")) {
			try {
				test = GetTestsFromDB();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ServerLog.writeNewLine("Message received: Show All Fields from: " + client);
			try {
				client.sendToClient(test);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (s[0].equals("Search")) {
			try {
				test = GetTestsFromDBBySearch(s[1]);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ServerLog.writeNewLine("Message received: '" + msg + "' from: " + client);
			try {
				client.sendToClient(test);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (s[0].equals("Update")) {
			int a = Integer.parseInt(s[2]);
			try {
				test = UpdateDB(s[1], a);
				ServerLog.writeNewLine("Message received: '" + s[0] + " New Duration " + s[2] + " for test ID: " + s[1]
						+ "' from " + client);
				client.sendToClient(test);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*------------------------------------------------------------------------------------------------------------------------
	 *													 Queries
	--------------------------------------------------------------------------------------------------------------------------*/

	private List<Test> UpdateDB(String IdUpdate, int newDuration) throws SQLException {
		Statement st = conn.createStatement();
		String sql = ("UPDATE Test SET Duration=" + newDuration + " WHERE ID=" + IdUpdate + ";");
		st.executeUpdate(sql);
		String sql2 = ("SELECT * FROM Test WHERE ID=" + IdUpdate + ";");
		return QueryExecute(sql2);
	}

	private List<Test> GetTestsFromDBBySearch(String string) throws SQLException {
		String sql = ("SELECT* FROM Test WHERE ID LIKE '%" + string + "%' OR Profession LIKE '%" + string+ "%' OR Course LIKE '%" + string + "%';");
		return QueryExecute(sql);
	}

	public List<Test> GetTestsFromDB() throws SQLException {
			String sql = ("SELECT * FROM Test;");
			return QueryExecute(sql);
	}

	public List<Test> QueryExecute(String query) {
		List<Test> tests = new ArrayList<>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs;
			rs = st.executeQuery(query);
			while (rs.next()) {
				String id = rs.getString("ID");
				String subject = rs.getString("Profession");
				String course = rs.getString("Course");
				int duration = rs.getInt("Duration");
				String pointPerQuestion = rs.getString("PointsPerQuestion");
				Test test = new Test(id, subject, course, duration, pointPerQuestion);
				tests.add(test);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tests;

	}

	/*
	 * -------------------------------------------------------------------------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------------------
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		ServerLog.writeNewLine("Client connected: " + client.toString());
	}

	@SuppressWarnings("deprecation")
	public void MySqlConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			ServerLog.writeNewLine("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			ServerLog.writeNewLine("Driver definition failed");
		}
		try {

			conn = DriverManager.getConnection("jdbc:mysql://localhost/CEMS?serverTimezone=IST", "root", "sk159159");

			CreateDataBase();
			ServerLog.writeNewLine("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors */
			ServerLog.writeNewLine("SQLException: " + ex.getMessage());
			ServerLog.writeNewLine("SQLState: " + ex.getSQLState());
			ServerLog.writeNewLine("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		// connect to DB
		MySqlConnection();

	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		ServerLog.writeNewLine("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there is
	 * no UI in this phase).
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			ServerLog.writeNewLine("ERROR - Could not listen for clients!");
		}
	}

}

//End of EchoServer class
