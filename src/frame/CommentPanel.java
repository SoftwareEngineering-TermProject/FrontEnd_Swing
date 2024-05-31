package frame;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import style.ProjColor;
import style.ProjStyleButton;
import style.ProjStyleScrollBar;

import util.RestClient;
import util.RestClient_Get;

public class CommentPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel insetPanel;
	private long userId;
	private long issueId;
	private String userName;
	private ArrayList<Object[]> commentList;
	private JPanel textBoxPanel;
	private int textAreaCount;
	private GridBagConstraints gbc;
	private JScrollPane scr;

	public CommentPanel(int width, int height, long userId, long issueId) {
		
		this.userId = userId;
		this.issueId = issueId;
		userName = getUserNameByUserId(userId);
		textAreaCount = 0;
		commentList = new ArrayList<>();
		
		setSize(width, height);
        setLayout(new BorderLayout());
        setBackground(ProjColor.customCommentBackgroundSkyblue);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(ProjColor.customCommentBackgroundSkyblue);
        JLabel titleLbl = new JLabel("Comment");
        titleLbl.setFont(new Font(null, Font.PLAIN, 20));
        titlePanel.add(titleLbl);
        add(titlePanel, BorderLayout.NORTH);

        textBoxPanel = new JPanel(new GridBagLayout());
        textBoxPanel.setBackground(ProjColor.customCommentBackgroundSkyblue);
        
        // 셀마다 다른 가중치와 크기를 가진 GridBagConstraints 생성
        gbc = new GridBagConstraints();
        
        scr = new JScrollPane(textBoxPanel);
        scr.setBackground(ProjColor.customCommentBackgroundSkyblue);
        scr.setBorder(null);
        scr.setVerticalScrollBar(new ProjStyleScrollBar(ProjColor.customCommentBarSkyblue, ProjColor.customCommentBackgroundSkyblue));
        scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scr, BorderLayout.CENTER);
        
        JPanel sendTextPanel = new JPanel();
        sendTextPanel.setBackground(ProjColor.customSendButtonYellow);
        sendTextPanel.setLayout(new BorderLayout());
        
        JTextArea sendTa = new JTextArea();
        sendTa.setLineWrap(true);
        sendTa.setWrapStyleWord(true);
        sendTa.setFont(new Font(null, Font.PLAIN, 30));
        sendTextPanel.add(sendTa, BorderLayout.CENTER);
        
        sendTa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	String text = sendTa.getText();
                    if(!text.trim().equals("")) {
                    	addComment(text);
        				sendTa.setText("");
        				
        				getComment(commentList);
                    }
        			e.consume(); // enter 다른 동작 방지
                }
            }
        });
        
        
        // 텍스트 영역 추가를 위한 버튼 생성
        ProjStyleButton addButton = new ProjStyleButton(ProjColor.customSendButtonYellow, ProjColor.clickedCustomSendButtonYellow, Color.BLACK, "send");
        
        sendTextPanel.add(addButton, BorderLayout.EAST);
        
        add(sendTextPanel, BorderLayout.SOUTH);
        
        insetPanel = new JPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 100.0;
        insetPanel.setBackground(ProjColor.customCommentBackgroundSkyblue);
        textBoxPanel.add(insetPanel, gbc);
        
        addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String text = sendTa.getText();
                if(!text.trim().equals("")) {
                	addComment(text);
    				sendTa.setText("");
    				
    				getComment(commentList);
                }
			}
        });
         
	}
	
	public void addComment(String text) {
		try {
			String encodedUserId = URLEncoder.encode(Long.toString(userId), "UTF-8");
			String jsonInputString = String.format("{\"issueId\":\"%s\", \"content\":\"%s\"}", issueId, text);
			String result = RestClient.sendPostRequest("http://localhost:8080/comments/?userId=" + encodedUserId, jsonInputString);
			
			JSONObject jsonResponse = new JSONObject(result);
			boolean isSuccess = jsonResponse.getBoolean("isSuccess");
	        String code = jsonResponse.getString("code");
	        
	        if (isSuccess && "COMMENT_4000".equals(code)) {
	        	JSONObject resultObject = jsonResponse.getJSONObject("result");
	        	long commentId = resultObject.getLong("commentId");
	        	String date = resultObject.getString("createAt").split("T")[0] + "   " + resultObject.getString("createAt").split("T")[1].split("\\.")[0];
	        	
	        	Object[] array = {commentId, userName, date, text};
	        	commentList.add(0, array);
	        }
	        else {
	            String message = jsonResponse.getString("message");
	            JOptionPane.showMessageDialog(CommentPanel.this, "코멘트 생성 실패: " + message, "Error", JOptionPane.ERROR_MESSAGE);
	        }
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(CommentPanel.this, "코멘트 생성 실패: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void getComment(ArrayList<Object[]> commentList) {
		
		// 오류나면 그냥 if문 지우자.
		if(this.commentList.size() == commentList.size()) { // 이전값과 차이가 있는가?
			for (int k = 0; k < commentList.size(); k++) {
				if(!Arrays.equals(this.commentList.get(k), commentList.get(k))) {
					return;
				};
			}
		}
		this.commentList = commentList;
		textBoxPanel.removeAll();
		textAreaCount = 0;

		for(int i = commentList.size() - 1; i >= 0; i--) {
			String writer = (String)commentList.get(i)[1];
			String date = (String)commentList.get(i)[2];
			String comment = (String)commentList.get(i)[3];
			
			JPanel textAreaAndTitlePanel = new JPanel();
			textAreaAndTitlePanel.setBackground(ProjColor.customCommentBackgroundSkyblue);
			textAreaAndTitlePanel.setLayout(new BorderLayout());
			textAreaAndTitlePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	        
	        JLabel commentName = new JLabel();
	        commentName.setText(writer + "   " + date);
	        textAreaAndTitlePanel.add(commentName, BorderLayout.NORTH);
	        
	        JPanel textAreaPanel = new JPanel() {
	            /**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				private int arc = 10; // 둥근 테두리의 반지름
	
	            @Override
	            protected void paintComponent(Graphics g) {
	                super.paintComponent(g);
	                Graphics2D g2d = (Graphics2D) g.create();
	                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	                g2d.setPaint(Color.WHITE);
	                g2d.setColor(Color.WHITE);
	                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
	                g2d.dispose();
	            }
	        };
	        textAreaPanel.setBackground(ProjColor.customCommentBackgroundSkyblue);
	        textAreaPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	        textAreaPanel.setLayout(new BorderLayout());
	        
	        JTextArea ta = new JTextArea(comment);
	        ta.setLineWrap(true);
	        ta.setWrapStyleWord(true);
	        ta.setBackground(Color.WHITE);
	        ta.setFont(new Font(null, Font.PLAIN, 15));
	        ta.setEditable(false);
	        gbc.gridx = 0;
	        gbc.gridy = textAreaCount;
	        gbc.weightx = 1.0;
	        gbc.weighty = 1.0;
	        gbc.fill = GridBagConstraints.BOTH;
	        gbc.insets = new Insets(10, 10, 10, 10);
	        
	        textAreaPanel.add(ta, BorderLayout.CENTER);
	        
	        textAreaAndTitlePanel.add(textAreaPanel, BorderLayout.CENTER);
	        
	        textBoxPanel.add(textAreaAndTitlePanel, gbc);
	        
	        textAreaCount++;
		}
		
        insetPanel = new JPanel();
        gbc.gridx = 0;
        gbc.gridy = textAreaCount;
        gbc.weightx = 1.0;
        gbc.weighty = 100.0;
        insetPanel.setBackground(ProjColor.customCommentBackgroundSkyblue);
        textBoxPanel.add(insetPanel, gbc);
        
        textBoxPanel.revalidate();
        textBoxPanel.repaint();
        
        if (scr.getVerticalScrollBar().isVisible()) {

            JScrollBar verticalScrollBar = scr.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum()); // 스크롤바를 제일 아래로 내림
        }
	}
	
	private String getUserNameByUserId(long userId) {
        String url = String.format("http://localhost:8080/users");

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

