package ar.com.antaresconsulting.antonstockapp.util;

public class AntonConstants {
	//Stock Picking types
	public static final String IN_PORDUCT_TYPE = "incoming";
	public static final String OUT_PORDUCT_TYPE = "outgoing";
	public static final String INTERNAL_PORDUCT_TYPE = "internal";
	
	//Location External IDs
	public static final String PRODUCT_LOCATION_SUPPLIER = "stock.stock_location_suppliers";
	public static final String PRODUCT_LOCATION_CUSTOMER = "stock.stock_location_customers";
	
	public static final String PRODUCT_LOCATION_OUTPUT = "product_marble.location_deposito_despacho";
	public static final String PRODUCT_LOCATION_PRODUCTION = "stock.location_production";
	public static final String PRODUCT_LOCATION_INSUMOS = "product_marble.location_stock_insumos";
	public static final String PRODUCT_LOCATION_SERVICIOS = "product_marble.location_stock_servicios";
	
	
	
	public static final String ANTON_COMPANY_ID = "1";
	public static final String DELIVERY_METHOD = "one";
	public static final String DIRECT_METHOD = "direct";
	
	//Default Server Settings
	public static final String DEFAULT_DB = "anton";
	public static final String DEFAULT_URL = "gestion.stock";
	public static final String DEFAULT_PORT = "80";
	
	public static final String DIMENSION_WIDTH = "width";
	public static final String DIMENSION_HIGHT = "hight";
	public static final String DIMENSION_THICKNESS = "thickness";
	public static final String DIMENSION_TYPE = "type";
	
	public static final int MATERIA_PRIMA = 1;
	public static final int BACHAS = 2;
	public static final int INSUMOS = 3;
	public static final int SERVICIOS = 4;
	public static final int REQUEST_IMAGE_CAPTURE = 1;
	
	public static final String UOM_MP = "20";
	public static final String UOM_BACHA = "1";
	
	public static final String CATEGORY_MP = "raw";
	public static final String CATEGORY_BACHA = "bacha";
	public static final String CATEGORY_INSUMO = "input";
	public static final String CATEGORY_SERVICIO = "service";	
	
	public static final String TPROD = "tProd";
	public static final String TPRODF = "tProdf";
	

	public static final String BACHA_LIST = "BACHA_LIST";
	public static final String MP_LIST = "MP_LIST";
	public static final String INSU_LIST = "INSU_LIST";

	
	public static final String MARBLE_CUTTER_GROUP = "Cortador de Deposito";
	public static final String MARBLE_RESP_GROUP = "Responsable de Deposito";
	public static final String MARBLE_MANAGER_GROUP = "Gerente de Deposito";
	
	public static final int PEDIDOS = 1;
	public static final int ORDENESDEENTREGA = 2;
	public static final int PEDIDOSLINEAS = 3;
	public static final String ARG_ITEM_ID = "ARG_ITEM_ID";
	public static final String RAW_PICKING = "raw";
	public static final String BACHA_PICKING = "bac";
	public static final String INSU_PICKING = "insu";
	public static final String BACHA_FILTER = "bacha";
	public static final String MP_FILTER = "raw";
	public static final String ESTADO_FIN = "done";
	public static final String ACERO = "ace";
	public static final String LOSA = "los";
	public static final String REDONDA = "red";
	public static final String SIMPLE = "sim";
	public static final String DOBLE = "dob";
	public static final int BACHAS_CLI = 1;
	public static final int BACHAS_PROV = 2;
	public static final int MIN_CHAR_LENGTH = 1;
	public static final String PRODUCT_CHANGE_MODEL = "stock.change.product.qty";
	public static final String PRODUCT_CHANGE_ACTION= "change_product_qty";

	public static final int DEFAULT_ESPESORES = 5;
	
	//ODOO Models
	
	public static final String PICKING_MODEL = "stock.picking";
	public static final String MOVE_MODEL = "stock.move";
	public static final String PRODUCT_MODEL = "product.product";
	public static final String PRODUCT_TPL_MODEL = "product.template";
	public static final String IR_DATA_MODEL = "ir.model.data";

	
	public static final String UOM_MODEL = "product.uom";
	public static final String PARTNER_MODEL = "res.partner";
	public static final String USERS_MODEL = "res.users";
	public static final String GROUPS_MODEL = "res.groups";
	public static final String MARBLE_DIM_MODEL = "product.marble.dimension";
	
	//ODOO PICKING Types
	public static final String PICKING_TYPE_ID_IN = "1";
	public static final String PICKING_TYPE_ID_OUT = "2";
	public static final String PICKING_TYPE_ID_INTERNAL = "3";
	public static final String SET_SUPPLIER = "set_supplier";
	public static final String DIMENSIONS_LIST = "dim_list";
	public static final String PRODUCT_SELECTED = "prod_selected";
	public static final String PRODUCT_TYPE = "prod_type";
	
	//Campors del Stock Move
	//public static final String STOCK_MOVE_DIM_QTY = "qty_dimension";
	public static final String STOCK_MOVE_DIM_QTY = "dimension_unit";
	public static final String STOCK_MOVE_DIM_ID = "dimension_id";

	//Campos del dim balance Move
	public static final String DIM_BALANCE_PROD_ID = "product_id";
	public static final String DIM_BALANCE_DIM_ID = "dimension_id";
	public static final String DIM_BALANCE_QTY_UNITS = "qty_unit";
	public static final String DIM_BALANCE_QTY_MT2 = "qty_m2";
	public static final String DB_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DISPLAY_DATE_FORMAT = "dd/MM/yyyy";
	
	

}
