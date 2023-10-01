package com.example.student.user;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {


    @Value(value = "classpath:configs/task-manager-f7b24-firebase-adminsdk-yewpu-18e72f35bd.json")
    private Resource serviceAccountResource;

    @Bean
    public FirebaseApp createFireBaseApp() throws IOException {
        InputStream serviceAccount = serviceAccountResource.getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        System.out.println("Firebase config initialized");

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    @DependsOn(value = "createFireBaseApp")
    public FirebaseAuth createFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

}
