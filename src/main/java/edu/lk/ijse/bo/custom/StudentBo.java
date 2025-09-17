package edu.lk.ijse.bo.custom;

import edu.lk.ijse.bo.SuperBO;
import edu.lk.ijse.dto.StudentDto;
import edu.lk.ijse.exception.RegistrationException;

import java.util.List;

public interface StudentBo extends SuperBO {
    void registerStudent(StudentDto studentDto) throws RegistrationException;
    List<StudentDto> getAllStudents();
    void deleteStudent(Integer studentId);
    void updateStudent(StudentDto studentDto);
    StudentDto getStudentById(Integer studentId);
    List<StudentDto> getAllStudentsWithCourses();
    String getCourseNameById(String courseId);
}