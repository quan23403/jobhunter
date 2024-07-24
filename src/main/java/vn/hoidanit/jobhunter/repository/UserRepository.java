package vn.hoidanit.jobhunter.repository;

import org.springframework.stereotype.Repository;
import vn.hoidanit.jobhunter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    User findByEmail(String email);
}
