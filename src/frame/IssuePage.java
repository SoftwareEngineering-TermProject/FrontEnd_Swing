package frame;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import style.ProjStyleComboBox;
import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleScrollBar;
import util.RestClient;
import util.RestClient_Delete;
import util.RestClient_Get;
import util.RestClient_Patch;

public class IssuePage extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField titleTextField;
	private String title;
	private JTextArea descriptionTextArea;
	private String description;
	private JLabel dateLabel;
	private String date;
	private JLabel reporterLabel;
	private String reporter;
	private ProjStyleComboBox assigneeComboBox;
	private String assignee;
	private ArrayList<Object[]> assigneeMember;
	private JLabel fixerLabel;
	private String fixer;
	private ProjStyleComboBox priorityComboBox;
	private String priority;
	private JLabel statusLabel;
	private String status;
	private long userId;
	private long issueId;
	private CommentPanel commentPanel;
	private boolean edit;
	private ProjStyleButton fixerButton;
	private ProjectFrame parentFrame;
	private String userRole;
	private boolean isClosed;
	private String url;
	
	public IssuePage (long userId, long issueId, ProjectFrame parentFrame) {
		
		this.title = "";
		this.description = "";
		this.date = "";
		this.userId = userId;
		this.issueId = issueId;
		edit = false;
		assigneeMember = new ArrayList<>();
		this.parentFrame = parentFrame;
		url = InputUrlPage.getUrl();
		userRole = getUserRoleByUserId(userId);
		
		setSize(1000,700);
		setLocationRelativeTo(null);
		setModal(true);
		setUndecorated(true);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(ProjColor.customDarkGray);
		mainPanel.setBorder(null);
		
		ProjStyleButton closeButton = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.customDarkGray, Color.BLACK, "X");
		closeButton.setFont(new Font(null, Font.PLAIN, 50));
		closeButton.setBounds(550, 20, 30, 35);
		closeButton.setPreferredSize(new Dimension(30, 35));
		
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				parentFrame.getIssuesFromServer("");
				setVisible(false);
				dispose();
			}
		});
		mainPanel.add(closeButton);
		
		titleTextField = new JTextField();
		titleTextField.setBackground(ProjColor.customDarkGray);
		titleTextField.setEnabled(false);
		titleTextField.setFont(new Font(null, Font.BOLD, 40));
		titleTextField.setBorder(null);
		titleTextField.setBounds(10, 20, 540, 40);
		titleTextField.setPreferredSize(new Dimension(540, 40));
		mainPanel.add(titleTextField);
		
		dateLabel = new JLabel();
		dateLabel.setBackground(Color.WHITE);
		dateLabel.setFont(new Font(null, Font.BOLD, 18));
		dateLabel.setBounds(30, 150, 100, 25);
		dateLabel.setPreferredSize(new Dimension(100, 25));
		dateLabel.setBorder(null);
		mainPanel.add(dateLabel);
		
		descriptionTextArea = new JTextArea();
		descriptionTextArea.setFont(new Font("맑은 고딕", 1, 15));
		descriptionTextArea.setBackground(ProjColor.customGray);
		descriptionTextArea.setText(description);
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setWrapStyleWord(true);
		descriptionTextArea.setEditable(false);
		descriptionTextArea.setBorder(null);
		
		JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
		scrollPane.setVerticalScrollBar(new ProjStyleScrollBar(ProjColor.tableHeaderGray, ProjColor.customGray));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // 수평 스크롤바 비활성화
        scrollPane.setBounds(30, 185, 540, 245);
        scrollPane.setPreferredSize(new Dimension(540, 245));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        mainPanel.add(scrollPane);
        
        ProjStyleButton editButton = new ProjStyleButton(ProjColor.customGray, ProjColor.clickedCustomGray, Color.BLACK, "Edit");
		editButton.setFont(new Font("맑은 고딕", 1, 25));
		editButton.setBounds(30, 80, 55, 40);
		editButton.setPreferredSize(new Dimension(55, 40));
		editButton.setBorder(null);
		
		editButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(isClosed == false) {
					edit = !edit;
					if(edit) {
						titleTextField.setEnabled(true);
						descriptionTextArea.setEditable(true);
					}
					else {
						titleTextField.setEnabled(false);
						descriptionTextArea.setEditable(false);
						String modifiedTitle = titleTextField.getText();
						String modifiedDescription = descriptionTextArea.getText();
						modifyIssue(modifiedTitle, modifiedDescription);
					}
				}
			}
		});
		mainPanel.add(editButton);
		
		JLabel reporterTextLabel = new JLabel("Reporter");
		reporterTextLabel.setFont(new Font(null, Font.BOLD, 18));
		reporterTextLabel.setBounds(30, 440, 100, 25);
		reporterTextLabel.setPreferredSize(new Dimension(100, 25));
		mainPanel.add(reporterTextLabel);
		
		reporterLabel = new JLabel();
		reporterLabel.setFont(new Font(null, Font.BOLD, 18));
		reporterLabel.setBounds(150, 440, 100, 25);
		reporterLabel.setPreferredSize(new Dimension(100, 25));
		mainPanel.add(reporterLabel);
		
		JLabel assigneeTextLabel = new JLabel("Assignee");
		assigneeTextLabel.setFont(new Font(null, Font.BOLD, 18));
		assigneeTextLabel.setBounds(30, 480, 100, 25);
		assigneeTextLabel.setPreferredSize(new Dimension(100, 25));
		mainPanel.add(assigneeTextLabel);
		
		String[] none = {"None"};
		assigneeComboBox = new ProjStyleComboBox(none);
		assigneeComboBox.setBackground(ProjColor.customGray);
		assigneeComboBox.setBounds(150, 480, 100, 25);
		assigneeComboBox.setPreferredSize(new Dimension(100, 25));
		mainPanel.add(assigneeComboBox);
		
		ProjStyleButton assignButton = new ProjStyleButton(ProjColor.customGray, ProjColor.clickedCustomGray, Color.BLACK, "assign");
		assignButton.setFont(new Font("맑은 고딕", 1, 18));
		assignButton.setBounds(270, 480, 80, 25);
		assignButton.setPreferredSize(new Dimension(80, 25));
		assignButton.setBorder(null);
		
		assignButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(isClosed == false) {
					String assignedUserName = (String)assigneeComboBox.getSelectedItem();
					assignMember(assignedUserName);
				}
			}
		});
		mainPanel.add(assignButton);
		
		JLabel fixerTextLabel = new JLabel("Fixer");
		fixerTextLabel.setFont(new Font(null, Font.BOLD, 18));
		fixerTextLabel.setBounds(30, 520, 100, 25);
		fixerTextLabel.setPreferredSize(new Dimension(100, 25));
		mainPanel.add(fixerTextLabel);
		
		fixerLabel = new JLabel();
		fixerLabel.setFont(new Font(null, Font.BOLD, 18));
		fixerLabel.setBounds(150, 520, 100, 25);
		fixerLabel.setPreferredSize(new Dimension(100, 25));
		mainPanel.add(fixerLabel);

		fixerButton = new ProjStyleButton(ProjColor.customGray, ProjColor.clickedCustomGray, Color.BLACK, "fix");
		fixerButton.setFont(new Font("맑은 고딕", 1, 18));
		fixerButton.setBounds(270, 520, 80, 25);
		fixerButton.setPreferredSize(new Dimension(80, 25));
		fixerButton.setBorder(null);
		
		fixerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(isClosed == false) {
					String userName = getUserNameByUserId(userId);
					if(userName.equals(assignee)) {
						if(status.equals("NEW") || status.equals("ASSIGNED") || status.equals("REOPENED")) {
							fixIssue();
						}
						else {
							JOptionPane.showMessageDialog(IssuePage.this, "fix 실패: fix 불가능한 상태입니다.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
					else {
						JOptionPane.showMessageDialog(IssuePage.this, "fix 실패: 담당자가 아닙니다.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mainPanel.add(fixerButton);
		
		JLabel priorityTextLabel = new JLabel("Priority");
		priorityTextLabel.setFont(new Font(null, Font.BOLD, 18));
		priorityTextLabel.setBounds(30, 560, 100, 25);
		priorityTextLabel.setPreferredSize(new Dimension(100, 25));
		mainPanel.add(priorityTextLabel);
		
		String[] priorityList = {"BLOCKER", "CRITICAL", "MAJOR", "MINOR", "TRIVIAL"};
		priorityComboBox = new ProjStyleComboBox(priorityList);
		priorityComboBox.setBackground(ProjColor.customGray);
		priorityComboBox.setBounds(150, 560, 100, 25);
		priorityComboBox.setPreferredSize(new Dimension(100, 25));
		mainPanel.add(priorityComboBox);
		
		ProjStyleButton prioritySaveButton = new ProjStyleButton(ProjColor.customGray, ProjColor.clickedCustomGray, Color.BLACK, "save");
		prioritySaveButton.setFont(new Font("맑은 고딕", 1, 18));
		prioritySaveButton.setBounds(270, 560, 80, 25);
		prioritySaveButton.setPreferredSize(new Dimension(80, 25));
		prioritySaveButton.setBorder(null);
		
		prioritySaveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(isClosed == false) {
					if(userRole.equals("PL") || userRole.equals("ADMIN")) {
						changePriorityNStatus();
					}
					else {
						JOptionPane.showMessageDialog(IssuePage.this, "priority 수정 실패: 권한이 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		mainPanel.add(prioritySaveButton);
		
		JLabel statusTextLabel = new JLabel("Status");
		statusTextLabel.setFont(new Font(null, Font.BOLD, 18));
		statusTextLabel.setBounds(30, 600, 100, 25);
		statusTextLabel.setPreferredSize(new Dimension(100, 25));
		mainPanel.add(statusTextLabel);
		
		statusLabel = new JLabel();
		statusLabel.setFont(new Font(null, Font.BOLD, 18));
		statusLabel.setBounds(150, 600, 100, 25);
		statusLabel.setPreferredSize(new Dimension(100, 25));
		mainPanel.add(statusLabel);
		
		ProjStyleButton statusOpenCloseButton = new ProjStyleButton(ProjColor.customGray, ProjColor.clickedCustomGray, Color.BLACK, "");
		statusOpenCloseButton.setFont(new Font("맑은 고딕", 1, 18));
		statusOpenCloseButton.setBounds(270, 600, 80, 25);
		statusOpenCloseButton.setPreferredSize(new Dimension(80, 25));
		statusOpenCloseButton.setBorder(null);
		
		statusOpenCloseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(userRole.equals("PL") || userRole.equals("ADMIN")) {
					if(status.equals("CLOSED")) {
						statusOpenCloseButton.setText("close");
						isClosed = false;
						status = "REOPENED";
						statusLabel.setText(status);
						changePriorityNStatus();
						deleteAssignee();
						assigneeComboBox.setSelectedItem("None");
						deleteFixer();
						fixerLabel.setText("None");
					}
					else {
						statusOpenCloseButton.setText("reopen");
						isClosed = true;
						status = "CLOSED";
						statusLabel.setText(status);
						changePriorityNStatus();
					}
				}
				else {
					JOptionPane.showMessageDialog(IssuePage.this, "status 변경 실패: 권한이 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		mainPanel.add(statusOpenCloseButton);

		commentPanel = new CommentPanel(400, 700, userId, issueId);
		commentPanel.setBounds(600, 0, 400, 700);
		add(commentPanel);
		
		getIssueDetailFromServer();
		
		if(status.equals("CLOSED")) {
			statusOpenCloseButton.setText("reopen");
			
		}
		else {
			statusOpenCloseButton.setText("close");
		}
		
		getIssueComment();

		getAssigneeMember();
		
		// 오류나면 안에 있는 함수만 쓰자.
		/*
		Timer timer = new Timer();
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				getIssueComment();
			}
		};

		timer.schedule(task, 0, 1000);
		*/ // 추가로 바꾸자. 자꾸 업데이트하니깐 껏다키는거 불편함
		
		add(mainPanel);	
		setVisible(true);

	}
	
	public void deleteAssignee () {
		String url = String.format(this.url + "issues/issues/assignee/%s?userId=%s", issueId, userId);
		try {
			String response = RestClient_Delete.sendDeleteRequest(url);
			
			JSONObject jsonResponse = new JSONObject(response);
            boolean isSuccess = jsonResponse.getBoolean("isSuccess");
            String code = jsonResponse.getString("code");
            String message = jsonResponse.getString("message");
            
            if (isSuccess && "ISSUE_3000".equals(code)) {

            } else {
                JOptionPane.showMessageDialog(IssuePage.this, "Project Deletion Failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
            }        
		} catch (Exception e) {
			JOptionPane.showMessageDialog(IssuePage.this, "Project Deletion Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void deleteFixer () {
		String url = String.format(this.url + "issues/issues/fixer/%s?userId=%s", issueId, userId);
		try {
			String response = RestClient_Delete.sendDeleteRequest(url);
			
			JSONObject jsonResponse = new JSONObject(response);
            boolean isSuccess = jsonResponse.getBoolean("isSuccess");
            String code = jsonResponse.getString("code");
            String message = jsonResponse.getString("message");
            
            if (isSuccess && "ISSUE_3000".equals(code)) {

            } else {
                JOptionPane.showMessageDialog(IssuePage.this, "Project Deletion Failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
            }        
		} catch (Exception e) {
			JOptionPane.showMessageDialog(IssuePage.this, "Project Deletion Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void modifyIssue(String modifiedTitle, String modifiedDescription) {
		String urlString = String.format(url + "issues/%s?userId=%s", issueId, userId);
		String jsonInputString = String.format("{\"title\":\"%s\", \"description\":\"%s\"}", modifiedTitle, modifiedDescription);
		try {
			String response = RestClient_Patch.sendPatchRequest(urlString, jsonInputString);
			
			JSONObject jsonResponse = new JSONObject(response);
	        boolean isSuccess = jsonResponse.getBoolean("isSuccess");
	        String code = jsonResponse.getString("code");
	        
	        if (isSuccess && "ISSUE_3000".equals(code)) {
	        	JSONObject resultObject = jsonResponse.getJSONObject("result");
	            String modifyingTitle = resultObject.getString("title");
	            String modifyingDescription = resultObject.getString("description");
	            titleTextField.setText(modifyingTitle);
	            descriptionTextArea.setText(modifyingDescription);
	        }  else {
	        	String message = jsonResponse.getString("message");
	            JOptionPane.showMessageDialog(IssuePage.this, "이슈 수정 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
	        }
		}
		catch (Exception e) {
			if (e.getMessage().equals("Failed : HTTP error code : 400")) {
				JOptionPane.showMessageDialog(IssuePage.this, "이슈 수정 불가: 권한이 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(IssuePage.this, "이슈 수정 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);	
			}
		}
	}
	
	public void getIssueDetailFromServer() {
		String urlString = String.format(url + "issues/%d", issueId);
		try {
			String response = RestClient_Get.sendGetRequest(urlString);
			JSONObject jsonResponse = new JSONObject(response);
	        boolean isSuccess = jsonResponse.getBoolean("isSuccess");
	        String code = jsonResponse.getString("code");

	        if (isSuccess && "ISSUE_3000".equals(code)) {
	            JSONObject issueObject = jsonResponse.getJSONObject("result");
	            
	            this.title = issueObject.getString("title");
	            this.description = issueObject.getString("description");
	            this.date = issueObject.getString("createAt").split("T")[0];
	            this.reporter = issueObject.getJSONObject("user").getString("userName");
	            this.fixer = issueObject.isNull("fixer") ? "None" : issueObject.getString("fixer");
	            this.assignee = issueObject.isNull("assignee") ? "None" : issueObject.getString("assignee");
	            this.priority = issueObject.getString("issuePriority");
	            this.status = issueObject.getString("issueStatus");
	            
	            
	            this.titleTextField.setText(title);
	            this.descriptionTextArea.setText(description);
	            this.dateLabel.setText(date);
	            this.reporterLabel.setText(reporter);
	            this.fixerLabel.setText(fixer);
	            //this.assigneeComboBox ;;
	            this.priorityComboBox.setSelectedItem(priority);
	            this.statusLabel.setText(status);
	            if(status.equals("NEW") || status.equals("ASSIGNED") || status.equals("REOPENED")) {
	            }
	            else {
	            }
	            
	            if(status.equals("CLOSED")) {
	            	isClosed = true;
	            }
	            else {
	            	isClosed = false;
	            }
	            
	        } else {
	            String message = jsonResponse.getString("message");
	            JOptionPane.showMessageDialog(IssuePage.this, "1이슈 목록 가져오기 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
	        }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(IssuePage.this, "2이슈 목록 가져오기 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public void getIssueComment() {
		String urlString = String.format(url + "issues/%s", issueId);
		try {
			String response = RestClient_Get.sendGetRequest(urlString);
			JSONObject jsonResponse = new JSONObject(response);
	        boolean isSuccess = jsonResponse.getBoolean("isSuccess");
	        String code = jsonResponse.getString("code");

	        if (isSuccess && "ISSUE_3000".equals(code)) {
	            JSONObject issueObject = jsonResponse.getJSONObject("result");
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
	
	@SuppressWarnings("unchecked")
	public void getAssigneeMember() {
		
		try {
			String urlString = url + "users";
			String response = RestClient_Get.sendGetRequest(urlString);
			
			JSONObject jsonResponse = new JSONObject(response);
            boolean isSuccess = jsonResponse.getBoolean("isSuccess");
            String code = jsonResponse.getString("code");
            
            if (isSuccess && "USER_1000".equals(code)) {
            	JSONObject resultObject = jsonResponse.getJSONObject("result");
                JSONArray projectsArray = resultObject.getJSONArray("users");
                
                assigneeMember.clear();
                for (int i = 0; i < projectsArray.length(); i++) {
                    JSONObject projectObject = projectsArray.getJSONObject(i);
                    long userId = projectObject.getLong("userId");
                    String userName = projectObject.getString("userName");

                    
                    Object[] array = {userId, userName};
                    

                    assigneeMember.add(array);

                }
                
                assigneeComboBox.removeAllItems();
                String none = "None";
                assigneeComboBox.addItem(none);
                for(int i = 0; i < assigneeMember.size(); i++) {
                	assigneeComboBox.addItem((String)assigneeMember.get(i)[1]);
                }
                assigneeComboBox.setSelectedItem(assignee);

                repaint(); //

            } else {
                // 실패 시 오류 메시지 표시
                String message = jsonResponse.getString("message");
                JOptionPane.showMessageDialog(IssuePage.this, "Searching failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
            }
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(IssuePage.this, "Searching Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void assignMember(String assignedUserName) {
		try {
			String urlString = String.format(url + "issues/assignee/%s?userId=%s", issueId, userId);
			String jsonInputString = String.format("{\"userName\":\"%s\"}", assignedUserName);
  
	        String response = RestClient.sendPostRequest(urlString, jsonInputString);
	        
	        JSONObject jsonResponse = new JSONObject(response);
            boolean isSuccess = jsonResponse.getBoolean("isSuccess");
            String code = jsonResponse.getString("code");
            
            if (isSuccess && "ISSUE_3000".equals(code)) {
            	JSONObject resultObject = jsonResponse.getJSONObject("result");
                assignee = resultObject.getString("userName");
                status = "ASSIGNED";
                statusLabel.setText("ASSIGNED");
                changePriorityNStatus();
                getAssigneeMember();
            } else {
                // 실패 시 오류 메시지 표시
                String message = jsonResponse.getString("message");
                JOptionPane.showMessageDialog(IssuePage.this, "이슈 담당자 할당 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
            }
		} catch (Exception e) {
			if (e.getMessage().equals("Failed : HTTP error code : 400")) {
				JOptionPane.showMessageDialog(IssuePage.this, "이슈 담당자 할당 실패: Nonedms 할당할 수 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if (e.getMessage().equals("Failed : HTTP error code : 500")) {
				JOptionPane.showMessageDialog(IssuePage.this, "이슈 담당자 할당 불가: 권한이 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(IssuePage.this, "이슈 담당자 할당 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void fixIssue() {
		String urlString = String.format(url + "issues/status/priority%s", issueId);
		String jsonInputString = String.format("{\"issueStatus\":\"%s\", \"issuePriority\":\"%s\"}", "RESOLVED", priority);
		try {
			String response = RestClient_Patch.sendPatchRequest(urlString, jsonInputString);
			
			JSONObject jsonResponse = new JSONObject(response);
	        boolean isSuccess = jsonResponse.getBoolean("isSuccess");
	        String code = jsonResponse.getString("code");
	        
	        if (isSuccess && "ISSUE_3000".equals(code)) {
	        	JSONObject resultObject = jsonResponse.getJSONObject("result");
	            status = resultObject.getString("issueStatus");
	            statusLabel.setText(status);
	            
	        }  else {
	        	String message = jsonResponse.getString("message");
	            JOptionPane.showMessageDialog(IssuePage.this, "1이슈 목록 가져오기 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
	        }
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(IssuePage.this, "2이슈 목록 가져오기 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setIssueFixer() {
		try {
			String urlString = String.format(url + "issues/fixer/%s?userId=%s", issueId, userId);
  
	        String response = RestClient.sendPostRequest(urlString);
	        
	        JSONObject jsonResponse = new JSONObject(response);
            boolean isSuccess = jsonResponse.getBoolean("isSuccess");
            String code = jsonResponse.getString("code");
            
            if (isSuccess && "ISSUE_3000".equals(code)) {
            	JSONObject resultObject = jsonResponse.getJSONObject("result");
                fixer = resultObject.getString("userName");
                fixerLabel.setText(fixer);
            } else {
                // 실패 시 오류 메시지 표시
                String message = jsonResponse.getString("message");
                JOptionPane.showMessageDialog(IssuePage.this, "프로젝트 멤버 추가 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
            }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(IssuePage.this, "프로젝트 멤버 추가 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void changePriorityNStatus() {
		String urlString = String.format(url + "issues/status/priority%s", issueId);
		
		this.priority = (String)priorityComboBox.getSelectedItem();
		String jsonInputString = String.format("{\"issueStatus\":\"%s\", \"issuePriority\":\"%s\"}", status, priority);
		try {
			String response = RestClient_Patch.sendPatchRequest(urlString, jsonInputString);
			
			JSONObject jsonResponse = new JSONObject(response);
	        boolean isSuccess = jsonResponse.getBoolean("isSuccess");
	        String code = jsonResponse.getString("code");
	        
	        if (isSuccess && "ISSUE_3000".equals(code)) {
	            
	        }  else {
	        	String message = jsonResponse.getString("message");
	            JOptionPane.showMessageDialog(IssuePage.this, "1이슈 목록 가져오기 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
	        }
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(IssuePage.this, "2이슈 목록 가져오기 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String getUserRoleByUserId(long userId) {
        String url = String.format(this.url + "projects/projectList/%s", userId);

        try {
            String response = RestClient_Get.sendGetRequest(url);

            JSONObject jsonResponse = new JSONObject(response);
            boolean isSuccess = jsonResponse.getBoolean("isSuccess");
            String code = jsonResponse.getString("code");
            
            if (isSuccess && "PROJECT_2000".equals(code)) {
                JSONObject resultObject = jsonResponse.getJSONObject("result");
                JSONArray projectArray = resultObject.getJSONArray("projects");
                for (int i = 0; i < projectArray.length(); i++) {
                    JSONObject project = projectArray.getJSONObject(i);
                    if (project.getLong("projectId") == parentFrame.getProjectId()) {
                        return project.getString("userRole");
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
	
	private String getUserNameByUserId(long userId) {
        String url = String.format(this.url + "users");

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

