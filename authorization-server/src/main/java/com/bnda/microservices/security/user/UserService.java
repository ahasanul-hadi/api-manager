package com.bnda.microservices.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(UserDTO userDTO) {
        User user = User.builder()
                        .username(userDTO.getUsername())
                        .password(passwordEncoder.encode(userDTO.getPassword()))
                        .fullName(userDTO.getFirstName() + " " + userDTO.getLastName())
                        .email(userDTO.getEmail())
                        .role(Role.ROLE_USER)
                        .build();

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user)).toList();
    }

    private UserDTO convertEntityToDto(User user){
        UserDTO userDTO = new UserDTO();
        String[] name = user.getFullName().split(" ");
        userDTO.setFirstName(name[0]);
        userDTO.setLastName(name[1]);
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
