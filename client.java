
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.Socket;

public class client extends JFrame {

	private JPanel contentPane;
	private JTextField inputmessage;
	Date time=null;
    SimpleDateFormat formattime=new SimpleDateFormat("E dd hh:mm:ss a");
    String clientName;
    PrintWriter output;
	Socket socket;
	static JTextArea messageview = new JTextArea();
	static JScrollPane scrollPane = new JScrollPane(messageview,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					client frame = new client(null,null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public client(String name,Socket soc) {
	    clientName=name;
		socket=soc;
		messageview.setEditable(false);
		DefaultCaret caret=(DefaultCaret)messageview.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        try {
			output = new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
		setTitle("Chats");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 601, 367);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		scrollPane.setViewportView(messageview);
		setContentPane(contentPane);
		inputmessage = new JTextField();
		inputmessage.setColumns(10);
		inputmessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				ClientRunnable.Execute=false;
				//System.out.println("On input "+ClientRunnable.Execute);
			}
		}); 
		JButton send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientRunnable.Execute=true;
				time=new Date();
				String userInput;
				String message = (formattime.format(time)+" "+"(" + clientName + ")" + " : " );
				userInput = inputmessage.getText();
				output.println(message + " " + userInput+"\n");
				//System.out.println("On send "+ClientRunnable.Execute);
				inputmessage.setText("");
			}
		});		
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				time=new Date();
				try {
					output.println(" "+formattime.format(time)+" "+clientName + " left the chat.\n");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});

		
		try {
        ClientRunnable clientRun = new ClientRunnable(socket);
        new Thread(clientRun).start();
		}
	catch(Exception e)
		{
		  e.getMessage();
		}
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 517, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(inputmessage, GroupLayout.PREFERRED_SIZE, 362, GroupLayout.PREFERRED_SIZE)
							.addGap(30)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(exit, 0, 0, Short.MAX_VALUE)
								.addComponent(send))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
					.addGap(27)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(inputmessage, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(send, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(exit))
		);
		contentPane.setLayout(gl_contentPane);
	}
		
	}
