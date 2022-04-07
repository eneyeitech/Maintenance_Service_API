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
public class UserController {

    private long id = 0L;
    private List<String> breachedPassword;

    {
        breachedPassword = new ArrayList<>(List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"));
    }

    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;

    @Autowired
    BCryptEncoderConfig b;



    @PostMapping("api/user/new")
    public ResponseEntity<Map<String, Object>> addUser(@AuthenticationPrincipal UserDetailsImpl details, @Valid @RequestBody User user, @RequestParam String type) {
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
            if (details == null) {
                return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
            }
            User loggedInUser = details.getUser();
            Company company = loggedInUser.getCompany();
            if (company == null){
                return new ResponseEntity<>(Map.of("error", "user has no company"), HttpStatus.BAD_REQUEST);
            }
            if (type.equalsIgnoreCase("tenant")){
                user.setCompany(company);
                userService.saveTenant(user);
            } else if (type.equalsIgnoreCase("technician")){
                user.setCompany(company);
                userService.saveTechnician(user);
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

    @PostMapping("api/company/new")
    public Object addCompany(@AuthenticationPrincipal UserDetailsImpl details, @RequestBody Company company){

        if (details == null){
            return new ResponseEntity<>(Map.of("error", "email not valid"), HttpStatus.BAD_REQUEST);
        }
        User user = details.getUser();
        Company addedCompany = companyService.createCompany(company);
        user.setCompany(addedCompany);
        userService.save(user);
        return addedCompany;
    }
}
