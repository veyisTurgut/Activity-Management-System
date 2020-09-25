package yte.intern.project.manageActivities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import yte.intern.project.manageActivities.validation.TcKimlikNo;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class UserDTO {

    @JsonProperty("name")
    @NotBlank(message = "Kullanıcı ismi boş olamaz!")
    private String name;

    @JsonProperty("surname")
    @NotBlank(message = "Kullanıcı soyismi boş olamaz!")
    private String surname;

    @JsonProperty("tcKimlikNo")
    @NotBlank(message = "Kullanıcı TC Kimlik No'su boş olamaz!")
    @Size(min = 11, max = 11, message = "TC Kimlik no uzunluğu 11 olmalı!")
    @TcKimlikNo(message = "TC Kimlik no geçerli olmalı!")
    private String tcKimlikNo;

    @JsonProperty("email")
    @NotBlank(message = "Kullanıcı e-mail adresi boş olamaz!")
    @Email(message = "e-mail adresi geçerli olmalı!")
    private String email;

    @JsonCreator
    public UserDTO(@JsonProperty("name") String name,
                   @JsonProperty("surname") String surname,
                   @JsonProperty("tcKimlikNo") String tcKimlikNo,
                   @JsonProperty("email") String email) {
        this.name = name;
        this.surname = surname;
        this.tcKimlikNo = tcKimlikNo;
        this.email = email;
    }
}
