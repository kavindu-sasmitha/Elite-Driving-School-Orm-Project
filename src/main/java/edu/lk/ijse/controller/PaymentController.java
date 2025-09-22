package edu.lk.ijse.controller;

import edu.lk.ijse.bo.custom.CourseBo;
import edu.lk.ijse.bo.custom.impl.CourseBoImpl;
import edu.lk.ijse.bo.custom.PaymentBo;
import edu.lk.ijse.bo.custom.impl.PaymentBoImpl;
import edu.lk.ijse.bo.custom.StudentBo;
import edu.lk.ijse.bo.custom.impl.StudentBoImpl;
import edu.lk.ijse.dto.PaymentDto;
import edu.lk.ijse.dto.StudentDto;
import edu.lk.ijse.tm.PaymentTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {


    @FXML
    private ComboBox<Integer> studentIdCmb;
    @FXML
    private TextField studentNameTxt;
    @FXML
    private TextField totalFeeTxt;
    @FXML
    private TextField alreadyPaidTxt;
    @FXML
    private TextField balanceTxt;
    @FXML
    private TextField paymentAmountTxt;
    @FXML
    private DatePicker paymentDateDp;
    @FXML
    private TableView<PaymentTm> paymentTbl;
    @FXML
    private TableColumn<PaymentTm, Integer> paymentIdCol;
    @FXML
    private TableColumn<PaymentTm, Integer> studentIdCol;
    @FXML
    private TableColumn<PaymentTm, String> studentNameCol;
    @FXML
    private TableColumn<PaymentTm, Double> amountCol;
    @FXML
    private TableColumn<PaymentTm, Date> dateCol;
    @FXML
    private TableColumn<PaymentTm, String> statusCol;
    @FXML
    private Label totalPaymentsLbl;


    private final StudentBo studentBo = new StudentBoImpl();
    private final CourseBo courseBo = new CourseBoImpl();
    private final PaymentBo paymentBo = new PaymentBoImpl();
    private final ObservableList<PaymentTm> paymentList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadStudentIds();
        setupTable();
        loadAllPayments();
    }

    private void loadStudentIds() {
        studentIdCmb.getItems().setAll(studentBo.getAllStudents().stream()
                .map(StudentDto::getStudentId)
                .collect(java.util.stream.Collectors.toList()));
    }

    private void setupTable() {
        paymentIdCol.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        paymentTbl.setItems(paymentList);
    }

    private void loadAllPayments() {
        paymentList.clear();
        List<PaymentDto> payments = paymentBo.getAllPayments();
        for (PaymentDto dto : payments) {
            StudentDto student = studentBo.getStudentById(dto.getStudentId());
            if (student != null) {

                double totalFee = 0.0;
                if (student.getCourseIds() != null) {
                    for (String courseId : student.getCourseIds()) {
                        totalFee += courseBo.getCourseById(courseId).getFee();
                    }
                }


                double alreadyPaid = paymentBo.getPaymentsByStudent(student.getStudentId()).stream()
                        .mapToDouble(PaymentDto::getAmount)
                        .sum();


                double balance = totalFee - alreadyPaid;


                String status = (balance <= 0) ? "Paid" : "Partially Paid";

                paymentList.add(new PaymentTm(
                        dto.getPaymentId(),
                        dto.getStudentId(),
                        student.getName(),
                        dto.getAmount(),
                        dto.getPaymentDate(),
                        status
                ));
            }
        }
        totalPaymentsLbl.setText("Total Payments: " + paymentList.size());
    }

    @FXML
    void onStudentIdSelected() {
        Integer selectedId = studentIdCmb.getSelectionModel().getSelectedItem();
        if (selectedId != null) {
            StudentDto student = studentBo.getStudentById(selectedId);
            if (student != null) {
                studentNameTxt.setText(student.getName());


                double totalFee = 0.0;
                if (student.getCourseIds() != null) {
                    for (String courseId : student.getCourseIds()) {
                        totalFee += courseBo.getCourseById(courseId).getFee();
                    }
                }
                totalFeeTxt.setText(String.format("%.2f", totalFee));

                double alreadyPaid = paymentBo.getPaymentsByStudent(selectedId).stream()
                        .mapToDouble(PaymentDto::getAmount)
                        .sum();
                alreadyPaidTxt.setText(String.format("%.2f", alreadyPaid));

                // Calculate balance
                double balance = totalFee - alreadyPaid;
                balanceTxt.setText(String.format("%.2f", balance));

            } else {
                clearStudentFields();
            }
        } else {
            clearStudentFields();
        }
    }

    @FXML
    void onPaymentAmountTyped() {
        try {
            double totalFee = Double.parseDouble(totalFeeTxt.getText());
            double alreadyPaid = Double.parseDouble(alreadyPaidTxt.getText());
            double newPayment = Double.parseDouble(paymentAmountTxt.getText());

            double newBalance = totalFee - (alreadyPaid + newPayment);
            balanceTxt.setText(String.format("%.2f", newBalance));

        } catch (NumberFormatException e) {

            double totalFee = 0.0;
            double alreadyPaid = 0.0;
            try {
                if (!totalFeeTxt.getText().isEmpty()) {
                    totalFee = Double.parseDouble(totalFeeTxt.getText());
                }
                if (!alreadyPaidTxt.getText().isEmpty()) {
                    alreadyPaid = Double.parseDouble(alreadyPaidTxt.getText());
                }
            } catch (NumberFormatException ignored) {

            }
            balanceTxt.setText(String.format("%.2f", totalFee - alreadyPaid));
        }
    }

    @FXML
    void onProcessPaymentBtn() {
        try {
            Integer studentId = studentIdCmb.getSelectionModel().getSelectedItem();
            Double paymentAmount = Double.parseDouble(paymentAmountTxt.getText());
            Date paymentDate = Date.from(paymentDateDp.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (studentId == null || paymentAmount <= 0 || paymentDate == null) {
                new Alert(Alert.AlertType.ERROR, "Please fill all required fields.").show();
                return;
            }


            double currentBalance = Double.parseDouble(balanceTxt.getText());
            if (currentBalance < 0) {
                new Alert(Alert.AlertType.WARNING, "Payment amount exceeds the outstanding balance. Please enter a valid amount.").show();
                return;
            }


            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setStudentId(studentId);
            paymentDto.setAmount(paymentAmount);
            paymentDto.setPaymentDate(paymentDate);

            paymentBo.processPayment(paymentDto);

            new Alert(Alert.AlertType.INFORMATION, "Payment successfully processed!").show();


            loadAllPayments();
            onStudentIdSelected();
            clearPaymentFields();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid amount. Please enter a number.").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to process payment: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void onClearBtn() {
        clearStudentFields();
        clearPaymentFields();
        studentIdCmb.getSelectionModel().clearSelection();
    }

    private void clearStudentFields() {
        studentNameTxt.clear();
        totalFeeTxt.clear();
        alreadyPaidTxt.clear();
        balanceTxt.clear();
    }

    private void clearPaymentFields() {
        paymentAmountTxt.clear();
        paymentDateDp.setValue(null);
    }
}