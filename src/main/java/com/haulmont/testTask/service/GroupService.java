package com.haulmont.testTask.service;

import com.haulmont.testTask.dao.GroupDao;

import com.haulmont.testTask.dao.StudentDao;
import com.haulmont.testTask.entity.Group;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private final GroupDao groupDao;
    private final StudentDao studentDao;

    public GroupService(GroupDao groupDao,
                        StudentDao studentDao) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
    }

    public List<Group> findAllGroups() {
        return groupDao.findAll();
    }


    public void deleteGroup(Group group) {
        if (studentDao.findAllStudentGroup(group).isEmpty()) {
            groupDao.delete(group);
        }
    }

    public void saveGroup(Group group) {
        if (group == null) {
            return;
        }
        groupDao.save(group);
    }
}