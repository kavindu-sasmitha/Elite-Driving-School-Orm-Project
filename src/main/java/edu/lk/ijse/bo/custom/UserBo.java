package edu.lk.ijse.bo.custom;

import edu.lk.ijse.bo.SuperBO;
import edu.lk.ijse.dto.UserDto;
import edu.lk.ijse.exception.InvalidCredentialsException;

public interface UserBo extends SuperBO {
    UserDto login(UserDto userDto) throws InvalidCredentialsException;
    void registerUser(UserDto userDto);
    void updateUser(UserDto userDto);
    void changePassword(String username, String oldPassword, String newPassword) throws InvalidCredentialsException;
}