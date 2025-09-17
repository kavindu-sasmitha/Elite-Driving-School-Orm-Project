package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.UserBo;
import edu.lk.ijse.bo.custom.impl.UserBoImpl;
import edu.lk.ijse.dto.UserDto;
import edu.lk.ijse.exception.InvalidCredentialsException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField TxtPassword;

    @FXML
    private Button btnButton; // This is your login button

    private final UserBo userBo = new UserBoImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the role combo box
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "Receptionist"));
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String role = cmbRole.getValue();
        String username = txtUserName.getText();
        String password = TxtPassword.getText();

        // Basic validation for required fields
        if (role == null || username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields are required.").show();
            return;
        }

        // Create a UserDto from the input fields
        UserDto userDto = new UserDto(username, password, role);

        try {
            // Attempt to log in using the UserBo
            UserDto loggedInUser = userBo.login(userDto);

            // If login is successful, show a success message and navigate to the dashboard
            new Alert(Alert.AlertType.INFORMATION, "Login Successful!").show();
            navigateToDashboard(loggedInUser.getRole()); // Pass the role to control access

        } catch (InvalidCredentialsException e) {
            // Show an error message if login fails
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void navigateToDashboard(String role) {
        try {
            // Load the dashboard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashBoardView.fxml"));
            Parent root = loader.load();

            // Get the controller for the dashboard and pass the role
            DashBoardController dashBoardController = loader.getController();
            dashBoardController.setLoggedInUserRole(role); // Method to set role in DashBoardController

            Scene scene = new Scene(root);
            Stage stage = (Stage) btnButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Elite Driving School - Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load the dashboard.").show();
        }
    }
}