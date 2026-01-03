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

    public UserResponse createUser(UserCreateRequest userCreateRequest) {


        if (userRepository.existsByEmail(userCreateRequest.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFullName(userCreateRequest.fullName());
        user.setEmail(userCreateRequest.email());
        user.setPassword(passwordEncoder.encode(userCreateRequest.password()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    @Transactional
    public void deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        user.setActive(false);

        userRepository.save(user);
    }

}
