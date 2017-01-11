package perspectiveDrawing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class widenMaze {

	public widenMaze() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(new File("level.txt"));
		while( scan.hasNextLine()){
		String line = scan.nextLine();
		for(int i = 0; i < line.length(); i ++){
			System.out.print(line.charAt(i));
			System.out.print(" ");
		}
		System.out.println();
		System.out.println();
		}
	}

}
