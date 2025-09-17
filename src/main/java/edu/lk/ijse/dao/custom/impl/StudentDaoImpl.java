package edu.lk.ijse.dao.custom.impl;

import edu.lk.ijse.dao.custom.CourseDao;
import edu.lk.ijse.dao.custom.StudentDao;
import edu.lk.ijse.db.HibernateUtil;
import edu.lk.ijse.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class StudentDaoImpl implements StudentDao {

    private final CourseDao courseDao = new CourseDaoImpl();

    @Override
    public boolean save(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Student> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Student.class, id));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    @Override
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public void update(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> findAllWithCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Use LEFT JOIN FETCH to load students and their courses in a single query
            return session.createQuery("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses", Student.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Student> findStudentsEnrolledInAllCourses() {
        long totalCourses = courseDao.countAllCourses();
        if (totalCourses == 0) {
            return List.of();
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM Student s WHERE (SELECT count(c) FROM s.courses c) = :totalCourses";
            Query<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("totalCourses", totalCourses);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}