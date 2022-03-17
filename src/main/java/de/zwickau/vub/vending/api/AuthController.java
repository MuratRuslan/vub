package de.zwickau.vub.vending.api;

import de.zwickau.vub.vending.jwt.JwtTokenUtil;
import de.zwickau.vub.vending.model.User;
import de.zwickau.vub.vending.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    @PreAuthorize("isAnonymous() or !isAuthenticated()")
    public ResponseEntity<String> authenticate(@RequestBody User user) {
        User found = userService.findUserByUsername(user.getUsername());
        if(passwordEncoder.matches(user.getPassword(), found.getPassword())) {
            String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                    user.getUsername(), found.getPassword(), List.of(found.getRole())
            ));
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.internalServerError().body("Could not authenticate user");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
