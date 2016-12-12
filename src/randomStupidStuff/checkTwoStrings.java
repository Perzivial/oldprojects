package randomStupidStuff;

import java.util.Scanner;

public class checkTwoStrings {

	public checkTwoStrings() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		do {
			System.out.println("Please enter two strings to be compared");
			String x = scan.nextLine();
			String y = scan.nextLine();
			if (areSimilar(x, y))
				System.out.println("they are similar");
			else
				System.out.println("they are not");
			System.out.println("test more strings?");
		} while (!scan.nextLine().equals("n"));

		scan.close();
	}

	public static boolean areSimilar(String x, String y) {
		int numX = 0;
		int numY = 0;
		for (char current : x.toCharArray()) {
			numX += Character.getNumericValue(current);
		}
		for (char current : y.toCharArray()) {
			numY += Character.getNumericValue(current);
		}
		double percent;
		if (numX < numY)
			percent = ((double) numX / (double) numY) * 100;
		else
			percent = ((double) numY / (double) numX) * 100;

		System.out.println(percent);
		if (percent > 80)
			return true;
		return false;
	}
}
