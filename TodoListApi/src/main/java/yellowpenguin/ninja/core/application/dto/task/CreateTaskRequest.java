package yellowpenguin.ninja.core.application.dto.task;

import yellowpenguin.ninja.core.domain.entities.PenguinUser;

public class CreateTaskRequest {
	
	private String title;
	private String description;
	private PenguinUser user;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public PenguinUser getUser() {
		return user;
	}
	public void setUser(PenguinUser user) {
		this.user = user;
	}	

}
