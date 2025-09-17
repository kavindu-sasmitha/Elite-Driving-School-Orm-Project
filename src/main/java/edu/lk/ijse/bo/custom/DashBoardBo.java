package edu.lk.ijse.bo.custom;

import edu.lk.ijse.bo.SuperBO;

public interface DashBoardBo extends SuperBO {
    long getStudentCount();
    long getInstructorCount();
    long getCourseCount();
}
