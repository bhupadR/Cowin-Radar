package cowin.appointment.finder.repository;

import cowin.appointment.finder.entity.MessageArchive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageArchiveRepository extends JpaRepository<MessageArchive,Integer> {
}
