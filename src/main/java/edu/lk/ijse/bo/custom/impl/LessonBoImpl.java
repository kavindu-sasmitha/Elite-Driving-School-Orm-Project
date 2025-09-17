package edu.lk.ijse.bo.custom.impl;

import edu.lk.ijse.bo.custom.LessonBo;
import edu.lk.ijse.bo.util.EntityDtoConvertor;
import edu.lk.ijse.dao.custom.CourseDao;
import edu.lk.ijse.dao.custom.InstructorDao;
import edu.lk.ijse.dao.custom.LessonDao;
import edu.lk.ijse.dao.custom.StudentDao;
import edu.lk.ijse.dao.custom.impl.CourseDaoImpl;
import edu.lk.ijse.dao.custom.impl.InstructorDaoImpl;
import edu.lk.ijse.dao.custom.impl.LessonDaoImpl;
import edu.lk.ijse.dao.custom.impl.StudentDaoImpl;
import edu.lk.ijse.dto.LessonDto;
import edu.lk.ijse.entity.Course;
import edu.lk.ijse.entity.Instructor;
import edu.lk.ijse.entity.Lesson;
import edu.lk.ijse.entity.Student;
import edu.lk.ijse.exception.SchedulingConflictException;

import java.util.List;
import java.util.stream.Collectors;

public class LessonBoImpl implements LessonBo {

    private final LessonDao lessonDao = new LessonDaoImpl();
    private final StudentDao studentDao = new StudentDaoImpl();
    private final InstructorDao instructorDao = new InstructorDaoImpl();
    private final CourseDao courseDao = new CourseDaoImpl();
    private final EntityDtoConvertor convertor = new EntityDtoConvertor();

    @Override
    public void scheduleLesson(LessonDto lessonDto) throws SchedulingConflictException {
        // Find entities from DTO IDs
        Student student = studentDao.findById(lessonDto.getStudentId())
                .orElseThrow(() -> new SchedulingConflictException("Student not found."));
        Instructor instructor = instructorDao.findById(lessonDto.getInstructorId())
                .orElseThrow(() -> new SchedulingConflictException("Instructor not found."));
        Course course = courseDao.findById(lessonDto.getCourseId())
                .orElseThrow(() -> new SchedulingConflictException("Course not found."));

        // Check for scheduling conflicts
        if (lessonDao.isInstructorBusy(instructor.getInstructorId(), lessonDto.getScheduledTime(), course.getDuration())) {
            throw new SchedulingConflictException("The selected instructor is busy at the scheduled time.");
        }

        // Create and save the new lesson using the convertor
        Lesson lesson = convertor.toLessonEntity(lessonDto, student, instructor, course);
        lesson.setStatus("SCHEDULED"); // Set status here as it's not in the DTO

        lessonDao.save(lesson);
    }

    @Override
    public void rescheduleLesson(Integer lessonId, LessonDto lessonDto) throws SchedulingConflictException {
        Lesson lesson = lessonDao.findById(lessonId)
                .orElseThrow(() -> new SchedulingConflictException("Lesson not found to reschedule."));

        // Find entities from DTO IDs
        Student student = studentDao.findById(lessonDto.getStudentId())
                .orElseThrow(() -> new SchedulingConflictException("Student not found."));
        Instructor instructor = instructorDao.findById(lessonDto.getInstructorId())
                .orElseThrow(() -> new SchedulingConflictException("Instructor not found."));
        Course course = courseDao.findById(lessonDto.getCourseId())
                .orElseThrow(() -> new SchedulingConflictException("Course not found."));

        // Check for scheduling conflicts, excluding the current lesson being updated
        if (lessonDao.isInstructorBusy(instructor.getInstructorId(), lessonDto.getScheduledTime(), course.getDuration(), lessonId)) {
            throw new SchedulingConflictException("The selected instructor is busy at the new scheduled time.");
        }

        // Update the existing lesson entity using the convertor
        Lesson updatedLesson = convertor.toLessonEntity(lessonDto, student, instructor, course);
        updatedLesson.setLessonId(lesson.getLessonId()); // Ensure the ID is maintained
        updatedLesson.setStatus("SCHEDULEED");

        lessonDao.update(updatedLesson);
    }

    @Override
    public void cancelLesson(Integer lessonId) {
        lessonDao.findById(lessonId).ifPresent(lesson -> {
            lesson.setStatus("CANCELED");
            lessonDao.update(lesson);
        });
    }

    @Override
    public List<LessonDto> getAllLessons() {
        return lessonDao.findAll().stream()
                .map(convertor::toLessonDto)
                .collect(Collectors.toList());
    }
}