package frame;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleScrollBar;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class MainFrame extends JFrame {
	private ArrayList<Object[]> projectList = new ArrayList<>();
	private ArrayList<ProjStyleButton> btnArray = new ArrayList<>();
	private JPanel btnPanel;
	private JScrollPane scr;
	private int numBtn = 0;
	
	private long userId; // 임시, 나중에는 로그인하면 특정 static 변수에 저장되어서 통신 요청에 쓰여야 함. 지금은 임시로, 모든 프로젝트 접근 등 가능.
	private ArrayList<Long> accessibleId; // 이것도 임시.
	private long projectId; // 이것도 임시. 
	
	//생성자
	public MainFrame() {
		
		//임시
		accessibleId = new ArrayList<>();
		userId = 20201708;
		projectId = 12345678;
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
	
	
	public void addProjectList(String title, String description) {
		Object[] array = {title, description, projectId};
		projectList.add(array);
		
	}
	
	public void repaintButtonPanel() {
		String title = (String)projectList.get(numBtn)[0];
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
				long projId = (long) projectList.get(Integer.parseInt(sourceButton.getActionCommand()))[2];
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
}


