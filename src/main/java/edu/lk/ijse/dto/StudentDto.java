package edu.lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDto {
    private Integer studentId;
    private String name;
    private String address;
    private String contactNumber;
    private Date registrationDate;
    private Set<String> courseIds;
}