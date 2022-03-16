package de.zwickau.vub.vending.api;

import de.zwickau.vub.vending.model.User;
import de.zwickau.vub.vending.service.UserException;
import de.zwickau.vub.vending.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
@RequestMapping("/users")
public class UserApi {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> postUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            System.out.println("Check");
            return ResponseEntity.ok("User registered!");
        } catch (UserException e) {
            return ResponseEntity.internalServerError()
                    .body("User already exists");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        try {
            userService.updateUser(user);
            return ResponseEntity.ok("User updated");
        } catch (UserException e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        if (userService.findUserByUsername(username) == null) {
            return ResponseEntity.badRequest().body("User not found!");
        }
        userService.deleteUser(username);
        return ResponseEntity.ok("User deleted!");
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User found = userService.findUserByUsername(username);
        if(null == found) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(found);
    }

    @PostMapping("/{username}/deposit/{amount}")
    public ResponseEntity<String> deposit(@PathVariable String username, @PathVariable Integer amount) {
        try {
            if (amount != 5 && amount != 10 && amount != 20 && amount != 50 && amount != 100) {
                return ResponseEntity.badRequest()
                        .body("User is not allowed to deposit other than 5, 10, 20, 50 or 100 coins");
            }
            User user = userService.findUserByUsername(username);
            user.setDeposit(user.getDeposit() + amount);
            userService.updateUser(user);
        } catch (UserException e) {
            return ResponseEntity.internalServerError().body("User with given username was not found");
        }
        return ResponseEntity.ok().body("Success!");
    }

    @PostMapping("/{username}/reset")
    public ResponseEntity<String> resetDeposit(@PathVariable String username) {
        try {
            User user = userService.findUserByUsername(username);
            user.setDeposit(0);
            userService.updateUser(user);
            return ResponseEntity.ok("Deposit was reset");
        } catch (UserException e) {
            return ResponseEntity.internalServerError()
                    .body("Could not reset deposit of user!");
        }
    }
}
