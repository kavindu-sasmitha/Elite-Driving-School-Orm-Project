package edu.lk.ijse.bo.custom.impl;

import edu.lk.ijse.bo.custom.CourseBo;
import edu.lk.ijse.bo.custom.StudentBo;
import edu.lk.ijse.dao.custom.StudentDao;
import edu.lk.ijse.dao.custom.impl.StudentDaoImpl;
import edu.lk.ijse.dto.StudentDto;
import edu.lk.ijse.entity.Course;
import edu.lk.ijse.entity.Student;
import edu.lk.ijse.exception.RegistrationException;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentBoImpl implements StudentBo {
    private final StudentDao studentDao = new StudentDaoImpl();
    private final CourseBo courseBo = new CourseBoImpl();
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public void registerStudent(StudentDto studentDto) throws RegistrationException {
        try {
            Student student = new Student();
            student.setStudentId(studentDto.getStudentId());
            student.setName(studentDto.getName());
            student.setAddress(studentDto.getAddress());
            student.setContactNumber(studentDto.getContactNumber());
            student.setRegistrationDate(studentDto.getRegistrationDate());
            studentDao.save(student);
        } catch (Exception e) {
            throw new RegistrationException("Error registering student");
        }
    }

    @Override
    public List<StudentDto> getAllStudents() {
        return studentDao.findAll().stream()
                .map(student -> {
                    StudentDto dto = modelMapper.map(student, StudentDto.class);
                    if (student.getCourses() != null) {
                        Set<String> courseIds = student.getCourses().stream()
                                .map(Course::getCourseId)
                                .collect(Collectors.toSet());
                        dto.setCourseIds(courseIds);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStudent(Integer studentId) {
        studentDao.findById(studentId).ifPresent(studentDao::delete);
    }

    @Override
    public void updateStudent(StudentDto studentDto) {
        Student student = studentDao.findById(studentDto.getStudentId()).orElse(null);
        if (student == null) {
            throw new RuntimeException("Student not found for update.");
        }
        student.setName(studentDto.getName());
        student.setAddress(studentDto.getAddress());
        student.setContactNumber(studentDto.getContactNumber());
        if (studentDto.getCourseIds() != null) {
            Set<Course> newCourses = new HashSet<>();
            for (String courseId : studentDto.getCourseIds()) {
                newCourses.add(modelMapper.map(courseBo.getCourseById(courseId), Course.class));
            }
            student.setCourses(newCourses);
        }
        studentDao.update(student);
    }

    @Override
    public StudentDto getStudentById(Integer studentId) {
        return studentDao.findById(studentId)
                .map(student -> {
                    StudentDto dto = modelMapper.map(student, StudentDto.class);
                    if (student.getCourses() != null) {
                        Set<String> courseIds = student.getCourses().stream()
                                .map(Course::getCourseId)
                                .collect(Collectors.toSet());
                        dto.setCourseIds(courseIds);
                    }
                    return dto;
                })
                .orElse(null);
    }

    @Override
    public List<StudentDto> getAllStudentsWithCourses() {
        return studentDao.findAllWithCourses().stream()
                .map(student -> {
                    StudentDto dto = modelMapper.map(student, StudentDto.class);
                    if (student.getCourses() != null) {
                        Set<String> courseIds = student.getCourses().stream()
                                .map(Course::getCourseId)
                                .collect(Collectors.toSet());
                        dto.setCourseIds(courseIds);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public String getCourseNameById(String courseId) {
        return courseBo.getCourseById(courseId).getCourseName();
    }
}