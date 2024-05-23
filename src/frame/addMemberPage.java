package frame;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleComboBox;
import util.RestClient;

import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import javax.swing.*;
import java.util.concurrent.ExecutionException;
import org.json.JSONObject;

class addMemberPage extends JDialog {
	private ProjectFrame parentFrame;
    private JTextField tfName;
    private JTextField tfID;
    private JPasswordField pfPassword;
    private ProjStyleComboBox cbUserID;
    private ProjStyleComboBox cbUserRole;

    public addMemberPage(ProjectFrame parentFrame) {
    	
    	this.parentFrame = parentFrame;
    	
        setTitle("Sign Up");
        setSize(400, 230);
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(ProjColor.customGray);
        
        /*
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 50, 80, 25);
        panel.add(lblName);

        tfName = new JTextField();
        tfName.setBounds(150, 50, 165, 25);
        tfName.setBackground(ProjColor.customDarkGray);
        tfName.setBorder(null);
        panel.add(tfName);
		*/
        
        JLabel lblID = new JLabel("ID:");
        lblID.setBounds(50, 50, 80, 25);
        panel.add(lblID);
        /*
        tfID = new JTextField();
        tfID.setBounds(150, 50, 165, 25);
        tfID.setBackground(ProjColor.customDarkGray);
        tfID.setBorder(null);
        panel.add(tfID);
        */
        // 모든 member ID 받아와서 동적으로 String 배열 생성. 지금은 임시.
        String[] tempUserID = {"ymca", "kakaroat", "jaemini", "tftBlade"};
        
        cbUserID= new ProjStyleComboBox(tempUserID);
        cbUserID.setBounds(150, 50, 165, 25);
        panel.add(cbUserID);
        
        /*
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 110, 80, 25);
        panel.add(lblPassword);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(150, 110, 165, 25);
        pfPassword.setBackground(ProjColor.customDarkGray);
        pfPassword.setBorder(null);
        panel.add(pfPassword);
        */
        //제거
        
        JLabel lblUserRole = new JLabel("User Role:");
        lblUserRole.setBounds(50, 90, 80, 25);
        panel.add(lblUserRole);

        cbUserRole = new ProjStyleComboBox(new String[]{"ADMIN", "PL", "DEV", "TESTER"});
        cbUserRole.setBounds(150, 90, 165, 25);
        panel.add(cbUserRole);
        
        ProjStyleButton btnSubmit = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "Submit");
        btnSubmit.setBounds(180, 130, 100, 30);
        btnSubmit.setPreferredSize(new Dimension(100, 30));
        btnSubmit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
            	String userName = (String) cbUserID.getSelectedItem();
            	String userRole = (String) cbUserRole.getSelectedItem();
                parentFrame.addProjectMember(userName, userRole);
                setVisible(false);
                dispose();
            }
        });
        panel.add(btnSubmit);

        add(panel);
        setVisible(true);
    }

    
	private void signUp() {
		/*
        String name = tfName.getText();
        String userName = tfID.getText();
        String password = new String(pfPassword.getPassword());
        String userRole = (String) cbUserRole.getSelectedItem();
        
        String jsonInputString = String.format("{\"userName\":\"%s\", \"password\":\"%s\", \"name\":\"%s\", \"userRole\":\"%s\"}", userName, password, name, userRole);
        System.out.println("Sending JSON: " + jsonInputString);  // JSON 데이터 출력

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return RestClient.sendPostRequest("http://localhost:8080/users/", jsonInputString);
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
                    	//성공시 DB에 저장하는 코드 삽입예정
                    	
                    } else {
                        // 실패 시 오류 메시지 표시
                        String message = jsonResponse.getString("message");
                        JOptionPane.showMessageDialog(SignUpFrame.this, "Login failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    // 서버 응답 처리 (필요에 따라 추가)
                    JOptionPane.showMessageDialog(SignUpFrame.this, "Sign Up Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                    dispose();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(SignUpFrame.this, "Sign Up Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
        */
	}
}	
