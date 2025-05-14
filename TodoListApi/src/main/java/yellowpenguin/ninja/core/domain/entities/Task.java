package yellowpenguin.ninja.core.domain.entities;

import java.time.LocalDateTime;

public class Task {
	
	private String id;
	private String title;
	private String description;
	private PenguinUser user;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Task() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
		
}
