package assignment.java5.kudo_computer.service.impl;

import assignment.java5.kudo_computer.entity.User;
import assignment.java5.kudo_computer.repository.UserRepository;
import assignment.java5.kudo_computer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(String username, String password) {
        // Hỗ trợ đăng nhập bằng cả Email hoặc Số điện thoại
        Optional<User> optUser = userRepository.findByEmail(username);
        if (optUser.isEmpty()) {
            optUser = userRepository.findByPhoneNumber(username);
        }

        if (optUser.isPresent() && optUser.get().getPassword().equals(password)) {
            return optUser.get();
        }
        return null;
    }

    @Override
    public User register(User user) {
        user.setActive(true);
        user.setAdmin(false);
        return userRepository.save(user);
    }

    @Override
    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        return userRepository.existsByPhoneNumber(phone);
    }

    @Override
    public Page<User> getUsersWithPaginationAndSearch(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.searchUsers(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    @Override
    public void toggleUserStatus(Long id) {
        // Tìm user, nếu có thì đảo ngược trạng thái isActive (Đang True -> False, Đang False -> True)
        userRepository.findById(id).ifPresent(user -> {
            user.setActive(!user.isActive());
            userRepository.save(user);
        });
    }
}