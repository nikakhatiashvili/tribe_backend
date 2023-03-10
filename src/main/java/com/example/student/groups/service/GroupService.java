package com.example.student.groups.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.groups.model.Invites;
import com.example.student.groups.model.TribeGroup;
import com.example.student.student.TribeUser;
import com.example.student.student.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final InviteRepository inviteRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository, InviteRepository inviteRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.inviteRepository = inviteRepository;
    }

    public void createGroup(TribeGroup group) throws AlreadyExistsException, NotFoundException {
        if (groupRepository.getGroupByAdminId(group.getAdminId()).isPresent()) {
            throw new AlreadyExistsException("User has already created group");
        }

        TribeUser user = findUserByFirebaseId(group.getAdminId());
        TribeGroup savedTribeGroup = groupRepository.save(group);

        user.addGroup(savedTribeGroup.getId());
        userRepository.save(user);
    }


    public List<TribeGroup> getGroups() {
        return groupRepository.findAll();
    }

    public void removeUserFromGroup(String adminFirebaseId, String userEmail) throws Exception {
        TribeGroup group = getGroupByAdminId(adminFirebaseId);
        TribeUser userToRemove = getUserByEmail(userEmail);

        if (userToRemove.getGroups().contains(group.getId())) {
            userToRemove.removeGroup(group.getId());
        } else {
            throw new NotFoundException("user is not in your group");
        }
        userRepository.save(userToRemove);
    }

    public List<Invites> getInvites(String firebaseId) {
        return inviteRepository.findByUserFirebaseId(firebaseId);
    }

    public void inviteUserToGroup(String adminFirebaseId, String userEmail) throws Exception {
        TribeGroup group = getGroupByAdminId(adminFirebaseId);
        TribeUser userToAdd = getUserByEmail(userEmail);

        if (userToAdd.getGroups().contains(group.getId())) {
            throw new AlreadyExistsException("User already belongs to this group");
        }

        Invites newInvite = new Invites(group.getTribeName(), group.getTribeDescription(), userToAdd.getFirebaseId(), group.getId());
        inviteRepository.save(newInvite);
    }

    public void invite(Long id, Integer accept, String firebaseId) throws NotFoundException {
        Invites invite = inviteRepository.findInviteById(id).orElseThrow(() -> new NotFoundException("invite not found"));
        if (!Objects.equals(invite.getUserFirebaseId(), firebaseId))
            throw new NotFoundException("user with this invite not found");
        if (accept == 1) {
            addUserToGroup(invite.getGroupId(), invite.getUserFirebaseId());
            inviteRepository.delete(invite);
        } else if (accept == -1) {
            inviteRepository.delete(invite);
        }
    }

    private void addUserToGroup(Long id, String firebaseId) throws NotFoundException {
        TribeGroup group = groupRepository.getGroupById(id)
                .orElseThrow(() -> new NotFoundException(" ID does not match to group ID"));
        TribeUser userToAdd = findUserByFirebaseId(firebaseId);
        userToAdd.addGroup(group.getId());
        userRepository.save(userToAdd);
    }


    public List<TribeUser> getUsersInGroup(String firebaseId, Long id) throws Exception {
        TribeUser user = findUserByFirebaseId(firebaseId);
        if (user.getGroups().contains(id)){
            return userRepository.findByGroupsContaining(id);
        }else throw new UnauthorizedException("you dont have access to this");
    }

    private TribeGroup getGroupByAdminId(String adminFirebaseId) throws NotFoundException {
        return groupRepository.getGroupByAdminId(adminFirebaseId)
                .orElseThrow(() -> new NotFoundException("Firebase ID does not match admin ID"));
    }

    private TribeUser findUserByFirebaseId(String firebaseId) throws NotFoundException {
        return userRepository.findUserByFirebaseId(firebaseId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private TribeUser getUserByEmail(String userEmail) throws NotFoundException {
        return userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));
    }
}
