package edu.lk.ijse.dao.custom;

import edu.lk.ijse.dao.CrudDAO;
import edu.lk.ijse.entity.Course;
import java.util.List;
import java.util.Optional;

public interface CourseDao extends CrudDAO<Course> {
    Optional<Course> findById(String id);

    long countAllCourses();
}
