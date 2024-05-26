package frame;

import style.ProjColor;
import style.ProjStyleButton;
import util.RestClient_Patch;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;
import org.json.JSONObject;

public class ProjModifyFrame extends JDialog {
	 	private JTextField tf1;
	    private JTextArea ta1;
	    private long projectId;
	    private long userId;
	    private MainFrame mainFrame;
	    
	    public ProjModifyFrame(long projectId, String currentTitle, String currentDescription, long userId, MainFrame mainFrame) {
	        this.projectId = projectId;
	        this.userId = userId;
	        this.mainFrame = mainFrame;
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

	        JLabel lbl2 = new JLabel("Project Name");
	        lbl2.setFont(new Font(null, Font.PLAIN, 15));
	        panel1.add(lbl2);
	        lbl2.setBounds(35, 55, 400, 80);

	        JLabel lbl3 = new JLabel("Description");
	        lbl3.setFont(new Font(null, Font.PLAIN, 15));
	        panel1.add(lbl3);
	        lbl3.setBounds(35, 158, 400, 80);

	        tf1 = new JTextField(currentTitle);
	        tf1.setBackground(ProjColor.customDarkGray);
	        tf1.setFont(new Font("맑은 고딕", Font.BOLD, 20));
	        tf1.setBorder(null);
	        panel1.add(tf1);
	        tf1.setBounds(37, 116, 465, 56);

	        ta1 = new JTextArea(currentDescription);
	        ta1.setBackground(ProjColor.customDarkGray);
	        ta1.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
	        panel1.add(ta1);
	        ta1.setBounds(37, 216, 465, 150);

	        ProjStyleButton btn1 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "Modify");
	        panel1.add(btn1);
	        btn1.setBounds(108, 380, 111, 56);
	        btn1.setPreferredSize(new Dimension(111, 56));
	        
	        btn1.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseReleased(MouseEvent e) {
	                modifyProject();
	            }
	        });

	        add(panel1);
	        setVisible(true);
	    }

	    private void modifyProject() {
	        String title = tf1.getText();
	        String description = ta1.getText();
	        
	        String jsonInputString = String.format("{\"title\":\"%s\", \"description\":\"%s\"}", title, description);
	        System.out.println("Sending JSON: " + jsonInputString);  // JSON 데이터 출력

	        new SwingWorker<String, Void>() {
	            @Override
	            protected String doInBackground() throws Exception {
	                String url = String.format("http://localhost:8080/projects/%d?userId=%d", projectId, userId);
	                return RestClient_Patch.sendPatchRequest(url, jsonInputString);
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
	                    
	                    if (isSuccess && "PROJECT_2000".equals(code)) {
	                        JOptionPane.showMessageDialog(ProjModifyFrame.this, "Project Modified Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	                        
	                        setVisible(false);
	                        dispose();
	                    } else {
	                        JOptionPane.showMessageDialog(ProjModifyFrame.this, "Project Modification Failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
	                    }
	                } catch (InterruptedException | ExecutionException e) {
	                    e.printStackTrace();
	                    JOptionPane.showMessageDialog(ProjModifyFrame.this, "Project Modification Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        }.execute();
	    }
	
	
	
}
