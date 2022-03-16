package de.zwickau.vub.vending.service.impl;

import de.zwickau.vub.vending.model.Role;
import de.zwickau.vub.vending.model.User;
import de.zwickau.vub.vending.repository.UserRepository;
import de.zwickau.vub.vending.service.UserException;
import de.zwickau.vub.vending.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JpaUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(User user) throws UserException {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserException("User already exists!");
        }
        if (user.getRole() == null) {
            user.setRole(Role.BUYER);
        }
        user.setDeposit(0);
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) throws UserException {
        userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
