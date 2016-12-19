package ua.kiev.prog;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
        Utils utils = new Utils();
		Scanner scanner = new Scanner(System.in);
		try {
			String login = "";
			String pswd = "";
			do{
				System.out.println("Enter your login: ");
				login = scanner.nextLine();

				System.out.println("Enter your password: ");
				pswd = scanner.nextLine();
			}while(utils.sendReq("/authorization?login=" + login + "&password=" + pswd) != 200);

			Thread thread = new Thread(new GetThread(login));
			thread.setDaemon(true);
			thread.start();

            System.out.println("Enter your message: ");
			while (true) {
				String text = scanner.nextLine();
				if (text.isEmpty()) break;
				Message m;
                int res = 200;
                if("getusers".equals(text)){
                    utils.sendReqforUsers();
                }else if(text.contains("::")){
                    int cut = text.indexOf("::");
                    m = new Message(login, text.substring(cut + 2));
                    m.setTo(text.substring(0, cut).trim());
                    res = m.send(Utils.getURL() + "/add");
                }else{
                    m = new Message(login, text);
                    res = m.send(Utils.getURL() + "/add");
                }

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
