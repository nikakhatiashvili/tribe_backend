package com.example.student.groups.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.model.TribeGroup;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private GroupService groupService;

    @Test
    public void testCreateGroup_Success() throws AlreadyExistsException, NotFoundException {
        // Setup
        String adminId = "adminId";
        TribeGroup tribeGroup = new TribeGroup();
        tribeGroup.setTribeName("Test Group");
        tribeGroup.setTribeDescription("This is a test group");
        tribeGroup.setAdminId(adminId);

        TribeUser user = new TribeUser();
        user.setFirebaseId(adminId);

        // Mock repository methods
        when(groupRepository.getGroupByAdminId(adminId)).thenReturn(Optional.empty());
        when(userRepository.findUserByFirebaseId(adminId)).thenReturn(Optional.of(user));
        when(groupRepository.save(ArgumentMatchers.any(TribeGroup.class))).thenReturn(tribeGroup);

        // Execute
        groupService.createGroup(tribeGroup);

        // Verify
        verify(groupRepository, Mockito.times(1)).getGroupByAdminId(adminId);
        verify(userRepository, Mockito.times(1)).findUserByFirebaseId(adminId);
        verify(groupRepository, Mockito.times(1)).save(tribeGroup);
        verify(userRepository, Mockito.times(1)).save(user);
    }

}