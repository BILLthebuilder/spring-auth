package com.unknown;

import com.unknown.model.User;
import com.unknown.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

@Transactional
@Component
@AllArgsConstructor
@Slf4j
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
       log.info("Creating user={}",String.valueOf(user));
       userRepository.save(user);
    }
}
