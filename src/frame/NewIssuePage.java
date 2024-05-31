package frame;

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleComboBox;
import util.RestClient;
import util.RestClient_Get;

public class NewIssuePage extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProjectFrame parentFrame;
	private JTextField tf1;
	private JTextArea ta1;
	private ProjStyleComboBox cbPriority;
	private long projectId;
    private long userId;
    private String url;
    
	public NewIssuePage(ProjectFrame parentFrame, long projectId, long userId) {
		
		this.parentFrame= parentFrame;
		this.projectId = projectId;
        this.userId = userId;
		url = InputUrlPage.getUrl();
	
		setSize(560, 530);
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
		
		JLabel lbltemp = new JLabel("priority : ");
		lbltemp.setFont(new Font(null, Font.PLAIN, 15));
		panel1.add(lbltemp);
		lbltemp.setBounds(35, 158, 400, 80);
		
		JLabel lbl3 = new JLabel("description");
		lbl3.setFont(new Font(null, Font.PLAIN, 15));
		panel1.add(lbl3);
		lbl3.setBounds(35, 198, 400, 80);
		
		tf1 = new JTextField();
		tf1.setBackground(ProjColor.customDarkGray);
		tf1.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		tf1.setBorder(null);
		panel1.add(tf1);
		tf1.setBounds(37, 116, 465, 56);
		
		cbPriority = new ProjStyleComboBox(new String[]{"BLOCKER", "CRITICAL", "MAJOR", "MINOR", "TRIVIAL"});
		cbPriority.setBackground(ProjColor.customDarkGray);
		cbPriority.setBounds(100, 188, 165, 25);
		cbPriority.setSelectedIndex(2);
        panel1.add(cbPriority);
		
		ta1 = new JTextArea();
		ta1.setBackground(ProjColor.customDarkGray);
		ta1.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		panel1.add(ta1);
		ta1.setBounds(37, 256, 465, 150);
		
		ProjStyleButton btn1 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "Add");
		panel1.add(btn1);
		btn1.setBounds(108, 430, 111, 56);
		btn1.setPreferredSize(new Dimension(111, 56));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				addIssueRow();
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customWhiteRed, ProjColor.clickedCustomWhiteRed, Color.BLACK, "Cancel");
		panel1.add(btn2);
		btn2.setBounds(325, 430, 111, 56);
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
		String priority = (String)cbPriority.getSelectedItem();

		if (title.equals("")) {
			JOptionPane.showMessageDialog(NewIssuePage.this, "Title cannot have empty spaces", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			sendIssueRequest(projectId, title, description, priority, userId);
			setVisible(false);
			dispose();
			
		}
	}
	
    
    private void sendIssueRequest(long projectId, String title, String description, String priority, long userId) {
        String url = this.url + String.format("issues/?userId=%d", userId);
    	String jsonInputString = String.format("{\"projectId\":%d, \"title\":\"%s\", \"description\":\"%s\", \"issuePriority\":\"%s\"}", projectId, title, description, priority);

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
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

                    if (isSuccess && "ISSUE_3000".equals(code)) {
                    	parentFrame.getIssuesFromServer(""); /////////////
                    	
                        JOptionPane.showMessageDialog(NewIssuePage.this, "Issue Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        setVisible(false);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(NewIssuePage.this, "Issue Creation Failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                	if (e.getMessage().equals("java.lang.RuntimeException: Failed : HTTP error code : 400")) {
                        JOptionPane.showMessageDialog(NewIssuePage.this, "이슈 생성 불가: 권한이 없습니다. ", "Error", JOptionPane.ERROR_MESSAGE);
                	}
                	else {
                		
                	}
                }
            }
        }.execute();
    }
}

	

	