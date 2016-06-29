import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import java.awt.Rectangle;
import javax.swing.JList;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	 JPanel jContentPane = null;
	 JButton jButton = null;
	 JList<String> jList = null;
	 static JTextArea jTextArea = null;
	 static JTextArea jTextArea1 = null;
	 static DefaultListModel<String> listModel = new DefaultListModel<String> ();
	 static JLabel jLabel = null;
	 static JLabel jLabel1 = null;
	// JScrollPane scrollBar = null;
	 int mess;
	 /**
	 * This is the default constructor
	 */
	
	public Frame() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJButton(), null);
			JScrollPane scrollBar1 = new JScrollPane(getJList());
			scrollBar1.setBounds(jList.getBounds());
			jContentPane.add(scrollBar1,null);
			scrollBar1.setVisible(true);
			
			JScrollPane scrollBar = new JScrollPane(getJTextArea());
		 	scrollBar.setBounds(jTextArea.getBounds());
			jContentPane.add(scrollBar,null);
			scrollBar.setVisible(true);
			
			jContentPane.add(getJlable(),null);
			jContentPane.add(getJlable1(),null);
			
			JScrollPane scrollBar3 = new JScrollPane(getJTextArea1());
		 	scrollBar3.setBounds(jTextArea1.getBounds());
			jContentPane.add(scrollBar3,null);
			scrollBar3.setVisible(true);
			
		
		}
		return jContentPane;
	}
	
	private JLabel getJlable(){
			if (jLabel == null){
				jLabel = new JLabel();
				jLabel.setBounds(new Rectangle(20, 260, 450, 20));
				jLabel.setText("Історія повідомлень");
			}
			return jLabel;
	}
	
	private JLabel getJlable1(){
		if (jLabel1 == null){
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(20, 120, 450, 50));
			jLabel1.setText("Адресат: ");
		}
		return jLabel1;
}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(330, 191, 117, 21));
			jButton.setText("Відправити");
			jButton.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent e) {
							String tmpString =jTextArea.getText();
							BigNumber[] temp = RSA.encrypt(tmpString, Client.users.get(mess).key) ;
							Message message = new Message(Client.fromserver,Client.users.get(mess),temp);
							message.hash = Md5.md5Custom(message.sender+tmpString);
							Client.sendMessage(message);
						}
					}
					);
		}
		return jButton;
	}

	/**
	 * This method initializes jList
	 *
	 * @return javax.swing.JList
	 */
	private JList<String> getJList() {
		if (jList == null) {
			jList = new JList<String>(listModel);
			jList.setBounds(new Rectangle(20, 20, 450, 100));
		}
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		jList.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()>1){
					//jList.addListSelectionListener(
					//		new ListSelectionListener(){
					//			 public void valueChanged(ListSelectionEvent e) {
					//				 Object element = jList.getSelectedValue();		 
					//			 }
					//		}
					//		);
								
					//message = new Message();
					mess = jList.getSelectedIndex();
					
					jLabel1.setText("Адресат: "+jList.getSelectedValue());
				}
			}
		});
		return jList;
	}
	
	

	/**
	 * This method initializes jTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setBounds(new Rectangle(20, 180, 250, 70));
			jTextArea.setLineWrap(true);
			jTextArea.setText("Введіть повідомлення");
		}
		return jTextArea;
	}
	
	private JTextArea getJTextArea1() {
		if (jTextArea1 == null) {
			jTextArea1 = new JTextArea();
			jTextArea1.setBounds(new Rectangle(20, 300, 450, 150));
			jTextArea1.setLineWrap(true);
		}
		return jTextArea1;
	}

	public static void main(String[] args) throws UnknownHostException, IOException{
		Frame frame = new Frame();
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing (WindowEvent e){
				try {
					Client.fromserver.close();
					System.out.println(Client.fromserver.isClosed());
				} catch (IOException e1) {
					System.out.println("Socket dont closed");
					e1.printStackTrace();
				}
				finally{
					System.exit(0);
				}
				
			}
		});
		frame.setVisible(true);
		Client.start();
		frame.setTitle(Client.fromserver.getLocalPort()+"");
		Client.run(listModel);
	
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
