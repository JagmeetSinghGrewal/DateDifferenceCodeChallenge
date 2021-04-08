package challenge;

import java.io.*;

public class DateDifference {
	// This variable holds how many days there are in each month so Index 0 is Jan,
	// index 1 is feb, etc
	public int[] daysinMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };


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
				//Date will be stored in arrays. With index 0 representing day
				//Index 1 representing Month
				//Index 2 representing year
				int[] date1 = new int[3];
				int[] date2 = new int[3];
				
				//If the data is in the correct format, the input will be 22 characters long
				if(input.length() != 22) {
					System.out.println("'" + input + "'" + " is not in the right format. Please change to match this format: DD MM YYYY, DD MM YYYY");
					continue;
				}
				
				validateDates(input, date1, date2);
				calculateDifference(date1, date2);
				for (int i = 0; i < 3; i++) {
					System.out.println(date1[i]);
					System.out.println(date2[i]);
				}
			}
		} catch (IOException e) {
			System.out.println("Couldn't read the data");
			System.out.println("Error Message: " + e.getLocalizedMessage());
		}
	}

	private static void validateDates(String input, int[] date1, int[] date2) {

		String[] dates = input.split(", ");
		String[] tempDate1 = dates[0].split(" ");
		String[] tempDate2 = dates[1].split(" ");

		for (int i = 0; i < 3; i++) {
			date1[i] = Integer.parseInt(tempDate1[i]);
			date2[i] = Integer.parseInt(tempDate2[i]);
		}
		
	}

	private static void calculateDifference(int[] date1, int[] date2) {
		// System.out.println(dates);
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
