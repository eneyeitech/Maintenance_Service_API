package com.eneyeitech.maintenance_service_api.presentation.api;

import com.eneyeitech.maintenance_service_api.business.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class ServiceController {

    @Autowired
    UserService userService;

    @Autowired
    LockedAccounts lockedAccounts;

    @Autowired
    LoginAttemptService loginAttemptService;

    @Autowired
    GroupService groupService;


    @PutMapping("api/admin/user/role")
    public Object changeUserRole(@AuthenticationPrincipal UserDetailsImpl details, @Valid @RequestBody Role role) {
        if (details == null) {
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        } else {
            String roleString = "ROLE_"+role.getRole().toUpperCase();
            if (!groupService.hasCode(roleString)) {
                return new ResponseEntity<>(Map.of("error", "Not Found",
                        "message", "Role not found!",
                        "path", "/api/admin/user/role",
                        "status", 404), HttpStatus.NOT_FOUND);
            }

            User user = details.getUser();

            User userToModify = userService.findUserByEmail(role.getUser());
            if (userToModify == null) {
                return new ResponseEntity<>(Map.of("error", "Not Found",
                        "message", "User not found!",
                        "path", "/api/admin/user/role",
                        "status", 404), HttpStatus.NOT_FOUND);
            }

            Set<Group> groups = userToModify.getUserGroups();
            Set<String> roles = new TreeSet<>();
            for (Group g: groups) {
                roles.add(g.getCode());
            }

            System.out.println("1="+roleString+" ");
            boolean foundRole = roles.contains(roleString);

            switch (role.getOperation().toLowerCase(Locale.ROOT)) {
                case "grant":
                    if (foundRole) {
                        return new ResponseEntity<>(Map.of("error", "Bad Request",
                                "message", "Role already exist!",
                                "path", "/api/admin/user/role",
                                "status", 400), HttpStatus.BAD_REQUEST);
                    }
                    if (user.getEmail().equalsIgnoreCase(role.getUser())) {
                        return new ResponseEntity<>(Map.of("error", "Bad Request",
                                "message", "The user cannot combine administrative and business roles!",
                                "path", "/api/admin/user/role",
                                "status", 400), HttpStatus.BAD_REQUEST);
                    }
                    if (roleString.equalsIgnoreCase("role_administrator")) {
                        return new ResponseEntity<>(Map.of("error", "Bad Request",
                                "message", "The user cannot combine administrative and business roles!",
                                "path", "/api/admin/user/role",
                                "status", 400), HttpStatus.BAD_REQUEST);
                    }

                    roles.add(roleString);
                    userToModify.addUserGroups(groupService.findByCode(roleString));
                    break;
                case "remove":
                    if (!foundRole) {
                        return new ResponseEntity<>(Map.of("error", "Bad Request",
                                "message", "The user does not have a role!",
                                "path", "/api/admin/user/role",
                                "status", 400), HttpStatus.BAD_REQUEST);
                    }
                    if (roles.size() == 0) {
                        return new ResponseEntity<>(Map.of("error", "Bad Request",
                                "message", "The user does not have a role!",
                                "path", "/api/admin/user/role",
                                "status", 400), HttpStatus.BAD_REQUEST);
                    }
                    if (user.getEmail().equalsIgnoreCase(role.getUser())) {
                        return new ResponseEntity<>(Map.of("error", "Bad Request",
                                "message", "Can't remove ADMINISTRATOR role!",
                                "path", "/api/admin/user/role",
                                "status", 400), HttpStatus.BAD_REQUEST);
                    }
                    if (roles.size() == 1) {
                        return new ResponseEntity<>(Map.of("error", "Bad Request",
                                "message", "The user must have at least one role!",
                                "path", "/api/admin/user/role",
                                "status", 400), HttpStatus.BAD_REQUEST);
                    }
                    roles.remove(roleString);
                    userToModify.removeUserGroups(groupService.findByCode(roleString));

                    break;
                default:
            }

            User user1 = userService.updateRole(userToModify);
            if (user1 == null) {

            }
            Set<Group> groups1 = user1.getUserGroups();
            Set<String> roles1 = new TreeSet<>();
            for (Group g: groups1) {
                roles1.add(g.getCode());
            }

            return new ResponseEntity<>(Map.of("id", user1.getId(),
                    "name", user1.getName(),
                    "lastname", user1.getLastname(),
                    "email", user1.getEmail().toLowerCase(),
                    "roles", roles), HttpStatus.OK);
        }
    }

    @PutMapping("api/admin/user/access")
    public Object lockUnlockUser(@AuthenticationPrincipal UserDetailsImpl details, @Valid @RequestBody Key key) {
        String path = "/api/admin/user/access";
        String userEmail = key.getUser();

        //event.setAction(operation.toUpperCase(Locale.ROOT));


        if (details == null) {
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        } else {
            User user = details.getUser();
            User userToModify = userService.findUserByEmail(key.getUser());
            if (userToModify == null) {
                return new ResponseEntity<>(Map.of("error", "Not Found",
                        "message", "User not found!",
                        "path", "/api/admin/user/access",
                        "status", 404), HttpStatus.NOT_FOUND);
            }

            switch (key.getOperation().toLowerCase(Locale.ROOT)) {
                case "lock":
                    if (user.getEmail().equalsIgnoreCase(key.getUser())) {
                        return new ResponseEntity<>(Map.of("error", "Bad Request",
                                "message", "Can't lock the ADMINISTRATOR!",
                                "path", "/api/admin/user/access",
                                "status", 400), HttpStatus.BAD_REQUEST);
                    }

                    userToModify.setLocked(true);
                    User userLocked = userService.update(userToModify);
                    lockedAccounts.lockUser(userLocked.getEmail());//lock
                    return new ResponseEntity<>(Map.of(
                            "status", "User "+key.getUser().toLowerCase()+" locked!"
                    ), HttpStatus.OK);
                case "unlock":

                    loginAttemptService.reset(userEmail.toLowerCase());
                    userToModify.setLocked(false);
                    User userUnLocked = userService.update(userToModify);
                    lockedAccounts.unLockUser(userUnLocked.getEmail());//unlock
                    return new ResponseEntity<>(Map.of(
                            "status", "User "+key.getUser().toLowerCase()+" unlocked!"
                    ), HttpStatus.OK);
                default:
            }

            return new ResponseEntity<>(Map.of("error", "Bad Request",
                    "message", "Operation not supported!",
                    "path", "/api/admin/user/access",
                    "status", 400), HttpStatus.BAD_REQUEST);



        }
    }

    @DeleteMapping("api/admin/user/{email}")
    public Object deleteUser(@AuthenticationPrincipal UserDetailsImpl details, @PathVariable String email) {
        if (details == null) {
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        } else {
            User user = details.getUser();
            if (user.getEmail().equalsIgnoreCase(email)) {
                return new ResponseEntity<>(Map.of("error", "Bad Request",
                        "message", "Can't remove ADMINISTRATOR role!",
                        "path", "/api/admin/user/"+email,
                        "status", 400), HttpStatus.BAD_REQUEST);
            }
            User userToDelete = userService.findUserByEmail(email);
            if (userToDelete == null) {
                return new ResponseEntity<>(Map.of("error", "Not Found",
                        "message", "User not found!",
                        "path", "/api/admin/user/"+email,
                        "status", 404), HttpStatus.NOT_FOUND);
            }
            userService.delete(userToDelete);

            return new ResponseEntity<>(Map.of("user", email,
                    "status", "Deleted successfully!"), HttpStatus.OK);
        }
    }

    @GetMapping("api/admin/user")
    public Object getUsers() {
        //return store.getUserStore();
        List<Object> users = new ArrayList<>();
        List<User> userList = (List<User>) userService.getUsers();
        for (User user: userList) {
            Set<Group> groups = user.getUserGroups();
            Set<String> roles = new TreeSet<>();
            for (Group g: groups) {
                roles.add(g.getCode());
            }
            if (roles.contains("ROLE_ADMINISTRATOR")) {
                //continue;
            }
            users.add(Map.of("id", user.getId(),
                    "name", user.getName(),
                    "lastname", user.getLastname(),
                    "email", user.getEmail().toLowerCase(),
                    "roles", roles));
        }
        //return userRepo.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("api/security/events")
    public Object getEvents() {
        return null;
    }
}

