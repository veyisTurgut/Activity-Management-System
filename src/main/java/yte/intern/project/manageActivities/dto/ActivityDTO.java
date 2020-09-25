package yte.intern.project.manageActivities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Builder
public class ActivityDTO {
    @JsonProperty("title")
    @NotBlank(message = "Etkinlik adı boş olamaz!")
    public String title;

    @JsonProperty("startDate")
    @Future(message = "Başlangıç tarihi bugünden sonra olmalı!")
    public LocalDate startDate;

    @JsonProperty("finishDate")
    @Future(message = "Bitiş tarihi bugünden sonra olmalı!")
    public LocalDate finishDate;

    @JsonProperty("latitude")
    public Double latitude;

    @JsonProperty("longitude")
    public Double longitude;

    @JsonProperty("remainingQuota")
    @Min(value = 0,message = "Kota pozitif bir sayı olmalı!")
    public Integer remainingQuota;


    @JsonCreator
    public ActivityDTO(@JsonProperty("title") String title,
                       @JsonProperty("startDate") LocalDate startDate,
                       @JsonProperty("finishDate") LocalDate finishDate,
                       @JsonProperty("latitude") Double latitude,
                       @JsonProperty("longitude") Double longitude,
                       @JsonProperty("remainingQuota") Integer quota
    ) {
        this.title = title;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.remainingQuota = quota;
    }


    //TODO : bu assertionlar DTOdan dönüyo
    @AssertTrue   (message = "Koordinatlar gerçekçi olmalı.")
    private boolean isCoordinatesValid() {
        return Math.abs(this.longitude) < 180.0 && Math.abs(this.latitude) < 90.0;
    }

    @AssertFalse (message = "Tarihler gerçekçi olmalı.")
    private boolean isFinishLaterThanStart() {
        return this.startDate.compareTo(this.finishDate) > 0;
    }

}

