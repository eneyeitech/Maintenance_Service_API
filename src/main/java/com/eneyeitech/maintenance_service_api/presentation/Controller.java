package com.eneyeitech.maintenance_service_api.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/home")
    public String homePage(){
        return "Home";
    }
}
