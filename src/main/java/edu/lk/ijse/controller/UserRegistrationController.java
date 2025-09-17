package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.UserBo;
import edu.lk.ijse.bo.custom.impl.UserBoImpl;
import edu.lk.ijse.dto.UserDto;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserRegistrationController implements Initializable {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnCancel;

    private final UserBo userBo = new UserBoImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the role combo box
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "Receptionist"));
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String role = cmbRole.getValue();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty() || role == null) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields.").show();
            return;
        }

        UserDto userDto = new UserDto(username, password, role);

        try {
            // Register the user
            userBo.registerUser(userDto);
            new Alert(Alert.AlertType.INFORMATION, "User registered successfully!").show();
            // Optionally clear fields or close the window after successful registration
            clearFields();
            closeWindow();
        } catch (Exception e) {
            // Handle potential exceptions during registration (e.g., duplicate username)
            new Alert(Alert.AlertType.ERROR, "Error registering user: " + e.getMessage()).show();
            e.printStackTrace(); // For debugging
        }
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void clearFields() {
        txtUsername.clear();
        txtPassword.clear();
        cmbRole.getSelectionModel().clearSelection();
    }
}