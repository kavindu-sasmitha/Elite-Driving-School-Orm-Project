package edu.lk.ijse.dao.custom;

import edu.lk.ijse.dao.CrudDAO;
import edu.lk.ijse.dao.SuperDAO;
import edu.lk.ijse.entity.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentDao extends CrudDAO<Payment> {

    List<Payment> findByStudentId(Integer studentId);

}
