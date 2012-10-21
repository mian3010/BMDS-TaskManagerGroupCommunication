package taskManagerGroupCommunication;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "user")
@XmlType(propOrder={"id", "name", "password"})
public class DeprecatedUser {
	private static int highestId = 0;
	
	private String name, password;
	private int userId;
	
	/**
	 * Creates an object in an unstable state, but is used by XML Reflection generation.
	 */
	@SuppressWarnings("unused")
  private DeprecatedUser() {}
	
	public DeprecatedUser(String name, String password) {
		this.name = name;
		this.password = password;
		this.userId = ++highestId;
	}
	
	@XmlAttribute
	public int getId() {
	  return userId;
	}
	
	public void setId(int id) {
	  this.userId = id;
	  if (highestId < id) highestId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * This implementation assumes that all users are unique by their id.
	 */
	public int hashCode() {
		return 37 * getId();
	}
	
	/**
	 * This implementation assumes that all users are unique by their id.
	 */
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o instanceof DeprecatedUser) {
			DeprecatedUser u = (DeprecatedUser)o;
			if(u.getId() == getId()) return true;
		}
		return false;
	}
}
