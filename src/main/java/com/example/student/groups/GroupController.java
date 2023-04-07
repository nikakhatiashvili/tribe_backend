package com.example.student.groups;

import com.example.student.groups.exceptions.AlreadyExistsException;
import com.example.student.groups.exceptions.CustomErrorResponse;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Object> createNewGroup(@Valid @RequestBody TribeGroup tribeGroup) {
        try {
            groupService.createGroup(tribeGroup);

            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Creating group was successful");

            return ResponseEntity.ok(responseMap);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
        }
    }


    @PostMapping(value = "/invite_user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> inviteUser(@RequestParam String firebaseId, @RequestParam String email) {
        try {
            groupService.inviteUserToGroup(firebaseId, email);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "user invited");

            return ResponseEntity.ok(responseMap);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new CustomErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
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
    public ResponseEntity<Object> removeUserFromGroup(@RequestParam String firebaseId, @RequestParam String email) {
        try {
            groupService.removeUserFromGroup(firebaseId, email);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "removing user was successful");
            return ResponseEntity.ok(responseMap);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
    }

    @PostMapping("/invite")
    public ResponseEntity<Object> invite(@RequestParam Long id, @RequestParam boolean accept, @RequestParam String firebaseId) {
        try {
            groupService.invite(id, accept, firebaseId);
            Map<String, String> responseMap = new HashMap<>();
            if (accept) {
                responseMap.put("message", "joining group was successful");
            } else {
                responseMap.put("message", "declining group was successful");
            }
            return ResponseEntity.ok(responseMap);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }

    }

    @GetMapping("/users")
    public List<TribeUser> getUsersInGroup(@Valid @RequestParam String firebaseId, @RequestParam Long id) throws Exception {
        return groupService.getUsersInGroup(firebaseId, id);
    }
}
