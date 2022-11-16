package s201810246;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;

class EchoServerThread implements Runnable {
	Socket CLIENT;
	InputStream is;
	OutputStream os;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	String receive;
	int repeat;
	boolean number = false;
	
	public EchoServerThread(Socket s) {
		CLIENT = s;
		try {
			System.out.println(CLIENT.getInetAddress() + "(으)로 부터 연결 요청");
			is = CLIENT.getInputStream();
			os = CLIENT.getOutputStream();
			ois = new ObjectInputStream(is);
			oos = new ObjectOutputStream(os);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				receive = (String)ois.readObject();
				if (!number) {
					if (isNumeric(receive)) {
						number = true;
						repeat = Integer.parseInt(receive);
					}
				}
				System.out.println(CLIENT.getInetAddress() + "의 메세지 : " + receive);
				for (int i = 0; i < repeat; i++) {
					oos.writeObject(receive);
				}
				oos.flush();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("클라이언트 강제 종료");
		}
		finally {
			try {
				is.close();
				ois.close();
				os.close();
				oos.close();
				CLIENT.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isNumeric(String input) {
	    try {
	        Integer.parseInt(input);
	        return true;
	    }
	    catch (NumberFormatException e) {
	        return false;
	    }
	}
}

public class ServerThread {
	ServerSocket SERVER;
	Socket CLIENT;
	static final int PORT = 7365;
	
	public ServerThread() {
		try {
			SERVER = new ServerSocket(PORT);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		System.out.println("- 다중 사용자 서버 -");
		System.out.println("서버는 클라이언트의 접속 요청을 기다리는 중...");
		
		while (true) {
			try {
				CLIENT = SERVER.accept();
				EchoServerThread clientThread = new EchoServerThread(CLIENT);
				Thread t = new Thread(clientThread);
				t.start();
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	public static void main(String[] args) {
		new ServerThread();
	}
}
