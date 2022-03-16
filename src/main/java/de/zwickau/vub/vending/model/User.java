package de.zwickau.vub.vending.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private Integer deposit;
    @Enumerated(EnumType.STRING)
    private Role role;
}
