package edu.lk.ijse.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstructorTm {
    private Integer instructorId;
    private String name;
    private String contactNumber;
    private String email;
    private String specialization;
}