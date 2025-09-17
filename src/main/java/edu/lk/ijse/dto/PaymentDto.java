package edu.lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDto {
    private Integer paymentId;
    private Integer studentId;
    private Double amount;
    private Date paymentDate;
    private String status;
}