package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.StudentBo;
import edu.lk.ijse.bo.custom.impl.StudentBoImpl;
import edu.lk.ijse.dto.StudentDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageStudentController implements Initializable {

    @FXML
    private TableView<StudentDto> tblStudents;

    @FXML
    private TableColumn<StudentDto, Integer> colStudentId;

    @FXML
    private TableColumn<StudentDto, String> colName;

    @FXML
    private TableColumn<StudentDto, String> colAddress;

    @FXML
    private TableColumn<StudentDto, String> colContact;

    @FXML
    private TableColumn<StudentDto, Date> colRegDate;

    private final StudentBo studentBo = new StudentBoImpl();
    private final ObservableList<StudentDto> studentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colRegDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));

        loadStudents();
    }

    private void loadStudents() {
        studentList.clear();
        List<StudentDto> students = studentBo.getAllStudents();
        studentList.addAll(students);
        tblStudents.setItems(studentList);
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/student/RegisterStudent.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Register New Student");
            stage.setScene(new Scene(root));

            RegisterStudentController registerController = loader.getController();
            registerController.setManageStudentController(this);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load the registration form.").show();
        }
    }

    // New or modified method
    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        StudentDto selectedStudent = tblStudents.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a student to update.").show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/student/UpdateStudent.fxml"));
            Parent root = loader.load();

            UpdateStudentController updateController = loader.getController();
            updateController.setManageStudentController(this);
            updateController.initData(selectedStudent);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Student Details");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load the update form.").show();
        }
    }

    // New or modified method
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        StudentDto selectedStudent = tblStudents.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a student to delete.").show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this student?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            studentBo.deleteStudent(selectedStudent.getStudentId());
            new Alert(Alert.AlertType.INFORMATION, "Student deleted successfully!").show();
            loadStudents(); // Refresh the table
        }
    }

    // No change
    public void refreshTable() {
        loadStudents();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        new Alert(Alert.AlertType.INFORMATION, "Search functionality is not implemented yet.").show();
    }
}