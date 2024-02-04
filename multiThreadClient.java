import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;

class writerThread implements Runnable {
	private ObjectOutputStream oos;
	private String name;
	private Socket socket;

	public writerThread(Socket socket, ObjectOutputStream oos, String name) {
		this.oos = oos;
		this.name = name;
		this.socket = socket;
		new Thread(this).start();
	}

	@Override
	public void run() {
		System.out.println("Enter the file name you want from server :");
		Scanner sc = new Scanner(System.in);
		while (true) {
			String message = sc.nextLine();
			if (message.equals("exit"))
				break;

			try {

				oos.writeObject(message + " 10.42.0.195");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}

class readerThread implements Runnable {

	private ObjectInputStream ois;
	private String name;
	private Socket socket;
	private DataOutputStream dataOutputStream = null;
	private static DataInputStream dataInputStream = null;

	public readerThread(Socket cSocket, ObjectInputStream ois, String name) throws IOException {
		this.ois = ois;
		this.name = name;
		this.socket = cSocket;
		dataInputStream = new DataInputStream(cSocket.getInputStream());
		dataOutputStream = new DataOutputStream(cSocket.getOutputStream());
		new Thread(this).start();

	}

	@Override
	public void run() {

		while (true) {
			try {
				// receive from server .
				Object fromServer = ois.readObject();
				String message = (String) fromServer;
				// System.out.println("\n" + this.name + " got message From server : " + (String) fromServer + "\n");
				if (message.equals("exit"))
					break;
				System.out.println("File downloading...");

				receiveFile("Physics.pdf");
				System.out.println("File downloaded");
				

				
				

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void receiveFile(String fileName)
			throws Exception {
		int bytes = 0;
		FileOutputStream fileOutputStream = new FileOutputStream(fileName);

		long size = dataInputStream.readLong(); // read file size
		byte[] buffer = new byte[4 * 1024];
		while (size > 0 && (bytes = dataInputStream.read(buffer, 0,(int) Math.min(buffer.length, size))) != -1) 
		{
			// Here we write the file using write method
			fileOutputStream.write(buffer, 0, bytes);
			size -= bytes; // read upto file size
		}

		// Here we received file
		

		fileOutputStream.close();
	}

}

public class multiThreadClient {
	public static void main(String args[]) throws IOException {
		Socket socket = new Socket("localHost", 22224);
		System.out.println("connection established") ;

		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

		// this portion is done by writter thread ;

		// Scanner sc = new Scanner(System.in) ;
		// String message = sc.nextLine() ;
		// if(message.equals("exit"))break ;

		// // send to server :
		// oos.writeObject(message) ;
		new writerThread(socket, oos, "client2");

		// this portion done by reader thread ;
		new readerThread(socket, ois, "client2");

		// socket.close() ;
		// receive file function is start here
		// receive file function is start here

	}

}