package frame;

import style.ProjStyleButton;
import util.RestClient;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import javax.swing.*;
import java.util.concurrent.ExecutionException;
import org.json.JSONObject;


public class LoginFrame extends JFrame{
	private JTextField tf1;
    private JPasswordField pf1;
    private static long userId; // userId를 저장하는 필드 추가
    
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

        
		tf1 = new JTextField();
		tf1.setBackground(userWhiteGray);
		tf1.setBorder(null);
		panel1.add(tf1);
		tf1.setBounds(100, 143, 100, 20);
		
		pf1 = new JPasswordField();
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
				login();
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(userDarkGray, clickedUserDarkGray, Color.BLACK, "SIGN UP");
		panel1.add(btn2);
		btn2.setBounds(250, 178, 100, 30);
		btn2.setPreferredSize(new Dimension(100, 30));
		
		btn2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                new SignUpFrame();
            }
        });
		
		
		
		
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
	
	private void login() {
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                String id = tf1.getText();
                String password = new String(pf1.getPassword());
                String userName = tf1.getText();  // id 필드를 userName으로 사용
                //String name = "FixedName";  // 고정 값 사용
                //String userRole = "ADMIN";  // 고정 값 사용
                String jsonInputString = String.format("{\"id\":\"%s\", \"userName\":\"%s\", \"password\":\"%s\"}", id, userName, password);
                System.out.println("Sending JSON: " + jsonInputString);  // JSON 데이터 출력
                return RestClient.sendPostRequest("http://localhost:8080/users/sign_in", jsonInputString); // 실제 서버 URL 사용
            }

            @Override
            protected void done() {
                try {
                    String result = get();
                    System.out.println("Server response: " + result);  // 서버 응답 출력
                    
                    // JSON 응답 파싱
                    JSONObject jsonResponse = new JSONObject(result);
                    boolean isSuccess = jsonResponse.getBoolean("isSuccess");
                    String code = jsonResponse.getString("code");
                    userId = jsonResponse.getJSONObject("result").getLong("userId"); // userId 저장
                    
                    //System.out.println("isSuccess: " + isSuccess);
                    //System.out.println("code: " + code); 
                    //응답 결과 처리
                    if (isSuccess && "USER_1000".equals(code)) {
                        // 성공 시 다음 프레임으로 이동
                        new MainFrame(userId);
                        setVisible(false);
                        dispose();
                    } else {
                        // 실패 시 오류 메시지 표시
                        String message = jsonResponse.getString("message");
                        JOptionPane.showMessageDialog(LoginFrame.this, "Login failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();  // 예외 메시지 출력
                    JOptionPane.showMessageDialog(LoginFrame.this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
	
	
	public static void main(String[] args) {
		
		//new LoginFrame();
		//new ProjectFrame("Test");
		SwingUtilities.invokeLater(LoginFrame::new);
	}
}
