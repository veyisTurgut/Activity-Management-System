package yte.intern.project.controller;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.manageActivities.dto.ActivityDTO;
import yte.intern.project.manageActivities.dto.UserDTO;
import yte.intern.project.manageActivities.entity.Activity;
import yte.intern.project.manageActivities.mapper.ActivityMapper;
import yte.intern.project.manageActivities.mapper.UserMapper;
import yte.intern.project.service.ActivityService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@EnableWebSecurity
@CrossOrigin
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;
    private final ActivityMapper activityMapper;
    private final UserMapper userMapper;

    @GetMapping("/pastIncluded")
    @Cacheable(value = "activities")
    public List<ActivityDTO> listAllActivities() {
        List<Activity> activities = activityService.listAllActivities();
        return activityMapper.mapToDto(activities);
    }

    @GetMapping("/numbers")
    public Map<String, Integer> numberOfUsersOfEveryActivity() {
        return activityService.numberOfUsersOfEveryActivity();
    }

    @GetMapping
    public List<ActivityDTO> listAllActivitiesFromNowOn() {
        List<Activity> activities = activityService.listAllActivitiesFromNowOn();
        return activityMapper.mapToDto(activities);
    }

    @GetMapping("/users")
    public Map<String, List<UserDTO>> listUsersOfActivity() {
        HashMap<String, List<UserDTO>> map = new HashMap<>();
        for (Activity activity : activityService.listAllActivities()) {
            map.put(activity.getTitle(), userMapper.mapToDto(activityService.listUsersOfActivity(activity.getTitle())));
        }
        return map;
    }

    @GetMapping("/{title}/users")
    public List<UserDTO> listUsersOfActivity(@PathVariable String title) {
        return userMapper.mapToDto(activityService.listUsersOfActivity(title));
    }

    @PutMapping("/{title}")
    public MessageResponse updateActivity(@PathVariable String title, @Valid @RequestBody ActivityDTO activityDTO) {
        Activity activity = activityMapper.mapToEntity(activityDTO);
        return activityService.updateActivity(title, activity);
    }

    @PostMapping
    @CachePut(value = "activities")
    public MessageResponse addActivity(@RequestBody @Valid ActivityDTO activityDTO) {
        return activityService.addActivity(activityMapper.mapToEntity(activityDTO));
    }

    @DeleteMapping("/{title}")
    @CacheEvict(value = "activities")
    public MessageResponse deleteActivity(@PathVariable String title) {
        return activityService.deleteActivity(title);
    }

    @DeleteMapping("/{title}/{tcKimlikNo}")
    public MessageResponse deleteUserFromActivity(@PathVariable String title, @PathVariable String tcKimlikNo) {
        return activityService.deleteUserFromActivity(title, tcKimlikNo);
    }

    @PostMapping("/{title}")
    public MessageResponse addUserToActivity(@PathVariable String title, @RequestBody @Valid UserDTO userDTO) throws IOException, WriterException, MessagingException {
        return activityService.addUserToActivity(title, userMapper.mapToEntity(userDTO));
    }
}
