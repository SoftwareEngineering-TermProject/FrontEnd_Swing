package frame;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleTable;
import util.RestClient;
import util.RestClient_Get;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProjectFrame extends JFrame{
	
	private DefaultTableModel model;
	private ArrayList<Object[]> issueList;
	private int issueNum;
	private long projectId;
	private long userId;
	public ProjectFrame(long projectId, long userId, String title, MainFrame parentFrame) {
		
		issueList = new ArrayList<>();
		issueNum = 0;
		this.projectId = projectId;
		this.userId = userId;
		System.out.println(projectId);
		
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
		btn1.setBounds(50, 100, 220, 50);
		btn1.setPreferredSize(new Dimension(220, 50));
		
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				callAddIssue();
			}
		});
		
		ProjStyleButton btn4 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "- remove issue");
		panel1.add(btn4);
		btn4.setBounds(511, 100, 219, 50);
		btn4.setPreferredSize(new Dimension(218, 50));
		
		btn4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//함수구현
			}
		});
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "+ add member");
		panel1.add(btn2);
		btn2.setBounds(280, 100, 220, 50);
		btn2.setPreferredSize(new Dimension(220, 50));
		
		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				createAddMemberPage();
			}
		});
		
		JTextField tf1 = new JTextField();
		tf1.setBackground(ProjColor.customWhiteGray);
		tf1.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		tf1.setBorder(null);
		panel1.add(tf1);
		tf1.setBounds(745, 100, 220, 50);
		
		ProjStyleButton btn3 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "search");
		panel1.add(btn3);
		btn3.setBounds(980, 100, 100, 50);
		btn3.setPreferredSize(new Dimension(100, 50));
		
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
		 getIssuesFromServer();
        
        table.addMouseListener(new MouseAdapter() { // row가 동적으로 생성됨에 따라 이놈도 동적으로 생성되어야 하는데... 동적으로 생성될때마다 override를 해주자.
        	@Override
        	public void mouseClicked(MouseEvent event) {
        		int row = table.rowAtPoint(event.getPoint()); // 0부터 시작
        		
        		if (row == 0) {
        			System.out.println("Issue description"); // issue description tap 생성
        		}
        		new IssuePage();
        	}
        });
        
        // table 생성 끝
		
		add(panel1);
		
		setVisible(true);
		
		addWindowListener(new WindowAdapter() { // 1. 화면 전환에 new가 맞는가 2. x가 아니라 뒤로가기 버튼을 새로 만들어야 하나
			public void windowClosing(WindowEvent e) {
				/*
				parentFrame.setVisible(true);
				setVisible(false);
				dispose(); // 현재 프레임만 없애기
				*/
				new MainFrame(userId);
				parentFrame.dispose();
				setVisible(false);
				dispose();
			}
		});
	}
	
	public void addIssue(String title, String reporter, String fixer, String assignee, String priority, String status, String date) {
		Object[] array = {issueNum+1, title, reporter, fixer, assignee, priority, status, date};
		issueList.add(array);
	}
	
	public void addModel() {
		model.addRow(issueList.get(issueNum));
		issueNum++;
	}
	
	public void callAddIssue() {
		new NewIssuePage(this, projectId, userId);
	}
	
	public void addProjectMember(String userName, String userRole) {
		new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
            	
            	String encodedUserName = URLEncoder.encode(userName, "UTF-8");
            	
                String urlString = "http://localhost:8080/users?search=";

                return RestClient_Get.sendGetRequest(urlString + encodedUserName);
            	
            }
            @Override
            protected void done() {
                try {
                    String response = get();
                    System.out.println("Response from project server: " + response);
                    
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isSuccess = jsonResponse.getBoolean("isSuccess");
                    String code = jsonResponse.getString("code");
                    
                    if (isSuccess && "USER_1000".equals(code)) {
                    	JSONObject resultObject = jsonResponse.getJSONObject("result");
                        JSONArray projectsArray = resultObject.getJSONArray("users");
                        
                        Object[] objects = new Object[projectsArray.length()];
                        for (int i = 0; i < projectsArray.length(); i++) {
                            JSONObject projectObject = projectsArray.getJSONObject(i);
                            long userId = projectObject.getLong("userId");
                            AddProjectUserDataBase(userId, userRole);

                        }                        
                        
                    } else {
                        // 실패 시 오류 메시지 표시
                        String message = jsonResponse.getString("message");
                        JOptionPane.showMessageDialog(ProjectFrame.this, "프로젝트 목록 가져오기 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ProjectFrame.this, "프로젝트 목록 가져오기 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
	}
	
	//이슈목록을 서버로부터 가져오는 코드. 
	public void getIssuesFromServer() {
	    new SwingWorker<String, Void>() {
	        @Override
	        protected String doInBackground() throws Exception {
	            String urlString = String.format("http://localhost:8080/issues/list/%d", projectId);
	            return RestClient_Get.sendGetRequest(urlString);
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
	                    JSONObject issuesArray = jsonResponse.getJSONObject("result");
	                    JSONArray projectsArray = issuesArray.getJSONArray("issues");
                        
	                    for (int i = 0; i < projectsArray.length(); i++) {
	                        JSONObject issueObject = projectsArray.getJSONObject(i);
	                        String title = issueObject.getString("title");
	                        String reporter = issueObject.getJSONObject("user").getString("userName");
	                        String fixer = issueObject.isNull("fixer") ? "None" : issueObject.getString("fixer");
	                        String assignee = issueObject.isNull("assignee") ? "None" : issueObject.getString("assignee");
	                        String priority = "MAJOR"; // Assuming priority is not present in JSON
	                        String status = issueObject.getString("issueStatus");
	                        String date = issueObject.getString("createAt").split("T")[0]; // Assuming date format is "YYYY-MM-DDTHH:MM:SS"

	                        addIssue(title, reporter, fixer, assignee, priority, status, date);
	                        addModel();
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

	
	private void AddProjectUserDataBase(long userId, String userRole) {
		new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {

                String encodedUserName = URLEncoder.encode(Long.toString(userId), "UTF-8");
                String jsonInputString = String.format("{\"projectId\":\"%s\", \"userRole\":\"%s\"}", Long.toString(projectId), userRole);
                System.out.println("Sending JSON: " + jsonInputString);  // JSON 데이터 출력
                return RestClient.sendPostRequest("http://localhost:8080/projects/add/" + encodedUserName, jsonInputString); // 실제 서버 URL 사용
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
                    
                    //System.out.println("isSuccess: " + isSuccess);
                    //System.out.println("code: " + code); 
                    //응답 결과 처리
                    if (!isSuccess || !"PROJECT_2000".equals(code)) {
                    	// 실패 시 오류 메시지 표시
                        String message = jsonResponse.getString("message");
                        JOptionPane.showMessageDialog(ProjectFrame.this, "프로젝트 유저 추가 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();  // 예외 메시지 출력
                    JOptionPane.showMessageDialog(ProjectFrame.this, "프로젝트 유저 추가 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
	}
	
	
	public void createAddMemberPage() {
		new AddMemberPage(this);
	}
	
	public long getProjectId() {
		return projectId;
	}
	
}
