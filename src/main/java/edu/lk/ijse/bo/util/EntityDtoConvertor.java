package edu.lk.ijse.bo.util;

import edu.lk.ijse.dto.CourseDto;
import edu.lk.ijse.dto.InstructorDto;
import edu.lk.ijse.dto.LessonDto;
import edu.lk.ijse.dto.PaymentDto;
import edu.lk.ijse.entity.*;

public class EntityDtoConvertor {
    public InstructorDto mapToDto(Instructor instructor) {
        return new InstructorDto(
                instructor.getInstructorId(),
                instructor.getName(),
                instructor.getContactNumber(),
                instructor.getEmail(),
                instructor.getSpecialization()
        );
    }

    public Instructor mapToEntity(InstructorDto instructorDto) {
        return new Instructor(
                instructorDto.getInstructorId(),
                instructorDto.getName(),
                instructorDto.getContactNumber(),
                instructorDto.getEmail(),
                instructorDto.getSpecialization()
        );
    }
    public CourseDto toCourseDto(Course course) {
        return new CourseDto(
                course.getCourseId(),
                course.getCourseName(),
                course.getDuration(),
                course.getFee()
        );
    }

    public Course toCourseEntity(CourseDto courseDto) {
        return new Course(
                courseDto.getCourseId(),
                courseDto.getCourseName(),
                courseDto.getDuration(),
                courseDto.getFee()
        );
    }
    public LessonDto toLessonDto(Lesson lesson) {
        return new LessonDto(
                lesson.getLessonId(),
                lesson.getStudent().getStudentId(),
                lesson.getInstructor().getInstructorId(),
                lesson.getCourse().getCourseId(),
                lesson.getScheduledTime(),
                lesson.getStatus()
        );
    }

    public Lesson toLessonEntity(LessonDto lessonDto, Student student, Instructor instructor, Course course) {
        Lesson lesson = new Lesson();
        lesson.setLessonId(lessonDto.getLessonId());
        lesson.setStudent(student);
        lesson.setInstructor(instructor);
        lesson.setCourse(course);
        lesson.setScheduledTime(lessonDto.getScheduledTime());
        lesson.setStatus(lessonDto.getStatus());
        return lesson;
    }
    public PaymentDto toPaymentDto(Payment payment) {
        return new PaymentDto(
                payment.getPaymentId(),
                payment.getStudent().getStudentId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getStatus()
        );
    }

    public Payment toPaymentEntity(PaymentDto paymentDto, Student student) {
        Payment payment = new Payment();
        payment.setPaymentId(paymentDto.getPaymentId());
        payment.setStudent(student);
        payment.setAmount(paymentDto.getAmount());
        payment.setPaymentDate(paymentDto.getPaymentDate());
        payment.setStatus(paymentDto.getStatus());
        return payment;
    }
}
