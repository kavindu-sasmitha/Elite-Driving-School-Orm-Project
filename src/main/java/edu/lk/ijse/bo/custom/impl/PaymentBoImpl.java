package edu.lk.ijse.bo.custom.impl;

import edu.lk.ijse.bo.custom.PaymentBo;
import edu.lk.ijse.bo.util.EntityDtoConvertor; // Import the converter
import edu.lk.ijse.dao.custom.PaymentDao;
import edu.lk.ijse.dao.custom.impl.PaymentDaoImpl;
import edu.lk.ijse.dao.custom.StudentDao;
import edu.lk.ijse.dao.custom.impl.StudentDaoImpl;
import edu.lk.ijse.dto.PaymentDto;
import edu.lk.ijse.entity.Payment;
import edu.lk.ijse.entity.Student;
import edu.lk.ijse.exception.PaymentException;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentBoImpl implements PaymentBo {

    private final PaymentDao paymentDao = new PaymentDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();
    private final EntityDtoConvertor convertor = new EntityDtoConvertor(); // Create an instance of the converter

    @Override
    public void processPayment(PaymentDto paymentDto) throws PaymentException {
        Student student = studentDao.findById(paymentDto.getStudentId())
                .orElseThrow(() -> new PaymentException("Student not found for payment processing."));

        Payment payment = convertor.toPaymentEntity(paymentDto, student);

        paymentDao.save(payment);
    }

    @Override
    public List<PaymentDto> getPaymentsByStudent(Integer studentId) {
        return paymentDao.findByStudentId(studentId).stream()
                .map(convertor::toPaymentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentDao.findAll().stream()
                .map(convertor::toPaymentDto)
                .collect(Collectors.toList());
    }

    // The private mapToDto method has been removed from here
    // as it is now in the EntityDtoConvertor class.
}