package cs213.photoAlbum.util;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Utility final class for handling date inputs on the command line
 * @author Ananth Rao
 * @author Allon Fineziber
 *
 */
public final class DateUtils {

	
	public static String createDateString(String year, String month, String day, String hour, String minute, String second) {
		int y, mo, d, h, mi, s;
		try {
			y = Integer.parseInt(year);
			mo = Integer.parseInt(month);
			d = Integer.parseInt(day);
			h = Integer.parseInt(hour);
			mi = Integer.parseInt(minute);
			s = Integer.parseInt(second);
		} catch (Exception e) {
			return null;
		}

		return mo + "/" + d + "/" + y + "-" + h + ":" + mi + ":" + s;
	}
	

	
	/**
	 * Extracts the date from a given string, in two different ways depending on whether the source is the system or the input
	 * @author Ananth Rao
	 * @author Allon Fineziber
	 * @param dateString
	 * @param isSourceSystem
	 * @return
	 */
	public static Calendar extractDate(String dateString, boolean isSourceSystem) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		StringTokenizer tk = new StringTokenizer(dateString, "/-:TZ");
		LinkedList<Integer> date = new LinkedList<Integer>();
		while(tk.hasMoreTokens()) {
			String tok = tk.nextToken();
			int dateField = 0;
			try {
				dateField = Integer.parseInt(tok);
			} catch(NumberFormatException e) {
				e.getLocalizedMessage();
				return null;
			}
			date.add(dateField);
		}
		try {
			switch(date.size()) {
			case 0:
				return null;
			case 1:
				cal.set(Calendar.YEAR, date.removeFirst());
				cal.set(Calendar.MONTH, 0);
				cal.set(Calendar.DATE, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.getTime();
				break;
			case 2:
				cal.set(Calendar.MONTH, date.removeFirst()-1);
				cal.set(Calendar.YEAR, date.removeFirst());
				cal.set(Calendar.DATE, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.getTime();
				break;
			case 3:
				cal.set(Calendar.MONTH, date.removeFirst()-1);
				cal.set(Calendar.DATE, date.removeFirst());
				cal.set(Calendar.YEAR, date.removeFirst());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.getTime();
				break;
			case 4:
				cal.set(Calendar.MONTH, date.removeFirst()-1);
				cal.set(Calendar.DATE, date.removeFirst());
				cal.set(Calendar.YEAR, date.removeFirst());
				cal.set(Calendar.HOUR_OF_DAY, date.removeFirst());
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.getTime();
				break;
			case 5:
				cal.set(Calendar.MONTH, date.removeFirst()-1);
				cal.set(Calendar.DATE, date.removeFirst());
				cal.set(Calendar.YEAR, date.removeFirst());
				cal.set(Calendar.HOUR_OF_DAY, date.removeFirst());
				cal.set(Calendar.MINUTE, date.removeFirst());
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.getTime();
				break;
			case 6:
				if(isSourceSystem) {
					cal.set(Calendar.YEAR, date.removeFirst());
					cal.getTime();					
					cal.set(Calendar.MONTH, date.removeFirst()-1);
					cal.getTime();					
					cal.set(Calendar.DATE, date.removeFirst());
					cal.getTime();					
					cal.set(Calendar.HOUR_OF_DAY, date.removeFirst());
					cal.getTime();					
					cal.set(Calendar.MINUTE, date.removeFirst());
					cal.getTime();					
					cal.set(Calendar.SECOND, date.removeFirst());
					cal.getTime();					
					cal.set(Calendar.MILLISECOND, 0);					
					cal.getTime();					
				} else {
					cal.set(Calendar.MONTH, date.removeFirst()-1);
					cal.set(Calendar.DATE, date.removeFirst());
					cal.set(Calendar.YEAR, date.removeFirst());
					cal.set(Calendar.HOUR_OF_DAY, date.removeFirst());
					cal.set(Calendar.MINUTE, date.removeFirst());
					cal.set(Calendar.SECOND, date.removeFirst());
					cal.set(Calendar.MILLISECOND, 0);
				}
				cal.getTime();					
				break;
			default:
				return null;
			}
		} catch(Exception e) {
			return null;
		}
		return cal;
	}
}
