import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
//import java.sql.Connection;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.Iterator;


public class Server {
	
	static ArrayList <Connection> connections  =  new ArrayList<Connection>();
	private ServerSocket server;
	static Message msg = null;
	//static Thread th = 
	
	public Server() throws ClassNotFoundException{

//		new Thread(new Runnable(){
//	    		public void run(){
//	    			if (th.isAlive()){
//	    				if (msg ==null){
//	    					th.interrupt();
//	    				}
//	    			}
//	    		}
//	    	}
//		).start();
		
		new Thread(new Runnable(){
	    	public void run(){
	    		while (true){
	    			try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		if (msg!= null){
		    			synchronized(connections) {
	    					Iterator<Connection> iter = connections.iterator();
	    					while(iter.hasNext()) {
	    						Connection temp = iter.next();
	    						if (temp.socket.isClosed()){
	    							iter.remove();
	    							continue;
	    						}
	    						if (msg.addressee.port == temp.socket.getPort()){
									try {
										System.out.println("message sent to "+msg.addressee.port);
										ObjectOutputStream outStream = new ObjectOutputStream(temp.socket.getOutputStream());
		    							outStream.writeObject(msg);
		    							outStream.flush();
		    							break;
									} catch (IOException e) {
									//	e.printStackTrace();
										
										continue;
									}
	    						}
	    					}
	    				}
		    			msg = null;
		    		}
	    		}
	    	}
	    }).start();
		
		new Thread(new Runnable(){
	    	public void run(){
	    		
	    		try {
	    			server = new ServerSocket(9998, 100);
	    			while (true) {
	    				Server.printConnections();
	    				Socket socket = server.accept();
	    				System.out.println(" Server created");
	    				Connection con = new Connection(socket);
	    				System.out.println(con.socket.toString()+" "+con.key.N.toString()+" current socket");
	    				connections.add(con);
	    				System.out.println(connections.size());
	    				Iterator<Connection> iter1 = connections.iterator();
    					while(iter1.hasNext()) {
    						Connection temp = iter1.next();
    						System.out.println("Я проверяю "+ temp.socket.isClosed());
    						if (temp.socket.isClosed()){
    							iter1.remove();
    							System.out.println("Deleted");
    						}
    					}
	    				ArrayList<User> users = null;
	    				users = new ArrayList<User>();
	    				Iterator<Connection> iter2 = connections.iterator();
	    				while(iter2.hasNext()) {
	    					users.add(new User(iter2.next()));
	    				}
	    				synchronized(connections) {
	    					Iterator<Connection> iter = connections.iterator();
	    					while(iter.hasNext()) {
	    						Connection temp = iter.next();
	    						if (temp.socket.isClosed()){
	    							iter.remove();
	    							System.out.println("Deleted");
	    							continue;
	    						}
	    						
	    						    try {ObjectOutputStream outStream = new ObjectOutputStream(temp.socket.getOutputStream());
	    						    	outStream.writeObject(users);
	    								outStream.flush();
	    						    } catch(IOException e){
	    						    	temp.socket.close();
	    						    	iter.remove();
	    						    }
	    					}
	    				}	
	    				con.start();
	    				
	    			}
	    		} catch (IOException e) {
	    			e.printStackTrace();
	    		} finally {
	    			closeAll();
	    			System.out.println("CLOSE_ALL");
	    		}		
	    		
	    	}
	    }).start();
		
		
	}
	
	public void closeAll() {
		try {
			server.close();
			
			// Перебор всех Connection и вызов метода close() для каждого. Блок
			// synchronized {} необходим для правильного доступа к одним данным
			// их разных нитей
			synchronized(connections) {
				Iterator<Connection> iter = connections.iterator();
				while(iter.hasNext()) {
					((Connection) iter.next()).close();
				}
			}
		} catch (
				Exception e) {
			System.err.println("Потоки не были закрыты!");
		}
	}
	
	public static void main(String[] args){
		try {
			new Server();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//new Frame().setVisible(true);
	}
	
	
	public static void printConnections(){
		synchronized(connections) {
			Iterator<Connection> iter = connections.iterator();
			while(iter.hasNext()) {
				Connection temp = iter.next();
				if (temp.socket.isClosed())
					iter.remove();
				System.out.println(((Connection) temp).toString());
			}
		}
	}
//
//	
//	private class Connection extends Thread implements Serializable{
//		
//		private static final long serialVersionUID = 1L;
//		private ObjectInputStream inStream;
//		private ObjectOutputStream outStream;
//		private Socket socket;
//		Keys key;
//	
//		private String name = "";
//	
//		/**
//		 * Инициализирует поля объекта и получает имя пользователя
//		 * 
//		 * @param socket
//		 *            сокет, полученный из server.accept()
//		 * @throws ClassNotFoundException 
//		 */
//		public Connection(Socket socket) {
//			this.socket = socket;
//			System.out.println("Connection create");
//			try {
//				System.out.println("Begin create stream");
//				outStream = new ObjectOutputStream(socket.getOutputStream());
//				inStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
//				System.out.println("Stream created");
//				try {
//					this.key = (Keys) inStream.readObject();
//					System.out.println("Object read");
//					System.out.println(this.key.toString());
//				} catch (ClassNotFoundException e) {
//					System.out.println("Exception czth");
//					e.printStackTrace();
//				}
//				
//				outStream.writeObject(connections);
//				outStream.flush();
//				System.out.println("Object flush");
//			
//			} catch (IOException e) {
//				e.printStackTrace();
//				close();
//			}
//			System.out.println("Connection close");
//		}
//	
//		/**
//		 * Запрашивает имя пользователя и ожидает от него сообщений. При
//		 * получении каждого сообщения, оно вместе с именем пользователя
//		 * пересылается всем остальным.
//		 * 
//		 * @see java.lang.Thread#run()
//		 */
//		@Override
//		public void run() {
//			try {
//				
////				try {
////				//	Keys k = (Keys) inStream.readObject();
////				} catch (ClassNotFoundException e) {
////					e.printStackTrace();
////				}
////				
//				synchronized(connections) {
//					Iterator<Connection> iter = connections.iterator();
//					while(iter.hasNext()) {
//						(iter.next()).outStream.writeObject(connections);
//					}
//				}
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				close();
//			}
//		}
//	
//		/**
//		 * Закрывает входной и выходной потоки и сокет
//		 */
//		public void close() {
//			try {
//				inStream.close();
//				outStream.close();
//				socket.close();
//				// Если больше не осталось соединений, закрываем всё, что есть и
//				// завершаем работу сервера
//				connections.remove(this);
//				if (connections.size() == 0) {
//					Server.this.closeAll();
//					System.exit(0);
//				}
//			} catch (Exception e) {
//				System.err.println("Потоки не были закрыты!");
//			}
//		}
//	}	
//*/
//
	}


//public static void main(String[] args) throws IOException {
//System.out.println("Welcome to Server side");
//BufferedReader in = null;
//PrintWriter    out= null;
//
//ServerSocket servers = null;
//Socket[]     fromclient = null;
//
//// create server socket
//try {
//  servers = new ServerSocket(4444, 100);
//} catch (IOException e) {
//  System.out.println("Couldn't listen to port 4444");
//  System.exit(-1);
//}
//int i = 0;
//try {
//  System.out.print("Waiting for a client...");
//  fromclient[i++]= servers.accept();
//  System.out.println("Client connected");
//} catch (IOException e) {
//  System.out.println("Can't accept");
//  System.exit(-1);
//}
//
//in  = new BufferedReader(new 
// InputStreamReader(fromclient[i].getInputStream()));
//out = new PrintWriter(fromclient[i].getOutputStream(),true);
//String         input,output;
//
//System.out.println("Wait for messages");
//while ((input = in.readLine()) != null) {
// if (input.equalsIgnoreCase("exit")) break;
// out.println("S ::: "+input);
// System.out.println(input);
//}
//out.close();
//in.close();
//fromclient[i].close();
//servers.close();
//}
