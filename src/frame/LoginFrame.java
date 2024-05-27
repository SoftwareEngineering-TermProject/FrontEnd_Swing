package frame;

import style.ProjColor;
import style.ProjStyleButton;
import util.RestClient;
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
import javax.swing.*;
import java.util.concurrent.ExecutionException;
import org.json.JSONObject;

public class LoginFrame extends JFrame{
	private JTextField tf1;
    private JPasswordField pf1;

    public LoginFrame() {
		setTitle("Log in");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(ProjColor.customGray);
		
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
		tf1.setBackground(ProjColor.customWhiteGray);
		tf1.setBorder(null);
		panel1.add(tf1);
		tf1.setBounds(100, 143, 100, 20);
		addFocusListenerToTextField(tf1);
		
		pf1 = new JPasswordField();
		pf1.setBackground(ProjColor.customWhiteGray);
		pf1.setBorder(null);
		panel1.add(pf1);
		pf1.setBounds(100, 173, 100, 20);
		addFocusListenerToTextField(pf1);
	
		ProjStyleButton btn1 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "LOG IN");
		panel1.add(btn1);
		btn1.setBounds(250, 143, 100, 30);
		btn1.setPreferredSize(new Dimension(100, 30));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				login();
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "SIGN UP");
		panel1.add(btn2);
		btn2.setBounds(250, 178, 100, 30);
		btn2.setPreferredSize(new Dimension(100, 30));
		
		btn2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                new SignUpFrame();
            }
        });
		
		
		
		/* 아이디 까먹었을때 버튼
		ProjStyleButton btn3 = new ProjStyleButton(ProjColor.customGray, ProjColor.customGray, Color.BLACK, "Forgot your ID/password?"); // 클릭/릴리즈 시 텍스트 변하게
		panel1.add(btn3);
		btn3.setBounds(20, 220, 228, 23);
		btn3.setPreferredSize(new Dimension(228, 23));
		
		btn3.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btn3.setForeground(ProjColor.clickedCustomDarkGray);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				btn3.setForeground(Color.BLACK);
			}
		});
		*/
	
		add(panel1);
		
		this.setFocusTraversalPolicy(new CustomFocusTraversalPolicy(new Component[]{tf1, pf1}));
		
		setVisible(true);
		
	}
	
	private void login() {
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception { // 좀 수정이 필요할듯
                String userName = tf1.getText();
                String password = new String(pf1.getPassword());
                String jsonInputString = String.format("{\"userName\":\"%s\", \"password\":\"%s\"}", userName, password);
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
	
	private static void addFocusListenerToTextField(JTextField textField) {
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setCaretPosition(textField.getText().length());
            }
        });
    }
	
	private static void addFocusListenerToTextField(JPasswordField passwordField) {
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setCaretPosition(passwordField.getPassword().length);
            }
        });
    }
	
	static class CustomFocusTraversalPolicy extends FocusTraversalPolicy {
        private final Component[] components;

        public CustomFocusTraversalPolicy(Component[] components) {
            this.components = components;
        }

        @Override
        public Component getComponentAfter(Container aContainer, Component aComponent) {
            for (int i = 0; i < components.length; i++) {
                if (components[i].equals(aComponent)) {
                    return components[(i + 1) % components.length];
                }
            }
            return components[0];
        }

        @Override
        public Component getComponentBefore(Container aContainer, Component aComponent) {
            for (int i = 0; i < components.length; i++) {
                if (components[i].equals(aComponent)) {
                    return components[(i - 1 + components.length) % components.length];
                }
            }
            return components[0];
        }

        @Override
        public Component getFirstComponent(Container aContainer) {
            return components[0];
        }

        @Override
        public Component getLastComponent(Container aContainer) {
            return components[components.length - 1];
        }

        @Override
        public Component getDefaultComponent(Container aContainer) {
            return components[0];
        }
    }
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(LoginFrame::new);
		
	}
}
