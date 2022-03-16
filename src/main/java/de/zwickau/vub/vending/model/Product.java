package de.zwickau.vub.vending.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    private Integer amountAvailable;
    //cost in cents
    private Integer cost;
}
