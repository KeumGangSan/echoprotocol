package s201810246;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	Socket CLIENT = null;
	String IP;
	BufferedReader reader;
	InputStream is;
	OutputStream os;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	String send;
	String receive;
	static final int PORT = 7365;
	
	public Client(String IPA) {
		IP = IPA;
		
		try {
			System.out.println("- 단일 클라이언트 -");
			CLIENT = new Socket(IP, PORT);
			reader = new BufferedReader(new InputStreamReader(System.in));
			is = CLIENT.getInputStream();
			os = CLIENT.getOutputStream();
			ois = new ObjectInputStream(is);
			oos = new ObjectOutputStream(os);
			System.out.print("반복 : ");
			
			while((send = reader.readLine()) != null) {
				oos.writeObject(send);
				oos.flush();
				
				if (send.equals("Q") || send.equals("q")) {
					break;
				}
				
				receive = (String)ois.readObject();
				System.out.println(CLIENT.getInetAddress() + "(오)로 부터 받은 메세지 : " + receive);
				System.out.print("입력 : ");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		finally {
			try {
				is.close();
				os.close();
				ois.close();
				oos.close();
				CLIENT.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new Client("127.0.0.1");
	}
}
