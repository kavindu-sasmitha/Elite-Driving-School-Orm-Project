package edu.lk.ijse.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonTm {
    private Integer lessonId;
    private Integer studentId;
    private Integer instructorId;
    private String courseId;
    private String instructorName;
    private String courseName;
    private LocalDate date;
    private LocalTime scheduledTime;
    // Duration field has been removed
    private String status;
}