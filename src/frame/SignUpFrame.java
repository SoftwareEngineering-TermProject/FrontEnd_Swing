package frame;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import style.ProjColor;
import style.ProjStyleButton;

import util.RestClient;

class SignUpFrame extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tfName;
    private JTextField tfUserName;
    private JPasswordField pfPassword;
    private String url;

    public SignUpFrame() {
    	
    	url = InputUrlPage.getUrl();
    	
        setTitle("Sign Up");
        setSize(400, 300);
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(ProjColor.customGray);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 50, 80, 25);
        panel.add(lblName);

        tfName = new JTextField();
        tfName.setBounds(150, 50, 165, 25);
        tfName.setBackground(ProjColor.customDarkGray);
        tfName.setBorder(null);
        panel.add(tfName);

        JLabel lblID = new JLabel("ID:");
        lblID.setBounds(50, 80, 80, 25);
        panel.add(lblID);

        tfUserName = new JTextField();
        tfUserName.setBounds(150, 80, 165, 25);
        tfUserName.setBackground(ProjColor.customDarkGray);
        tfUserName.setBorder(null);
        panel.add(tfUserName);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 110, 80, 25);
        panel.add(lblPassword);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(150, 110, 165, 25);
        pfPassword.setBackground(ProjColor.customDarkGray);
        pfPassword.setBorder(null);
        panel.add(pfPassword);
        
        ProjStyleButton btnSubmit = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "Submit");
        btnSubmit.setBounds(180, 180, 100, 30);
        btnSubmit.setPreferredSize(new Dimension(100, 30));
        btnSubmit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                signUp();
            }
        });
        panel.add(btnSubmit);

        add(panel);
        setVisible(true);
    }

    
	private void signUp() {
        String name = tfName.getText();
        String userName = tfUserName.getText();
        String password = new String(pfPassword.getPassword());
        
        String jsonInputString = String.format("{\"userName\":\"%s\", \"password\":\"%s\", \"name\":\"%s\"}", userName, password, name);
        System.out.println("Sending JSON: " + jsonInputString);  // JSON 데이터 출력

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return RestClient.sendPostRequest(url + "users/sign_up", jsonInputString);
            }
            @Override
            protected void done() {
                try {
                    String response = get();
                    System.out.println("Response from server: " + response);
                    // JSON 응답 파싱
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isSuccess = jsonResponse.getBoolean("isSuccess");
                    String code = jsonResponse.getString("code");
                    
                    if (isSuccess && "USER_1000".equals(code)) {
                    	JOptionPane.showMessageDialog(SignUpFrame.this, "회원가입 성공!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        setVisible(false);
                        dispose();  	
                    } else {
                        // 실패 시 오류 메시지 표시
                        String message = jsonResponse.getString("message");
                        JOptionPane.showMessageDialog(SignUpFrame.this, "회원가입 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (InterruptedException | ExecutionException e) {
                    if (e.getMessage().equals("java.net.ConnectException: Connection refused: connect")) {
                    	JOptionPane.showMessageDialog(SignUpFrame.this, "회원가입 실패: 서버와 연결이 되지 않았습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (e.getMessage().equals("java.lang.RuntimeException: Failed : HTTP error code : 409")) {
                        JOptionPane.showMessageDialog(SignUpFrame.this, "회원가입 실패: 사용중인 아이디입니다.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (e.getMessage().startsWith("java.net.MalformedURLException: no protocol")) {
                    	JOptionPane.showMessageDialog(SignUpFrame.this, "회원가입 실패: 주소 " + url + "을(를) 찾을 수 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                    	JOptionPane.showMessageDialog(SignUpFrame.this, "회원가입 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }.execute();
	}
}	
