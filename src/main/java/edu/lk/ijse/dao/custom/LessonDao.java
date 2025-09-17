package edu.lk.ijse.dao.custom;

import edu.lk.ijse.dao.CrudDAO;
import edu.lk.ijse.dao.SuperDAO;
import edu.lk.ijse.entity.Lesson;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface LessonDao extends CrudDAO<Lesson> {
    boolean isInstructorBusy(Integer instructorId, LocalDateTime scheduledTime, String duration);
    boolean isInstructorBusy(Integer instructorId, LocalDateTime scheduledTime, String duration, Integer lessonId);
}