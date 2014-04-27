

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


public class MainScreen extends JFrame
{
	public MainScreen()
	{
		InputStream in;
    	AudioStream as = null;
		try
			{
				in = new FileInputStream("Beep11.wav");
				as = new AudioStream(in);
				AudioPlayer.player.start(as);
			}
		catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		JFrame screen = new JFrame();
		screen.setDefaultCloseOperation(EXIT_ON_CLOSE);
		screen.setSize(960, 960);
		screen.setResizable(false);
		screen.setUndecorated(true);
		screen.setLocationRelativeTo(null);
		screen.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("Intro.png"));

		lblNewLabel.setBounds(10, 0, 960, 960);
		screen.getContentPane().add(lblNewLabel);
		
		JButton Play = new JButton("New button");
		Play.setBounds(40, 825, 260, 55);
		Play.setContentAreaFilled(false);
		screen.getContentPane().add(Play);
		screen.setVisible(true);
		
		Play.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e){
				setVisible(false);
				dispose();
				Test test = new Test();
				test.init();
				
			}
		});
    }
}