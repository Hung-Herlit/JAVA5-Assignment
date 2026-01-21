package assignment.java5.kudo_computer.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import assignment.java5.kudo_computer.entity.Company;

public interface CompanyDAO extends JpaRepository<Company,Integer>{
    
}
