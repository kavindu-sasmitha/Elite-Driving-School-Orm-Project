package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.CourseBo;
import edu.lk.ijse.bo.custom.impl.CourseBoImpl;
import edu.lk.ijse.bo.custom.InstructorBo;
import edu.lk.ijse.bo.custom.impl.InstructorBoImpl;
import edu.lk.ijse.dto.InstructorDto;
import edu.lk.ijse.tm.InstructorTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ManageInstructorController implements Initializable {

    @FXML
    private ComboBox<String> cmbSpecialization;

    @FXML
    private TableColumn<InstructorTm, String> colContact;

    @FXML
    private TableColumn<InstructorTm, String> colEmail;

    @FXML
    private TableColumn<InstructorTm, Integer> colId;

    @FXML
    private TableColumn<InstructorTm, String> colName;

    @FXML
    private TableColumn<InstructorTm, String> colSpecialization;

    @FXML
    private TableView<InstructorTm> tblInstructors;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtSearch;

    @FXML
    private Label totalInstructorsLbl;

    private final InstructorBo instructorBo = new InstructorBoImpl();
    private final CourseBo courseBo = new CourseBoImpl();
    private final ObservableList<InstructorTm> instructorList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadInstructors();
        loadSpecializations();

        tblInstructors.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        tblInstructors.setItems(instructorList);
    }

    private void loadInstructors() {
        instructorList.clear();
        List<InstructorDto> instructors = instructorBo.getAllInstructors();
        for (InstructorDto dto : instructors) {
            instructorList.add(new InstructorTm(
                    dto.getInstructorId(),
                    dto.getName(),
                    dto.getContactNumber(),
                    dto.getEmail(),
                    dto.getSpecialization()
            ));
        }
        totalInstructorsLbl.setText("Total Instructors: " + instructorList.size());
    }

    private void loadSpecializations() {
        List<String> courseNames = courseBo.getAllCourseNames();
        cmbSpecialization.getItems().setAll(courseNames);
    }

    private void populateForm(InstructorTm instructorTm) {
        txtId.setText(String.valueOf(instructorTm.getInstructorId()));
        txtName.setText(instructorTm.getName());
        txtContact.setText(instructorTm.getContactNumber());
        txtEmail.setText(instructorTm.getEmail());
        cmbSpecialization.setValue(instructorTm.getSpecialization());
    }

    private void clearForm() {
        txtId.clear();
        txtName.clear();
        txtContact.clear();
        txtEmail.clear();
        cmbSpecialization.getSelectionModel().clearSelection();
        tblInstructors.getSelectionModel().clearSelection();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            // ID is set to null because the database will generate it automatically
            InstructorDto instructorDto = new InstructorDto(
                    null,
                    txtName.getText(),
                    txtContact.getText(),
                    txtEmail.getText(),
                    cmbSpecialization.getValue()
            );
            instructorBo.addInstructor(instructorDto);
            new Alert(Alert.AlertType.INFORMATION, "Instructor added successfully!").show();
            loadInstructors();
            clearForm();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to add instructor: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            Integer id = Integer.parseInt(txtId.getText());
            InstructorDto instructorDto = new InstructorDto(
                    id,
                    txtName.getText(),
                    txtContact.getText(),
                    txtEmail.getText(),
                    cmbSpecialization.getValue()
            );
            instructorBo.updateInstructor(instructorDto);
            new Alert(Alert.AlertType.INFORMATION, "Instructor updated successfully!").show();
            loadInstructors();
            clearForm();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Instructor ID. Please select an instructor from the table.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update instructor: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            Integer id = Integer.parseInt(txtId.getText());
            instructorBo.deleteInstructor(id);
            new Alert(Alert.AlertType.INFORMATION, "Instructor deleted successfully!").show();
            loadInstructors();
            clearForm();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Instructor ID. Please select an instructor from the table.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to delete instructor: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearForm();
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearForm();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchText = txtSearch.getText();
        if (searchText.isEmpty()) {
            loadInstructors();
            return;
        }

        ObservableList<InstructorTm> filteredList = FXCollections.observableArrayList();
        for (InstructorTm tm : instructorList) {
            if (String.valueOf(tm.getInstructorId()).contains(searchText) ||
                    tm.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    tm.getSpecialization().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(tm);
            }
        }
        tblInstructors.setItems(filteredList);
    }
}