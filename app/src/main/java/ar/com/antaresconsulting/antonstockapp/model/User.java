package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;


public class User implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -8332442030304208826L;
	private Integer id;
	private String nombre;
	private String birthdate;
	private String phone;
	private String mobile;
	private String email;
	private String partnerImg;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nombre;
	}

	public String getPartnerImg() {
		return partnerImg;
	}

	public void setPartnerImg(String partnerImg) {
		this.partnerImg = partnerImg;
	}

	public User(Integer id, String nom, String email, String phone, String img, String mobile, String birthdate) {
		this.nombre = nom;
		this.id = id;
		this.phone = phone;
		this.email = email;
		this.birthdate = birthdate;
		this.mobile = mobile;
		this.partnerImg = img;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
