package vn.hoidanit.jobhunter.repository;

import org.springframework.stereotype.Repository;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;



@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findById(long id);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    User findByRefreshTokenAndEmail(String refreshToken, String email);
    List<User> findByCompany(Company company);

}
