package cn.touch.demo.hibernate.basic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.NaturalId;

@Entity
public class User {
	
	@Id
	@GeneratedValue
	private int id;

	@NaturalId
	private String email;

	private String name;

	private String phone;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "\nName: " ).append( name );
		sb.append( "\nEmail: " ).append( email );
		sb.append( "\nPhone: " ).append( phone );
		return sb.toString();
	}
}
