package com.knits.enterprise.model.security;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
@NoArgsConstructor
@SuperBuilder(toBuilder=true)
@Entity
public class Role{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;
    private String name;
}
