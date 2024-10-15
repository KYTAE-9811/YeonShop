package shoppingmall.yeonshop.Users.login.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shoppingmall.yeonshop.Users.Repository.CustomUserRepository;
import shoppingmall.yeonshop.Users.domain.Users;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsersRepositoryImpl implements CustomUserRepository{

    private final EntityManager em;

    @Override
    public Optional<Users> findByEmail(String email) {
        List<Users> results =  em.createQuery("select u from Users u where u.email = :email", Users.class)
                .setParameter("email", email)
                .getResultList();
        if (results.isEmpty()) {
            return Optional.empty();
        }else {
            return Optional.of(results.get(0));
        }

    }

    public void clearStore() {
        em.createQuery("drop table users");
    }

}
