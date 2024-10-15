package shoppingmall.yeonshop.Users.login.presentation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import shoppingmall.yeonshop.Users.login.dto.LoginDTO;
import shoppingmall.yeonshop.Users.service.UserService;

@Controller
@RequiredArgsConstructor
public class SSLoginController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login"; // login.html
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new LoginDTO());
        return "register"; // register.html 반환
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") LoginDTO loginDTO) {

        // 회원가입 로직 구현
        String encodedPassword = passwordEncoder.encode(loginDTO.getPassword());
        loginDTO.setPassword(encodedPassword);
        userService.join(loginDTO);

        return "redirect:/login?registerSuccess";
    }

}
