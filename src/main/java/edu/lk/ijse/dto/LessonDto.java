package edu.lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonDto {
    private Integer lessonId;
    private Integer studentId;
    private Integer instructorId;
    private String courseId;
    private LocalDateTime scheduledTime;
    private String status;
}
