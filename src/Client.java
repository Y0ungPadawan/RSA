import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.DefaultListModel;


public class Client {
	static ArrayList <User> users =  new ArrayList<User>();
	//static ArrayList <User> users1 =  new ArrayList<User>();
	static Scanner scan = new Scanner(System.in);
	static Object temp;
	static Socket fromserver;
	
	static RSA rsa = null;
	
	static public void start() throws UnknownHostException, IOException{
		 System.out.println("Welcome to Client side");

		 //   System.out.println("Connecting to... "+args[0]);

		    fromserver = new Socket("127.0.0.1",9998);
		    System.out.println(fromserver.toString());
		    ObjectOutputStream  out = new ObjectOutputStream (fromserver.getOutputStream());
		    rsa = new RSA(256);
		    Keys k = rsa.publicKey;
		    System.out.println("Keys went");
		    out.writeObject(k);
		    System.out.println("flush begined");
		    out.flush();
		    System.out.println("flush ended");
	}
	
	static public void run( final DefaultListModel<String> listModel) throws IOException{
		new Thread(new Runnable(){
			@SuppressWarnings("unchecked")
			public void run() {
				Object obj;
				while (true) {
					try {
						try {
							ObjectInputStream in = new ObjectInputStream(
									fromserver.getInputStream());
							obj = in.readObject();
							try {
								users = (ArrayList<User>) obj;
								listModel.removeAllElements();
								synchronized (users) {
									Iterator<User> iter = users.iterator();
									while (iter.hasNext()) {
										listModel.addElement(iter.next()
												.toString());
									}
								}
								System.out.println("bingo");
							} catch (Exception e) {
								System.out.println("fuck");
								temp = obj;
								System.out.println("Cåé÷àñ áóäåì ïğèíèìàòü ñîîáùåíèå");
						        Message msg = (Message) temp;
						        printMessage(msg.sender.toString()+": ");
						        String decrytpString = rsa.decrypt(msg.text);
						        if (!Md5.md5Custom(msg.sender+decrytpString).equals(msg.hash))
						        	printMessage("Ö²Ë²ÑÍ²ÑÒÜ ÏÎÂ²ÄÎÌËÅÍÍß ÍÅ ÇÁÅĞÅÆÅÍÀ. ÌÎÆËÈÂÅ ÂÒĞÓ×ÀÍÍß ÏÎÑÒÎĞÎÍÍÜÎ¯ ÎÑÎÁÈ");	
						        printMessage(decrytpString+"\n");
								System.out.println(rsa.decrypt(msg.text));
								System.out.println("ÂÑå äîëæíî áûòü");
							}
						} catch (ClassNotFoundException e) {
						}
					} catch (IOException e) {
					}

					// Client.printList(users);
				}
			}
	    }).start();		   
	    
	    
	    
	    System.out.println("Stream in create");
	   
	    String str = "";
		while (!str.equals("exit")) {
			str = scan.nextLine();
		}
		 System.out.println("go to closed");
	    
	    
	    fromserver.close();
	}
	
	static void sendMessage(final Message msg){
		new Thread(new Runnable(){
			public void run(){
				 try {
					 System.out.println("Ñîîáùåíèå ïîøëî");
					ObjectOutputStream out = new ObjectOutputStream (fromserver.getOutputStream());
					out.writeObject(msg);
					out.flush();
					 System.out.println("Îòïğàâèëîñü");
				} catch (IOException e) {
				}
			}
		}).start();
		
	}
	
	static void printMessage(String msg){
		Frame.jTextArea1.append(msg);
	}

//	public static void main(String[] args) throws IOException {
//		
//	   
//	   // final ObjectInputStream in;
//	    
//	    new Thread(new Runnable(){
//	    	public void run(){
//	    		 Object obj;
//	    		while (true) {
//		    		 try {
//		 				try {
//		 					ObjectInputStream in  = new ObjectInputStream(fromserver.getInputStream());
//							obj = in.readObject();
//							try {
//								users =(ArrayList<User>)obj;
//							//	frame.listModel.removeAllElements();
//								synchronized(users) {
//									Iterator<User> iter = users.iterator();
//									while(iter.hasNext()) {
//								//		frame.listModel.addElement(iter.next().toString());
//									}
//								}
//							System.out.println("bingo");
//							} catch(Exception e){
//								System.out.println("fuck");
//								temp = obj;
//							}
//						} catch (ClassNotFoundException e) {
//							e.printStackTrace();
//						}
//		 			} catch (IOException e) {
//		 				e.printStackTrace();
//		 			}
//		    		
//		    		 Client.printList(users);
//	    		}
//	    	}
//	    }).start();		   
//	    
//	    
//	    
//	    System.out.println("Stream in create");
//	   
//	    String str = "";
//		while (!str.equals("exit")) {
//			str = scan.nextLine();
//		}
//		 System.out.println("go to closed");
//	    
//	  //  while(){
//	  //  	
//	 //   }
//	    
//	      
////	      fserver = in.readLine();
////	      System.out.println(fserver);
//
//	    
//
//	 
//	//    in.close();
//	//    inu.close();
//	    fromserver.close();
//	    }
//	
	public static void printList( ArrayList <User> users){
		synchronized(users) {
			Iterator<User> iter = users.iterator();
			while(iter.hasNext()) {
				System.out.println(((User) iter.next()).toString());
			}
		}
	}
 }

