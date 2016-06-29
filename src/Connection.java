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
		 * �������������� ���� ������� � �������� ��� ������������
		 * 
		 * @param socket
		 *            �����, ���������� �� server.accept()
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
		 * ����������� ��� ������������ � ������� �� ���� ���������. ���
		 * ��������� ������� ���������, ��� ������ � ������ ������������
		 * ������������ ���� ���������.
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
					System.out.println("���������� ��������� "+ msg.toString());
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
//					System.out.println("��� ��������");
				} catch (ClassNotFoundException e) {
				//	e.printStackTrace();
				}
			}	
		}
	
		/**
		 * ��������� ������� � �������� ������ � �����
		 */
		public void close() {
			try {
				inStream.close();
				//outStream.close();
				socket.close();
				// ���� ������ �� �������� ����������, ��������� ��, ��� ���� �
				// ��������� ������ �������
				Server.connections.remove(this);
			//	if (Server.connections.size() == 0) {
				//	Server.this.closeAll();
					//System.exit(0);
				//}
			} catch (Exception e) {
				System.err.println("������ �� ���� �������!");
			}
		}
		
		/**
		 * @overrides toString
		 */
		public String toString(){
			return this.socket.toString()+" | "+this.key;
			
		}
	}	