package edu.lk.ijse.bo.custom;

import edu.lk.ijse.bo.SuperBO;
import edu.lk.ijse.dto.LessonDto;
import edu.lk.ijse.exception.SchedulingConflictException;

import java.util.List;

public interface LessonBo extends SuperBO {
    void scheduleLesson(LessonDto lessonDto) throws SchedulingConflictException;
    void rescheduleLesson(Integer lessonId, LessonDto lessonDto) throws SchedulingConflictException;
    void cancelLesson(Integer lessonId);
    List<LessonDto> getAllLessons();

}