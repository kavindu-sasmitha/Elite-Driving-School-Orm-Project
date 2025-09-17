package edu.icet.elite.view.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EnrollmentTm {
    private Integer studentId;
    private String studentName;
    private String courseId;
    private String courseName;
}