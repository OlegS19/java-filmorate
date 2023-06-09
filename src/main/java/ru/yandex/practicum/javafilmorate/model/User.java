package ru.yandex.practicum.javafilmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Objects;

@Builder
@AllArgsConstructor
@Data
public class User {
    private Integer id;
    @Email(message = "Email is not valid")
    @NotBlank(message = "Email can't be empty")
    private String email;
    @Pattern(regexp = "^\\S*", message = "There is a space")
    @NotBlank(message = "Login can't be blank")
    private String login;
    private String name;

    @NotNull(message = "The birthday can't be empty")
    @PastOrPresent(message = "The birthday has to be before today")
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
