import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream; 
import java.io.DataOutputStream ;
import java.io.IOException ;
import java.net.ServerSocket ;
import java.net.Socket ;
import java.io.ObjectInputStream ;
import java.io.ObjectOutputStream ;
import java.util.* ;


class serverThread implements Runnable
{
	public Socket clientSocket ;
	public String name ;
	public Thread t ;
	public ObjectInputStream ois ;
	public ObjectOutputStream oos ;
	public DataOutputStream dataOutputStream = null ;
	public DataInputStream DataInputStream = null ;

	public serverThread(Socket cSocket,String name) throws IOException
	{
		this.clientSocket = cSocket ;
		this.name = name ;		

		ois = new ObjectInputStream(this.clientSocket.getInputStream()) ;
		oos = new ObjectOutputStream(this.clientSocket.getOutputStream()) ;

		dataOutputStream = new DataOutputStream(this.clientSocket.getOutputStream());
		DataInputStream = new DataInputStream(this.clientSocket.getInputStream()) ;

		this.t  = new Thread(this);
		this.t.start();

	}


	@Override
	public void run()
	{		
		try{
			// ObjectInputStream  ois = new ObjectInputStream(this.clientSocket.getInputStream()) ;
			// ObjectOutputStream oos = new ObjectOutputStream(this.clientSocket.getOutputStream()) ;

				// Read from client :
			while(true)
			{
				try 
				{
					Object cMsg = ois.readObject() ;
					if(cMsg==null) break ;
					System.out.println("Request for " + (String)cMsg  + "\n") ;

					
					String message = (String)cMsg ;

					String[] words = message.split("\\s+") ;

					String ipAddress = words[1] ;
					String filename = words[0] ;




				
					

					oos.writeObject(message) ;

					if(filename.equals("Physics.pdf"))
					{

						System.out.println("sending file from server to this ip Address " + ipAddress) ;

						sendFile("/home/rokon/Documents/network_programming/Physics.pdf") ;
						System.out.println("File sent successfully to " + ipAddress);

						// System.out.println("file sent successfully to the ip Address " + ipAddress) ;

					}
					

				}
				catch(Exception e)
				{
					System.out.println(e);
				}
			}

		}
		catch(Exception e)
		{
			System.out.println("pai nai ") ;
			e.printStackTrace() ;
		}	

		// try{
		// 	// clientSocket.close();

		// }
		// catch(IOException e)
		// {
		// 	e.printStackTrace() ;
		// }
		
	
			
	}

	public  void sendFile(String path) throws Exception
	{
		int bytes = 0 ;
		//open the file where he located in the server ;
		File file = new File(path) ;
		FileInputStream fileInputStream = new FileInputStream(file) ;

		this.dataOutputStream.writeLong(file.length()) ;

		byte[] buffer = new byte[4*1024] ;
		
		while((bytes= fileInputStream.read(buffer))!=-1)
		{
			dataOutputStream.write(buffer,0,bytes) ;
			dataOutputStream.flush() ;
		}
		

		fileInputStream.close() ;

		
	}
}

public class multiThreadServer 
{
	public static void main(String args[]) throws IOException
	{
		ServerSocket serverSocket = new ServerSocket(22224) ;

		System.out.println("Server is waiting") ;
		int i =1 ;
		while(true)
		{
			// server accept .
			Socket socket = serverSocket.accept() ;

			// new server thread start.......
			serverThread thread = new serverThread(socket,"client" + i );

			
		}
	}
}