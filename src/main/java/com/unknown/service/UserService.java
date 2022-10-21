package com.unknown.service;


import com.unknown.dto.CreateUserRequest;
import com.unknown.dto.CreateUserResponse;
import com.unknown.model.User;
import com.unknown.repository.UserRepository;


import javax.validation.ValidationException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(CreateUserRequest request) {
        var user = new User();
        if (emailExists(request.email())) {
            throw new ValidationException("Email exists!");
        }
        if (!request.password().equals(request.repeatPassword())) {
            throw new ValidationException("Passwords don't match!");
        }

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());

        userRepository.save(user);
    }
    public void editUser(){}

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) userRepository
                .findByEmail(email)
                .orElseThrow(
                        () ->
                                new UsernameNotFoundException(
                                        format("User with email - %s, not found", email)));
    }
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
