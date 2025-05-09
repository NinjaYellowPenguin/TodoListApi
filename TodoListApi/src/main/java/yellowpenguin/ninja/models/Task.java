package yellowpenguin.ninja.models;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Task {
	
	private String id;
	private String title;
	private String description;
	private String userId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
