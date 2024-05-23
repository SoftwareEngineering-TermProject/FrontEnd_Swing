package frame;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleTable;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProjectFrame extends JFrame{
	
	private DefaultTableModel model;
	private ArrayList<Object[]> issueList;
	private int issueNum;
	private ArrayList<Object[]> projectMember;
	
	public ProjectFrame(long projId, String title, MainFrame parentFrame) {
		
		issueList = new ArrayList<>();
		issueNum = 0;
		projectMember = new ArrayList<>();
		
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
		
		ProjStyleButton btn2 = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "+ add member");
		panel1.add(btn2);
		btn2.setBounds(285, 100, 220, 50);
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
        
		for (int i = 1; i <= 20; i++) { // 데이터 받아와서 동적으로 추가
            addIssue("draw picture", "tester1", "dev1", "dev1", "major", "closed", "24-04-30");
            addModel();
        }
        
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
				parentFrame.setVisible(true);
				setVisible(false);
				dispose(); // 현재 프레임만 없애기
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
		new NewIssuePage(this);
	}
	
	public void addProjectMember(String userName, String userRole) { // userName(우리가 생각하는 id)
		Object[] array = {userName, userRole};
		projectMember.add(array);
	}
	
	public void createAddMemberPage() {
		new addMemberPage(this);
	}
	
}
