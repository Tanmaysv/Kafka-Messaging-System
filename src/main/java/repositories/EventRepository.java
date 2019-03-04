package repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import entity.EventEntity;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, Long>{
	
	@Query(value="Insert INTO UCL_GAME_INSTANCE(Actual_Time) VALUES (?1)", nativeQuery=true)
	void insertActualTime(Date date);
}
