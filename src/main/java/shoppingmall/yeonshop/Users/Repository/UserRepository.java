package shoppingmall.yeonshop.Users.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingmall.yeonshop.Users.domain.Users;

public interface UserRepository extends JpaRepository<Users, Long>, CustomUserRepository {
}
