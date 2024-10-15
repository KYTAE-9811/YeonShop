package shoppingmall.yeonshop.Users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shoppingmall.yeonshop.Users.Repository.UserRepository;
import shoppingmall.yeonshop.Users.domain.Role;
import shoppingmall.yeonshop.Users.domain.Users;
import shoppingmall.yeonshop.Users.login.repository.UsersRepositoryImpl;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저가 발견되지 않습니다"));

        return new User(findUser.getEmail(), findUser.getPassword(), getAuthorities(findUser.getRole()));
    }
    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
