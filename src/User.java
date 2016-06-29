import java.io.Serializable;
import java.net.InetAddress;


public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String socket;
	public InetAddress addr;
	public int port;
	public int localport;
	public Keys key;
	
	User(Connection c){
		this.socket = c.socket.toString();
		this.addr = c.socket.getInetAddress();
		this.port = c.socket.getPort();
		this.localport = c.socket.getLocalPort();
		
		this.key = new Keys(c.key.N,c.key.KB);
	}
	
	public String toString(){
		return this.socket+" | "+this.key;
	}
	
}
