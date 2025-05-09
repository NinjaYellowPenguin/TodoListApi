package yellowpenguin.ninja.models;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {
	
	private String id;
	private String name;
	private String email;
	//private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
