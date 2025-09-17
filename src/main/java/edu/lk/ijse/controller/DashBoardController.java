package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.DashBoardBo;
import edu.lk.ijse.bo.custom.impl.DashBoardBoImpl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button; // Import Button
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    @FXML
    private VBox vbox; // Assuming this is your main content VBox

    @FXML
    private AnchorPane ancMainContainer;

    @FXML
    private Label lbDateTime;

    @FXML
    private Label lblStudentCount;

    @FXML
    private Label lblCourseCount;

    @FXML
    private Label lblInstructorCount;
    @FXML
    private Button btnUserRegistration;

    // Navigation Buttons (assuming these are your navigation buttons)
    @FXML
    private Button btnStudentOnAction;
    @FXML
    private Button btnEnrollCourseOnAction;
    @FXML
    private Button btnCourseOnAction;
    @FXML
    private Button btnLessonOnAction;
    @FXML
    private Button btnInstructorOnAction;
    @FXML
    private Button btnPaymentDetailsOnAction;
    @FXML
    private Button btnCourseDetailsOnAction; // Assuming this is the correct fx:id
    @FXML
    private Button btnDashBoardOnAction; // Assuming this is the correct fx:id

    private final DashBoardBo dashBoardBo = new DashBoardBoImpl();
    private String loggedInUserRole; // To store the role of the logged-in user

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCounts();
        startClock();
        // Initially hide elements not visible to receptionists
        if ("Receptionist".equals(loggedInUserRole)) {
            hideAdminOptions();
        }
    }



    private void hideAdminOptions() {
        // Hide buttons or sections that only Admins should see
        if (btnCourseOnAction != null) btnCourseOnAction.setVisible(false);
        if (btnLessonOnAction != null) btnLessonOnAction.setVisible(false);
        if (btnInstructorOnAction != null) btnInstructorOnAction.setVisible(false);
        // Add any other admin-specific elements here
    }

    private void loadCounts() {
        // These methods would fetch counts from your database/backend
        lblStudentCount.setText(String.valueOf(dashBoardBo.getStudentCount()));
        lblCourseCount.setText(String.valueOf(dashBoardBo.getCourseCount()));
        lblInstructorCount.setText(String.valueOf(dashBoardBo.getInstructorCount()));
    }

    private void startClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            lbDateTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    @FXML
    void btnDashBoardOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(true);
        ancMainContainer.getChildren().clear();
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/student/ManageStudent.fxml");
    }

    @FXML
    void btnEnrollCourseOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/course/CourseEnroll.fxml");
    }

    @FXML
    void btnCourseOnAction(ActionEvent event) { // Admin only
        vbox.visibleProperty().setValue(false);
        loadPage("/view/course/ManageCourse.fxml");
    }

    @FXML
    void btnLessonOnAction(ActionEvent event) { // Admin only
        vbox.visibleProperty().setValue(false);
        loadPage("/view/course/lesson.fxml");
    }

    @FXML
    void btnInstructorOnAction(ActionEvent event) { // Admin only
        vbox.visibleProperty().setValue(false);
        loadPage("/view/instructor/ManageInstructor.fxml");
    }

    @FXML
    void btnPaymentDetailsOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/course/payment.fxml");
    }

    @FXML
    void btnCourseDetailsOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/course/CourseDetails.fxml"); // Updated path
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/loginView/LoginController.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ancMainContainer.getScene().getWindow(); // Use a consistent element for getting the window
            stage.setScene(scene);
            stage.setTitle("Elite Driving School - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load login page.").show();
        }
    }
    @FXML
    void btnUserRegistrationOnAction(ActionEvent event) {
        vbox.visibleProperty().setValue(false);
        loadPage("/view/UserRegistration.fxml"); // Path to your UserRegistration.fxml
    }

    // Add this method to handle role-based visibility of the User Registration button if needed
    public void setLoggedInUserRole(String role) {
        this.loggedInUserRole = role;
        if ("Receptionist".equals(role)) {
            hideAdminOptions();
            // Also hide the user registration button if it's admin-only
            if (btnUserRegistration != null) {
                btnUserRegistration.setVisible(false);
            }
        }
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            // If the loaded page needs the role, you can get its controller and pass the role
            // Example:
            // if (fxmlPath.equals("/view/some/path.fxml")) {
            //     SomeController controller = loader.getController();
            //     controller.setRole(this.loggedInUserRole);
            // }
            ancMainContainer.getChildren().clear();
            ancMainContainer.getChildren().add(root);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load page: " + fxmlPath).show();
        }
    }
}