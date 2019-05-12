package guet.sgc.sv.service;

import guet.sgc.sv.entity.User;

public interface UserService {
    User getUser(String username, String userType);

    void editUser(User saveUser);
}
