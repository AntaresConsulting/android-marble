package ar.com.antaresconsulting.antonstockapp;

import android.view.View;

public interface InProductActions {
	
	public void confirmStock() ;
	public void searchByEAN(String scanContent);
	public void addProduct(View view);

}
