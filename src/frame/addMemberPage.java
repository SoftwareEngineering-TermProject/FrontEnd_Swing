package frame;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleComboBox;

import util.RestClient_Get;

class AddMemberPage extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProjectFrame parentFrame;
    private ProjStyleComboBox cbUserName;
    private ProjStyleComboBox cbUserRole;
    private ArrayList<Object[]> Member;
    private JPanel panel;
    private String url;

    public AddMemberPage(ProjectFrame parentFrame) {
    	
    	this.parentFrame = parentFrame;
    	Member = new ArrayList<>();
    	url = InputUrlPage.getUrl();
    	
        setTitle("Add Member");
        setSize(400, 230);
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(ProjColor.customGray);
        
        JLabel lblID = new JLabel("ID:");
        lblID.setBounds(50, 50, 80, 25);
        panel.add(lblID);
        
        readAllMember();
        
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
            	
            	String userName = (String) cbUserName.getSelectedItem();
            	String userRole = (String) cbUserRole.getSelectedItem();
            	
            	long addMemberId = 0;
            	for(int i = 0; i < Member.size(); i++) {
            		 if(userName.equals((String)Member.get(i)[1])) {
            			 addMemberId = (long)Member.get(i)[0];
            		 }
            	}

                parentFrame.addProjectMember(addMemberId, userRole);
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
            	
                String urlString = url + "users";

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
            	
            	String encodedProjectId = URLEncoder.encode(Long.toString(parentFrame.getProjectId()), "UTF-8");
                String urlString = url + "projects/participants/" + encodedProjectId;

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
                    
                    if (isSuccess && "PROJECT_2000".equals(code)) {
                    	JSONObject resultObject = jsonResponse.getJSONObject("result");
                        JSONArray usersArray = resultObject.getJSONArray("users");
                        
                        for (int i = 0; i < usersArray.length(); i++) {
                            JSONObject projectObject = usersArray.getJSONObject(i);
                            long userId = projectObject.getLong("userId");
                            
                            for(int j = 0; j < Member.size(); j++) {
                            	if((long)Member.get(j)[0] == userId) {
                            		Member.remove(j);
                            	}
                            }

                        }
                        
                        CreateComboBox();
                        
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
		String[] ComboBoxUserName = new String[Member.size()];
        for(int i = 0; i < Member.size(); i++) {
        	ComboBoxUserName[i] = (String)Member.get(i)[1];
        }
        
        cbUserName= new ProjStyleComboBox(ComboBoxUserName);
        cbUserName.setBounds(150, 50, 165, 25);
        panel.add(cbUserName);
        
        repaint();
	}
}	
