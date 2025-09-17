package edu.lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstructorDto {
    private Integer instructorId;
    private String name;
    private String contactNumber;
    private String email;
    private String specialization;
}