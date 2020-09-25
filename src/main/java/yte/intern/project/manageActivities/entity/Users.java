package yte.intern.project.manageActivities.entity;

import lombok.*;
import yte.intern.project.common.entity.BaseEntity;
import yte.intern.project.manageActivities.dto.UserDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "idgen", sequenceName = "USER_SEQ")
public class Users extends BaseEntity {

    @Column(name = "NAME")
    private String name;
    @Column(name = "SURNAME")
    private String surname;
    @Column(name = "TC_KIMLIK_NO", unique = true)
    private String tcKimlikNo;
    @Column(name = "EMAIL")
    private String email;


    @ManyToMany(mappedBy = "enrolledUsers", cascade = CascadeType.ALL)
    private Set<Activity> enrolledActivities;

    public Set<Activity> getEnrolledActivities() {
        if (this.enrolledActivities == null) {
            return new HashSet<Activity>();
        } else {
            return this.enrolledActivities;
        }
    }

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private Set<Enrollment> enrollments;
//
//
//    public Set<Enrollment> getEnrollments() {
//        if (this.enrollments == null) {
//            return new HashSet<Enrollment>();
//        } else {
//            return this.enrollments;
//        }
//    }
//
//    public void addEnrollment(Enrollment e) {
//        if (this.enrollments == null) {
//            this.enrollments = new HashSet<>();
//        }
//            this.enrollments.add(e);
//    }
//    public void deleteEnrollment(Enrollment e){
//        if (this.enrollments == null) {
//            this.enrollments = new HashSet<>();
//        }
//        this.enrollments.remove(e);
//    }


}

