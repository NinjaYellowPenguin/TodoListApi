package yellowpenguin.ninja.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskResponse {
	
	private String id;
	private String title;
	private String description;

}
