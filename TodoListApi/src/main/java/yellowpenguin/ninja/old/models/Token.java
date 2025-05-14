package yellowpenguin.ninja.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "tokens")
public class Token {
	
	public enum TokenType{
		BEARER;
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(unique = true)
	private String token;
	
	@Enumerated(EnumType.STRING)
	private TokenType tokenType = TokenType.BEARER;
	
	private boolean revoked;
	private boolean expired;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

}
