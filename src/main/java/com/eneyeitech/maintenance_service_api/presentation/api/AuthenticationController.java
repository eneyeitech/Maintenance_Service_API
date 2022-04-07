package com.eneyeitech.maintenance_service_api.presentation.api;

import com.eneyeitech.maintenance_service_api.business.*;
import com.eneyeitech.maintenance_service_api.utility.BCryptEncoderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@RestController
public class AuthenticationController {

    private long id = 0L;
    private List<String> breachedPassword;

    {
        breachedPassword = new ArrayList<>(List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"));
    }

    @Autowired
    UserService userService;

    @Autowired
    BCryptEncoderConfig b;



    @PostMapping("api/auth/signup")
    public ResponseEntity<Map<String, Object>> signUp(@Valid @RequestBody User user, @RequestParam String type) {
        if (user.getEmail().isBlank() || user.getEmail().equals("") || user.getEmail().isEmpty()){
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(Map.of("error", "email empty"), HttpStatus.BAD_REQUEST);
        }
        if (!user.getEmail().matches("\\w+(@acme.com)$")){
            //return new ResponseEntity<>(Map.of("error", "email incorrect"), HttpStatus.BAD_REQUEST);
        }
        if (user.getLastname().isBlank() || user.getLastname().equals("") || user.getLastname().isEmpty()){
            return new ResponseEntity<>(Map.of("error", "lastname not valid"), HttpStatus.BAD_REQUEST);
        }
        if (user.getPassword().isBlank() || user.getPassword().equals("") || user.getPassword().isEmpty() ){
            return new ResponseEntity<>(Map.of("error", "password not valid"), HttpStatus.BAD_REQUEST);
        }
        if (breachedPassword.contains(user.getPassword())) {
            return new ResponseEntity<>(Map.of("message", "The password is in the hacker's database!",
                    "error","Bad Request",
                    "status", 400,
                    "path", "/api/auth/signup"), HttpStatus.BAD_REQUEST);
        }
        if (user.getName().isBlank() || user.getName().equals("") || user.getName().isEmpty()){
            return new ResponseEntity<>(Map.of("error", "name not valid"), HttpStatus.BAD_REQUEST);
        }
        //return new ResponseEntity<>(user, HttpStatus.OK);
        //store.getUserStore().put(user.getEmail(), user);
        //id++;
        user.setPassword(b.getEncoder().encode(user.getPassword()));
        //user.setId(id);
        if (userService.hasUser(user)) {
            return new ResponseEntity<>(Map.of("message", "User exist!", "error","Bad Request","status", 400), HttpStatus.BAD_REQUEST);
        }else{
            user.setLocked(false);
            if(type.isEmpty() || type.isBlank()){
                userService.save(user);
            } else if (type.equalsIgnoreCase("manager")){
                userService.saveManager(user);
            } else {
                return new ResponseEntity<>(Map.of("error", "type is not valid"), HttpStatus.BAD_REQUEST);
            }

        }

        Set<Group> groups = user.getUserGroups();
        Set<String> roles = new TreeSet<>();
        for (Group g: groups) {
            roles.add(g.getCode());
        }

        return new ResponseEntity<>(Map.of("id", user.getId(),
                "name", user.getName(),
                "lastname", user.getLastname(),
                "email", user.getEmail(), "roles", roles), HttpStatus.OK);
    }

    @PostMapping("api/auth/changepass")
    public ResponseEntity<Object> changePassword(@AuthenticationPrincipal UserDetailsImpl details, @Valid @RequestBody Password password) {
        if (details == null) {
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        } else {
            if (breachedPassword.contains(password.getNew_password())) {
                return new ResponseEntity<>(Map.of("message", "The password is in the hacker's database!", "error","Bad Request","status", 400), HttpStatus.BAD_REQUEST);
            }

            User user = details.getUser();

            if (b.getEncoder().matches(password.getNew_password(), user.getPassword())) {
                return new ResponseEntity<>(Map.of("message", "The passwords must be different!",
                        "error","Bad Request",
                        "status", 400,
                        "path", "/api/auth/changepass"), HttpStatus.BAD_REQUEST);
            }

            user.setPassword(b.getEncoder().encode(password.getNew_password()));
            userService.updatePassword(user);

            return new ResponseEntity<>(Map.of("email", user.getEmail(), "status", "The password has been updated successfully"), HttpStatus.OK);
        }
    }
}

