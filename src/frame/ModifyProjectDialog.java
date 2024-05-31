package frame;

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;

import java.util.concurrent.ExecutionException;

import java.net.URLEncoder;

import org.json.JSONObject;

import style.ProjColor;
import style.ProjStyleButton;

import util.RestClient_Get;
import util.RestClient_Patch;

public class ModifyProjectDialog extends JDialog { // Modal 창 만들기 위해
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame parentFrame;
	private JTextField tf1;
	private JTextArea ta1;
	private long projectId;
	private String url;
	
	public ModifyProjectDialog(long projectId, MainFrame parentFrame) {
		
		this.parentFrame = parentFrame;
		this.projectId = projectId;
		url = InputUrlPage.getUrl();
		
		setTitle("Modify Project");
		setSize(560, 480);
		setLocationRelativeTo(null); // 화면 중앙 위치
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModal(true);
		setUndecorated(true); // 타이틀바 제거
		setOpacity(0.0f); // 처음엔 안보이게
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(ProjColor.customGray);
		panel1.setLayout(null);
		
		JLabel lbl1 = new JLabel("Modify Project");
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
		
		try {
			checkProjectDetails();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ProjStyleButton btn1 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "Modify");
		panel1.add(btn1);
		btn1.setBounds(108, 380, 111, 56);
		btn1.setPreferredSize(new Dimension(111, 56));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String title = tf1.getText().trim();
				
				if(title.equals("")) {
					JOptionPane.showMessageDialog(ModifyProjectDialog.this, "프로젝트 수정 실패: Title은 공백이 불가능합니다.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					ModifyProject();
				}
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
	
	public void checkProjectDetails() throws Exception {
		String encodedProjectId = URLEncoder.encode(projectId + "", "UTF-8");

        // URL에 파라미터 추가
        String urlString = url + "projects/" + encodedProjectId;
        
        try {
        	String response = RestClient_Get.sendGetRequest(urlString);
        	
        	JSONObject jsonResponse = new JSONObject(response);
            boolean isSuccess = jsonResponse.getBoolean("isSuccess");
            String code = jsonResponse.getString("code");
            
            if (isSuccess && "PROJECT_2000".equals(code)) {
            	JSONObject resultObject = jsonResponse.getJSONObject("result");
            	String title = resultObject.getString("title");
            	String description = resultObject.getString("description");
            	tf1.setText(title);
            	ta1.setText(description);
            } else {}
        }
        catch (InterruptedException | ExecutionException e) {
        	if(e.getMessage().equals("java.net.ConnectException: Connection refused: connect")) {
            	JOptionPane.showMessageDialog(ModifyProjectDialog.this, "프로젝트 불러오기 실패: 서버와 연결이 되지 않았습니다.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
            	JOptionPane.showMessageDialog(ModifyProjectDialog.this, "프로젝트 불러오기 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
	}
	
	public void ModifyProject() {
		String title = tf1.getText();
        String description = ta1.getText();
        long userId = parentFrame.getUserId();
        
        String jsonInputString = String.format("{\"title\":\"%s\", \"description\":\"%s\"}", title, description);
        System.out.println("Sending JSON: " + jsonInputString);  // JSON 데이터 출력

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
            	// 파라미터를 URL 인코딩
            	String encodedProjectId = URLEncoder.encode(projectId + "", "UTF-8");
                String encodedUserId = URLEncoder.encode(userId + "", "UTF-8");

                // URL에 파라미터 추가
                String urlString = url + "projects/" + encodedProjectId + "?userId=" + encodedUserId;
                
                return RestClient_Patch.sendPatchRequest(urlString, jsonInputString);
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
                    
                    if (isSuccess && "PROJECT_2000".equals(code)) {
                    	parentFrame.modifyButtonTitle(projectId, title);
                        setVisible(false);
                        dispose();  	
                    } else {
                        // 실패 시 오류 메시지 표시
                        String message = jsonResponse.getString("message");
                        JOptionPane.showMessageDialog(ModifyProjectDialog.this, "Modify Project failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    if(e.getMessage().equals("java.net.ConnectException: Connection refused: connect")) {
                    	JOptionPane.showMessageDialog(ModifyProjectDialog.this, "프로젝트 수정 실패: 서버와 연결이 되지 않았습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else if(e.getMessage().startsWith("java.lang.RuntimeException: Failed : HTTP error code : 400")) {
                    	JOptionPane.showMessageDialog(ModifyProjectDialog.this, "프로젝트 수정 불가: 권한이 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                    	JOptionPane.showMessageDialog(ModifyProjectDialog.this, "프로젝트 수정 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }.execute();
	}
	
}
