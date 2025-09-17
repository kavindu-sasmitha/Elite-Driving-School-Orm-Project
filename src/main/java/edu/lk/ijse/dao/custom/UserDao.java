package edu.lk.ijse.dao.custom;

import edu.lk.ijse.dao.CrudDAO;
import edu.lk.ijse.dao.SuperDAO;
import edu.lk.ijse.entity.User;
import java.util.Optional;

public interface UserDao extends CrudDAO<User> {

    Optional<User> findByUsername(String username);

}