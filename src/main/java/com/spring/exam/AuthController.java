package com.spring.exam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @IsAuth(isAuth = Auth.AUTH)
    @GetMapping("/auth")
    public boolean acceptUser(@RequestParam String userId){
        return true;
    }

    @IsAuth
    @GetMapping("/nonauth")
    public boolean acceptAll(@RequestParam String userId){
        return true;
    }
}
