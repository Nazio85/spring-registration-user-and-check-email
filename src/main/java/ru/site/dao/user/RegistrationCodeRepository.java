package ru.site.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.site.model.RegistrationCode;
import ru.site.model.Users;

@Repository
public interface RegistrationCodeRepository extends JpaRepository<RegistrationCode, Long> {
    RegistrationCode findByUuid(String code);
    RegistrationCode findByUsers(Users user);
}
