import java.io.Serializable;
import java.net.Socket;


public class Message implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BigNumber[] text;
	User addressee;
	String sender;
	String hash; 
	
	Message(Socket sender, User addressee, BigNumber[] text){
		this.addressee = addressee;
		this.text = text;
		this.sender = sender.toString();
	}
	
	public String toString(){
		return "addr: "+this.addressee.addr+", port: "+this.addressee.port+", message: \""+text+"\"";
	}
	
}
