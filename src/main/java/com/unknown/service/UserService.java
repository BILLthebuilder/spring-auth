package com.unknown.service;


import com.unknown.dto.CreateUserRequest;
import com.unknown.dto.LoginUserRequest;
import com.unknown.dto.UpdateUserRequest;
import com.unknown.exception.UserCrudException;
import com.unknown.model.User;
import com.unknown.repository.UserRepository;


import javax.validation.ValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @Transactional
    public void create(CreateUserRequest request) {
        var user = new User();
        try {
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
        }catch (Exception ex){
            log.error("Error creating user=%s",ex);
            throw new UserCrudException("Unable to create user");
        }
    }

    public String login(LoginUserRequest request) {
        String token = null;
        try {
            var authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(request.email(), request.password()));

            var user = (User) authentication.getPrincipal();

            var now = Instant.now();
            var expiry = 36000L;
//            var scope =
//                    authentication.getAuthorities().stream()
//                            .map(GrantedAuthority::getAuthority)
//                            .collect(joining(" "));

            var claims =
                    JwtClaimsSet.builder()
                            .issuer("example.io")
                            .issuedAt(now)
                            .expiresAt(now.plusSeconds(expiry))
                            .subject(format("%s,%s", user.getId(), user.getEmail()))
//                            .claim("roles", scope)
                            .build();

            token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        } catch (BadCredentialsException e) {
            log.error("Invalid Credentials=%s",e);
            throw new BadCredentialsException("Invalid Credentials");
        }
        return token;
    }



    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = loadUserByUsername(authentication.getName());
        return user;
    }

    @Transactional
    public void update(long id, UpdateUserRequest request) {
        var user = new User();
        try {
            if (!userExists(id)) {
                throw new UsernameNotFoundException("User not found");
            }
            user.setEmail(request.email());
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setPhoneNumber(request.phoneNumber());
            userRepository.save(user);
        }catch (Exception ex){
            log.error("Unable to update user=%s",ex);
            throw new UserCrudException("Unable to update user");
        }
    }

    @Transactional
    public void delete(long id) {
        var user = new User();
        try {
            if (!userExists(id)) {
                throw new UsernameNotFoundException("User not found");
            }
            user.setStatus(false);
            userRepository.save(user);
        }catch (Exception ex){
            log.error("Unable to delete user=%s",ex);
            throw new UserCrudException("Unable to delete user");
        }

    }




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

    public boolean userExists(long id) {
        return userRepository.findById(id).isPresent();
    }



}
