package com.example.TaskManagementSystem.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePasswordDTO {
    private String oldPassword;

    @NotBlank(message = "Password does not blank")
    @Size(min = 8, message = "Password must have at least 8 letters")
    @Pattern.List({
            @Pattern(regexp = ".*[A-Z].*", message = "Password must have at least 1 upper letter"),
            @Pattern(regexp = ".*\\d.*", message = "Password must have at least 1 number"),
            @Pattern(regexp = ".*[!@#$%^&*()_+\\-=].*", message = "Password must have at least 1 special symbol")
    })
    private String newPassword;
}
