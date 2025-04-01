package com.yithsopheakktra.co.springblogapi.init;

import com.yithsopheakktra.co.springblogapi.domain.User;
import com.yithsopheakktra.co.springblogapi.features.user.UserRepository;
import com.yithsopheakktra.co.springblogapi.utils.enums.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInit {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostConstruct
    public void init() {
        if(userRepository.count() < 1){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("qwerqwer"));
            admin.setEmail("admin@gmail.com");
            admin.setRole(Role.ADMIN);
            admin.setUuid(UUID.randomUUID().toString());

            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("qwerqwer"));
            user.setEmail("user@gmail.com");
            user.setRole(Role.USER);
            user.setUuid(UUID.randomUUID().toString());

            userRepository.save(user);
            userRepository.save(admin);
        }
    }

}
