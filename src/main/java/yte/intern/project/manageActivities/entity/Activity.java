package yte.intern.project.manageActivities.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import yte.intern.project.common.entity.BaseEntity;


@Entity
@Getter
@Setter
@NoArgsConstructor
@SequenceGenerator(name = "idgen", sequenceName = "ACTIVITY_SEQ")
public class Activity extends BaseEntity {

    @Column(name = "TITLE", unique = true)
    private String title;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "FINISH_DATE")
    private LocalDate finishDate;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "QUOTA")
    private Integer remainingQuota;

    @ManyToMany
    @JoinTable(
            name = "ENROLLMENT",
            joinColumns = @JoinColumn(name = "ACTIVITY_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    private Set<Users> enrolledUsers;

//    @OneToMany(mappedBy = "activity",cascade = CascadeType.ALL)
//    private Set<Enrollment> enrollments;

    public boolean hasQuota() {
        return this.remainingQuota > 0;
    }

}
