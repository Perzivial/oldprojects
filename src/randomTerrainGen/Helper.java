package randomTerrainGen;

import java.awt.Color;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Helper {
	public static Helper help = new Helper();
	ArrayList<String> firstNames = new ArrayList<String>();
	ArrayList<String> lastNames = new ArrayList<String>();

	public Helper() {
		firstNames.add("Bob");
		firstNames.add("Jerry");
		firstNames.add("James");
		firstNames.add("Megan");
		firstNames.add("Zoe");
		firstNames.add("Nat");

		lastNames.add("Jones");
		lastNames.add("Smith");
		lastNames.add("Pan");
		lastNames.add("MacDonald");
		lastNames.add("Lee");
		lastNames.add("Richmond");
	}

	public static int randInt(int min, int max) {
		Random random = new Random();
		int randomNum = random.nextInt(max - min + 1) + min;
		return randomNum;
	}

	public static Color randomrainbowcolor(float lightness) {

		Random rand = new Random();
		/*
		 * 
		 * float r = rand.nextFloat() / 2f + 0.5f; float g = rand.nextFloat() /
		 * 2f + 0.5f; float b = rand.nextFloat() / 2f + 0.5f; Color randomColor
		 * = new Color(r, g, b);
		 */
		final float hue = rand.nextFloat();
		// Saturation between 0.1 and 0.3
		final float saturation = (rand.nextInt(2000) + 1000) / 10000f;
		final Color randomColor = Color.getHSBColor(hue, saturation, lightness);
		return randomColor;
	}

	public Color transparent(Color color) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
	}

	public static ArrayList<Integer> toArray(int[] arr) {
		ArrayList<Integer> out = new ArrayList<Integer>();
		for (int current : arr) {
			out.add(current);
		}
		return out;
	}

	public static int amountOf(String in, ArrayList<String> list) {
		int out = 0;
		for (String str : list) {
			if (str.equals(in))
				out++;
		}
		return out;
	}

	public static int weigh(int... list) {
		int index = 0;
		int value = Integer.MIN_VALUE;
		for (int i = 0; i < list.length; i++) {
			int current = list[i];
			if (current > value) {
				value = current;
				index = i;
			}

		}
		System.out.println(index);
		return index;
	}

}
