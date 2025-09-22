package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.StudentBo;
import edu.lk.ijse.bo.custom.impl.StudentBoImpl;
import edu.lk.ijse.dto.StudentDto;
import edu.lk.ijse.exception.RegistrationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Date;

public class RegisterStudentController {

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtAddress;

    @FXML
    private Button btnRegisterOnAction;

    private ManageStudentController manageStudentController;
    private final StudentBo studentBo = new StudentBoImpl();

    public void setManageStudentController(ManageStudentController manageStudentController) {
        this.manageStudentController = manageStudentController;
    }

    @FXML
    void registerStudent(ActionEvent event) {

        if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || txtAddress.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.").show();
            return;
        }

        StudentDto studentDto = new StudentDto();
        studentDto.setName(txtFirstName.getText() + " " + txtLastName.getText());
        studentDto.setAddress(txtAddress.getText());
        studentDto.setContactNumber(txtContact.getText());

        studentDto.setRegistrationDate(new Date());

        try {
            studentBo.registerStudent(studentDto);
            new Alert(Alert.AlertType.INFORMATION, "Student registered successfully!").show();


            if (manageStudentController != null) {
                manageStudentController.refreshTable();
            }


            Stage stage = (Stage) btnRegisterOnAction.getScene().getWindow();
            stage.close();

        } catch (RegistrationException e) {
            new Alert(Alert.AlertType.ERROR, "Registration failed: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnresetOnAction(ActionEvent event) {
        txtFirstName.clear();
        txtLastName.clear();
        txtContact.clear();
        txtEmail.clear();
        txtAddress.clear();
    }
}
