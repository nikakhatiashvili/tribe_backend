package com.example.student.groups;

import com.example.student.groups.exceptions.NotFoundException;
import com.example.student.groups.model.Invites;
import com.example.student.groups.model.TribeGroup;
import com.example.student.groups.service.GroupService;
import com.example.student.student.TribeUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/create")
    public void createNewGroup(@Valid @RequestBody TribeGroup tribeGroup) throws Exception {
        groupService.createGroup(tribeGroup);
    }

    @PostMapping("/invite_user")
    public void inviteUser(@RequestParam String firebaseId, @RequestParam String email) throws Exception {
        groupService.inviteUserToGroup(firebaseId, email);
    }

    @GetMapping("/invites")
    public List<Invites> addUserToGroup(@RequestParam String firebaseId){
        return groupService.getInvites(firebaseId);
    }


    @PostMapping("/removeuser")
    public void removeUserFromGroup(@RequestParam String firebaseId, @RequestParam String email) throws Exception {
        groupService.removeUserFromGroup(firebaseId, email);
    }

    @PostMapping("/invite")
    public void invite(@RequestParam Long id, @RequestParam Integer accept, @RequestParam String firebaseId) throws NotFoundException {
        groupService.invite(id,accept,firebaseId);
    }

    @GetMapping("/users")
    public List<TribeUser> getUsersInGroup(@Valid @RequestParam String firebaseId, @RequestParam Long id) throws Exception {
        return groupService.getUsersInGroup(firebaseId,id);
    }
}
