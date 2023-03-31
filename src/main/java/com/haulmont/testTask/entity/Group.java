package com.haulmont.testTask.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@Entity
@FieldNameConstants
@Table(name = "groups")
public class Group implements Serializable {
    @Serial
    private static final long serialVersionUID = -5020751766650323534L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "number")
    private String number;
    @Column(name = "faculty")
    private String faculty;
    @OneToMany(mappedBy = "group")
    private List<Student> students;


}
