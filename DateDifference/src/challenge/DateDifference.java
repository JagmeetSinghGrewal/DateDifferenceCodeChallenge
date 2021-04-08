package challenge;

import java.io.*;

public class DateDifference {
	// This variable holds how many days there are in each month so Index 0 is Jan,
	// index 1 is feb, etc
	private enum MONTHS {JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC};
	public static int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private static int dayIndex = 0;
	private static int monthIndex = 1;
	private static int yearIndex = 2;
	private static int minYear = 1900;
	private static int maxYear = 2010;

	public static void main(String[] args) {
		// Read the test data file, irrespective of the Operating System
		File file = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "challenge"
				+ File.separator + "testdata.txt");

		// Read the dates one by one and calcualte the difference
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Exiting");
			System.out.println("Error Message: " + e.getLocalizedMessage());
			System.exit(1);
		}

		String input;
		try {
			while ((input = br.readLine()) != null) {
				// Date will be stored in arrays. With index 0 representing day
				// Index 1 representing Month
				// Index 2 representing year
				int[] date1 = new int[3];
				int[] date2 = new int[3];
				
				int totalDays = 0;
				Boolean inputValid = false;
				String err = new String("");
				// If the data is in the correct format, the input will be 22 characters long
				if (input.length() != 22) {
					System.out.println("'" + input + "'"
							+ " is not in the right format. Please change to match this format: DD MM YYYY, DD MM YYYY");
					continue;
				}

				inputValid = validateDates(input, date1, date2, err);

				if (inputValid) {
					totalDays = calculateDifference(date1, date2);
					System.out.println(input + ", " + totalDays);
				}
				
			}
		} catch (IOException e) {
			System.out.println("Couldn't read the data");
			System.out.println("Error Message: " + e.getLocalizedMessage());
		}
	}

	private static Boolean validateDates(String input, int[] date1, int[] date2, String err) {

		
		// Parse the string input to integers
		String[] dates = input.split(", ");
		String[] tempDate1 = dates[0].split(" ");
		String[] tempDate2 = dates[1].split(" ");

		for (int i = 0; i < 3; i++) {
			date1[i] = Integer.parseInt(tempDate1[i]);
			date2[i] = Integer.parseInt(tempDate2[i]);
		}

		if (!(dateValid(date1) && dateValid(date2))) {
			System.out.println(" Therefore, '" + input + "' is not valid");
			return false;
		}

		if(!inputOrderValid(date1, date2)) {
			System.out.println("The dates aren't in the correct order. The first date comes after the second (" + input + ").");
			return false;
		}
		return true;
	}

	//Check is the date is valid. Whether the date is the right year, month and day. 
	private static boolean dateValid(int[] date) {
		
		if (date[yearIndex] < minYear || date[yearIndex] > maxYear) {
			System.out.print(date[yearIndex] + " isn't a valid year. ");
			return false;
		}

		if (date[monthIndex] < 1 || date[monthIndex] > 12) {
			System.out.print(date[monthIndex] + " isn't a valid month. ");
			return false;
		}

		// If the year is a leap year, make sure feb has 29 days.
		if (isLeapYear(date[yearIndex])) {
			daysInMonth[1] = 29;
		}

		if (date[dayIndex] < 1 || date[dayIndex] > daysInMonth[date[monthIndex] - 1]) {
			System.out.print(date[dayIndex] + " isn't a valid day for "+ MONTHS.values()[date[monthIndex] - 1] + " " + date[yearIndex]);
			return false;
		}

		// Change the day back for the next validation
		if (isLeapYear(date[yearIndex])) {
			daysInMonth[1] = 28;
		}

		return true;
	}

	//Checks if the first input first paramter comes before the 2nd parameter
	private static Boolean inputOrderValid(int[] date1, int[] date2) {
		
		//Check date1's year is less than the other, then its valid
		if(date1[yearIndex] >= date2[yearIndex]) {
			
			//if the years are the same and but the date1's month is greater, its not valid
			if(date1[yearIndex] == date2[yearIndex] && date1[monthIndex] <= date2[monthIndex]) {
				//if the months are the same, check is the date1's day is greater, if so its invalid. Otherwise its valid.
				if(date1[monthIndex] == date2[monthIndex] && date1[dayIndex] > date2[dayIndex]) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
		return true;
	}


	private static int calculateDifference(int[] date1, int[] date2) {
		int total = 0;
		total += calculateRemaining(date1) + 1;
		//System.out.println("nice: "+total);
		for(int i = date1[yearIndex] + 1; i < date2[yearIndex]; i++) {
			if(isLeapYear(i)) {
				total += 366;
			} else {
				total += 365;
			}
		}
		
		//Calculate the days from the date til the end of the year, but plus 1 to re
		int temp = calculateRemaining(date2); 
		//System.out.println("ouch:" +temp);
		if(date1[yearIndex] == date2[yearIndex]) {
			total -= temp;
		} else {
			total += isLeapYear(date2[yearIndex]) ? 366 - temp : 365 - temp;
		}
	
		return total;
	}

	private static int calculateRemaining(int[] date) {
		int daysInYear = isLeapYear(date[yearIndex]) ? 366 : 365;
		int total = 0;
		if (isLeapYear(date[yearIndex])) {
			daysInMonth[1] = 29;
		}
		for (int i = 0; i < date[monthIndex] - 1; i++) {
			total += daysInMonth[i];
		}
		
		if (isLeapYear(date[yearIndex])) {
			daysInMonth[1] = 28;
		}
		total += date[dayIndex];
		return daysInYear - total;
	}

	private static Boolean isLeapYear(int year) {
		Boolean leapYear = false;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0) {
					leapYear = true;
				}
			} else {
				leapYear = true;
			}
		}
		return leapYear;
	}

}
