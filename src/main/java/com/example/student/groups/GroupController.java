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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<TribeGroup>>> getGroups() {
        List<TribeGroup> groups = groupService.getGroups();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Fetched groups successfully", groups));
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> createNewGroup(@Valid @RequestBody TribeGroup tribeGroup) {
        try {
            groupService.createGroup(tribeGroup);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Group creation successful", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null));
        }
    }

    @PostMapping("/leave_group")
    public ResponseEntity<ApiResponse<String>> leaveGroup(@RequestParam String firebaseId, @RequestParam Long groupId) {
        try {
            groupService.leaveGroup(firebaseId, groupId);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Successfully left the group", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }

    @PostMapping(value = "/invite_user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> inviteUser(@RequestParam String firebaseId, @RequestParam String email) {
        try {
            groupService.inviteUserToGroup(firebaseId, email);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User successfully invited", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/groups")
    public ResponseEntity<ApiResponse<List<TribeGroup>>> getGroups(@RequestParam String firebaseId) {
        try {
            List<TribeGroup> userGroups = groupService.getUserGroups(firebaseId);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Fetched user groups successfully", userGroups));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }
    @GetMapping("/invites")
    public ResponseEntity<ApiResponse<List<Invites>>> getGroupInvites(@RequestParam String firebaseId) {
        List<Invites> invites = groupService.getInvites(firebaseId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Fetched group invites successfully", invites));
    }

    @PostMapping("/removeuser")
    public ResponseEntity<ApiResponse<String>> removeUserFromGroup(@RequestParam String firebaseId, @RequestParam String email) {
        try {
            groupService.removeUserFromGroup(firebaseId, email);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User successfully removed from group", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }

    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<String>> respondToInvite(@RequestParam Long id, @RequestParam boolean accept, @RequestParam String firebaseId) {
        try {
            groupService.invite(id, accept, firebaseId);
            String message = accept ? "Successfully joined the group" : "Declined the group invitation";
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), message, null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<TribeUser>>> getUsersInGroup(@RequestParam String firebaseId, @RequestParam Long id) {
        try {
            List<TribeUser> users = groupService.getUsersInGroup(firebaseId, id);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Fetched users in group successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }
}