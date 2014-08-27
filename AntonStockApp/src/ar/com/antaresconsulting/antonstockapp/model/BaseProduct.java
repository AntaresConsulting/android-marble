package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class BaseProduct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7171920080509184313L;
	private Integer id;
	private String codigo;
	private String nombre;
	private String atributos;
	private Double cantidadReal;
	private Double cantidadForecast;
	private Double cantidadIncome;
	private Double price;
	private String productImg;
	private String ean13;
	private Object[] uom;

	public BaseProduct(Integer id, String cod, String nom, String prodImg,
			Double price, Double cant, String ean13) {
		this.codigo = cod;
		this.id = id;
		this.nombre = nom;
		this.productImg = prodImg;
		this.price = price;
		this.cantidadReal = cant;
		this.cantidadForecast = cant;
		this.cantidadIncome = cant;
		this.ean13 = ean13;
	}

	public String getEan13() {
		return ean13;
	}

	public void setEan13(String ean13) {
		this.ean13 = ean13;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BaseProduct() {
	}

	public BaseProduct(Integer id, String nom, String img) {
		this.id = id;
		this.nombre = nom;
		this.productImg = img;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	private Double formatStock(Double baseStock) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		twoDForm.setDecimalFormatSymbols(dfs);     
		return new Double(twoDForm.format(baseStock.doubleValue()));
	}


	public Double getCantidadReal() {
		return formatStock(cantidadReal);
	}

	public void setCantidadReal(Double cantidadReal) {
		this.cantidadReal = cantidadReal;
	}

	public Double getCantidadForecast() {
		return formatStock(cantidadForecast);
	}

	public void setCantidadForecast(Double cantidadForecast) {
		this.cantidadForecast = cantidadForecast;
	}

	public Double getCantidadIncome() {
		return formatStock(cantidadIncome);
	}

	public void setCantidadIncome(Double cantidadIncome) {
		this.cantidadIncome = cantidadIncome;
	}

	public String getProductImg() {
		return productImg;
	}

	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}


	public Object[] getUom() {
		return uom;
	}

	public void setUom(Object[] uom) {
		this.uom = uom;
	}

	public String getAtributos() {
		return atributos;
	}

	public void setAtributos(String atributos) {
		this.atributos = atributos;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getNombre()+ " "+this.getAtributos() +" - Cant. :" +this.getCantidadReal()+ " "+this.getUom()[1];
	}

}
