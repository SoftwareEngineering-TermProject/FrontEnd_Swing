package frame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Timer;
import java.util.ArrayList;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import style.ProjStyleComboBox;
import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleScrollBar;

import util.RestClient_Get;

public class IssuePage extends JDialog {
	
	private String[] tester; // 요청 받아와서 tester 배열 생성.
	private String[] dev; // 요청 받아와서 dev 배열 생성
	private String[] priority = {"None", "critical", "major", "high", "medium", "low", "trivial"};
	private String[] status = {"new", "assigned", "fixed", "investigating", "closed"};
	private JTextArea tempTa;
	private JPanel tempPanel;
	private long userId;
	private long issueId;
	private CommentPanel commentPanel;
	
	public IssuePage (long userId, long issueId) {
		
		this.userId = userId;
		this.issueId = issueId;
		
		setSize(1000,700);
		setLocationRelativeTo(null);
		setModal(true);
		setUndecorated(true);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(Color.WHITE);
		panel1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		ProjStyleButton btn1 = new ProjStyleButton(Color.WHITE, Color.WHITE, Color.BLACK, "X");
		btn1.setFont(new Font(null, Font.PLAIN, 50));
		panel1.add(btn1);
		btn1.setBounds(550, 20, 30, 35);
		btn1.setPreferredSize(new Dimension(30, 35));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
				dispose();
			}
		});
		
		JLabel lbl1 = new JLabel("Issue title");
		lbl1.setFont(new Font(null, Font.BOLD, 40));
		panel1.add(lbl1);
		lbl1.setBounds(10, 20, 190, 40);
		lbl1.setPreferredSize(new Dimension(190, 40));
		
		JLabel lbl2 = new JLabel("Reporter");
		lbl2.setFont(new Font(null, Font.BOLD, 18));
		panel1.add(lbl2);
		lbl2.setBounds(30, 440, 100, 25);
		lbl2.setPreferredSize(new Dimension(100, 25));
		
		JLabel lbl3 = new JLabel("Fixer");
		lbl3.setFont(new Font(null, Font.BOLD, 18));
		panel1.add(lbl3);
		lbl3.setBounds(30, 480, 100, 25);
		lbl3.setPreferredSize(new Dimension(100, 25));
		
		JLabel lbl4 = new JLabel("Assignee");
		lbl4.setFont(new Font(null, Font.BOLD, 18));
		panel1.add(lbl4);
		lbl4.setBounds(30, 520, 100, 25);
		lbl4.setPreferredSize(new Dimension(100, 25));
		
		JLabel lbl5 = new JLabel("Priority");
		lbl5.setFont(new Font(null, Font.BOLD, 18));
		panel1.add(lbl5);
		lbl5.setBounds(30, 560, 100, 25);
		lbl5.setPreferredSize(new Dimension(100, 25));
		
		JLabel lbl6 = new JLabel("Status");
		lbl6.setFont(new Font(null, Font.BOLD, 18));
		panel1.add(lbl6);
		lbl6.setBounds(30, 600, 100, 25);
		lbl6.setPreferredSize(new Dimension(100, 25));
		
		JTextField tf = new JTextField("00-00-00");
		tf.setBackground(Color.WHITE);
		tf.setFont(new Font(null, Font.BOLD, 18));
		tf.setEditable(false);
		panel1.add(tf);
		tf.setBounds(30, 150, 100, 25);
		tf.setPreferredSize(new Dimension(100, 25));
		tf.setBorder(null);
		
		String[] tester = {"tester1", "tester2", "tester3", "tester4"};
		String[] dev = {"dev1", "dev2", "dev3", "dev4"};
		String[] priority = {"critical", "major", "high", "medium", "low", "trivial"};
		String[] status = {"new", "assigned", "fixed", "investigating", "closed"};
		
		ProjStyleComboBox cb1 = new ProjStyleComboBox(tester);
		cb1.setEnabled(false);
		panel1.add(cb1);
		cb1.setBounds(150, 440, 100, 25);
		cb1.setPreferredSize(new Dimension(100, 25));
		
		ProjStyleComboBox cb2 = new ProjStyleComboBox(dev);
		cb2.setEnabled(false);
		panel1.add(cb2);
		cb2.setBounds(150, 480, 100, 25);
		cb2.setPreferredSize(new Dimension(100, 25));
		
		ProjStyleComboBox cb3 = new ProjStyleComboBox(dev);
		cb3.getEditor().getEditorComponent().setBackground(Color.red);
		cb3.setEnabled(false);
		panel1.add(cb3);
		cb3.setBounds(150, 520, 100, 25);
		cb3.setPreferredSize(new Dimension(100, 25));
		
		ProjStyleComboBox cb4 = new ProjStyleComboBox(priority);
		cb4.setEnabled(false);
		panel1.add(cb4);
		cb4.setBounds(150, 560, 100, 25);
		cb4.setPreferredSize(new Dimension(100, 25));
		
		ProjStyleComboBox cb5 = new ProjStyleComboBox(status);
		cb5.setEnabled(false);
		panel1.add(cb5);
		cb5.setBounds(150, 600, 100, 25);
		cb5.setPreferredSize(new Dimension(100, 25));
		
		
		
		
		JTextArea ta = new JTextArea();
		ta.setFont(new Font("맑은 고딕", 1, 15));
		ta.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
		ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
		ta.setEditable(false);
		ta.setBorder(null);
		
		JScrollPane scrollPane = new JScrollPane(ta);
		scrollPane.setVerticalScrollBar(new ProjStyleScrollBar(ProjColor.tableHeaderGray, ProjColor.customGray));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 수평 스크롤바 비활성화
        panel1.add(scrollPane);
        scrollPane.setBounds(30, 185, 540, 245);
        scrollPane.setPreferredSize(new Dimension(540, 245));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customGray, ProjColor.clickedCustomGray, Color.BLACK, "Edit");
		btn2.setFont(new Font("맑은 고딕", 1, 25));
		panel1.add(btn2);
		btn2.setBounds(30, 80, 55, 40);
		btn2.setPreferredSize(new Dimension(55, 40));
		btn2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(tf.isEditable()) {
					tf.setEditable(false);
					ta.setEditable(false);
					cb1.setEnabled(false);
					cb2.setEnabled(false);
					cb3.setEnabled(false);
					cb4.setEnabled(false);
					cb5.setEnabled(false);
					tf.setBackground(Color.WHITE);
				}
				else {
					tf.setEditable(true);
					ta.setEditable(true);
					cb1.setEnabled(true);
					cb2.setEnabled(true);
					cb3.setEnabled(true);
					cb4.setEnabled(true);
					cb5.setEnabled(true);
					tf.setBackground(ProjColor.customStaleSkyBlue);
				}
			}
		});
		
		commentPanel = new CommentPanel(400, 700, userId, issueId);
		add(commentPanel);
		commentPanel.setBounds(600, 0, 400, 700);
		
		// 오류나면 안에 있는 함수만 쓰자.
		Timer timer = new Timer();
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				getIssueDetailFromServer();
			}
		};

		timer.schedule(task, 0, 1000);
		
		add(panel1);	
		setVisible(true);

	}
	
	public void getIssueDetailFromServer() {
		String urlString = String.format("http://localhost:8080/issues/%d", issueId);
		try {
			String response = RestClient_Get.sendGetRequest(urlString);
			JSONObject jsonResponse = new JSONObject(response);
	        boolean isSuccess = jsonResponse.getBoolean("isSuccess");
	        String code = jsonResponse.getString("code");

	        if (isSuccess && "ISSUE_3000".equals(code)) {
	            JSONObject issueObject = jsonResponse.getJSONObject("result");
	            
	            String title = issueObject.getString("title");
	            String reporter = issueObject.getJSONObject("user").getString("userName");
	            String fixer = issueObject.isNull("fixer") ? "None" : issueObject.getString("fixer");
	            String assignee = issueObject.isNull("assignee") ? "None" : issueObject.getString("assignee");
	            String priority = issueObject.getString("issuePriority");
	            String status = issueObject.getString("issueStatus");
	            String date = issueObject.getString("createAt").split("T")[0];
	            
	            JSONArray issueComments = issueObject.getJSONArray("comments");
	            
	            ArrayList<Object[]> commentList = new ArrayList<>();
	            
	            for (int i = 0; i < issueComments.length(); i++) {
	                JSONObject commentObject = issueComments.getJSONObject(i);
	                JSONObject writerObject = commentObject.getJSONObject("user");
	                
	                long commentId = commentObject.getLong("commentId");
	                
	                String writer = writerObject.getString("userName");

	                String creationDate = commentObject.getString("createdAt").split("T")[0] + " " + commentObject.getString("createdAt").split("T")[1].split("\\.")[0];
	                
	                String comment = commentObject.getString("content");
	                
	                Object[] array = {commentId, writer, creationDate, comment};
	                
	                commentList.add(array);

	            }
	            commentPanel.getComment(commentList);
	            
	        } else {
	            String message = jsonResponse.getString("message");
	            JOptionPane.showMessageDialog(IssuePage.this, "1이슈 목록 가져오기 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
	        }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(IssuePage.this, "2이슈 목록 가져오기 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
}

