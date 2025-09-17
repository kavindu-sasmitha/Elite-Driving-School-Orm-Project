package edu.lk.ijse.dao;

import edu.lk.ijse.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory ;
    private DAOFactory() {

    }
    public static DAOFactory getInstance() {
        return daoFactory==null?daoFactory=new DAOFactory():daoFactory;
    }
    public <T extends SuperDAO>T getDAO(DAOType daoType){
        switch(daoType){
            case STUDENT:return (T)new StudentDaoImpl();
            case LESSON:return (T)new LessonDaoImpl();
            case PAYMENT:return (T)new PaymentDaoImpl();
            case USER:return (T)new UserDaoImpl();
            case INSTRUCTOR:return (T)new InstructorDaoImpl();
            case COURSE:return (T)new CourseDaoImpl();
            default :return null;
        }

    }
}
