package com.example.student.student.data;

import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StudentConfig {

//    @Bean
//    CommandLineRunner commandLineRunner(
//            UserRepository repository
//    ) {
//        return args -> {
//            TribeUser mariam = new TribeUser(
//                    "sad",
//                    "sa",
//                    "mariam@gmail.com"
//            );
//            TribeUser alex = new TribeUser(
//                    "sadasd",
//                    "sad",
//                    "alex@gmail.com"
//            );
//            repository.saveAll(
//                    List.of(mariam,alex)
//            );
//        };
//    }
}
