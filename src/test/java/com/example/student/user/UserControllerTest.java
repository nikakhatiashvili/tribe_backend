//package com.example.student.user;
//
//import com.example.student.user.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.UserRecord;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UserControllerTest {
//
//    private MockMvc mockMvc;
//
//
//    @Mock
//    private FirebaseAuth firebaseAuth;
//
//    @InjectMocks
//    private UserController userController;
//
//    @Mock
//    private UserRecord mockUserRecord;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    public void testRegisterNewUser_Success() throws Exception {
//        // Arrange
//        when(mockUserRecord.getUid()).thenReturn("firebaseId");
//        when(mockUserRecord.getEmail()).thenReturn("email@example.com");
//        when(firebaseAuth.createUser(any(UserRecord.CreateRequest.class))).thenReturn(mockUserRecord);
//
//        // Act & Assert
//        mockMvc.perform(post("/api/v1/user/signup")
//                        .param("timezone", "UTC")
//                        .param("username", "testUser")
//                        .param("email", "email@example.com")
//                        .param("password", "password")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
//                .andExpect(jsonPath("$.message").value("Sign up was successful"));
//    }
//}