package frame;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import util.RestClient;
import util.RestClient_Get;
import javax.swing.*;

import java.util.concurrent.ExecutionException;

import style.ProjColor;
import style.ProjStyleButton;
import org.json.JSONArray;
import org.json.JSONObject;
public class NewIssuePage extends JDialog {
	
	private ProjectFrame parentFrame;
	private String userName;
	private JTextField tf1;
	private JTextArea ta1;
	private long projectId;
    private long userId;
    
	public NewIssuePage(ProjectFrame parentFrame, long projectId, long userId) {
		
		this.parentFrame= parentFrame;
		this.projectId = projectId;
        this.userId = userId;
		userName = getUsernameByUserId(userId);
	
		setSize(560, 480);
		setLocationRelativeTo(null); // 화면 중앙 위치
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModal(true);
		setUndecorated(true); // 타이틀바 제거
		setOpacity(0.0f); // 처음엔 안보이게
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(ProjColor.customGray);
		panel1.setLayout(null);
		panel1.setBorder(BorderFactory.createLineBorder(ProjColor.clickedCustomDarkGray, 5));
		
		JLabel lbl1 = new JLabel("New Issue");
		lbl1.setFont(new Font(null, Font.PLAIN, 50));
		panel1.add(lbl1);
		lbl1.setBounds(170, 1, 400, 80);
		
		JLabel lbl2 = new JLabel("Issue title");
		lbl2.setFont(new Font(null, Font.PLAIN, 15));
		panel1.add(lbl2);
		lbl2.setBounds(35, 55, 400, 80);
		
		JLabel lbl3 = new JLabel("description");
		lbl3.setFont(new Font(null, Font.PLAIN, 15));
		panel1.add(lbl3);
		lbl3.setBounds(35, 158, 400, 80);
		
		tf1 = new JTextField();
		tf1.setBackground(ProjColor.customDarkGray);
		tf1.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		tf1.setBorder(null);
		panel1.add(tf1);
		tf1.setBounds(37, 116, 465, 56);
		
		ta1 = new JTextArea();
		ta1.setBackground(ProjColor.customDarkGray);
		ta1.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		panel1.add(ta1);
		ta1.setBounds(37, 216, 465, 150);
		
		ProjStyleButton btn1 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "Add");
		panel1.add(btn1);
		btn1.setBounds(108, 380, 111, 56);
		btn1.setPreferredSize(new Dimension(111, 56));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				addIssueRow();
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customWhiteRed, ProjColor.clickedCustomWhiteRed, Color.BLACK, "Cancel");
		panel1.add(btn2);
		btn2.setBounds(325, 380, 111, 56);
		btn2.setPreferredSize(new Dimension(111, 56));

		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		Timer timer = new Timer(1, new ActionListener() {
            float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (opacity < 1.0f) {
                    opacity += 0.1f;
                    setOpacity(Math.min(opacity, 1.0f));
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();

		add(panel1);
		
		setVisible(true);
		
		
	}
	
	public void addIssueRow() {
		String title = tf1.getText().trim();
		String description = ta1.getText().trim();

		if (title.equals("")) {
			JOptionPane.showMessageDialog(NewIssuePage.this, "Title cannot have empty spaces", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			sendIssueRequest(projectId, title, description, userId);
			setVisible(false);
			dispose();
			
		}
	}
	
    
    private void sendIssueRequest(long projectId, String title, String description, long userId) {
        String jsonInputString = String.format("{\"projectId\":%d, \"title\":\"%s\", \"description\":\"%s\", \"issuePriority\":\"MAJOR\"}", projectId, title, description);

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String url = String.format("http://localhost:8080/issues/?userId=%d", userId);
                return RestClient.sendPostRequest(url, jsonInputString);
            }

            @Override
            protected void done() {
                try {
                    String response = get();
                    System.out.println("Response from server: " + response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isSuccess = jsonResponse.getBoolean("isSuccess");
                    String message = jsonResponse.getString("message");
                    String code = jsonResponse.getString("code");
                    
            		LocalDate now = LocalDate.now();
            		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
            		String formatedNow = now.format(formatter);
                    

                    if (isSuccess && "ISSUE_3000".equals(code)) {
                    	parentFrame.addIssue(title, userName, "None", "None", "MAJOR", "NEW", formatedNow);
                        parentFrame.addModel();
                        JOptionPane.showMessageDialog(NewIssuePage.this, "Issue Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        setVisible(false);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(NewIssuePage.this, "Issue Creation Failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(NewIssuePage.this, "Issue Creation Failed: 이슈생성 권한이 없습니다. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }	
	
    //userId로 userName 받아오는 코드. 
    private String getUsernameByUserId(long userId) {
        String url = String.format("http://localhost:8080/users");

        try {
            String response = RestClient_Get.sendGetRequest(url);
            System.out.println("Response from server: " + response);

            JSONObject jsonResponse = new JSONObject(response);
            boolean isSuccess = jsonResponse.getBoolean("isSuccess");
            String code = jsonResponse.getString("code");
            
            if (isSuccess && "USER_1000".equals(code)) {
                JSONObject resultObject = jsonResponse.getJSONObject("result");
                JSONArray usersArray = resultObject.getJSONArray("users");
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject user = usersArray.getJSONObject(i);
                    if (user.getLong("userId") == userId) {
                        return user.getString("userName");
                    }
                }
                throw new RuntimeException("User ID not found");
            } else {
                throw new RuntimeException("Failed to get username: " + jsonResponse.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error occurred while fetching username", e);
        }
    }
    
}

	

	