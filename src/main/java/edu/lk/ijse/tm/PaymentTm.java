package edu.lk.ijse.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentTm {
    private Integer paymentId;
    private Integer studentId;
    private String studentName;
    private Double amount;
    private Date paymentDate;
    private String status;
}