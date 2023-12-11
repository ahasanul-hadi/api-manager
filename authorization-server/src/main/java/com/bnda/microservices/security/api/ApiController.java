package com.bnda.microservices.security.api;

import com.bnda.microservices.security.user.User;
import com.bnda.microservices.security.user.UserDTO;
import com.bnda.microservices.security.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class ApiController {

    private final UserService userService;

    @GetMapping("/welcome")
    ResponseEntity fun(){
        return ResponseEntity.ok("Hello there!");
    }

    @GetMapping("/all")
    ResponseEntity fun2(){
        return ResponseEntity.ok("Hello all!");
    }

    @GetMapping("/login")
    ResponseEntity login(){
        return ResponseEntity.ok("Hello login here!");
    }

    @PostMapping("/save")
    ResponseEntity saveuser(UserDTO user){
        User newUser = userService.saveUser(user);
        return ResponseEntity.ok("Saved with id: "+user.getId());
    }
}
