package shoppingmall.yeonshop.Users.login.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import shoppingmall.yeonshop.Users.domain.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LoginDTO {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String username;

    private Role role;
}
