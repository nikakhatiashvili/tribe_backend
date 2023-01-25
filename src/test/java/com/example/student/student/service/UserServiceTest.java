package com.example.student.student.service;

import com.example.student.groups.service.GroupRepository;
import com.example.student.student.domain.UserRepository;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void testAddUserToGroupSuccess() {
//        TribeUser admin = new TribeUser(1L, "admin", "admin@email.com", "firebaseId");
//        admin.setGroupId(1L);
//        TribeUser userToAdd = new TribeUser(2L, "userToAdd", "userToAdd@email.com", "firebaseId2");
//
//        TribeGroup group = new TribeGroup("S", "group1", "firebaseId");
//        group.setAdminId("firebaseId");
//
//        when(userRepository.findUserByFirebaseId("firebaseId")).thenReturn(Optional.of(admin));
//        when(groupRepository.getGroupByAdminId("firebaseId")).thenReturn(Optional.of(group));
//        when(userRepository.getUserByEmail("userToAdd@email.com")).thenReturn(Optional.of(userToAdd));
//
//        userController.addUserToGroup("firebaseId", "userToAdd@email.com");
//
//        verify(userRepository, times(1)).saveUser(userToAdd);
//        assertEquals(userToAdd.getGroupId(), admin.getGroupId());
//    }

}
