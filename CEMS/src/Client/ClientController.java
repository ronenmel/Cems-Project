package Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import logic.Test;

public class ClientController implements Initializable {

	private ObservableList<Test> TestList = FXCollections.observableArrayList();

	@FXML
	private Button btnSearch;

	@FXML
	private TextField txtSearch;

	@FXML
	private TextField txtID;

	@FXML
	private TableView<Test> TableViewTests;

	@FXML
	private TableColumn<Test, String> TestIDColumn;

	@FXML
	private TableColumn<Test, String> ProfessionColumn;

	@FXML
	private TableColumn<Test, String> CourseColumn;

	@FXML
	private TableColumn<Test, Integer> DurationColumn;

	@FXML
	private TableColumn<Test, String> PointsPerQuestionColumn;

	@FXML
	private Button btnUpdate;
	
	@FXML
	private Button btnshowAll;

	@FXML
	private TextField txtDuration;
	
	@FXML
	private Label txtStatus;

	@FXML
	private Button btnExit;

	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		TableViewTests.setVisible(false);
		if (TestList != null) {
			TestIDColumn.setCellValueFactory(new PropertyValueFactory<Test, String>("ID"));
			CourseColumn.setCellValueFactory(new PropertyValueFactory<Test, String>("Course"));
			ProfessionColumn.setCellValueFactory(new PropertyValueFactory<Test, String>("Profession"));
			DurationColumn.setCellValueFactory(new PropertyValueFactory<Test, Integer>("Duration"));
			PointsPerQuestionColumn.setCellValueFactory(new PropertyValueFactory<Test, String>("Points"));
		}
	}

	@FXML
	void ExitBtn(ActionEvent event) {
		System.out.println("Bye Bye!!");
		System.exit(0);
	}

	@FXML
	void SeatchInDataBase(ActionEvent event) {
		TableViewTests.getItems().clear();
		if(txtSearch.getText().isEmpty()) {
			txtStatus.setText(" ");
			//doNothing
		}
		else {
		ClientUI.chat.accept("Search " + txtSearch.getText());
		this.TestList = FXCollections.observableArrayList(ChatClient.TestList);
		TableViewTests.setVisible(true);
		TableViewTests.getItems().setAll(TestList);
		txtStatus.setText(" ");
		
		}
	}

	@FXML
	void showAll(ActionEvent event) {
		TableViewTests.getItems().clear();
		ClientUI.chat.accept("Get");
		txtStatus.setText(" ");
		this.TestList = FXCollections.observableArrayList(ChatClient.TestList);
		TableViewTests.setVisible(true);
		TableViewTests.getItems().setAll(TestList);
	}

	@FXML
	void UpdateDuration(ActionEvent event) {
		if (txtID.getText().isEmpty() || txtDuration.getText().isEmpty()) {
			txtStatus.setText("All Fields Are Required");
			txtStatus.setStyle("-fx-text-fill: RED;");
		} else {
			ClientUI.chat.accept("Update " + txtID.getText() + " " + txtDuration.getText());
			this.TestList = FXCollections.observableArrayList(ChatClient.TestList);
			if (TestList.isEmpty()) {
				txtStatus.setText("ID Not Found");
				txtStatus.setStyle("-fx-text-fill: RED;");
			} else {
				TableViewTests.setVisible(true);
				TableViewTests.getItems().clear();
				TableViewTests.getItems().setAll(TestList);
				txtStatus.setText("Updated Succesfully");
				txtStatus.setStyle("-fx-text-fill: Green;");
			}
		}

	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("ClientWindow.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("CEMS Client Prototype");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
