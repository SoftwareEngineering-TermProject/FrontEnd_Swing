package frame;

import style.ProjStyleButton;
import util.RestClient;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.JFrame;
import java.util.concurrent.ExecutionException;
import org.json.JSONObject;

public class CreateProjectFrame extends JFrame {
	
	Color userGray = new Color(196, 196, 196);
	Color userDarkGray = new Color(157, 157, 157);
	Color clickedUserDarkGray = new Color(120, 120, 120);
	Color userDarkSkyBlue = new Color(78, 157, 157);
	Color clickedUserDarySkyBlue = new Color(70, 140, 140);	
	Color userWhiteRed = new Color(217, 102, 102);
	Color clickedUserWhiteRed = new Color(200, 75, 75);
	
	private JTextField tf1;
	private JTextArea ta1;
	private long userId;
	
	public CreateProjectFrame(long userId) {
		this.userId = userId;
		
		setTitle("New Project");
		setSize(560, 480);
		setLocationRelativeTo(null); // 화면 중앙 위치
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(userGray);
		panel1.setLayout(null);
		
		JLabel lbl1 = new JLabel("New Project");
		lbl1.setFont(new Font(null, Font.PLAIN, 50));
		panel1.add(lbl1);
		lbl1.setBounds(140, 1, 400, 80);
		
		JLabel lbl2 = new JLabel("project name");
		lbl2.setFont(new Font(null, Font.PLAIN, 15));
		panel1.add(lbl2);
		lbl2.setBounds(35, 55, 400, 80);
		
		JLabel lbl3 = new JLabel("description");
		lbl3.setFont(new Font(null, Font.PLAIN, 15));
		panel1.add(lbl3);
		lbl3.setBounds(35, 158, 400, 80);
		
		tf1 = new JTextField();
		tf1.setBackground(userDarkGray);
		tf1.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		tf1.setBorder(null);
		panel1.add(tf1);
		tf1.setBounds(37, 116, 465, 56);
		
		ta1 = new JTextArea();
		ta1.setBackground(userDarkGray);
		ta1.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		panel1.add(ta1);
		ta1.setBounds(37, 216, 465, 150);
		
		ProjStyleButton btn1 = new ProjStyleButton(userDarkGray, clickedUserDarkGray, Color.BLACK, "Create");
		panel1.add(btn1);
		btn1.setBounds(108, 380, 111, 56);
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				createProject(userId);
			}
		});
		
		
		ProjStyleButton btn2 = new ProjStyleButton(userWhiteRed, clickedUserWhiteRed, Color.BLACK, "Cancel");
		panel1.add(btn2);
		btn2.setBounds(325, 380, 111, 56);
		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		add(panel1);		
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose(); // 현재 프레임만 없애기
			}
		});
	}

private void createProject(long userId) {
	String title = tf1.getText();
	String description = ta1.getText();
	
	String jsonInputString = String.format("{\"title\":\"%s\", \"description\":\"%s\"}", title, description);
	System.out.println("Sending JSON: " + jsonInputString);  // JSON 데이터 출력

	new SwingWorker<String, Void>() {
		@Override
		protected String doInBackground() throws Exception {
			String url = String.format("http://localhost:8080/projects/?userId=%d", userId);
            return RestClient.sendPostRequest(url, jsonInputString);
		}

		@Override
		protected void done() {
			String response = null;
			try {
				response = get();
				System.out.println("Response from server: " + response);
				JSONObject jsonResponse = new JSONObject(response);
				boolean isSuccess = jsonResponse.getBoolean("isSuccess");
				String code = jsonResponse.getString("code");

				if (isSuccess && "PROJECT_2000".equals(code)) {
					JOptionPane.showMessageDialog(CreateProjectFrame.this, "Project Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);	               
					setVisible(false);
					dispose();
				} else {
					String message = jsonResponse.getString("message");
					JOptionPane.showMessageDialog(CreateProjectFrame.this, "Project Creation Failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (InterruptedException | ExecutionException e) {			
				
				System.out.println("Response from server: " + response);
				e.printStackTrace();
				JOptionPane.showMessageDialog(CreateProjectFrame.this, "Project Creation Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}.execute();
}
}