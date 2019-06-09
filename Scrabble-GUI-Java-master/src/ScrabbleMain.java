/*
 * Dean Pakravan : 757389
 * Assignment 2 - Distributed Systems - Sem 2 2018
 * Driver Class for the Scrabble Game
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrabbleMain{
	public static void main(String[] args) {
		
// The Start-up GUI		
//--------------------------------------------------------
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 500, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.getHSBColor(0.394F, 0.86F, 0.3F));
		
	    JLabel textBox = new JLabel("Welcome to Scrabble");
        textBox.setBounds(150, 20, 200, 40);
        textBox.setForeground(Color.getHSBColor(0.505F, 0.81F, 0.94F));
        textBox.setFont(new Font("TimesNewRoman", Font.BOLD, 20));
        frame.add(textBox);
        
        JLabel IPinfo = new JLabel("Enter IP address xxx.xxx.xxx.xxx");
        IPinfo.setBounds(100, 90, 200, 20);
        IPinfo.setForeground(Color.WHITE);
        IPinfo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        frame.add(IPinfo);
        
        JTextField IP = new JTextField();
        IP.setBounds(100, 120, 300, 20);
        IP.setEditable(true);
        frame.add(IP);
        
        JLabel Portinfo = new JLabel("Enter Port number");
        Portinfo.setBounds(100, 220, 200, 20);
        Portinfo.setForeground(Color.WHITE);
        Portinfo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        frame.add(Portinfo);
        
        JTextField PORT = new JTextField();
        PORT.setBounds(100, 250, 300, 20);
        PORT.setEditable(true);
        frame.add(PORT);
        
        JButton hostGameBtn = new JButton("Host Game");
        hostGameBtn.setBounds(50, 400, 100, 20);
        hostGameBtn.setEnabled(false);
        frame.getContentPane().add(hostGameBtn);
        
        JButton joinGameBtn = new JButton("Join Game");
        joinGameBtn.setBounds(350, 400, 100, 20);
        joinGameBtn.setEnabled(false);
        frame.getContentPane().add(joinGameBtn);
        		
		JButton btnNewButton = new JButton("Start Game");
		btnNewButton.setBounds(200, 500, 100, 20);
		frame.getContentPane().add(btnNewButton);
		
		frame.setVisible(true);
//----------------------------------------------------------------------
		
		// Listener for when we start the game
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GUI Window = new GUI();
				Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Window.startUp();
				frame.dispose();
			}
		});
	}
}
