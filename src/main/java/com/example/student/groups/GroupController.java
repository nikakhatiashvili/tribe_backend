package com.example.student.groups;

import com.example.student.ApiResponse;
import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.model.Invites;
import com.example.student.groups.model.TribeGroup;
import com.example.student.groups.service.GroupService;
import com.example.student.user.TribeUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/group")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public List<TribeGroup> getGroups() {
        return groupService.getGroups();
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> createNewGroup(@Valid @RequestBody TribeGroup tribeGroup) {
        try {
            groupService.createGroup(tribeGroup);
            return new ApiResponse<>(HttpStatus.OK.value(), "Creating group was successful", null);
        } catch (NotFoundException e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (AlreadyExistsException e) {
            return new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
        }
    }

    @PostMapping("/leave_group")
    public ApiResponse<String> leaveGroup(@RequestParam String firebaseId, @RequestParam Long groupId) {
        try {
            groupService.leaveGroup(firebaseId, groupId);
            return new ApiResponse<>(HttpStatus.OK.value(), "User has successfully left the group", null);
        } catch (NotFoundException e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        }
    }

    @PostMapping(value = "/invite_user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> inviteUser(@RequestParam String firebaseId, @RequestParam String email) {
        try {
            groupService.inviteUserToGroup(firebaseId, email);
            return new ApiResponse<>(HttpStatus.OK.value(), "User invited", null);
        } catch (NotFoundException e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (AlreadyExistsException e) {
            return new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
        }
    }

    @GetMapping("/groups")
    public List<TribeGroup> getGroups(@RequestParam String firebaseId) throws NotFoundException {
        return groupService.getUserGroups(firebaseId);
    }

    @GetMapping("/invites")
    public List<Invites> addUserToGroup(@RequestParam String firebaseId) {
        return groupService.getInvites(firebaseId);
    }

    @PostMapping("/removeuser")
    public ApiResponse<String> removeUserFromGroup(@RequestParam String firebaseId, @RequestParam String email) {
        try {
            groupService.removeUserFromGroup(firebaseId, email);
            return new ApiResponse<>(HttpStatus.OK.value(), "Removing user was successful", null);
        } catch (NotFoundException e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        }
    }

    @PostMapping("/invite")
    public ApiResponse<String> invite(@RequestParam Long id, @RequestParam boolean accept, @RequestParam String firebaseId) {
        try {
            groupService.invite(id, accept, firebaseId);
            if (accept) {
                return new ApiResponse<>(HttpStatus.OK.value(), "Joining group was successful", null);
            } else {
                return new ApiResponse<>(HttpStatus.OK.value(), "Declining group was successful", null);
            }
        } catch (NotFoundException e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        }
    }

    @GetMapping("/users")
    public List<TribeUser> getUsersInGroup(@Valid @RequestParam String firebaseId, @RequestParam Long id) throws Exception {
        return groupService.getUsersInGroup(firebaseId, id);
    }
}
