package edu.lk.ijse.bo.custom.impl;

import edu.lk.ijse.bo.custom.DashBoardBo;
import edu.lk.ijse.dao.custom.CourseDao;
import edu.lk.ijse.dao.custom.impl.CourseDaoImpl;
import edu.lk.ijse.dao.custom.InstructorDao;
import edu.lk.ijse.dao.custom.impl.InstructorDaoImpl;
import edu.lk.ijse.dao.custom.StudentDao;
import edu.lk.ijse.dao.custom.impl.StudentDaoImpl;

public class DashBoardBoImpl implements DashBoardBo {

    private final StudentDao studentDao = new StudentDaoImpl();
    private final InstructorDao instructorDao = new InstructorDaoImpl();
    private final CourseDao courseDao = new CourseDaoImpl();

    @Override
    public long getStudentCount() {

        return studentDao.findAll().size();
    }

    @Override
    public long getInstructorCount() {
        return instructorDao.findAll().size();
    }

    @Override
    public long getCourseCount() {
        return courseDao.countAllCourses();
    }
}
