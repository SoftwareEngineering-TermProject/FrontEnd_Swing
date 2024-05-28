package frame;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleScrollBar;
import util.RestClient_Get;
import util.RestClient_Delete;
import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainFrame extends JFrame {
	private ArrayList<Object[]> projectList = new ArrayList<>();
	private ArrayList<ProjStyleButton> projectBtnArray;
	private JPanel btnPanel;
	private JScrollPane scr;
	private int numBtn = 0;
	private static long userId;
	private boolean modifyMode;
	private boolean deleteMode;
	private ArrayList<MouseListener> entryProjectListeners;
	private ArrayList<MouseListener> modifyProjectListeners;
	private ArrayList<MouseListener> deleteProjectListeners;
	
	private ArrayList<Long> accessibleId; // 이것도 임시.
	
	//생성자
	public MainFrame(long userId) {	
		projectBtnArray = new ArrayList<>();
		this.userId = userId;
		modifyMode = false;
		deleteMode = false;
		entryProjectListeners = new ArrayList<>();
		modifyProjectListeners = new ArrayList<>();
		deleteProjectListeners = new ArrayList<>();
		
		readProjectList(); // 나중에 project에 속한 username 생기면 그걸로 바꾸기. 지금은 일단 모든 project 가져옴.
		
		accessibleId = new ArrayList<>();
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
		btnPanel.setPreferredSize(new Dimension(1000, 90));
		
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
		
		ProjStyleButton btn4 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "modify");
		panel1.add(btn4);
		btn4.setBounds(550, 24, 150, 57);
		btn4.setPreferredSize(new Dimension(150, 57));
		btn4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { // 원래 기능 + new
            	if(!deleteMode) {
	            	modifyMode = !modifyMode;
	            	if(modifyMode) {
	            		for(int i = 0; i < projectBtnArray.size(); i++) {
	            			projectBtnArray.get(i).setBackground(ProjColor.customDarkGreen);
	            			projectBtnArray.get(i).setUnClickBackground(ProjColor.customDarkGreen);
	            			projectBtnArray.get(i).setClickBackground(ProjColor.clickedCustomDarkGreen);
	            			projectBtnArray.get(i).removeMouseListener(entryProjectListeners.get(i));
	            			projectBtnArray.get(i).addMouseListener(modifyProjectListeners.get(i));
	            		}
	            	}
	            	else {
	            		for(int i = 0; i < projectBtnArray.size(); i++) {
	            			projectBtnArray.get(i).setBackground(ProjColor.customDarkSkyblue);
	            			projectBtnArray.get(i).setUnClickBackground(ProjColor.customDarkSkyblue);
	            			projectBtnArray.get(i).setClickBackground(ProjColor.clickedCustomDarkSkyblue);
	            			projectBtnArray.get(i).removeMouseListener(modifyProjectListeners.get(i));
	            			projectBtnArray.get(i).addMouseListener(entryProjectListeners.get(i));
	            		}
	            	}
            	}
            	else {
            		deleteMode = !deleteMode;
            		for(int i = 0; i < numBtn; i++) {
            			projectBtnArray.get(i).setBackground(ProjColor.customDarkSkyblue);
            			projectBtnArray.get(i).setUnClickBackground(ProjColor.customDarkSkyblue);
            			projectBtnArray.get(i).setClickBackground(ProjColor.clickedCustomDarkSkyblue);
            			projectBtnArray.get(i).removeMouseListener(deleteProjectListeners.get(i));
            			projectBtnArray.get(i).addMouseListener(entryProjectListeners.get(i));
            		}
            		modifyMode = !modifyMode;
            		for(int i = 0; i < projectBtnArray.size(); i++) {
            			projectBtnArray.get(i).setBackground(ProjColor.customDarkGreen);
            			projectBtnArray.get(i).setUnClickBackground(ProjColor.customDarkGreen);
            			projectBtnArray.get(i).setClickBackground(ProjColor.clickedCustomDarkGreen);
            			projectBtnArray.get(i).removeMouseListener(entryProjectListeners.get(i));
            			projectBtnArray.get(i).addMouseListener(modifyProjectListeners.get(i));
            		}
            	}
            }
        });
		
		ProjStyleButton btn5 = new ProjStyleButton(ProjColor.customWhiteRed, ProjColor.clickedCustomWhiteRed, Color.BLACK, "delete");
		panel1.add(btn5);
		btn5.setBounds(720, 24, 150, 57);
		btn5.setPreferredSize(new Dimension(150, 57));
		
		btn5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { // 원래 기능 + new
            	if (!modifyMode) {
            		deleteMode = !deleteMode;
	            	if(deleteMode) {
	            		for(int i = 0; i < numBtn; i++) {
	            			projectBtnArray.get(i).setBackground(ProjColor.customLightRed);
	            			projectBtnArray.get(i).setUnClickBackground(ProjColor.customLightRed);
	            			projectBtnArray.get(i).setClickBackground(ProjColor.clickedCustomLightRed);
	            			projectBtnArray.get(i).removeMouseListener(entryProjectListeners.get(i));
	            			projectBtnArray.get(i).addMouseListener(deleteProjectListeners.get(i));
	            		}
	            	}
	            	else {
	            		for(int i = 0; i < numBtn; i++) {
	            			projectBtnArray.get(i).setBackground(ProjColor.customDarkSkyblue);
	            			projectBtnArray.get(i).setUnClickBackground(ProjColor.customDarkSkyblue);
	            			projectBtnArray.get(i).setClickBackground(ProjColor.clickedCustomDarkSkyblue);
	            			projectBtnArray.get(i).removeMouseListener(deleteProjectListeners.get(i));
	            			projectBtnArray.get(i).addMouseListener(entryProjectListeners.get(i));
	            		}
	            	}
            	}
            	else {
            		modifyMode = !modifyMode;
            		for(int i = 0; i < projectBtnArray.size(); i++) {
            			projectBtnArray.get(i).setBackground(ProjColor.customDarkSkyblue);
            			projectBtnArray.get(i).setUnClickBackground(ProjColor.customDarkSkyblue);
            			projectBtnArray.get(i).setClickBackground(ProjColor.clickedCustomDarkSkyblue);
            			projectBtnArray.get(i).removeMouseListener(modifyProjectListeners.get(i));
            			projectBtnArray.get(i).addMouseListener(entryProjectListeners.get(i));
            		}
            		deleteMode = !deleteMode;
            		for(int i = 0; i < numBtn; i++) {
            			projectBtnArray.get(i).setBackground(ProjColor.customLightRed);
            			projectBtnArray.get(i).setUnClickBackground(ProjColor.customLightRed);
            			projectBtnArray.get(i).setClickBackground(ProjColor.clickedCustomLightRed);
            			projectBtnArray.get(i).removeMouseListener(entryProjectListeners.get(i));
            			projectBtnArray.get(i).addMouseListener(deleteProjectListeners.get(i));
            		}
            	}
            }
        });
		
		ProjStyleButton btn6 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "+ new project");
		panel1.add(btn6);
		btn6.setBounds(890, 24, 220, 57);
		btn6.setPreferredSize(new Dimension(220, 57));
		
		btn6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { // 원래 기능 + new
            	if(modifyMode) {
            		modifyMode = !modifyMode;
            		for(int i = 0; i < projectBtnArray.size(); i++) {
            			projectBtnArray.get(i).setBackground(ProjColor.customDarkSkyblue);
            			projectBtnArray.get(i).setUnClickBackground(ProjColor.customDarkSkyblue);
            			projectBtnArray.get(i).setClickBackground(ProjColor.clickedCustomDarkSkyblue);
            			projectBtnArray.get(i).removeMouseListener(modifyProjectListeners.get(i));
            			projectBtnArray.get(i).addMouseListener(entryProjectListeners.get(i));
            		}
            		newCreateProject();
            	}
            	else if (deleteMode) {
            		deleteMode = !deleteMode;
            		for(int i = 0; i < numBtn; i++) {
            			projectBtnArray.get(i).setBackground(ProjColor.customDarkSkyblue);
            			projectBtnArray.get(i).setUnClickBackground(ProjColor.customDarkSkyblue);
            			projectBtnArray.get(i).setClickBackground(ProjColor.clickedCustomDarkSkyblue);
            			projectBtnArray.get(i).removeMouseListener(deleteProjectListeners.get(i));
            			projectBtnArray.get(i).addMouseListener(entryProjectListeners.get(i));
            		}
            		newCreateProject();
            	}
            	else {
            		newCreateProject();
            	}
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
                        
                        for (int i = 0; i < projectsArray.length(); i++) {
                            JSONObject projectObject = projectsArray.getJSONObject(projectsArray.length() -i -1);
                            long projectId = projectObject.getLong("projectId");
                            String title = projectObject.getString("title");
                            String description = projectObject.getString("description");
                            
                            Object[] array = {projectId, title, description};

                            projectList.add(array);

                        }
                        
                        paintProjectList();
                        
                        
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
			addNewProjectButton();
		}
	}
	
	public void addProjectList(String title, String description) {
		//Object[] array = {projectId, title, description};
		//projectList.add(array);
	}
	
	public void addNewProjectButton() {
		String title = (String)projectList.get(numBtn)[1];
		
		for(int i = 0; i < projectBtnArray.size(); i++) {
			Rectangle bounds = projectBtnArray.get(i).getBounds();
			int x = bounds.x;
            int y = bounds.y;
            projectBtnArray.get(i).setBounds(x, y + 60, 1000, 40);
		}
		
		ProjStyleButton tempbtn = new ProjStyleButton(ProjColor.customDarkSkyblue, ProjColor.clickedCustomDarkSkyblue, Color.BLACK, title) {
			@Override
		    public void paintComponent(Graphics g) {
		        Graphics2D g2 = (Graphics2D) g;
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		        Dimension dimension = getPreferredSize();
		        int w = (int) dimension.getWidth();
		        int h = (int) dimension.getHeight();

		        g2.setColor(getBackground());
		        g2.fillRoundRect(0, 0, w, h, 30, 30);

		        g2.setColor(getForeground());

		        FontMetrics fontMetrics = g2.getFontMetrics();
		        Rectangle rectangle = fontMetrics.getStringBounds(getText(), g2).getBounds();

		        g2.drawString(getText(), (w - rectangle.width) / 2, (h - rectangle.height) / 2 + fontMetrics.getAscent());
		    }
		};
		tempbtn.setActionCommand(String.valueOf(numBtn));
	    tempbtn.setBounds(31, 15, 1000, 40); // 크기 조정
	    tempbtn.setPreferredSize(new Dimension(1000, 40));
	    
	    projectBtnArray.add(tempbtn);
	    btnPanel.add(tempbtn);
	    
	    entryProjectListeners.add(createProjectBtnMouseAdapter(title));
	    modifyProjectListeners.add(modifyProjectBtnMouseAdapter());
	    deleteProjectListeners.add(deleteProjectBtnMouseAdapter());
	    projectBtnArray.get(numBtn).addMouseListener(entryProjectListeners.get(numBtn));

	    btnPanel.revalidate();
	    btnPanel.setPreferredSize(new Dimension(1000, 70 + 60 * numBtn));
		numBtn++;
		scr.revalidate();
		scr.repaint();
	    
	}
	
	public void newCreateProject() {
		new CreateProjectDialog(this);
	}
	
	public MouseListener createProjectBtnMouseAdapter(String title) {
		MouseListener Listener = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JButton sourceButton = (JButton) e.getSource();
				long projId = (long) projectList.get(Integer.parseInt(sourceButton.getActionCommand()))[0];
				accessProject(projId, title);
			}
		};
		return Listener;
	}
	
	public MouseListener modifyProjectBtnMouseAdapter() {
		MouseListener Listener = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JButton sourceButton = (JButton) e.getSource();
				long projectId = (long) projectList.get(Integer.parseInt(sourceButton.getActionCommand()))[0];
				modifyProject(projectId);
			}
		};
		return Listener;
	}
	
	public MouseListener deleteProjectBtnMouseAdapter() {
		MouseListener Listener = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JButton sourceButton = (JButton) e.getSource();
				long projectId = (long) projectList.get(Integer.parseInt(sourceButton.getActionCommand()))[0];//////////
				deleteProject(projectId);
			}
		};
		return Listener;
	}
	
	public void accessProject(long projectId, String title) {
		//new ProjectFrame(title, this);
		
		//통신 요청으로 프로젝트 접속할때 userId 확인해서 접근 권한 있는지 체크. 일단은 임시로 느낌만 만듬.
		/*
		if(!accessibleId.contains(userId)) {
			JOptionPane.showMessageDialog(MainFrame.this, "NOT accessible", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			new ProjectFrame(projId, title, this);
			setVisible(false);
		}
		*/
		new ProjectFrame(projectId,userId, title, this);
		setVisible(false);
	}
	
	public void modifyProject(long projectId) {
		new ModifyProjectDialog(projectId, this);
	}
	
	public void deleteProject(long projectId) {
		
		int yesOrNo = JOptionPane.showConfirmDialog(this, "Do you want to proceed?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        // 응답 처리
        if (yesOrNo == JOptionPane.YES_OPTION) {
        	String url = String.format("http://localhost:8080/projects/%d?userId=%d", projectId, userId);
    		try {
    			String response = RestClient_Delete.sendDeleteRequest(url);
    			
    			JSONObject jsonResponse = new JSONObject(response);
                boolean isSuccess = jsonResponse.getBoolean("isSuccess");
                String code = jsonResponse.getString("code");
                String message = jsonResponse.getString("message");
                
                if (isSuccess && "PROJECT_2000".equals(code)) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Project Deleted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // 삭제 성공 시 리스트에서 제거하고 UI 업데이트
                    int index = 0;
                    for(int i = 0; i < projectList.size(); i++) {
                    	if ((long)projectList.get(i)[0] == projectId) {
                    		index = i;
                    	}
                    }
                    removeButton(index);

                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "Project Deletion Failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                }        
    		} catch (Exception e) {
    			JOptionPane.showMessageDialog(MainFrame.this, "Project Deletion Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    		}
        }
	}
	
	public void removeButton(int index) {
		projectList.remove(index);
		btnPanel.remove(projectBtnArray.get(index));
		projectBtnArray.remove(index);
		entryProjectListeners.remove(index);
		modifyProjectListeners.remove(index);
		deleteProjectListeners.remove(index);
		numBtn--;
		
		for(int i = index-1; i >= 0; i--) {
			Rectangle bounds = projectBtnArray.get(i).getBounds();
			int x = bounds.x;
            int y = bounds.y;
            projectBtnArray.get(i).setBounds(x, y - 60, 1000, 40);
		}
		for(int i = index; i < numBtn; i++) {
			projectBtnArray.get(i).setActionCommand(String.valueOf(i));
		}
		
		btnPanel.revalidate();
		btnPanel.setPreferredSize(new Dimension(1000, 90 + 60 * numBtn));
		scr.revalidate();
		scr.repaint();
		
	}
	
	public void addProjectArrayList(Object[] objects) {
		projectList.add(objects);
	}
	//프로젝트 삭제기능
	/*
	public void deleteProject(long projectId, int index) {
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String url = String.format("http://localhost:8080/projects/%d?userId=%d", projectId, userId);
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
                        projectBtnArray.clear();
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
    */
	
	public void modifyButtonTitle(long projectId, String title) {
		for(int i = 0; i < projectList.size(); i++) {
			if ((long)projectList.get(i)[0] == projectId) {
				projectBtnArray.get(i).setText(title);
			}
		}
	}
	
	public long getUserId() {
		return userId;
	}
		
}


