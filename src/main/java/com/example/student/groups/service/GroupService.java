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

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private final InviteRepository inviteRepository;
    @Autowired
    private GroupMembershipRepository groupMembershipRepository;

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

        TribeUser user = userRepository.findUserByFirebaseId(group.getAdminId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        group.addMember(user);


        TribeGroup savedTribeGroup = groupRepository.save(group);

        user.addGroup(savedTribeGroup.getId());
        userRepository.save(user);
    }

    public void inviteUserToGroup(String adminFirebaseId, String userEmail) throws Exception {
        TribeGroup group = groupRepository.getGroupByAdminId(adminFirebaseId)
                .orElseThrow(() -> new UnauthorizedException("Firebase ID does not match admin ID"));

        TribeUser userToAdd = userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        if (userToAdd.getGroups().contains(group.getId())) {
            throw new AlreadyExistsException("User already belongs to this group");
        }

        Invites newInvite = new Invites(group.getTribeName(), group.getTribeDescription(), userToAdd.getFirebaseId());

        inviteRepository.save(newInvite);
    }

    public List<Invites> getInvites(String firebaseId) {
        return inviteRepository.findByUserFirebaseId(firebaseId);
    }

//    public List<TribeUser> getUsersInGroup(String firebaseId) throws Exception {
//        TribeUser user = userRepository.findUserByFirebaseId(firebaseId)
//                .orElseThrow(() -> new NotFoundException("User not found"));
//        if (user.getGroupId() == null) {
//            throw new NotFoundException("User is not in any group");
//        }
//        return userRepository.findByGroupId(user.getGroupId());
//    }

    public List<TribeGroup> getGroups() {
        return groupRepository.findAll();
    }


    public void removeUserFromGroup(String adminFirebaseId, String userEmail) throws Exception {
        TribeGroup group = groupRepository.getGroupByAdminId(adminFirebaseId)
                .orElseThrow(() -> new NotFoundException("Firebase ID does not match admin ID"));

        TribeUser userToRemove = userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));
        if (userToRemove.getGroups().contains(group.getId())){
            userToRemove.removeGroup(group.getId());
        }else {
            throw new NotFoundException("user is not in your group");
        }
        userRepository.save(userToRemove);
    }

    public void invite(Long id, Integer accept) throws NotFoundException {
        Invites invite = inviteRepository.findInviteById(id).orElseThrow(() -> new NotFoundException(""));
        if(accept == 1){
            addUserToGroup(id,invite.getUserFirebaseId());
        }else if (accept == -1){
            inviteRepository.delete(invite);
        }
    }

    private void addUserToGroup(Long id,String firebaseId) throws NotFoundException {
        TribeGroup group = groupRepository.getGroupById(id)
                .orElseThrow(() -> new NotFoundException(" ID does not match to group ID"));
        TribeUser userToAdd = userRepository.findUserByFirebaseId(firebaseId)
                .orElseThrow(() -> new NotFoundException("User not found with : " + firebaseId));
        userToAdd.addGroup(group.getId());
        userRepository.save(userToAdd);
    }
}
