package challenge;

import java.io.*;

public class DateDifference {
	
	////////////////////////Global Variables////////////////////////
	private enum MONTHS {JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC};		//Used for Printing some debug information
	public static int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };	//Number of days in each month (Index 0 is Jan, Index 1 is Feb, etc)
	
	//Variables used to refer to elements in date arrays. Makes code more readable than date[0], etc.
	private static int dayIndex = 0;
	private static int monthIndex = 1;
	private static int yearIndex = 2;
	
	//Year Range for date difference calculator 
	private static int minYear = 1900;
	private static int maxYear = 2010;
	
	//A boolean to decide if we want extra debug information
	private static Boolean debug = false;

	////////////////////////Methods////////////////////////
	public static void main(String[] args) {
		
		// Read the test data file, irrespective of the Operating System 
		File file = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "challenge" + File.separator + "testdata.txt");

		// Read the dates one by one and calculate the difference
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			//If error occurred, print error messages
			System.out.println("File not found. Exiting");
			System.out.println("Error Message: " + e.getLocalizedMessage());
			System.exit(1);
		}

		String input;
		try {
			//Continue until all dates are processed. 
			while ((input = br.readLine()) != null) {
				
				Boolean inputValid = false; 	
				String processedInput = "";
				int daysDiff = 0;				//Days difference between the dates
				
				// Date will be stored in int arrays. Index 0=day, 1=month, 2=year
				int[] date1 = new int[3];
				int[] date2 = new int[3];
				
				//Remove any leading or trailing white spaces.
				input = input.trim();
				
				//Remove any whitespaces so there are only the numbers and comma left
				processedInput = input.replace(" ","");
				
				// If the data is in the correct format, the input will be 17 characters long (1 for comma, 16 for the numbers
				if (processedInput.length() != 17) {
					System.out.println("'" + input + "'"+ " is not in the right format. Please change to match this format: DD MM YYYY, DD MM YYYY");
					continue;
				}

				//Validate the dates to check the days, months and years are correct and that the first date comes before the 2nd
				inputValid = validateDates(input, processedInput,  date1, date2);

				if (inputValid) {
					//If the inputs were valid, calculate the difference
					daysDiff = calculateDifference(date1, date2);
					System.out.println(input + ", " + daysDiff);
				}
				
			}
		} catch (IOException e) {
			//Error in reading the data from text file
			System.out.println("Couldn't read the data");
			System.out.println("Error Message: " + e.getLocalizedMessage());
			System.exit(1);
		}
	}

	//Checks if the dates are correct and input order was correct
	private static Boolean validateDates(String input, String processedInput, int[] date1, int[] date2) {
		
		// Parse the string input to integers
		String[] dates = processedInput.split(",");
		
		if(dates.length != 2) {
			System.out.println("'" + input + "'"+ " is not in the right format. Please change to match this format: DD MM YYYY, DD MM YYYY");
			return false;
		}
		
		String[] tempDate1 = {dates[0].substring(0,2), dates[0].substring(2,4),dates[0].substring(4)};
		String[] tempDate2 = {dates[1].substring(0,2), dates[1].substring(2,4),dates[1].substring(4)};

		//Store the dates into the date parameters,
		for (int i = 0; i < 3; i++) {
			date1[i] = Integer.parseInt(tempDate1[i]);
			date2[i] = Integer.parseInt(tempDate2[i]);
		}

		//Check if the values for the each day, month and year are valid
		if (!(dateValid(date1) && dateValid(date2))) {
			System.out.println("'" + input + "' is not valid");
			return false;
		}

		//Check whether the 1st date comes before the 2nd
		if(!inputOrderValid(date1, date2)) {
			System.out.println("The dates aren't in the correct order. The first date comes after the second (" + input + ").");
			return false;
		}
		return true;
	}

	//Check is the date is valid. Whether the date is the right year, month and day. 
	private static boolean dateValid(int[] date) {
		
		//If the year is not within the range, the date isn't valid. 
		if (date[yearIndex] < minYear || date[yearIndex] > maxYear) {
			if(debug) System.out.print(date[yearIndex] + " isn't a valid year. ");
			return false;
		}

		//If the months aren't number from 1 to 12, the date isn't valid. 
		if (date[monthIndex] < 1 || date[monthIndex] > 12) {
			if(debug) System.out.print(date[monthIndex] + " isn't a valid month. ");
			return false;
		}

		// If the year is a leap year, make sure feb has 29 days instead of the default 28
		Boolean isLeap = isLeapYear(date[yearIndex]);
		int tempDaysInMonth = daysInMonth[date[monthIndex] - 1]; 	//temporarily hold the number of days in that month. 
		if (isLeap && date[monthIndex] == 2) {						//If its a leap year and its feb, change the number of days to 29 
			tempDaysInMonth = 29;
		}

		//If the date isn't between 1 and the number of days of that month (inclusive). Then the date isnt valid. 
		if (date[dayIndex] < 1 || date[dayIndex] > tempDaysInMonth) {
			if(debug) System.out.print(date[dayIndex] + " isn't a valid day for "+ MONTHS.values()[date[monthIndex] - 1] + " " + date[yearIndex]+". ");
			return false;
		}

		// return true if day, month and year are valid 
		return true;
	}

	//Checks if the first input 1st parameter comes before the 2nd parameter
	private static Boolean inputOrderValid(int[] date1, int[] date2) {
		
		//Check if date1's year is less than the other. If so, then its valid
		if(date1[yearIndex] >= date2[yearIndex]) {
			
			//if the years are the same and but the date1's month is greater, its not valid
			if(date1[yearIndex] == date2[yearIndex] && date1[monthIndex] <= date2[monthIndex]) {
				//if the months are the same, check if date1's day is greater, if so its invalid. Otherwise its valid.
				if(date1[monthIndex] == date2[monthIndex] && date1[dayIndex] > date2[dayIndex]) {
					return false;
				} else {
					return true;
				}
			} else {
				//if date1's year was greater than the 2nd or if the month was greater than its not in the correct order
				return false;
			}
		}
		//date1's year was less than the 2nd, then its valid so return true.
		return true;
	}

	//Calculate difference in days for the 2 dates
	private static int calculateDifference(int[] date1, int[] date2) {
		
		int daysDiff = 0;
		
		//Find the remaining days left in the year (doesn't include the date)
		daysDiff += calculateRemaining(date1);
		
		//Add the days for the years between date1's year and date2's year
		for(int i = date1[yearIndex] + 1; i < date2[yearIndex]; i++) {
			if(isLeapYear(i)) {
				daysDiff += 366;
			} else {
				daysDiff += 365;
			}
		}
		
		//Calculate the remaining days for date2
		int temp = calculateRemaining(date2);		
		
		if(date1[yearIndex] == date2[yearIndex]) {
			//if its the same year, we want to take away the days remaining for date2 from date1 (e.g. date1's remaining is 160, date2's is 150, 160-150 means a 10 day diff)
			daysDiff -= temp;
		} else {
			//depending on if its a leap year or not, we want to take away days remaining from the total number of days in the year to get how many days we are in for the last year
			daysDiff += isLeapYear(date2[yearIndex]) ? 366 - temp : 365 - temp;
		}
		
		//Return the difference 
		return daysDiff;
	}

	//
	private static int calculateRemaining(int[] date) {
		Boolean isLeap = isLeapYear(date[yearIndex]);
		int daysInYear = isLeap ? 366 : 365;
		int daysGoneBy = 0;								//Holds the number of days that have gone by in the year so far (including the 'date')
		
		//if its a leap year, change the number of days for Feb to 29
		if (isLeap) {
			daysInMonth[1] = 29;
		}
		
		//Add the days of the months that have already passed in the year  (e.g. if the date was 12 05 2000, get the days Betwen Jan and May
		for (int i = 0; i < date[monthIndex] - 1; i++) {
			daysGoneBy += daysInMonth[i];
		}
		
		//if its a leap year, change the number of days for Feb to backk to 28
		if (isLeap) {
			daysInMonth[1] = 28;
		}
		
		//Add on the days for the date's month
		daysGoneBy += date[dayIndex];
		
		//return the difference to get days remaining in the year
		return daysInYear - daysGoneBy;
	}

	//Calculates whether the year is a leap year or not
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
