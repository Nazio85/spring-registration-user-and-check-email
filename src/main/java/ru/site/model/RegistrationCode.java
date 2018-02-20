package ru.site.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
public class RegistrationCode {
    @Id
    @GeneratedValue
    private long id;
    private String uuid;
    private boolean notActivation;
    private boolean updatePassword;
    // Вторая переменая защита от дурака (клиент перепутал ссылку регистраиции с обновлением пароля)
    @OneToOne(mappedBy = "registrationCode")
    private Users users;

    public RegistrationCode() {
        this.uuid = UUID.randomUUID().toString();
        notActivation = true;
        updatePassword = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public boolean isNotActivation() {
        return notActivation;
    }

    public void setNotActivation(boolean notActivation) {
        this.notActivation = notActivation;
    }

    public boolean isUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(boolean updatePassword) {
        this.updatePassword = updatePassword;
    }
    public void remindPassword(){
        this.uuid = UUID.randomUUID().toString();
        updatePassword = true;
    }


}
