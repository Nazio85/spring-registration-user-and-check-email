package ru.site.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.site.model.RegistrationCode;
import ru.site.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String name);
    Users findByEmail(String email);
    Users findByRegistrationCode(RegistrationCode registrationCode);
}
