package com.example.student.groups;

import com.example.student.groups.exceptions.AlreadyExistsException;
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

    @PostMapping("/create")
    public void createNewGroup(@Valid @RequestBody TribeGroup tribeGroup) throws AlreadyExistsException {
        groupService.createGroup(tribeGroup);
    }

    @GetMapping("/users")
    public List<TribeUser> getUsersInGroup(@Valid @RequestParam String firebaseId) {
        return groupService.getUsersInGroup(firebaseId);
    }
}
