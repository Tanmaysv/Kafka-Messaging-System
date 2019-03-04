package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
//@Data
@Table(name="UCL_GAME_INSTANCE")
public class EventEntity {
	
	@Id
	@Column(name="ID")
	public Long id;
	
	@Column(name="ACTUAL_TIME")
	public String acutalTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAcutalTime() {
		return acutalTime;
	}

	public void setAcutalTime(String acutalTime) {
		this.acutalTime = acutalTime;
	}
	
}
