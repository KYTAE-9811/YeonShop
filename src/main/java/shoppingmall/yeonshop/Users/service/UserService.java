package shoppingmall.yeonshop.Users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shoppingmall.yeonshop.Users.Repository.UserRepository;
import shoppingmall.yeonshop.Users.domain.Role;
import shoppingmall.yeonshop.Users.domain.Users;
import shoppingmall.yeonshop.Users.login.dto.LoginDTO;
import shoppingmall.yeonshop.Users.login.repository.UsersRepositoryImpl;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // DTO -> 엔티티 전환 회원가입
    public String join(LoginDTO loginUser) {
        userDuplicateValidation(loginUser);
        // 생성자를 통해 엔티티로 전환
        Users user = new Users(loginUser.getEmail(), loginUser.getPassword(), Role.USER);
        userRepository.save(user);
        return loginUser.getEmail();
    }

    private void userDuplicateValidation(LoginDTO loinUser) {
        Optional<Users> findUser = userRepository.findByEmail(loinUser.getEmail());
        if (!findUser.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
}
