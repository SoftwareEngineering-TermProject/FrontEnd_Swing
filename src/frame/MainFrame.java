package frame;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleScrollBar;
import util.RestClient;

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
	
	private long userId; // 임시, 나중에는 로그인하면 특정 static 변수에 저장되어서 통신 요청에 쓰여야 함. 지금은 임시로, 모든 프로젝트 접근 등 가능.
	private ArrayList<Long> accessibleId; // 이것도 임시.
	//private long projectId; // 이것도 임시. 
	
	//생성자
	public MainFrame() {
		
		readProjectList(""); // 나중에 project에 속한 username 생기면 그걸로 바꾸기. 지금은 일단 모든 project 가져옴.
		
		//임시
		accessibleId = new ArrayList<>();
		userId = 1;
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
	
	public void readProjectList(String userName) {

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
            	// 파라미터를 URL 인코딩
                String encodedUsername = URLEncoder.encode(userName, "UTF-8");

                // URL에 파라미터 추가
                String urlString = "http://localhost:8080/projects";
                URL url = new URL(urlString + encodedUsername); // 
                
                // 연결 설정
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "application/json");

                // 응답 읽기
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 응답 반환
                return response.toString();
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
		ProjStyleButton tempbtn = new ProjStyleButton(ProjColor.customDarkSkyblue, ProjColor.clickedCustomDarkSkyblue, Color.BLACK, title);
		tempbtn.setActionCommand(String.valueOf(numBtn));
		btnArray.add(tempbtn);
		btnArray.get(numBtn).setBounds(31, 35 + 110 * numBtn, 997, 75);
		btnPanel.add(btnArray.get(numBtn));
		btnArray.get(numBtn).setPreferredSize(new Dimension(997, 75));
		btnPanel.setPreferredSize(new Dimension(1000, 120 + 110 * numBtn));
		
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
}


