package com.ovgusev.init;

import com.ovgusev.domain.User;
import com.ovgusev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDefaultUsersConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private void addUser(String name, String password) {
        userRepository.save(User.of(name, passwordEncoder.encode(password)));
    }

    @PostConstruct
    public void initUserList() {
        addUser("user", "pass");
    }
}
