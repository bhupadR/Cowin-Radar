package cowin.appointment.finder.repository;

import cowin.appointment.finder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    List<User> findAllByStatus(int status);
}
