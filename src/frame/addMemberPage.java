package frame;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleComboBox;
import util.RestClient;
import util.RestClient_Get;

import java.awt.Color;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URLEncoder;
import java.awt.Dimension;
import javax.swing.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

class AddMemberPage extends JDialog {
	private ProjectFrame parentFrame;
    private JTextField tfName;
    private JTextField tfID;
    private JPasswordField pfPassword;
    private ProjStyleComboBox cbUserID;
    private ProjStyleComboBox cbUserRole;
    private ArrayList<Object[]> Member;
    private JPanel panel;

    public AddMemberPage(ProjectFrame parentFrame) {
    	
    	this.parentFrame = parentFrame;
    	Member = new ArrayList<>();
    	
        setTitle("Sign Up");
        setSize(400, 230);
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(ProjColor.customGray);
        
        /*
        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(50, 50, 80, 25);
        panel.add(lblName);

        tfName = new JTextField();
        tfName.setBounds(150, 50, 165, 25);
        tfName.setBackground(ProjColor.customDarkGray);
        tfName.setBorder(null);
        panel.add(tfName);
		*/
        
        JLabel lblID = new JLabel("ID:");
        lblID.setBounds(50, 50, 80, 25);
        panel.add(lblID);
        /*
        tfID = new JTextField();
        tfID.setBounds(150, 50, 165, 25);
        tfID.setBackground(ProjColor.customDarkGray);
        tfID.setBorder(null);
        panel.add(tfID);
        */
        
        readAllMember();
        
        
        /*
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(50, 110, 80, 25);
        panel.add(lblPassword);

        pfPassword = new JPasswordField();
        pfPassword.setBounds(150, 110, 165, 25);
        pfPassword.setBackground(ProjColor.customDarkGray);
        pfPassword.setBorder(null);
        panel.add(pfPassword);
        */
        //제거
        
        JLabel lblUserRole = new JLabel("User Role:");
        lblUserRole.setBounds(50, 90, 80, 25);
        panel.add(lblUserRole);

        cbUserRole = new ProjStyleComboBox(new String[]{"PL", "DEV", "TESTER"});
        cbUserRole.setBounds(150, 90, 165, 25);
        panel.add(cbUserRole);
        
        ProjStyleButton btnSubmit = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "Submit");
        btnSubmit.setBounds(180, 130, 100, 30);
        btnSubmit.setPreferredSize(new Dimension(100, 30));
        btnSubmit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
            	String userName = (String) cbUserID.getSelectedItem();
            	String userRole = (String) cbUserRole.getSelectedItem();
                parentFrame.addProjectMember(userName, userRole);
                setVisible(false);
                dispose();
            }
        });
        panel.add(btnSubmit);
        
        add(panel);
        setVisible(true);
    }
	
	public void readAllMember() {

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
            	
                String urlString = "http://localhost:8080/users";

                return RestClient_Get.sendGetRequest(urlString);
            	
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
                        
                        for (int i = 0; i < projectsArray.length(); i++) {
                            JSONObject projectObject = projectsArray.getJSONObject(i);
                            long userId = projectObject.getLong("userId");
                            String userName = projectObject.getString("userName");

                            
                            Object[] array = {userId, userName};
                            

                            Member.add(array);

                        }
                        
                        readProjectMember();
                        
                        
                    	JOptionPane.showMessageDialog(AddMemberPage.this, "Project list Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                    } else {
                        // 실패 시 오류 메시지 표시
                        String message = jsonResponse.getString("message");
                        JOptionPane.showMessageDialog(AddMemberPage.this, "Searching failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(AddMemberPage.this, "Searching Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
	}
	
	private void readProjectMember() {
		new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
            	
            	String encodedUserName = URLEncoder.encode(Long.toString(parentFrame.getProjectId()), "UTF-8");
                String urlString = "http://localhost:8080/projects/participants/";

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
                    
                    if (isSuccess && "PROJECT_2000".equals(code)) {
                    	JSONObject resultObject = jsonResponse.getJSONObject("result");
                        JSONArray projectsArray = resultObject.getJSONArray("users");
                        
                        Object[] objects = new Object[projectsArray.length()];

                        for (int i = 0; i < projectsArray.length(); i++) {
                            JSONObject projectObject = projectsArray.getJSONObject(i);
                            long userId = projectObject.getLong("userId");
                            String userName = projectObject.getString("userName");
                            
                            for(int j = 0; j < Member.size(); j++) {
                            	if((long)Member.get(j)[0] == userId) {
                            		Member.remove(j);
                            	}
                            }

                        }
                        
                        CreateComboBox();
                        
                    	JOptionPane.showMessageDialog(AddMemberPage.this, "Project list Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                    } else {
                        // 실패 시 오류 메시지 표시
                        String message = jsonResponse.getString("message");
                        JOptionPane.showMessageDialog(AddMemberPage.this, "Searching failed: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(AddMemberPage.this, "Searching Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
	}
	
	public void CreateComboBox() {
		String[] ComboBoxUserId = new String[Member.size()];
        for(int i = 0; i < Member.size(); i++) {
        	ComboBoxUserId[i] = (String)Member.get(i)[1];
        }
        
        cbUserID= new ProjStyleComboBox(ComboBoxUserId);
        cbUserID.setBounds(150, 50, 165, 25);
        panel.add(cbUserID);
        
        repaint();
	}
}	
