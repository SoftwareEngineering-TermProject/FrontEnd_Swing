package frame;

import javax.swing.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import style.ProjColor;
import style.ProjStyleButton;

import util.RestClient;

public class LogInFrame extends JFrame{

    public LogInFrame() {
		setTitle("Log in");
		setSize(570, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(ProjColor.customGray);
		
		JLabel logInTitleLabel = new JLabel("Trouble Ticket System");
		logInTitleLabel.setFont(new Font(null, Font.PLAIN, 50));
		logInTitleLabel.setBounds(20, 1, 550, 100);
		mainPanel.add(logInTitleLabel);
		
		JLabel userNameLabel = new JLabel("ID : ");
		userNameLabel.setFont(new Font(null, Font.BOLD, 15));
		userNameLabel.setBounds(70, 100, 500, 100);
		mainPanel.add(userNameLabel);
		
		JTextField userNameTextField = new JTextField();
		userNameTextField.setBackground(ProjColor.customWhiteGray);
		userNameTextField.setBorder(null);
		userNameTextField.setBounds(100, 143, 100, 20);	
		mainPanel.add(userNameTextField);
		
		JLabel passwordLabel = new JLabel("password : ");
		passwordLabel.setFont(new Font(null, Font.BOLD, 15));
		passwordLabel.setBounds(10, 130, 500, 100);
		mainPanel.add(passwordLabel);
		
		JPasswordField passwordField = new JPasswordField();
		passwordField.setBackground(ProjColor.customWhiteGray);
		passwordField.setBorder(null);
		passwordField.setBounds(100, 173, 100, 20);
		mainPanel.add(passwordField);
	
		ProjStyleButton logInButton = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "LOG IN");
		logInButton.setBounds(250, 143, 100, 30);
		logInButton.setPreferredSize(new Dimension(100, 30));
		logInButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				logIn(userNameTextField.getText(), new String(passwordField.getPassword()));
			}
		});
		mainPanel.add(logInButton);
		
		ProjStyleButton signUpButton = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "SIGN UP");
		signUpButton.setBounds(250, 178, 100, 30);
		signUpButton.setPreferredSize(new Dimension(100, 30));
		signUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                new SignUpFrame();
            }
        });
		mainPanel.add(signUpButton);
	
		add(mainPanel);

		setVisible(true);
		
	}
    
    private void logIn(String userName, String password) {
        String urlString = "http://localhost:8080/users/sign_in";
        String jsonInputString = String.format("{\"userName\":\"%s\", \"password\":\"%s\"}", userName, password);
        
        try {
	        String response = RestClient.sendPostRequest(urlString, jsonInputString);
	        
	        JSONObject jsonResponse = new JSONObject(response);
	        boolean isSuccess = jsonResponse.getBoolean("isSuccess");
	        String code = jsonResponse.getString("code");
	        
	        if (isSuccess && "USER_1000".equals(code)) {
	        	
	        	JSONObject resultObject = jsonResponse.getJSONObject("result");
	        	long userId = resultObject.getLong("userId");
	        	
	            // 성공 시 다음 프레임으로 이동
	            new MainFrame(userId);
	            setVisible(false);
	            dispose();
	        } else {
	            // 실패 시 오류 메시지 표시
	            String message = jsonResponse.getString("message");
	            JOptionPane.showMessageDialog(LogInFrame.this, "접속 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
	        }
        }
        catch (Exception e) {
            if (e.getMessage().equals("Connection refused: connect")) {
            	JOptionPane.showMessageDialog(LogInFrame.this, "접속 실패: 서버와 연결이 되지 않았습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if (e.getMessage().equals("Failed : HTTP error code : 500")) {
            	JOptionPane.showMessageDialog(LogInFrame.this, "접속 실패: 아이디 또는 비밀번호가 일치하지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
            	JOptionPane.showMessageDialog(LogInFrame.this, "접속 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
	
	public static void main(String[] args) {
		
		new LogInFrame();
		
	}
}
