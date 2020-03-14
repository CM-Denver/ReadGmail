package reademail;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		ReadMail readMail = new ReadMail();
		String[][] messages = readMail.ReadEmail("minecraftmason09@gmail.com", "secretrv1985", 4);
		int currentMessage = 1;
		System.out.println(messages[currentMessage][0]);
		System.out.println(messages[currentMessage][1]);
		System.out.println(messages[currentMessage][2]);

	}

}
