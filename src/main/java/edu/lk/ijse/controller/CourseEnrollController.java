package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.CourseBo;
import edu.lk.ijse.bo.custom.impl.CourseBoImpl;
import edu.lk.ijse.bo.custom.StudentBo;
import edu.lk.ijse.bo.custom.impl.StudentBoImpl;
import edu.lk.ijse.dto.CourseDto;
import edu.lk.ijse.dto.StudentDto;
import edu.lk.ijse.tm.EnrollmentTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CourseEnrollController implements Initializable {


    @FXML
    private ComboBox<CourseDto> selectCourseCmb;
    @FXML
    private TextField feeTxt;
    @FXML
    private TextField studentIdTxt;
    @FXML
    private TextField studentNameTxt;
    @FXML
    private TextField durationTxt;
    @FXML
    private TableView<EnrollmentTm> enrollmentTbl;
    @FXML
    private TableColumn<EnrollmentTm, Integer> studentIdCol;
    @FXML
    private TableColumn<EnrollmentTm, String> studentNameCol;
    @FXML
    private TableColumn<EnrollmentTm, String> courseIdCol;
    @FXML
    private TableColumn<EnrollmentTm, String> courseNameCol;
    @FXML
    private javafx.scene.control.Label enrollmentCountLbl;


    private final CourseBo courseBo = new CourseBoImpl();
    private final StudentBo studentBo = new StudentBoImpl();
    private final ObservableList<EnrollmentTm> enrollmentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCourses();
      //  setupTable();
        loadAllEnrolledStudents();
    }

    private void loadCourses() {
        List<CourseDto> courses = courseBo.getAllCourses();
        selectCourseCmb.setConverter(new StringConverter<CourseDto>() {
            @Override
            public String toString(CourseDto courseDto) {
                return courseDto != null ? courseDto.getCourseName() : "";
            }

            @Override
            public CourseDto fromString(String s) {
                return null;
            }
        });
        selectCourseCmb.getItems().addAll(courses);
    }

//    private void setupTable() {
//        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
//        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
//        courseIdCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
//        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("courseName"));
//
//        enrollmentTbl.setItems(enrollmentList);
//    }

    private void loadAllEnrolledStudents() {
        enrollmentList.clear();
        List<StudentDto> allStudents = studentBo.getAllStudentsWithCourses(); // New method in StudentBo

        for (StudentDto student : allStudents) {
            if (student.getCourseIds() != null) {
                for (String courseId : student.getCourseIds()) {
                    CourseDto course = courseBo.getCourseById(courseId);
                    if (course != null) {
                        enrollmentList.add(new EnrollmentTm(
                                student.getStudentId(),
                                student.getName(),
                                course.getCourseId(),
                                course.getCourseName()
                        ));
                    }
                }
            }
        }
        updateEnrollmentCount();
    }

    private void updateEnrollmentCount() {
        //enrollmentCountLbl.setText("Total Enrollments: " + enrollmentList.size());
    }

    @FXML
    void onSelectCourse() {
        CourseDto selectedCourse = selectCourseCmb.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            feeTxt.setText(String.format("%.2f", selectedCourse.getFee()));
            durationTxt.setText(selectedCourse.getDuration());
        } else {
            feeTxt.clear();
            durationTxt.clear();
        }
    }

    @FXML
    void onStudentIdTyped() {
        try {
            Integer studentId = Integer.parseInt(studentIdTxt.getText());
            StudentDto studentDto = studentBo.getStudentById(studentId);
            if (studentDto != null) {
                studentNameTxt.setText(studentDto.getName());
            } else {
                studentNameTxt.clear();
            }
        } catch (NumberFormatException e) {
            studentNameTxt.clear();
        }
    }

    @FXML
    void onEnrollNowBtn() {
        try {
            Integer studentId = Integer.parseInt(studentIdTxt.getText());
            CourseDto selectedCourse = selectCourseCmb.getSelectionModel().getSelectedItem();

            if (selectedCourse == null || studentIdTxt.getText().isEmpty() || studentNameTxt.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please fill all required fields.").show();
                return;
            }


            StudentDto studentDto = studentBo.getStudentById(studentId);
            if (studentDto == null) {
                new Alert(Alert.AlertType.ERROR, "Student not found with this ID.").show();
                return;
            }


            if (studentDto.getCourseIds() != null && studentDto.getCourseIds().contains(selectedCourse.getCourseId())) {
                new Alert(Alert.AlertType.WARNING, "Student is already enrolled in this course.").show();
                return;
            }


            studentDto.getCourseIds().add(selectedCourse.getCourseId());


            studentBo.updateStudent(studentDto);

            new Alert(Alert.AlertType.INFORMATION, "Enrollment successful!").show();


            loadAllEnrolledStudents();
            clearForm();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Student ID! Please enter a valid number.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to enroll: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void onClearFormBtn() {
        clearForm();
    }

    private void clearForm() {
        studentIdTxt.clear();
        studentNameTxt.clear();
        selectCourseCmb.getSelectionModel().clearSelection();
        feeTxt.clear();
        durationTxt.clear();
    }
}