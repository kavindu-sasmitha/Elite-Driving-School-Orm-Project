package edu.lk.ijse.bo;

import edu.lk.ijse.bo.custom.InstructorBo;
import edu.lk.ijse.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;
    private BOFactory() {

    }
    public static BOFactory getInstance() {
        if (boFactory == null) {
            boFactory = new BOFactory();
        }
        return boFactory;
    }
    public<T extends SuperBO>T getBO(BOTypes boType) {
        switch (boType) {
            case USER:return (T) new UserBoImpl();
            case DASHBOARD:return (T) new DashBoardBoImpl();
            case LESSONS:return (T) new LessonBoImpl();
            case INSTRUCTOR:return (T) new InstructorBoImpl();
            case PAYMENT:return (T) new PaymentBoImpl();
            case COURSE:return (T) new CourseBoImpl();
            case STUDENT:return (T) new StudentBoImpl();
            default:return null;
        }
    }
}
