package org.prathmesh.BankingBackend.Controller;

import org.prathmesh.BankingBackend.Dto.UserCreateRequest;
import org.prathmesh.BankingBackend.Dto.UserResponse;
import org.prathmesh.BankingBackend.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody UserCreateRequest request) {

        UserResponse response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<UserResponse> getUserByEmail(
            @RequestParam String email) {

        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {

        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

}
