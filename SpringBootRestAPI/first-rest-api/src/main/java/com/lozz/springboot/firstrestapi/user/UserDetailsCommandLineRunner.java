package com.lozz.springboot.firstrestapi.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserDetailsCommandLineRunner implements CommandLineRunner {

    public UserDetailsCommandLineRunner(UserDetailsRepository repository) {
        super();
        this.repository = repository;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private UserDetailsRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.save(new UserDetails("Lozz", "Admin"));
        repository.save(new UserDetails("Bob", "Admin"));
        repository.save(new UserDetails("Ashe", "User"));


        //List<UserDetails> users = repository.findAll();
        List<UserDetails> users = repository.findByRole("Admin");

        users.forEach(user -> logger.info(user.toString()));



    }
}
