package assignment.java5.kudo_computer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import assignment.java5.kudo_computer.entity.Address;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    // Lấy danh sách địa chỉ nhận hàng của 1 user
    List<Address> findByUser_Id(Long userId);
}