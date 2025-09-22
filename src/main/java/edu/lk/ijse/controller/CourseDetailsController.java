package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.StudentBo;
import edu.lk.ijse.bo.custom.impl.StudentBoImpl;
import edu.lk.ijse.dto.StudentDto;
import edu.lk.ijse.tm.CourseDetailsTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CourseDetailsController implements Initializable {

    @FXML
    private TableColumn<CourseDetailsTm, String> colCourseName;

    @FXML
    private TableColumn<CourseDetailsTm, String> colStudentName;

    @FXML
    private TableView<CourseDetailsTm> tblCourseDetails;

    private final StudentBo studentBo = new StudentBoImpl();
    private final ObservableList<CourseDetailsTm> courseDetailsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadCourseDetails();
    }

    private void setupTable() {
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tblCourseDetails.setItems(courseDetailsList);
    }

    private void loadCourseDetails() {
        courseDetailsList.clear();
        List<StudentDto> students = studentBo.getAllStudentsWithCourses();

        List<CourseDetailsTm> tmList = new ArrayList<>();
        for (StudentDto student : students) {
            if (student.getCourseIds() != null && !student.getCourseIds().isEmpty()) {
                boolean isFirstCourse = true;
                for (String courseId : student.getCourseIds()) {

                    String courseName = studentBo.getCourseNameById(courseId);
                    if (isFirstCourse) {
                        tmList.add(new CourseDetailsTm(student.getName(), courseName));
                        isFirstCourse = false;
                    } else {
                        tmList.add(new CourseDetailsTm("", courseName));
                    }
                }
            } else {
                tmList.add(new CourseDetailsTm(student.getName(), "No courses enrolled"));
            }
        }
        courseDetailsList.setAll(tmList);
    }
}