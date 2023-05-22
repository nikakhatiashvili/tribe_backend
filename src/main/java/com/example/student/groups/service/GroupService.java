package com.example.student.groups.service;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.exceptions.UnauthorizedException;
import com.example.student.groups.model.Invites;
import com.example.student.groups.model.TribeGroup;
import com.example.student.user.TribeUser;
import com.example.student.user.domain.UserRepository;
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

        TribeUser user = findUserById(group.getAdminId());
        TribeGroup savedTribeGroup = groupRepository.save(group);

        user.addGroup(savedTribeGroup.getId());
        userRepository.save(user);
    }

    public List<TribeGroup> getGroups() {
        return groupRepository.findAll();
    }

    public List<TribeGroup> getUserGroups(Long UserId) throws NotFoundException {
        TribeUser user = findUserById(UserId);
        return groupRepository.findAllById(user.getGroups());
    }

    public void removeUserFromGroup(Long UserId, String userEmail) throws NotFoundException {
        TribeGroup group = getGroupByAdminId(UserId);
        TribeUser userToRemove = getUserByEmail(userEmail);

        if (userToRemove.getGroups().contains(group.getId())) {
            userToRemove.removeGroup(group.getId());
        } else {
            throw new NotFoundException("user is not in your group");
        }
        userRepository.save(userToRemove);
    }

    public List<Invites> getInvites(Long userId) {
        return inviteRepository.findByUserBaseId(userId);
    }

    public void inviteUserToGroup(Long adminId, String userEmail) throws AlreadyExistsException, NotFoundException {
        TribeUser admin = findUserById(adminId);
        TribeGroup group = getGroupByAdminId(adminId);
        TribeUser userToAdd = getUserByEmail(userEmail);

        if (userToAdd.getGroups().contains(group.getId())) {
            throw new AlreadyExistsException("User already belongs to this group");
        }
        Invites newInvite = new Invites(
                group.getTribeName(), group.getTribeDescription(),
                userToAdd.getId(), admin.getEmail(), group.getId());
        inviteRepository.save(newInvite);
    }

    public void invite(Long id, Boolean accept, Long userId) throws NotFoundException {
        Invites invite = inviteRepository.
                findInviteById(id).orElseThrow(() -> new NotFoundException("invite not found"));

        if (!Objects.equals(invite.getUserBaseId(), userId))
            throw new NotFoundException("user with this invite not found");
        if (accept) {
            addUserToGroup(invite.getGroupId(), invite.getUserBaseId());
            inviteRepository.delete(invite);
        } else {
            inviteRepository.delete(invite);
        }
    }

    private void addUserToGroup(Long UserId, Long userId) throws NotFoundException {
        TribeGroup group = groupRepository.getGroupById(UserId)
                .orElseThrow(() -> new NotFoundException(" ID does not match to group ID"));
        TribeUser userToAdd = findUserById(userId);
        userToAdd.addGroup(group.getId());
        userRepository.save(userToAdd);
    }

    public void leaveGroup(Long UserId, Long groupId) throws NotFoundException {
        TribeUser user = findUserById(UserId);
        if (user.getGroups().contains(groupId)) {
            user.removeGroup(groupId);
            userRepository.save(user);
        } else {
            throw new NotFoundException("User is not a member of the group");
        }
    }


    public List<TribeUser> getUsersInGroup(Long UserId, Long id) throws Exception {
        TribeUser user = findUserById(UserId);
        if (user.getGroups().contains(id)) {
            return userRepository.findByGroupsContaining(id);
        } else throw new UnauthorizedException("you dont have access to this");
    }

    private TribeGroup getGroupByAdminId(Long userId) throws NotFoundException {
        return groupRepository.getGroupByAdminId(userId)
                .orElseThrow(() -> new NotFoundException("Firebase ID does not match admin ID"));
    }

    private TribeUser findUserById(Long userId) throws NotFoundException {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private TribeUser getUserByEmail(String userEmail) throws NotFoundException {
        return userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));
    }
}
