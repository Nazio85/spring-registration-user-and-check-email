package ru.site.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.site.dao.user.UsersRepository;
import ru.site.model.Users;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

@Service
public class MyUserService implements UserDetailsService {
    @Autowired
    private UsersRepository userDao;

    @Override
    @Transactional (readOnly = true)
    public UserDetails loadUserByUsername(@NotNull String name) {
        Users users = userDao.findByEmail(name);
        if(users == null){
            throw new UsernameNotFoundException("User not authorized.");
        }
        return users;
    }

    @PostConstruct
    public void init() {
    }
}
