package com.eneyeitech.maintenance_service_api.business;

import com.eneyeitech.maintenance_service_api.persistence.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService{
    final private Map<String, User> users = new ConcurrentHashMap<>();

    @Autowired
    IUserRepository repository;

    @Autowired
    GroupService groupService;


    public User findUserByEmail(String email) {
        List<User> list = new ArrayList<>();
        repository.findAll().forEach(user -> list.add(user));

        Optional<User> matchingUser = list.stream().
                filter(u -> u.getEmail().equalsIgnoreCase(email)).
                findFirst();
        User user = null;
        if (matchingUser.isPresent()){
            user = matchingUser.get();
            user.setEmail(email);
        }
        //return matchingUser.orElse(null);
        return user;
    }


    public User findUserByID(long id) {
        return repository.findById(id).get();
    }

    public void save(User user) {
        //users.put(user.getEmail(), user);
        updateAdminOrUserGroup(user);
        User savedUser = repository.save(user);
        System.out.println(savedUser + " saved.");
    }

    public void saveManager(User user) {
        //users.put(user.getEmail(), user);
        updateManagerGroup(user);
        User savedUser = repository.save(user);
        System.out.println(savedUser + " saved.");
    }

    public void saveTenant(User user) {
        //users.put(user.getEmail(), user);
        updateTenantGroup(user);
        User savedUser = repository.save(user);
        System.out.println(savedUser + " saved.");
    }

    public void saveTechnician(User user) {
        //users.put(user.getEmail(), user);
        updateTechnicianGroup(user);
        User savedUser = repository.save(user);
        System.out.println(savedUser + " saved.");
    }

    public User update(User user) {
        User updatedUser = repository.save(user);
        System.out.println(updatedUser + " updated.");
        return updatedUser;
    }

    public void delete(User user) {
        //users.put(user.getEmail(), user);
        repository.delete(user);
        System.out.println(user + " deleted.");
    }

    public void updatePassword(User user) {
        //users.put(user.getEmail(), user);
        User savedUser = repository.save(user);
        System.out.println(savedUser + " saved.");
    }

    public User updateRole(User user) {
        User updatedUser = repository.save(user);
        System.out.println(updatedUser + " updated.");
        return updatedUser;
    }

    public boolean hasUser(User user) {
        //return users.containsKey(user.getEmail());
        List<User> list = new ArrayList<>();
        repository.findAll().forEach(us -> list.add(us));

        Optional<User> matchingUser = list.stream().
                filter(u -> u.getEmail().equalsIgnoreCase(user.getEmail())).
                findFirst();

        return matchingUser.isPresent();
    }


    public boolean hasUser(String email) {
        //return users.containsKey(user.getEmail());
        List<User> list = new ArrayList<>();
        repository.findAll().forEach(us -> list.add(us));

        Optional<User> matchingUser = list.stream().
                filter(u -> u.getEmail().equalsIgnoreCase(email)).
                findFirst();

        return matchingUser.isPresent();
    }

    public boolean hasAdmin() {
        //return users.containsKey(user.getEmail());
        List<User> list = new ArrayList<>();
        repository.findAll().forEach(us -> list.add(us));
        System.out.println("1oo");
        if(list.size()<=0){
            return false;
        }
        Iterator i = list.iterator();
        while(i.hasNext()){
            System.out.println("2");
            User u = (User) i.next();
            Set<Group> groups = u.getUserGroups();
            Iterator setI = groups.iterator();
            while(setI.hasNext()){
                System.out.println("3");
                Group g = (Group) setI.next();
                if (g.getCode().equalsIgnoreCase("ROLE_ADMINISTRATOR")){
                    return true;
                }
            }
            System.out.println("4");
        }
        return false;
    }

    public boolean isAdmin(String email) {

        List<User> list = new ArrayList<>();
        repository.findAll().forEach(us -> list.add(us));
        System.out.println("2oo");
        if(list.size()<=0){
            return false;
        }
        Iterator i = list.iterator();
        while(i.hasNext()) {
            System.out.println("22");
            User u = (User) i.next();
            if (u.getEmail().equalsIgnoreCase(email)) {
                Set<Group> groups = u.getUserGroups();
                Iterator setI = groups.iterator();
                while (setI.hasNext()) {
                    System.out.println("33");
                    Group g = (Group) setI.next();
                    if (g.getCode().equalsIgnoreCase("ROLE_ADMINISTRATOR")) {
                        return true;
                    }
                }
                System.out.println("44");
            }
        }
        return false;
    }

    public Object getUsers() {
        //return  users;
        List<User> us = new ArrayList<>();
        repository.findAll().forEach(user -> us.add(user));
        return us;
    }

    private void updateAdminOrUserGroup(User user){
        Group group = null;
        if (hasAdmin()) {
            group = groupService.findByCode("ROLE_USER");
            user.addUserGroups(group);
            //updateManagerGroup(user);
        } else {
            group = groupService.findByCode("ROLE_ADMINISTRATOR");
            user.addUserGroups(group);
        }
    }

    private void updateUserGroup(User user){
        Group group = null;
        group = groupService.findByCode("ROLE_USER");
        user.addUserGroups(group);
    }

    private void updateManagerGroup(User user){
        Group group = null;
        group = groupService.findByCode("ROLE_MANAGER");
        user.addUserGroups(group);
        updateUserGroup(user);
    }

    private void updateTechnicianGroup(User user){
        Group group = null;
        group = groupService.findByCode("ROLE_TECHNICIAN");
        user.addUserGroups(group);
        updateUserGroup(user);
    }

    private void updateTenantGroup(User user){
        Group group = null;
        group = groupService.findByCode("ROLE_TENANT");
        user.addUserGroups(group);
        updateUserGroup(user);
    }

    private void updateAdministratorGroup(User user){
        Group group = null;
        group = groupService.findByCode("ROLE_ADMINISTRATOR");
        user.addUserGroups(group);
    }

}
