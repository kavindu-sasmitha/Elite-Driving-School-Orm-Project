package edu.lk.ijse.bo.custom.impl;

import edu.lk.ijse.bo.custom.CourseBo;
import edu.lk.ijse.bo.util.EntityDtoConvertor;
import edu.lk.ijse.dao.custom.CourseDao;
import edu.lk.ijse.dao.custom.impl.CourseDaoImpl;
import edu.lk.ijse.dto.CourseDto;
import edu.lk.ijse.entity.Course;

import java.util.List;
import java.util.stream.Collectors;

public class CourseBoImpl implements CourseBo {

    private final CourseDao courseDao = new CourseDaoImpl();
    private final EntityDtoConvertor convertor = new EntityDtoConvertor(); // Create an instance of the converter

    @Override
    public void addCourse(CourseDto courseDto) {
        Course course = convertor.toCourseEntity(courseDto);
        courseDao.save(course);
    }

    @Override
    public CourseDto getCourseById(String id) {
        return courseDao.findById(id)
                .map(convertor::toCourseDto)
                .orElse(null);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseDao.findAll().stream()
                .map(convertor::toCourseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateCourse(CourseDto courseDto) {
        Course course = convertor.toCourseEntity(courseDto);
        courseDao.update(course);
    }

    @Override
    public void deleteCourse(String id) {
        courseDao.findById(id).ifPresent(courseDao::delete);
    }

    @Override
    public List<String> getAllCourseNames() {
        return courseDao.findAll().stream()
                .map(Course::getCourseName)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseByName(String name) {
        return courseDao.findAll().stream()
                .filter(c -> c.getCourseName().equalsIgnoreCase(name))
                .findFirst()
                .map(convertor::toCourseDto)
                .orElse(null);
    }
}