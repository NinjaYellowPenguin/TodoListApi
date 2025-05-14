package yellowpenguin.ninja.core.domain.entities;

public class JWToken {

	public enum TokenType {
		BEARER;
	}

	private Long id;
	private String token;
	private TokenType tokenType = TokenType.BEARER;
	private boolean revoked;
	private boolean expired;
	private PenguinUser user;
	
	public JWToken() {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public TokenType getTokenType() {
		return tokenType;
	}
	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}
	public boolean isRevoked() {
		return revoked;
	}
	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	public PenguinUser getUser() {
		return user;
	}
	public void setUser(PenguinUser user) {
		this.user = user;
	}

}
