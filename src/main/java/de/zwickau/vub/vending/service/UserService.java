package de.zwickau.vub.vending.service;

import de.zwickau.vub.vending.model.User;

public interface UserService {

    void createUser(User user) throws UserException;
    void updateUser(User user) throws UserException;
    User findUserByUsername(String username);
    User findUserById(Long id);
    void deleteUser(String username);
}
