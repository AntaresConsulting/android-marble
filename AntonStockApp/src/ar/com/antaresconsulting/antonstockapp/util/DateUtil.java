package ar.com.antaresconsulting.antonstockapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String formatDateToStr(Date date){
		SimpleDateFormat sd = new SimpleDateFormat(AntonConstants.DB_DATE_FORMAT);
		return sd.format(date);
	}

	public static String formatDateToStr(String arrivalDate) {
		SimpleDateFormat sd = new SimpleDateFormat(AntonConstants.DB_DATE_FORMAT);
		SimpleDateFormat sd2 = new SimpleDateFormat(AntonConstants.DISPLAY_DATE_FORMAT);		
		try {
			return sd2.format(sd.parse(arrivalDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrivalDate;
	}
}
