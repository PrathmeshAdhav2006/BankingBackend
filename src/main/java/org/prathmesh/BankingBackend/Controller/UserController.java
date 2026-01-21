package org.prathmesh.BankingBackend.Controller;

import org.prathmesh.BankingBackend.Dto.UserCreateRequest;
import org.prathmesh.BankingBackend.Dto.UserResponse;
import org.prathmesh.BankingBackend.Service.UserService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    // ---------------- REGISTER ----------------
    @PostMapping
    public ResponseEntity<UserResponse> register(
            @RequestBody UserCreateRequest request) {

        return new ResponseEntity<>(
                userService.createUser(request),
                HttpStatus.CREATED
        );
    }

    // ---------------- PROFILE ----------------
    @GetMapping("/me")
    public ResponseEntity<UserResponse> myProfile(
            Authentication authentication) {

        return ResponseEntity.ok(
                userService.getUserResponseByEmail(
                        authentication.getName())
        );
    }

    // ---------------- DEACTIVATE ----------------
    @PutMapping("/deactivate")
    public ResponseEntity<Void> deactivate(
            Authentication authentication) {

        userService.deactivateUser(
                userService.getByEmail(
                        authentication.getName()).getId()
        );

        return ResponseEntity.noContent().build();
    }
}
