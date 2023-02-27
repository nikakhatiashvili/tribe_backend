package com.example.student.student.data;

import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserLoader implements CommandLineRunner {
    private static final String CSV_FILE_PATH = "C:\\Users\\GeoComputers\\Downloads\\user_data.csv";

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // read the user details from the CSV file
        List<TribeUser> users = readUsersFromCsvFile(CSV_FILE_PATH);
        System.out.println(users);
        // save the users to the database
        userRepository.saveAll(users);

        // print the saved users
        System.out.println(userRepository.findAll());
    }

    private List<TribeUser> readUsersFromCsvFile(String csvFilePath) throws IOException {
        List<TribeUser> users = new ArrayList<>();

        File file = ResourceUtils.getFile(csvFilePath);
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");

            System.out.println(data[1]);
            TribeUser user = new TribeUser();
            user.setName(data[0]);
            user.setEmail(data[1]);
            user.setFirebaseId(data[2]);
            user.setTimezone(data[4]);

            users.add(user);
        }

        bufferedReader.close();

        return users;
    }
}