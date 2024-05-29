package frame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleTable;

import util.RestClient;
import util.RestClient_Get;

public class ProjectFrame extends JFrame{
	
	private DefaultTableModel model;
	private ArrayList<Long> issueList;
	private int issueNum;
	private long projectId;
	private long userId;
	private String userRole;
	
	public ProjectFrame(long projectId, long userId, String userRole, String title, MainFrame parentFrame) {
		
		issueList = new ArrayList<>();
		issueNum = 0;
		this.projectId = projectId;
		this.userId = userId;
		this.userRole = userRole;
		
		setTitle(title);
		setSize(1150, 820);
		setLocationRelativeTo(null); // 화면 중앙 위치
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(ProjColor.customGray);
		panel1.setLayout(null);
		
		JLabel lbl1 = new JLabel(title);
		lbl1.setFont(new Font(null, Font.PLAIN, 50));
		panel1.add(lbl1);
		lbl1.setBounds(20, 1, 800, 80);
		
		ProjStyleButton btn1 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "+ new issue");
		panel1.add(btn1);
		btn1.setBounds(50, 100, 160, 50);
		btn1.setPreferredSize(new Dimension(160, 50));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				callAddIssue();
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "remove issue");
		panel1.add(btn2);
		btn2.setBounds(220, 100, 160, 50);
		btn2.setPreferredSize(new Dimension(160, 50));
		
		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//함수구현
			}
		});
		
		ProjStyleButton btn3 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "+ add member");
		panel1.add(btn3);
		btn3.setBounds(390, 100, 160, 50);
		btn3.setPreferredSize(new Dimension(160, 50));
		
		btn3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				createAddMemberPage();
			}
		});
		
		ProjStyleButton btn4 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "remove member");
		panel1.add(btn4);
		btn4.setBounds(560, 100, 160, 50);
		btn4.setPreferredSize(new Dimension(160, 50));
		
		btn4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				createRemoveMemberPage();
			}
		});
		
		JTextField tf1 = new JTextField();
		tf1.setBackground(ProjColor.customWhiteGray);
		tf1.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		tf1.setBorder(null);
		panel1.add(tf1);
		tf1.setBounds(745, 100, 220, 50);
		
		// table 생성
		ProjStyleTable table = new ProjStyleTable();
		
		table.setModel(new javax.swing.table.DefaultTableModel(
			new Object [][] {

			},
		    new String [] {
		    	"ID", "Issue title", "Reporter", "Fixer", "Assignee", "Priority", "Status", "Date"
		    }
		) {
		    public boolean isCellEditable(int rowIndex, int columnIndex) {
		    	return false;
		    }
		});
		
		JScrollPane scr = new JScrollPane(table);
		panel1.add(scr);
		scr.setBounds(50,200,1030,500);
		
		table.fixTable(scr);
		
        table.setColumnWidth(0, 50);
        table.setColumnAlignment(0, JLabel.CENTER);
        table.setCellAlignment(0, JLabel.CENTER);
        table.setColumnWidth(1, 300);
        table.setColumnAlignment(7, JLabel.CENTER);
        table.setCellAlignment(7, JLabel.CENTER);
		
		model = (DefaultTableModel) table.getModel();
		getIssuesFromServer("");
        
        table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		int row = table.rowAtPoint(e.getPoint());
        		
        		long issueId = (long)issueList.get(row);
        		new IssuePage(userId, issueId);
        	}
        });
        
        ProjStyleButton btn5 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "search");
		panel1.add(btn5);
		btn5.setBounds(980, 100, 100, 50);
		btn5.setPreferredSize(new Dimension(100, 50));
		btn5.addMouseListener(new MouseAdapter() {
			@Override
            public void mouseReleased(MouseEvent e) {
                String keyword = tf1.getText().trim();
                getIssuesFromServer(keyword);
            }
		});

		add(panel1);
		
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new MainFrame(userId);
				parentFrame.dispose();
				setVisible(false);
				dispose();
			}
		});
	}
	
	public void callAddIssue() {
		new NewIssuePage(this, projectId, userId);
	}
	
	public void addProjectMember(long addMemberId, String userRole) {
		
		try {
			String encodedUserName = URLEncoder.encode(Long.toString(addMemberId), "UTF-8");
			String jsonInputString = String.format("{\"adminId\":\"%s\", \"projectId\":\"%s\", \"userRole\":\"%s\"}", userId, projectId, userRole);
	    	
	        String urlString = "http://localhost:8080/projects/add/" + encodedUserName;
	        
	        String response = RestClient.sendPostRequest(urlString, jsonInputString);
	        
	        JSONObject jsonResponse = new JSONObject(response);
            boolean isSuccess = jsonResponse.getBoolean("isSuccess");
            String code = jsonResponse.getString("code");
            
            if (isSuccess && "PROJECT_2000".equals(code)) {
                
            } else {
                // 실패 시 오류 메시지 표시
                String message = jsonResponse.getString("message");
                JOptionPane.showMessageDialog(ProjectFrame.this, "프로젝트 멤버 추가 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
            }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(ProjectFrame.this, "프로젝트 멤버 추가 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	//이슈목록을 서버로부터 가져오는 코드. 
	public void getIssuesFromServer(String keyword) {
	    new SwingWorker<String, Void>() {
	        @Override
	        protected String doInBackground() throws Exception {
	        	if(keyword.equals("")) {
		            String urlString = String.format("http://localhost:8080/issues/list/%d", projectId);
		            return RestClient_Get.sendGetRequest(urlString);
	        	}
	        	else {
	        		String urlString = String.format("http://localhost:8080/issues/list/%d?search=%s", projectId, keyword);
		            return RestClient_Get.sendGetRequest(urlString);
	        	}
	        }

	        @Override
	        protected void done() {
	            try {
	                String response = get();
	                System.out.println("Response from server: " + response);

	                JSONObject jsonResponse = new JSONObject(response);
	                boolean isSuccess = jsonResponse.getBoolean("isSuccess");
	                String code = jsonResponse.getString("code");

	                if (isSuccess && "ISSUE_3000".equals(code)) {
	                	model.setRowCount(0);
	                    JSONObject issuesArray = jsonResponse.getJSONObject("result");
	                    JSONArray projectsArray = issuesArray.getJSONArray("issues");
                        
	                    for (int i = 0; i < projectsArray.length(); i++) {
	                        JSONObject issueObject = projectsArray.getJSONObject(i);
	                        long issueId = issueObject.getLong("issueId");
	                        String title = issueObject.getString("title");
	                        String reporter = issueObject.getJSONObject("user").getString("userName");
	                        String fixer = issueObject.isNull("fixer") ? "None" : issueObject.getString("fixer");
	                        String assignee = issueObject.isNull("assignee") ? "None" : issueObject.getString("assignee");
	                        String priority = issueObject.getString("issuePriority");
	                        String status = issueObject.getString("issueStatus");
	                        String date = issueObject.getString("createAt").split("T")[0]; // Assuming date format is "YYYY-MM-DDTHH:MM:SS"
	                        
	                		issueList.add(issueId);
	                        
	                        Object[] array = {i+1, title, reporter, fixer, assignee, priority, status, date};
	                		model.addRow(array);

	                    }
	                } else {
	                    String message = jsonResponse.getString("message");
	                    JOptionPane.showMessageDialog(ProjectFrame.this, "이슈 목록 가져오기 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            } catch (InterruptedException | ExecutionException e) {
	                e.printStackTrace();
	                JOptionPane.showMessageDialog(ProjectFrame.this, "이슈 목록 가져오기 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    }.execute();
	}
	
	public void createAddMemberPage() {
		new AddMemberPage(this);
	}
	
	public void createRemoveMemberPage() {
		//new RemoveMemberPage(this);
	}
	
	public long getProjectId() {
		return projectId;
	}
	
}
