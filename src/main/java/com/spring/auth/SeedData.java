package com.spring.auth;

import com.spring.auth.model.User;
import com.spring.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Transactional
@Component
@AllArgsConstructor
public class SeedData implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
       var user = new User();
       user.setFirstName("Bill");
       user.setLastName("Kariri");
       user.setEmail("bill@email.com");
       user.setPassword(passwordEncoder.encode("123456"));
       user.setPhoneNumber("254701123456");
       userRepository.save(user);

    }
}
