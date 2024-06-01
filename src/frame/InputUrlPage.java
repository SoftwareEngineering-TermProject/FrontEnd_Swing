package frame;

import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import style.ProjColor;
import style.ProjStyleButton;

public class InputUrlPage extends JFrame{
	
	private static String url;

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InputUrlPage() {
		setTitle("connect URL");
		setSize(570, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBackground(ProjColor.customGray);
		
		JLabel logInTitleLabel = new JLabel("Trouble Ticket System");
		logInTitleLabel.setFont(new Font(null, Font.PLAIN, 50));
		logInTitleLabel.setBounds(20, 1, 550, 100);
		mainPanel.add(logInTitleLabel);
		
		JLabel urlLabel = new JLabel("URL : ");
		urlLabel.setFont(new Font(null, Font.BOLD, 15));
		urlLabel.setBounds(100, 70, 500, 100);
		mainPanel.add(urlLabel);
		
		JTextField urlTextField = new JTextField();
		urlTextField.setBackground(ProjColor.customWhiteGray);
		urlTextField.setBorder(null);
		urlTextField.setBounds(160, 113, 100, 25);	
		mainPanel.add(urlTextField);

		ProjStyleButton connectButton = new ProjStyleButton(ProjColor.customDarkGray, ProjColor.clickedCustomDarkGray, Color.BLACK, "connect");
		connectButton.setBounds(280, 113, 100, 30);
		connectButton.setPreferredSize(new Dimension(100, 25));
		connectButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				connect(urlTextField.getText().trim());
				setVisible(false);
				dispose();
				new LogInFrame();
			}
		});
		mainPanel.add(connectButton);

		add(mainPanel);

		setVisible(true);
		
	}
    
    private void connect(String url) {
        
        if(url.equals("")) {
        	url = "http://localhost:8080/";
        }
        
        InputUrlPage.url = url;
    }
    
    public static String getUrl() {
    	return InputUrlPage.url;
    }
	
	public static void main(String[] args) {
		
		new InputUrlPage();
		
	}
}
