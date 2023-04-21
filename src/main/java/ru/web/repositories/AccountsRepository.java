package ru.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.web.models.Account;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByToken(String token);
}
