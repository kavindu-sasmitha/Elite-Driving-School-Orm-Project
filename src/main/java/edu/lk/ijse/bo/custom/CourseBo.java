package edu.lk.ijse.bo.custom;

import edu.lk.ijse.bo.SuperBO;
import edu.lk.ijse.dto.CourseDto;
import java.util.List;

public interface CourseBo extends SuperBO {
    void addCourse(CourseDto courseDto);
    CourseDto getCourseById(String id);
    List<CourseDto> getAllCourses();
    void updateCourse(CourseDto courseDto);
    void deleteCourse(String id);
    List<String> getAllCourseNames();
    CourseDto getCourseByName(String name);
}