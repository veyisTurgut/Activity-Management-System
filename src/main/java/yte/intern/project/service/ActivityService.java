package yte.intern.project.service;

//TODO : database ilişkisini değiştirdim. Artık kolayca .save yapamıycam.


import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.manageActivities.entity.Activity;
import yte.intern.project.manageActivities.entity.Users;
import yte.intern.project.manageActivities.repository.ActivityRepository;
import yte.intern.project.manageActivities.repository.UserRepository;
import yte.intern.project.util.EmailSender;
import yte.intern.project.util.qrCode;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public List<Activity> listAllActivities() {
        return activityRepository.findAll();
    }

    public List<Activity> listAllActivitiesFromNowOn() {
        return activityRepository.findByStartDateAfter(LocalDate.now());
    }

    public MessageResponse addActivity(Activity activity) {
        if (activityRepository.findByTitle(activity.getTitle()).isPresent()) {
            return new MessageResponse("Etkinlik ismi özgün olmalı!", MessageType.ERROR);
        }
        activityRepository.save(activity);
        return new MessageResponse("Etkinlik eklendi!", MessageType.SUCCESS);
    }

    public MessageResponse addUserToActivity(String activityTitle, Users user)
            throws IOException, WriterException, MessagingException {
        Optional<Activity> optionalActivity = activityRepository.findByTitle(activityTitle);
        //etkinlik var mı?
        if (optionalActivity.isPresent()) {
            Activity activity = optionalActivity.get();
            Optional<Users> optionalUser = userRepository.findByTcKimlikNo(user.getTcKimlikNo());
            //kullanıcı var mı?
            Users userToBeAdded;
            if (optionalUser.isPresent()) {
                userToBeAdded = optionalUser.get();
                //kullanıcı zaten etkinlikte mi?
                //TODO : burası karıştı, debug gerekebilir.
                if (activity.getEnrolledUsers().contains(userToBeAdded)) {
                    return new MessageResponse("Aynı TC ile bir sefer kaydolabilirsin!", MessageType.ERROR);
                }
            } else { //yoksa yarat
                //kullanıcı DB ye eklendi.
                userToBeAdded = userRepository.save(user);
            }
            //etkinlikte yer var mı?
            if (activity.getRemainingQuota() > 0) {
                activity.setRemainingQuota(activity.getRemainingQuota() - 1);
                //TODO : işe yaradı mı emin değilim.         ps:yaramadı

                activity.getEnrolledUsers().add(userToBeAdded);
                userToBeAdded.getEnrolledActivities().add(activity);
                activityRepository.save(activity);
                userRepository.save(userToBeAdded);
                qrCode.generateQRCodeImage(activity, userToBeAdded);
                EmailSender.emailSender(activity, userToBeAdded);
                return new MessageResponse("Kullanıcı etkinliğe eklendi!", MessageType.SUCCESS);
            }
            return new MessageResponse("Etkinlik kapasitesine ulaştı!", MessageType.ERROR);
        }
        return new MessageResponse("Böyle bir etkinlik yok!", MessageType.ERROR);
    }


    public MessageResponse deleteUserFromActivity(String activityTitle, String tcKimlikNo) {
        Optional<Activity> optionalActivity = activityRepository.findByTitle(activityTitle);
        //etkinlik var mı?
        if (optionalActivity.isPresent()) {
            Activity activity = optionalActivity.get();
            Optional<Users> optionalUser = userRepository.findByTcKimlikNo(tcKimlikNo);
            //kullanıcı var mı?
            if (optionalUser.isPresent()) {
                if (activity.getEnrolledUsers().contains(optionalUser.get())) {
                    //removeda sıkıntı çıkabilir
                    removeUserFromActivityHelper(activity, tcKimlikNo);
                    activity.setRemainingQuota(activity.getRemainingQuota() + 1);
                    return new MessageResponse("Kullanıcı etkinlikten kaldırıldı!", MessageType.SUCCESS);
                }
                return new MessageResponse("Kullanıcı etkinliğe kayıtlı değil!", MessageType.ERROR);
            }
            return new MessageResponse("Böyle bir kullanıcı yok!", MessageType.ERROR);
        }
        return new MessageResponse("Böyle bir etkinlik yok!", MessageType.ERROR);
    }

    @Transactional
    public MessageResponse updateActivity(String title, Activity activity) {
        Optional<Activity> oldActivity = activityRepository.findByTitle(title);
        //etkinlik var mı?
        if (!oldActivity.isPresent()) {
            return new MessageResponse("Etkinlik bulunamadı!", MessageType.ERROR);
        }
        Activity activityFromDB = oldActivity.get();
        //etkinlik başladı mı?
        if (activityFromDB.getStartDate().compareTo(LocalDate.now()) >= 0) {
            updateActivityFromDatabase(activity, activityFromDB);
            activityRepository.save(activityFromDB);
            return new MessageResponse("Etkinlik güncellendi!", MessageType.SUCCESS);
        }
        return new MessageResponse("Etkinlik çoktan başladı!", MessageType.ERROR);
    }

    private void updateActivityFromDatabase(Activity activity, Activity oldActivity) {
        oldActivity.setTitle(activity.getTitle());
        oldActivity.setRemainingQuota(activity.getRemainingQuota());
        oldActivity.setEnrolledUsers(activity.getEnrolledUsers());
        oldActivity.setFinishDate(activity.getFinishDate());
        oldActivity.setStartDate(activity.getStartDate());
        oldActivity.setLatitude(activity.getLatitude());
        oldActivity.setLongitude(activity.getLongitude());
    }


    private void removeUserFromActivityHelper(Activity activity, String tcKimlikNo) {
        Set<Users> filteredUsers = activity.getEnrolledUsers()
                .stream()
                .filter(x -> !x.getTcKimlikNo().equals(tcKimlikNo))
                .collect(Collectors.toSet());
        activity.getEnrolledUsers().clear();
        activity.getEnrolledUsers().addAll(filteredUsers);
    }

    public MessageResponse deleteActivity(String title) {
        Optional<Activity> optionalActivity = activityRepository.findByTitle(title);
        if (optionalActivity.isPresent()) {
            activityRepository.deleteByTitle(title);
            return new MessageResponse("Etkinlik başarıyla kaldırıldı!", MessageType.SUCCESS);
        }
        return new MessageResponse("Etkinlik zaten yok!", MessageType.ERROR);
    }

    public List<Users> listUsersOfActivity(String title) {
        Optional<Activity> optionalActivity = Optional.ofNullable(activityRepository.findByTitle(title)
                .orElseThrow(EntityNotFoundException::new));
        return optionalActivity.get().getEnrolledUsers().stream().collect(Collectors.toList());
    }

    public Map<String, Integer> numberOfUsersOfEveryActivity() {
        List<Activity> allActivities = activityRepository.findAll();

        HashMap<String, Integer> map = new HashMap<>();
        for (Activity a : allActivities) {
            map.put(a.getTitle(), a.getEnrolledUsers().size());
        }
        return map;
    }
}
