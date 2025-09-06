package com.shakir.email_summarizer.Repository;

import com.shakir.email_summarizer.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByEmail(String email);
    @Query("select count(u) from User u join u.authorities a where a.authority = 'ROLE_ADMIN'")
    long countAdminUsers();
}
