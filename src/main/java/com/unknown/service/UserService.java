package com.unknown.service;


import com.unknown.dto.CreateUserRequest;
import com.unknown.dto.CustomUserDetails;
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
import org.springframework.security.authentication.DisabledException;
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
public class UserService  {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    private final CustomUserDetailsService customUserDetailsService;

    @Transactional
    public void create(CreateUserRequest request) {
        var user = new User();
        try {
            if (emailExists(request.email())) {
                throw new ValidationException("Email exists!");
            }
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setPhoneNumber(request.phoneNumber());
            user.setStatus(true);
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
                if(!authentication.isAuthenticated()){
                    token = null;
                }else {
                    var now = Instant.now();
                    var expiry = 36000L;
                    var claims =
                            JwtClaimsSet.builder()
                                    .issuer("example.io")
                                    .issuedAt(now)
                                    .expiresAt(now.plusSeconds(expiry))
                                    .subject("something")
//                            .claim("roles", scope)
                                    .build();

                    token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
                    //}
                }
        } catch (DisabledException d){
            log.error("User is disabled",d.getMessage());
        }catch (BadCredentialsException b){
            log.error("Invalid Credentials",b.getMessage());
        } catch (Exception e) {
            log.error("Invalid Credentials",e);
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
        return customUserDetailsService
                .loadUserByUsername(email);
    }

    public boolean emailExists(String email) {
        boolean isPresent = false;
        if(userRepository.findByEmail(email) != null){
            isPresent = true;
        }
      return isPresent;
    }

    public boolean userExists(long id) {
        boolean isPresent = false;
        if(userRepository.findById(id) != null){
            isPresent = true;
        }
        return isPresent;
    }



}
