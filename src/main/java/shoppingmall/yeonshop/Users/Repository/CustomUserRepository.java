package shoppingmall.yeonshop.Users.Repository;

import shoppingmall.yeonshop.Users.domain.Users;

import java.util.Optional;

public interface CustomUserRepository {
    public Optional<Users> findByEmail(String email);
}
