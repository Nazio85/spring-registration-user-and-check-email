package ru.site.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.site.services.MyUserService;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class MyConfig extends WebSecurityConfigurerAdapter {

    public static final String USER = "USER";
    public static final String LOGIN_FOR_EMAIL = "info@site.ru";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/img/**", "/js/**",
                        "/bootstrap/**", "/icons/**", "/login/**", "/test/**").permitAll()
                .antMatchers("/profile/**").hasRole(USER)
                .and()
                .formLogin()
                .loginPage("/login")
                .successForwardUrl("/login/success")
                .failureForwardUrl("/login/fail")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll();
    }

    @Autowired
    private MyUserService userService;

    @Bean
    public BCryptPasswordEncoder bcrypt() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userService).passwordEncoder(bcrypt());
    }


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.yandex.ru");
        mailSender.setPort(465);

        mailSender.setUsername(LOGIN_FOR_EMAIL);
        mailSender.setPassword("password");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
