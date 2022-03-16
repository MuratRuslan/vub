package de.zwickau.vub.vending.service.impl;
import de.zwickau.vub.vending.model.User;
import de.zwickau.vub.vending.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service("DefaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findByUsername(username);
        org.springframework.security.core.userdetails.User user =
                new org.springframework.security.core.userdetails.User(
                        username, foundUser.get().getPassword(), Set.of(foundUser.get().getRole()));

        return user;
    }
}
