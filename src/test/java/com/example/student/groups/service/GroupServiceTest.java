package com.example.student.groups.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.model.Invites;
import com.example.student.groups.model.TribeGroup;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InviteRepository inviteRepository;
    @InjectMocks
    private GroupService groupService;

    @Test
    public void testCreateGroup_Success() throws AlreadyExistsException, NotFoundException {
        String adminId = "adminId";
        TribeGroup tribeGroup = new TribeGroup();
        tribeGroup.setTribeName("Test Group");
        tribeGroup.setTribeDescription("This is a test group");
        tribeGroup.setAdminId(adminId);

        TribeUser user = new TribeUser();
        user.setFirebaseId(adminId);

        when(groupRepository.getGroupByAdminId(adminId)).thenReturn(Optional.empty());
        when(userRepository.findUserByFirebaseId(adminId)).thenReturn(Optional.of(user));
        when(groupRepository.save(ArgumentMatchers.any(TribeGroup.class))).thenReturn(tribeGroup);

        groupService.createGroup(tribeGroup);

        verify(groupRepository, Mockito.times(1)).getGroupByAdminId(adminId);
        verify(userRepository, Mockito.times(1)).findUserByFirebaseId(adminId);
        verify(groupRepository, Mockito.times(1)).save(tribeGroup);
        verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void testCreateGroup_UserAlreadyHasCreatedGroup(){
        String adminId = "adminId";
        TribeGroup tribeGroup = new TribeGroup();
        tribeGroup.setTribeName("Test Group");
        tribeGroup.setTribeDescription("This is a test group");
        tribeGroup.setAdminId(adminId);

        when(groupRepository.getGroupByAdminId(adminId)).thenReturn(Optional.of(tribeGroup));


        assertThrows(AlreadyExistsException.class, () -> groupService.createGroup(tribeGroup));

        verify(groupRepository, Mockito.times(1)).getGroupByAdminId(adminId);
        verify(groupRepository, never()).save(any(TribeGroup.class));
        verify(userRepository, never()).save(any(TribeUser.class));
    }

    @Test
    public void testCreateGroup_UserNotFound() {

        String adminId = "adminId";
        TribeGroup tribeGroup = new TribeGroup();
        tribeGroup.setTribeName("Test Group");
        tribeGroup.setTribeDescription("This is a test group");
        tribeGroup.setAdminId(adminId);

        when(groupRepository.getGroupByAdminId(adminId)).thenReturn(Optional.empty());
        when(userRepository.findUserByFirebaseId(adminId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            groupService.createGroup(tribeGroup);
        });

        verify(groupRepository, Mockito.times(1)).getGroupByAdminId(adminId);
        verify(userRepository, Mockito.times(1)).findUserByFirebaseId(adminId);
        verify(groupRepository, never()).save(any(TribeGroup.class));
        verify(userRepository, never()).save(any(TribeUser.class));
    }

    @Test
    public void testInviteUserToGroup_Success() throws Exception {

        String adminFirebaseId = "adminId";
        String userEmail = "user@example.com";

        TribeGroup group = new TribeGroup();
        group.setId(1L);
        group.setTribeName("Test Group");
        group.setTribeDescription("This is a test group");
        group.setAdminId(adminFirebaseId);

        TribeUser userToAdd = new TribeUser();
        userToAdd.setId(2L);
        userToAdd.setFirebaseId("userFirebaseId");
        userToAdd.setEmail(userEmail);

        when(groupRepository.getGroupByAdminId(adminFirebaseId)).thenReturn(Optional.of(group));
        when(userRepository.getUserByEmail(userEmail)).thenReturn(Optional.of(userToAdd));
        when(inviteRepository.save(any(Invites.class))).thenReturn(new Invites());

        groupService.inviteUserToGroup(adminFirebaseId, userEmail);

        verify(groupRepository, Mockito.times(1)).getGroupByAdminId(adminFirebaseId);
        verify(userRepository, Mockito.times(1)).getUserByEmail(userEmail);
        verify(inviteRepository, Mockito.times(1)).save(any(Invites.class));
    }

    @Test
    public void testInviteUserToGroup_UserAlreadyBelongsToGroup() {

        String adminFirebaseId = "adminId";
        String userEmail = "user@example.com";

        TribeGroup group = new TribeGroup();
        group.setId(1L);
        group.setTribeName("Test Group");
        group.setTribeDescription("This is a test group");
        group.setAdminId(adminFirebaseId);

        TribeUser userToAdd = new TribeUser();
        userToAdd.setId(2L);
        userToAdd.setFirebaseId("userFirebaseId");
        userToAdd.setEmail(userEmail);
        userToAdd.addGroup(group.getId());

        when(groupRepository.getGroupByAdminId(adminFirebaseId)).thenReturn(Optional.of(group));
        when(userRepository.getUserByEmail(userEmail)).thenReturn(Optional.of(userToAdd));

        assertThrows(AlreadyExistsException.class, () -> groupService.inviteUserToGroup(adminFirebaseId, userEmail));

        verify(groupRepository, Mockito.times(1)).getGroupByAdminId(adminFirebaseId);
        verify(userRepository, Mockito.times(1)).getUserByEmail(userEmail);
        verify(inviteRepository, never()).save(any(Invites.class));

        assertTrue(userToAdd.getGroups().contains(group.getId()));
    }

    @Test
    public void testInviteUserToGroup_GroupNotFound(){

        String adminFirebaseId = "adminId";
        String userEmail = "user@example.com";

        when(groupRepository.getGroupByAdminId(adminFirebaseId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> groupService.inviteUserToGroup(adminFirebaseId, userEmail));

        verify(groupRepository, Mockito.times(1)).getGroupByAdminId(adminFirebaseId);
        verify(userRepository, never()).getUserByEmail(anyString());
        verify(inviteRepository, never()).save(any(Invites.class));
    }
}