package com.haulmont.testTask.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@FieldNameConstants
@Table(name = "students")
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
    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;
    @ManyToOne
    @JoinColumn(name = "group_fk")
    private Group group;




}
