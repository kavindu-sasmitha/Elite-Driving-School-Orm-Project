package edu.lk.ijse.dao.custom;

import edu.lk.ijse.dao.CrudDAO;
import edu.lk.ijse.dao.SuperDAO;
import edu.lk.ijse.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentDao extends CrudDAO<Student> {

    Optional<Student> findById(Integer id);
    List<Student> findAllWithCourses();
    List<Student> findStudentsEnrolledInAllCourses();
}