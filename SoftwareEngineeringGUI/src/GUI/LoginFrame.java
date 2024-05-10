package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import javax.swing.*;

public class LoginFrame extends JFrame{
	
	Color userGray = new Color(196, 196, 196);
	
	Color userDarkGray = new Color(157, 157, 157);
	Color clickedUserDarkGray = new Color(120, 120, 120);
	
	Color userDarkSkyBlue = new Color(78, 157, 157);
	Color clickedUserDarySkyBlue = new Color(70, 140, 140);
	
	Color userWhiteRed = new Color(217, 102, 102);
	Color clickedUserWhiteRed = new Color(200, 75, 75);
	
	Color userWhiteGray = new Color(215, 215, 215);

	public LoginFrame() {
		setTitle("Log in");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(userGray);
		
		JLabel lbl1 = new JLabel("SE term project");
		lbl1.setFont(new Font(null, Font.PLAIN, 50));
		panel1.add(lbl1);
		lbl1.setBounds(20, 1, 500, 100);
		
		JLabel lbl2 = new JLabel("Trouble Ticket System");
		lbl2.setFont(new Font(null, Font.PLAIN, 20));
		panel1.add(lbl2);
		lbl2.setBounds(20, 50, 500, 100);
		
		JLabel lbl3 = new JLabel("ID : ");
		lbl3.setFont(new Font(null, Font.BOLD, 15));
		panel1.add(lbl3);
		lbl3.setBounds(70, 100, 500, 100);
		
		JLabel lbl4 = new JLabel("password : ");
		lbl4.setFont(new Font(null, Font.BOLD, 15));
		panel1.add(lbl4);
		lbl4.setBounds(10, 130, 500, 100);
		
		JTextField tf1 = new JTextField();
		tf1.setBackground(userWhiteGray);
		tf1.setBorder(null);
		panel1.add(tf1);
		tf1.setBounds(100, 143, 100, 20);
		
		JPasswordField pf1 = new JPasswordField();
		pf1.setBackground(userWhiteGray);
		pf1.setBorder(null);
		panel1.add(pf1);
		pf1.setBounds(100, 173, 100, 20);
		
		ProjStyleButton btn1 = new ProjStyleButton(userDarkGray, clickedUserDarkGray, Color.BLACK, "LOG IN");
		panel1.add(btn1);
		btn1.setBounds(250, 143, 100, 30);
		btn1.setPreferredSize(new Dimension(100, 30));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				new MainFrame();
				setVisible(false);
				dispose();
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(userDarkGray, clickedUserDarkGray, Color.BLACK, "SIGN UP");
		panel1.add(btn2);
		btn2.setBounds(250, 178, 100, 30);
		btn2.setPreferredSize(new Dimension(100, 30));
		
		ProjStyleButton btn3 = new ProjStyleButton(userGray, userGray, Color.BLACK, "Forgot your ID/password?"); // 클릭/릴리즈 시 텍스트 변하게
		btn3.setFont(new Font(null, Font.PLAIN, 5)); // 지금 안됨. 오버라이드 해야할듯?
		panel1.add(btn3);
		btn3.setBounds(20, 208, 228, 23);
		btn3.setPreferredSize(new Dimension(228, 23));
		
		btn3.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btn3.setForeground(clickedUserDarkGray);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				btn3.setForeground(Color.BLACK);
			}
		});
	
		add(panel1);
		
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		
		new LoginFrame();
		//new ProjectFrame("Test");
	}
}
