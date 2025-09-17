package edu.lk.ijse.dao.custom.impl;

import edu.lk.ijse.dao.custom.LessonDao;
import edu.lk.ijse.db.HibernateUtil;
import edu.lk.ijse.entity.Course;
import edu.lk.ijse.entity.Lesson;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class LessonDaoImpl implements LessonDao {

    @Override
    public boolean save(Lesson lesson) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(lesson);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Lesson> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Lesson.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Lesson> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Lesson", Lesson.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public void update(Lesson lesson) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(lesson);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Lesson lesson) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(lesson);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public boolean isInstructorBusy(Integer instructorId, LocalDateTime scheduledTime, String duration) {
        return isInstructorBusy(instructorId, scheduledTime, duration, null);
    }

    @Override
    public boolean isInstructorBusy(Integer instructorId, LocalDateTime scheduledTime, String duration, Integer lessonIdToExclude) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            long newLessonDurationMinutes;
            try {
                String[] parts = duration.toLowerCase().split(" ");
                if (parts.length >= 2 && parts[1].startsWith("hour")) {
                    newLessonDurationMinutes = Long.parseLong(parts[0]) * 60;
                } else if (parts.length >= 2 && parts[1].startsWith("minute")) {
                    newLessonDurationMinutes = Long.parseLong(parts[0]);
                } else {
                    newLessonDurationMinutes = Long.parseLong(parts[0]);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.err.println("Invalid duration format. Assuming default of 60 minutes.");
                newLessonDurationMinutes = 60;
            }

            LocalDateTime newLessonEndTime = scheduledTime.plusMinutes(newLessonDurationMinutes);

            // Fetch all scheduled lessons for the instructor on the same day
            String hql = "SELECT l FROM Lesson l " +
                    "WHERE l.instructor.instructorId = :instructorId " +
                    "AND l.status = 'SCHEDULED' " +
                    "AND l.scheduledTime BETWEEN :startOfDay AND :endOfDay";

            Query<Lesson> query = session.createQuery(hql, Lesson.class);
            query.setParameter("instructorId", instructorId);
            query.setParameter("startOfDay", scheduledTime.toLocalDate().atStartOfDay());
            query.setParameter("endOfDay", scheduledTime.toLocalDate().atTime(23, 59, 59));

            List<Lesson> existingLessons = query.list();

            // Check for time overlap in Java
            for (Lesson existingLesson : existingLessons) {
                // Skip the lesson being updated
                if (lessonIdToExclude != null && existingLesson.getLessonId().equals(lessonIdToExclude)) {
                    continue;
                }

                // Retrieve duration from the associated course
                Course associatedCourse = existingLesson.getCourse();
                if (associatedCourse == null) {
                    continue;
                }
                String existingDurationStr = associatedCourse.getDuration();
                long existingDurationMinutes = parseDuration(existingDurationStr);

                LocalDateTime existingLessonStartTime = existingLesson.getScheduledTime();
                LocalDateTime existingLessonEndTime = existingLessonStartTime.plusMinutes(existingDurationMinutes);

                // Check for overlap using standard time interval logic
                if (scheduledTime.isBefore(existingLessonEndTime) && newLessonEndTime.isAfter(existingLessonStartTime)) {
                    return true; // Overlap found
                }
            }
            return false; // No overlap found
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    // Helper method to parse duration string to minutes
    private long parseDuration(String durationStr) {
        try {
            String[] parts = durationStr.toLowerCase().split(" ");
            if (parts.length >= 2 && parts[1].startsWith("hour")) {
                return Long.parseLong(parts[0]) * 60;
            } else if (parts.length >= 2 && parts[1].startsWith("minute")) {
                return Long.parseLong(parts[0]);
            } else {
                return Long.parseLong(parts[0]);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return 60; // Default to 60 minutes if format is incorrect
        }
    }
}