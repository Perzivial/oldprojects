package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Test {
	static ServerSocket MyService;
	static Socket MyClient;
	public Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		if(scan.nextLine().equals("server")){
			
		    open();
		}
	}
	public static void connect(){
		  
		    try {
		           MyClient = new Socket("Machine name", 2556);
		    }
		    catch (IOException e) {
		        System.out.println(e);
		    }
	}
	public static void open(){
		try {
		       MyService = new ServerSocket(2556);
		        }
		        catch (IOException e) {
		           System.out.println(e);
		        }
	}
}
