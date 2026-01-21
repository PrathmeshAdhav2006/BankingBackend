package org.prathmesh.BankingBackend.Service;

import org.prathmesh.BankingBackend.Dto.UserCreateRequest;
import org.prathmesh.BankingBackend.Dto.UserResponse;
import org.prathmesh.BankingBackend.Enums.Role;
import org.prathmesh.BankingBackend.Models.User;
import org.prathmesh.BankingBackend.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =====================================================
    // REGISTER USER
    // =====================================================

    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setActive(true);

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getCreatedAt()
        );
    }

    // =====================================================
    // INTERNAL USE (ENTITY)
    // =====================================================

    @Transactional(readOnly = true)
    public User getByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    // =====================================================
    // API RESPONSE METHODS (DTO)
    // =====================================================

    @Transactional(readOnly = true)
    public UserResponse getUserResponseById(Long id) {

        User user = getById(id);

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public UserResponse getUserResponseByEmail(String email) {

        User user = getByEmail(email);

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    // =====================================================
    // DEACTIVATE USER
    // =====================================================

    @Transactional
    public void deactivateUser(Long id) {

        User user = getById(id);

        user.setActive(false);
        // no save() needed (dirty checking)
    }
}
