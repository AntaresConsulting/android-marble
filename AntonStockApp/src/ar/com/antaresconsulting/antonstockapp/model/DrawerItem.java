package ar.com.antaresconsulting.antonstockapp.model;

import java.io.Serializable;
 
public class DrawerItem implements Serializable {
 
      /**
	 * 
	 */
	private static final long serialVersionUID = -2474999476500123271L;
	String ItemName;
      int imgResID;
 
      public DrawerItem(String itemName, int imgResID) {
            super();
            ItemName = itemName;
            this.imgResID = imgResID;
      }
 
      public String getItemName() {
            return ItemName;
      }
      public void setItemName(String itemName) {
            ItemName = itemName;
      }
      public int getImgResID() {
            return imgResID;
      }
      public void setImgResID(int imgResID) {
            this.imgResID = imgResID;
      }
 
}