package assignment.java5.kudo_computer.service;

import org.springframework.data.domain.Page;

import assignment.java5.kudo_computer.entity.User;

public interface UserService {
    User login(String username, String password);

    User register(User user);

    boolean checkEmailExists(String email);

    boolean checkPhoneExists(String phone);

    Page<User> getUsersWithPaginationAndSearch(String keyword, int page, int size);

    void toggleUserStatus(Long id);
}