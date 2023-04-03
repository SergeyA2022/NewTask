package com.haulmont.testTask.service;


import com.haulmont.testTask.entity.Group;
import com.haulmont.testTask.entity.Student;
import com.haulmont.testTask.dao.StudentDao;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StudentService {
    private final StudentDao studentDao;
    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public void deleteStudent(Student student) {
        studentDao.delete(student);
    }
    public void saveStudent(Student student) {
        if (student == null) {
            return;
        }
        studentDao.save(student);
    }

    public List<Student> findAllStudentName(String name) {
        if (name == null || name.isEmpty()) {
            return studentDao.findAll();
        } else {
            return studentDao.findAllByName(name);
        }
    }

    public List<Student> findAllStudentGroup(String lastName, Group group) {
        if (lastName == null || lastName.isEmpty()) {
            return studentDao.findAllStudentGroup(group);
        } else {
            return studentDao.findAllStudentGroupStudent(lastName, group);
        }
    }

    public List<Student> findAllStudent() {
        return studentDao.findAll();
    }
}
