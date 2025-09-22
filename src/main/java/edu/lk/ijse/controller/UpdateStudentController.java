package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.StudentBo;
import edu.lk.ijse.bo.custom.impl.StudentBoImpl;
import edu.lk.ijse.dto.StudentDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.ZoneId;

public class UpdateStudentController {

    @FXML
    private TextField txtRegistrationNo;

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
    private DatePicker dpDateOfBirth;

    @FXML
    private DatePicker dpRegistrationDate;

    private ManageStudentController manageStudentController;
    private final StudentBo studentBo = new StudentBoImpl();
    private StudentDto studentToUpdate;

    public void setManageStudentController(ManageStudentController manageStudentController) {
        this.manageStudentController = manageStudentController;
    }

    public void initData(StudentDto student) {
        this.studentToUpdate = student;
        String[] nameParts = student.getName().split(" ", 2);
        txtFirstName.setText(nameParts.length > 0 ? nameParts[0] : "");
        txtLastName.setText(nameParts.length > 1 ? nameParts[1] : "");

        txtRegistrationNo.setText(String.valueOf(student.getStudentId()));
        txtContact.setText(student.getContactNumber());
        txtAddress.setText(student.getAddress());
        dpRegistrationDate.setValue(student.getRegistrationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());


    }

    @FXML
    void updateStudent(ActionEvent event) {
        if (studentToUpdate == null) {
            new Alert(Alert.AlertType.ERROR, "No student data to update.").show();
            return;
        }


        if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || txtAddress.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.").show();
            return;
        }

        studentToUpdate.setName(txtFirstName.getText() + " " + txtLastName.getText());
        studentToUpdate.setAddress(txtAddress.getText());
        studentToUpdate.setContactNumber(txtContact.getText());

        studentBo.updateStudent(studentToUpdate); // This method needs to be added to the BO
        new Alert(Alert.AlertType.INFORMATION, "Student updated successfully!").show();

        if (manageStudentController != null) {
            manageStudentController.refreshTable();
        }


        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}