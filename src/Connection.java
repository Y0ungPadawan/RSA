import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;



public class Connection extends Thread implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ObjectInputStream inStream;
		public  Socket socket;
	    public	Keys key;
	    static Message msg; 
	   
	
	
		/**
		 * Инициализирует поля объекта и получает имя пользователя
		 * 
		 * @param socket
		 *            сокет, полученный из server.accept()
		 * @throws ClassNotFoundException 
		 */
		public Connection(Socket socket) {
			this.socket = socket;
			System.out.println("Connection create");
			try {
				System.out.println("Begin create stream");
				inStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				System.out.println("Stream created");
				try {
					this.key = (Keys) inStream.readObject();
					System.out.println("Object read");
					System.out.println(this.key.toString());
				} catch (ClassNotFoundException e) {
					System.out.println("Exception czth");
					e.printStackTrace();
				}
			
			} catch (IOException e) {
				e.printStackTrace();
				close();
			}
			System.out.println("Connection close");
		}
	
		/**
		 * Запрашивает имя пользователя и ожидает от него сообщений. При
		 * получении каждого сообщения, оно вместе с именем пользователя
		 * пересылается всем остальным.
		 * 
		 * @see java.lang.Thread#run()
		 */
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			while(true){
				try {
					inStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				     msg = (Message) inStream.readObject();
					System.out.println("Присланное сообщение "+ msg.toString());
					Server.msg = msg;
					System.out.println(msg.text.toString());
					//if(Server.th.isAlive())
						//Server.th.interrupt();
					//else
					//Server.th.start();
					//while(Server.msg!=null){}
					//Server.th.stop();
				} catch (IOException e) {
					//e.printStackTrace();
//					System.out.println("Нас покинули");
				} catch (ClassNotFoundException e) {
				//	e.printStackTrace();
				}
			}	
		}
	
		/**
		 * Закрывает входной и выходной потоки и сокет
		 */
		public void close() {
			try {
				inStream.close();
				//outStream.close();
				socket.close();
				// Если больше не осталось соединений, закрываем всё, что есть и
				// завершаем работу сервера
				Server.connections.remove(this);
			//	if (Server.connections.size() == 0) {
				//	Server.this.closeAll();
					//System.exit(0);
				//}
			} catch (Exception e) {
				System.err.println("Потоки не были закрыты!");
			}
		}
		
		/**
		 * @overrides toString
		 */
		public String toString(){
			return this.socket.toString()+" | "+this.key;
			
		}
	}	