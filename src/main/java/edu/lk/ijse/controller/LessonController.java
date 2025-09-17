package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.CourseBo;
import edu.lk.ijse.bo.custom.impl.CourseBoImpl;
import edu.lk.ijse.bo.custom.InstructorBo;
import edu.lk.ijse.bo.custom.impl.InstructorBoImpl;
import edu.lk.ijse.bo.custom.LessonBo;
import edu.lk.ijse.bo.custom.impl.LessonBoImpl;
import edu.lk.ijse.bo.custom.StudentBo;
import edu.lk.ijse.bo.custom.impl.StudentBoImpl;
import edu.lk.ijse.dto.CourseDto;
import edu.lk.ijse.dto.InstructorDto;
import edu.lk.ijse.dto.LessonDto;
import edu.lk.ijse.dto.StudentDto;
import edu.lk.ijse.exception.SchedulingConflictException;
import edu.lk.ijse.tm.LessonTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LessonController implements Initializable {

    @FXML
    private DatePicker DatePiker;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbCourse;

    @FXML
    private ComboBox<String> cmbInstructor;

    @FXML
    private TableColumn<LessonTm, String> colCourse;

    @FXML
    private TableColumn<LessonTm, String> colDate;

    @FXML
    private TableColumn<LessonTm, Integer> colID;

    @FXML
    private TableColumn<LessonTm, String> colInstructor;

    @FXML
    private TableColumn<LessonTm, String> colStartTime;

    @FXML
    private TableColumn<LessonTm, String> colStatus;

    @FXML
    private TableView<LessonTm> tblLesson;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtStartTime;

    @FXML
    private TextField txtStudentId;

    @FXML
    private Label totalLessonsLbl;

    private final LessonBo lessonBo = new LessonBoImpl();
    private final InstructorBo instructorBo = new InstructorBoImpl();
    private final CourseBo courseBo = new CourseBoImpl();
    private final StudentBo studentBo = new StudentBoImpl();
    private final ObservableList<LessonTm> lessonList = FXCollections.observableArrayList();

    // Maps to store IDs for combo boxes
    private List<InstructorDto> allInstructors;
    private List<CourseDto> allCourses;
    private List<StudentDto> allStudents;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadLessons();
        loadInitialData();
        setupListeners();
    }

    private void setupListeners() {
        DatePiker.setValue(LocalDate.now());

        txtStudentId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                try {
                    Integer studentId = Integer.parseInt(newValue);
                    StudentDto student = studentBo.getStudentById(studentId);
                    if (student != null) {
                        loadStudentCourses(student);
                    } else {
                        cmbCourse.getItems().clear();
                        cmbInstructor.getItems().clear();
                        new Alert(Alert.AlertType.WARNING, "Student not found with this ID.").show();
                    }
                } catch (NumberFormatException e) {
                    // Ignore non-numeric input
                }
            } else {
                cmbCourse.getItems().clear();
                cmbInstructor.getItems().clear();
            }
        });

        cmbCourse.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadInstructorsForCourse(newValue);
            }
        });
    }

    private void loadStudentCourses(StudentDto student) {
        cmbCourse.getItems().clear();
        if (student.getCourseIds() != null) {
            List<String> studentCourseNames = allCourses.stream()
                    .filter(c -> student.getCourseIds().contains(c.getCourseId()))
                    .map(CourseDto::getCourseName)
                    .collect(Collectors.toList());
            cmbCourse.getItems().setAll(studentCourseNames);
        }
    }

    private void loadInstructorsForCourse(String courseName) {
        cmbInstructor.getItems().clear();
        // Find all instructors whose specialization matches the selected course name
        List<InstructorDto> specializedInstructors = allInstructors.stream()
                .filter(i -> i.getSpecialization() != null && i.getSpecialization().equalsIgnoreCase(courseName))
                .collect(Collectors.toList());

        if (!specializedInstructors.isEmpty()) {
            // Add all matching instructor names to the combo box
            List<String> instructorNames = specializedInstructors.stream()
                    .map(InstructorDto::getName)
                    .collect(Collectors.toList());
            cmbInstructor.getItems().setAll(instructorNames);
            cmbInstructor.getSelectionModel().selectFirst(); // Select the first one by default
        } else {
            new Alert(Alert.AlertType.WARNING, "No instructors found for this course.").show();
        }
    }


    private void setupTable() {
        colID.setCellValueFactory(new PropertyValueFactory<>("lessonId"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colInstructor.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("scheduledTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblLesson.setItems(lessonList);
        tblLesson.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });
    }

    private void loadLessons() {
        lessonList.clear();
        List<LessonDto> lessons = lessonBo.getAllLessons();
        for (LessonDto dto : lessons) {
            InstructorDto instructor = instructorBo.getInstructorById(dto.getInstructorId());
            CourseDto course = courseBo.getCourseById(dto.getCourseId());

            lessonList.add(new LessonTm(
                    dto.getLessonId(),
                    dto.getStudentId(),
                    dto.getInstructorId(),
                    dto.getCourseId(),
                    instructor != null ? instructor.getName() : "N/A",
                    course != null ? course.getCourseName() : "N/A",
                    dto.getScheduledTime().toLocalDate(),
                    dto.getScheduledTime().toLocalTime(),
                    dto.getStatus()
            ));
        }
        totalLessonsLbl.setText("Total Lessons: " + lessonList.size());
    }

    private void loadInitialData() {
        allInstructors = instructorBo.getAllInstructors();
        allCourses = courseBo.getAllCourses();
        allStudents = studentBo.getAllStudents();
    }

    private void populateForm(LessonTm lessonTm) {
        txtId.setText(String.valueOf(lessonTm.getLessonId()));
        txtStudentId.setText(String.valueOf(lessonTm.getStudentId()));
        DatePiker.setValue(lessonTm.getDate());
        txtStartTime.setText(lessonTm.getScheduledTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        cmbCourse.getItems().setAll(allCourses.stream().map(CourseDto::getCourseName).collect(Collectors.toList()));
        cmbCourse.setValue(lessonTm.getCourseName());

        // When populating from a selected row, we need to ensure the instructor combo box
        // is updated to reflect instructors for the selected course.
        // The listener for cmbCourse will handle this if it's triggered again,
        // but to be safe, we can call it directly or ensure the selected instructor is available.
        // For simplicity, we'll rely on the listener to populate based on cmbCourse.setValue() above.
        // However, if the selected lesson's instructor is not the first one for that course,
        // it might not be selected correctly if not explicitly handled.
        // A more robust solution would be to find the instructor ID from lessonTm and then
        // set the value of cmbInstructor.

        // For now, let's just set all possible instructors and then try to select the one from the TM.
        cmbInstructor.getItems().setAll(allInstructors.stream().map(InstructorDto::getName).collect(Collectors.toList()));
        cmbInstructor.setValue(lessonTm.getInstructorName());

        // If the selected lesson's instructor is not in the current list (which shouldn't happen if data is consistent),
        // this might fail. A better approach would be to load instructors based on the course name and then select.
        // Let's refine this:
        loadInstructorsForCourse(lessonTm.getCourseName()); // This will populate and select the first instructor
        cmbInstructor.setValue(lessonTm.getInstructorName()); // Then try to set the specific instructor from the lessonTm
    }

    private void clearForm() {
        txtId.clear();
        txtStudentId.clear();
        DatePiker.setValue(LocalDate.now());
        txtStartTime.clear();
        cmbCourse.getItems().clear();
        cmbInstructor.getItems().clear();
        tblLesson.getSelectionModel().clearSelection();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            Integer studentId = Integer.parseInt(txtStudentId.getText());
            String courseName = cmbCourse.getValue();
            String instructorName = cmbInstructor.getValue();
            LocalDate date = DatePiker.getValue();
            String startTimeText = txtStartTime.getText();

            if (courseName == null || instructorName == null || date == null || startTimeText.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please fill all required fields.").show();
                return;
            }

            Integer instructorId = allInstructors.stream().filter(i -> i.getName().equals(instructorName)).map(InstructorDto::getInstructorId).findFirst().orElse(null);
            String courseId = allCourses.stream().filter(c -> c.getCourseName().equals(courseName)).map(CourseDto::getCourseId).findFirst().orElse(null);

            if (instructorId == null || courseId == null) {
                new Alert(Alert.AlertType.ERROR, "Invalid course or instructor selected.").show();
                return;
            }

            LocalTime startTime = LocalTime.parse(startTimeText, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime scheduledTime = LocalDateTime.of(date, startTime);

            boolean studentExists = allStudents.stream().anyMatch(s -> s.getStudentId().equals(studentId));
            if (!studentExists) {
                new Alert(Alert.AlertType.ERROR, "Student ID not found.").show();
                return;
            }

            LessonDto lessonDto = new LessonDto(
                    null,
                    studentId,
                    instructorId,
                    courseId,
                    scheduledTime,
                    "SCHEDULED"
            );

            lessonBo.scheduleLesson(lessonDto);
            new Alert(Alert.AlertType.INFORMATION, "Lesson scheduled successfully!").show();
            loadLessons();
            clearForm();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Student ID format.").show();
        } catch (DateTimeParseException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid time format. Please use HH:mm.").show();
        } catch (SchedulingConflictException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            Integer lessonId = Integer.parseInt(txtId.getText());
            Integer studentId = Integer.parseInt(txtStudentId.getText());
            String courseName = cmbCourse.getValue();
            String instructorName = cmbInstructor.getValue();
            LocalDate date = DatePiker.getValue();
            String startTimeText = txtStartTime.getText();

            if (courseName == null || instructorName == null || date == null || startTimeText.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please fill all required fields.").show();
                return;
            }

            Integer instructorId = allInstructors.stream().filter(i -> i.getName().equals(instructorName)).map(InstructorDto::getInstructorId).findFirst().orElse(null);
            String courseId = allCourses.stream().filter(c -> c.getCourseName().equals(courseName)).map(CourseDto::getCourseId).findFirst().orElse(null);

            if (instructorId == null || courseId == null) {
                new Alert(Alert.AlertType.ERROR, "Invalid course or instructor selected.").show();
                return;
            }

            LocalTime startTime = LocalTime.parse(startTimeText, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime scheduledTime = LocalDateTime.of(date, startTime);

            LessonDto lessonDto = new LessonDto(
                    lessonId,
                    studentId,
                    instructorId,
                    courseId,
                    scheduledTime,
                    "SCHEDULED"
            );

            lessonBo.rescheduleLesson(lessonId, lessonDto);
            new Alert(Alert.AlertType.INFORMATION, "Lesson updated successfully!").show();
            loadLessons();
            clearForm();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Lesson ID or Student ID format.").show();
        } catch (DateTimeParseException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid time format. Please use HH:mm.").show();
        } catch (SchedulingConflictException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        try {
            Integer lessonId = Integer.parseInt(txtId.getText());
            lessonBo.cancelLesson(lessonId);
            new Alert(Alert.AlertType.INFORMATION, "Lesson canceled successfully!").show();
            loadLessons();
            clearForm();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Lesson ID.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to cancel lesson: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearForm();
    }
}