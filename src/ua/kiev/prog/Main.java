package ua.kiev.prog;

import java.io.IOException;
import java.util.Scanner;

import static ua.kiev.prog.Utils.sendReq;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		try {
			String login = "";
			String pswd = "";
			do{
				System.out.println("Enter your login: ");
				login = scanner.nextLine();

				System.out.println("Enter your password: ");
				pswd = scanner.nextLine();
			}while(sendReq("/authorization?login=" + login + "&password=" + pswd) != 200);

			Thread thread = new Thread(new GetThread(login));
			thread.setDaemon(true);
			thread.start();

            System.out.println("Enter your message: ");
			while (true) {
				String text = scanner.nextLine();
				if (text.isEmpty()) break;
				Message m;
                if(text.contains("::")){
                    int cut = text.indexOf("::");
                    m = new Message(login, text.substring(cut + 2));
                    m.setTo(text.substring(0, cut).trim());
                }else{
                    m = new Message(login, text);
                }
				int res = m.send(Utils.getURL() + "/add");

				if (res != 200) { // 200 OK
					System.out.println("HTTP error occured: " + res);
					return;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			scanner.close();
		}
	}
}
