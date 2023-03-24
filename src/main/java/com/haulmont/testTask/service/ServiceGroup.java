package com.haulmont.testTask.service;

import com.haulmont.testTask.dao.GroupDao;
import com.haulmont.testTask.entity.Group;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceGroup {
    private final GroupDao groupDao;
    public ServiceGroup(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public List<Group> findAllGroup(String name) {
        if (name == null || name.isEmpty()) {
            return groupDao.findAll();
        } else {
            return groupDao.findAllByName(name);
        }
    }

    public void deleteGroup(Group group) {
        groupDao.delete(group);
    }

    public void saveGroup(Group group) {
        if (group == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        groupDao.save(group);
    }

}