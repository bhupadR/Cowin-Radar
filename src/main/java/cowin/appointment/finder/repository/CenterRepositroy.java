package cowin.appointment.finder.repository;

import cowin.appointment.finder.entity.CenterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CenterRepositroy extends JpaRepository<CenterEntity,Integer> {
}
