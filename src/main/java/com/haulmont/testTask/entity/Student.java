package com.haulmont.testTask.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Student implements Serializable {
    @Serial
    private static final long serialVersionUID = -5020751766650323534L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "fistName")
    private String fistName;
    @Column(name = "patronymic")
    private String patronymic;
    @Column(name = "birthday")
    private Date birthday;
    @ManyToOne
    @JoinColumn(name = "group_fk")
    private Group group;


}
