package yte.intern.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.manageActivities.dto.ActivityDTO;
import yte.intern.project.manageActivities.dto.UserDTO;
import yte.intern.project.manageActivities.entity.Activity;
import yte.intern.project.manageActivities.entity.Users;
import yte.intern.project.manageActivities.mapper.ActivityMapper;
import yte.intern.project.manageActivities.mapper.UserMapper;
import yte.intern.project.service.UserService;

import javax.persistence.Access;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ActivityMapper activityMapper;
    private final UserMapper userMapper;


//    @PostMapping
//    public MessageResponse addUser(@Valid @RequestBody UserDTO UserDTO) {
//        Users user = userMapper.mapToEntity(UserDTO);
//        return userService.addUser(user);
//    }
//
//    @GetMapping
//    public List<UserDTO> listAllUsers() {
//        List<Users> users = userService.listAllUsers();
//        return userMapper.mapToDto(users);
//    }
//
//    @GetMapping("/{tcKimlikNo}")
//    public UserDTO getUserByTc(@PathVariable String tcKimlikNo) {
//        Users user = userService.getUserByTc(tcKimlikNo);
//        return userMapper.mapToDto(user);
//    }

//    @DeleteMapping("/{tcKimlikNo}")
//    public MessageResponse deleteUser(@PathVariable String tcKimlikNo) {
//        return userService.deleteUser(tcKimlikNo);
//    }
//
//    @PutMapping("/{tcKimlikNo}")
//    public MessageResponse updateUser(@PathVariable String tcKimlikNo, @Valid @RequestBody UserDTO userDTO) {
//        Users user = userMapper.mapToEntity(userDTO);
//        return userService.updateUser(tcKimlikNo, user);
//    }

    @GetMapping("/{tcKimlikNo}/activities")
    public List<ActivityDTO> getUserActivities(@PathVariable String tcKimlikNo) {
        Set<Activity> userActivities = userService.getActivities(tcKimlikNo);
        return activityMapper.mapToDto(new ArrayList<>(userActivities));
    }


}
