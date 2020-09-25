package yte.intern.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.manageActivities.entity.Activity;
import yte.intern.project.manageActivities.entity.Users;
import yte.intern.project.manageActivities.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<Users> listAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserByTc(String tcKimlikNo) {
        return userRepository.findByTcKimlikNo(tcKimlikNo)
                .orElseThrow(EntityNotFoundException::new);
    }

    public MessageResponse addUser(Users user) {
        userRepository.save(user);
        return new MessageResponse("User has been added successfully!", MessageType.SUCCESS);
    }

    public MessageResponse deleteUser(String tcKimlikNo) {
        if (userRepository.existsByTcKimlikNo(tcKimlikNo)) {
            userRepository.deleteByTcKimlikNo(tcKimlikNo);
            return new MessageResponse("User with TC " + tcKimlikNo + " has been deleted!", MessageType.SUCCESS);
        }
        return new MessageResponse("User with TC " + tcKimlikNo + " not found!", MessageType.ERROR);
    }

    @Transactional
    public MessageResponse updateUser(String tcKimlikNo, Users user) {
        Optional<Users> oldUser = userRepository.findByTcKimlikNo(tcKimlikNo);
        if (!oldUser.isPresent())
            return new MessageResponse("User with TC " + tcKimlikNo + " not found!", MessageType.ERROR);
        else {
            Users userFromDatabase = oldUser.get();
            updateUserFromDatabase(user, userFromDatabase);
            userRepository.save(userFromDatabase);
            return new MessageResponse("User with TC " + tcKimlikNo + " has been deleted!", MessageType.SUCCESS);
        }
    }

    private void updateUserFromDatabase(Users user, Users oldUser) {
        oldUser.setEmail(user.getEmail());
        oldUser.setName(user.getName());
        oldUser.setSurname(user.getSurname());
    }

    public Set<Activity> getActivities(String tcKimlikNo) {
        return userRepository.findByTcKimlikNo(tcKimlikNo).map(Users::getEnrolledActivities)
                .orElseThrow(EntityNotFoundException::new);
    }

//    public MessageResponse addActivityToUser(String tcKimlikNo, Activity activity) {
//        Optional<Users> userOptional = userRepository.findByTcKimlikNo(tcKimlikNo);
//        if (userOptional.isPresent()) {
//            Users user = userOptional.get();
//            Set<Activity> activities = user.getEnrolledActivities();
//            activities.add(activity);
//            return new MessageResponse(activity.getTitle() + " aktivitesi kullanıcıya başarıyla eklendi.", MessageType.SUCCESS);
//        } else
//            return new MessageResponse("Kullanıcı bulanamadı.", MessageType.ERROR);
//    }

}
