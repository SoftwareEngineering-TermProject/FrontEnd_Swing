package frame;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleScrollBar;
import util.RestClient_Get;
import util.RestClient;
import util.RestClient_Delete;
import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainFrame extends JFrame {
	private ArrayList<Object[]> projectList = new ArrayList<>();
	private ArrayList<ProjStyleButton> btnArray = new ArrayList<>();
	private JPanel btnPanel;
	private JScrollPane scr;
	private int numBtn = 0;
	private static long userId;
	
	private ArrayList<Long> accessibleId; // 이것도 임시.
	
	//생성자
	public MainFrame(long userId) {	
		
		this.userId = userId;
		
		readProjectList(); // 나중에 project에 속한 username 생기면 그걸로 바꾸기. 지금은 일단 모든 project 가져옴.
		
		accessibleId = new ArrayList<>();
		//projectId = 12345678;
		accessibleId.add(userId);
		
		setTitle("Main Frame");
		setSize(1150,820);
		setLocationRelativeTo(null); // 화면 중앙 위치
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// layout manager 전달
		//setLayout(null);
		
		// panel 생성
		JPanel panel1 = new JPanel(); // 배경 패널
		panel1.setBackground(ProjColor.customGray);
		
		// panel1 레이아웃
		panel1.setLayout(null);
		
		btnPanel = new JPanel(); // 내부 배경 패널
		btnPanel.setBackground(ProjColor.customDarkGray);
		btnPanel.setLayout(null);
		//btnPanel.setBounds(48, 95, 1062, 680);
		btnPanel.setPreferredSize(new Dimension(1000, 120));;
		
		scr = new JScrollPane(btnPanel);
		scr.setBackground(ProjColor.customDarkGray);
		scr.setBorder(null);
		panel1.add(scr);
		scr.setBounds(48, 95, 1062, 680);
		scr.setVerticalScrollBar(new ProjStyleScrollBar());
		scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//라벨
		JLabel lbl1 = new JLabel("All Projects");
		lbl1.setFont(new Font(null, Font.PLAIN, 50)); // 폰트, 굵게, 크기
		panel1.add(lbl1);
		lbl1.setBounds(20,1,400,80);
		
		ProjStyleButton btn4 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "+ new project");
		panel1.add(btn4);
		btn4.setBounds(890, 24, 220, 57);
		btn4.setPreferredSize(new Dimension(220, 57));
		
		btn4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { // 원래 기능 + new
            	newCreateProject();
            }
        });
		
		
	
				
		add(panel1);
		
		// visible
		setVisible(true);		
	}
	
	public void readProjectList() {

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
            	
                String urlString = "http://localhost:8080/projects";

                return RestClient_Get.sendGetRequest(urlString); //
            	
            }
            @Override
            protected void done() {
                try {
                    String response = get();
                    System.out.println("Response from project server: " + response);
                    
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean isSuccess = jsonResponse.getBoolean("isSuccess");
                    String code = jsonResponse.getString("code");
                    
                    if (isSuccess && "PROJECT_2000".equals(code)) {
                    	JSONObject resultObject = jsonResponse.getJSONObject("result");
                        JSONArray projectsArray = resultObject.getJSONArray("projects");
                        
                        Object[] objects = new Object[projectsArray.length()];
                        for (int i = 0; i < projectsArray.length(); i++) {
                            JSONObject projectObject = projectsArray.getJSONObject(i);
                            long projectId = projectObject.getLong("projectId");
                            String title = projectObject.getString("title");
                            String description = projectObject.getString("description");
                            
                            Object[] array = {projectId, title, description};

                            projectList.add(array);

                        }
                        
                        paintProjectList();
                        
                    	JOptionPane.showMessageDialog(MainFrame.this, "Project list Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                    } else {
                        // 실패 시 오류 메시지 표시
                        String message = jsonResponse.getString("message");
                        JOptionPane.showMessageDialog(MainFrame.this, "Searching failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(MainFrame.this, "Searching Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
	}
	
	public void paintProjectList() {
		while(numBtn < projectList.size()) {
			repaintButtonPanel();
		}
	}
	
	public void addProjectList(String title, String description) {
		//Object[] array = {projectId, title, description};
		//projectList.add(array);
		
	}
	
	public void repaintButtonPanel() {
		String title = (String)projectList.get(numBtn)[1];
//		long projectId = (long) projectList.get(numBtn)[0];
		
		 ProjStyleButton tempbtn = new ProjStyleButton(ProjColor.customDarkSkyblue, ProjColor.clickedCustomDarkSkyblue, Color.BLACK, title);
		    tempbtn.setActionCommand(String.valueOf(numBtn));
		    btnArray.add(tempbtn);
		    tempbtn.setBounds(31, 35 + 110 * numBtn, 700, 75); // 크기 조정
		    tempbtn.setPreferredSize(new Dimension(700, 75));
		    btnPanel.add(tempbtn);
		
		// 삭제 버튼 생성
		    ProjStyleButton deleteBtn = new ProjStyleButton(ProjColor.customDarkRed, ProjColor.clickedCustomDarkRed, Color.BLACK, "Delete");
		    deleteBtn.setBounds(750, 35 + 110 * numBtn, 135, 75); // 삭제 버튼 위치 조정
		    deleteBtn.setPreferredSize(new Dimension(135, 75));
		    deleteBtn.setActionCommand(String.valueOf(numBtn));
		    deleteBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 테두리 추가
		    deleteBtn.setFocusPainted(false);  // 포커스 테두리 제거
		    deleteBtn.setContentAreaFilled(true); // 버튼 배경 채우기		    				    
		    btnPanel.add(deleteBtn);

	    deleteBtn.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseReleased(MouseEvent e) {
	            int index = Integer.parseInt(deleteBtn.getActionCommand());
	            long projId = (long) projectList.get(index)[0];
	            deleteProject(projId, index);
	            deleteBtn.setBackground(ProjColor.customRed); // 기본 색상으로 복원
	        }	          	        	        	   	        
	    });
	    
	    // 수정 버튼 추가
	    ProjStyleButton modifyBtn = new ProjStyleButton(ProjColor.customDarkRed, ProjColor.clickedCustomDarkRed, Color.BLACK, "Modify");
	    modifyBtn.setBounds(900, 35 + 110 * numBtn, 135, 75); //위치조정
	    modifyBtn.setPreferredSize(new Dimension(135, 75));
	    modifyBtn.setActionCommand(String.valueOf(numBtn));
	    modifyBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 테두리 추가
	    modifyBtn.setFocusPainted(false);  // 포커스 테두리 제거
	    modifyBtn.setContentAreaFilled(true); // 버튼 배경 채우기	
	    btnPanel.add(modifyBtn);
	    btnArray.add(modifyBtn);

	    modifyBtn.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseReleased(MouseEvent e) {
	            JButton sourceButton = (JButton) e.getSource();
	            int index = Integer.parseInt(sourceButton.getActionCommand());
	            long projId = (long) projectList.get(index)[0];
	            String currentTitle = (String) projectList.get(index)[1];
	            String currentDescription = (String) projectList.get(index)[2];
	            SwingUtilities.invokeLater(() -> new ProjModifyFrame(projId, currentTitle, currentDescription, userId, MainFrame.this));
	            // 실행시 멈춤현상 발생 -->> 고쳐야됨.
	        }
	    });
	    
		btnArray.get(numBtn).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JButton sourceButton = (JButton) e.getSource();
				long projId = (long) projectList.get(Integer.parseInt(sourceButton.getActionCommand()))[0];
				accessProject(projId, title);
			}
		});
		
		btnPanel.revalidate();
		numBtn++;
		
		scr.revalidate();
        scr.repaint();
	}
	
	
	public void newCreateProject() {
		new CreateProjectDialog(this);
	}
	
	
	
	public void accessProject(long projId, String title) {
		//new ProjectFrame(title, this);
		
		//통신 요청으로 프로젝트 접속할때 userId 확인해서 접근 권한 있는지 체크. 일단은 임시로 느낌만 만듬.
		if(!accessibleId.contains(userId)) {
			JOptionPane.showMessageDialog(MainFrame.this, "NOT accessible", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			new ProjectFrame(projId, title, this);
			setVisible(false);
		}
	}
	
	public void addProjectArrayList(Object[] objects) {
		projectList.add(objects);
	}
	//프로젝트 삭제기능
	public void deleteProject(long projId, int index) {
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String url = String.format("http://localhost:8080/projects/%d?userId=%d", projId, userId);
                return RestClient_Delete.sendDeleteRequest(url);
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
                        JOptionPane.showMessageDialog(MainFrame.this, "Project Deleted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        // 삭제 성공 시 리스트에서 제거하고 UI 업데이트
                        projectList.remove(index);
                        btnPanel.removeAll();
                        btnArray.clear();
                        numBtn = 0;
                        paintProjectList();
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.this, "Project Deletion Failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(MainFrame.this, "Project Deletion Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
	
	
	
	public long getUserId() {
		return userId;
	}
		
}


