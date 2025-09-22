package edu.lk.ijse.bo.custom.impl;

import edu.lk.ijse.bo.custom.UserBo;
import edu.lk.ijse.dao.custom.UserDao;
import edu.lk.ijse.dao.custom.impl.UserDaoImpl;
import edu.lk.ijse.dto.UserDto;
import edu.lk.ijse.entity.User;
import edu.lk.ijse.exception.InvalidCredentialsException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UserBoImpl implements UserBo {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    public UserDto login(UserDto userDto) throws InvalidCredentialsException {
        Optional<User> userOptional = userDao.findByUsername(userDto.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (BCrypt.checkpw(userDto.getPassword(), user.getPassword())) {
                return new UserDto(user.getUsername(), null, user.getRole());
            }
        }
        throw new InvalidCredentialsException("Invalid username or password.");
    }

    @Override
    public void registerUser(UserDto userDto) {
        String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
        User user = new User(userDto.getUsername(), hashedPassword, userDto.getRole());
        userDao.save(user);
    }

    @Override
    public void updateUser(UserDto userDto) {
        Optional<User> userOptional = userDao.findByUsername(userDto.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRole(userDto.getRole());
            userDao.update(user);
        }
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) throws InvalidCredentialsException {
        Optional<User> userOptional = userDao.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (BCrypt.checkpw(oldPassword, user.getPassword())) {
                String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                user.setPassword(hashedNewPassword);
                userDao.update(user);
                return;
            }
        }
        throw new InvalidCredentialsException("Invalid credentials. Password could not be changed.");
    }
}