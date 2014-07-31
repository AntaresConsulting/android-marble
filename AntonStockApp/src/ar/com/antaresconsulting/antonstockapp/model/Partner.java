package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;


public class Partner implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8332442030304208826L;
	private Integer id;
	private String nombre;
	private String domicilio;
	private String web;
	private String phone;
	private String cuit;
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

	public Partner(Integer id,String nom, String dom, String web, String email, String phone, String img, String cuit) {
		this.nombre = nom;
		this.id = id;
		this.domicilio = dom;
		this.web = web;
		this.phone = phone;
		this.email = email;
		this.cuit = cuit;
		this.partnerImg = img;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}





}
