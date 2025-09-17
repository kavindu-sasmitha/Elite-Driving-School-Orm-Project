package edu.lk.ijse.bo.custom;

import edu.lk.ijse.bo.SuperBO;
import edu.lk.ijse.dto.PaymentDto;
import edu.lk.ijse.exception.PaymentException;

import java.util.List;

public interface PaymentBo  extends SuperBO {
    void processPayment(PaymentDto paymentDto) throws PaymentException;
    List<PaymentDto> getPaymentsByStudent(Integer studentId);
    List<PaymentDto> getAllPayments();
}
