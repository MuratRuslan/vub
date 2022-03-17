package de.zwickau.vub.vending.api;

import de.zwickau.vub.vending.model.User;
import de.zwickau.vub.vending.service.UserException;
import de.zwickau.vub.vending.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAnonymous() or !isAuthenticated()")
    public ResponseEntity<?> postUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            return ResponseEntity.ok(user);
        } catch (UserException e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseUtil.createJsonWithMessage(e.getMessage()));
        }
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAnyRole('BUYER', 'SELLER')")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            userService.updateUser(user);
            return ResponseEntity.ok(user);
        } catch (UserException e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseUtil.createJsonWithMessage(e.getMessage()));
        }
    }

    @DeleteMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAnyRole('BUYER', 'SELLER')")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        if (userService.findUserByUsername(username) == null) {
            return ResponseEntity.badRequest().body("User not found!");
        }
        userService.deleteUser(username);
        return ResponseEntity.ok(ResponseUtil.createJsonWithMessage("User deleted!"));
    }

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasAnyRole('BUYER', 'SELLER')")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        User found = userService.findUserByUsername(username);
        if(null == found) {
            return ResponseEntity.badRequest().body(ResponseUtil.createJsonWithMessage("User not found!"));
        }
        return ResponseEntity.ok().body(found);
    }

    @PostMapping(value = "/{username}/deposit/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasRole('BUYER')")
    public ResponseEntity<String> deposit(@PathVariable String username, @PathVariable Integer amount) {
        try {
            if (amount != 5 && amount != 10 && amount != 20 && amount != 50 && amount != 100) {
                return ResponseEntity.badRequest()
                        .body(ResponseUtil.createJsonWithMessage("User is not allowed to deposit other than 5, 10, 20, 50 or 100 coins"));
            }
            User user = userService.findUserByUsername(username);
            user.setDeposit(user.getDeposit() + amount);
            userService.updateUser(user);
        } catch (UserException e) {
            return ResponseEntity.internalServerError().body(ResponseUtil.createJsonWithMessage(e.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseUtil.createJsonWithMessage("Success"));
    }

    @PostMapping(value = "/{username}/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated() and hasRole('BUYER')")
    public ResponseEntity<String> resetDeposit(@PathVariable String username) {
        try {
            User user = userService.findUserByUsername(username);
            user.setDeposit(0);
            userService.updateUser(user);
            return ResponseEntity.ok(ResponseUtil.createJsonWithMessage("Deposit reset!"));
        } catch (UserException e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseUtil.createJsonWithMessage(e.getMessage()));
        }
    }
}
