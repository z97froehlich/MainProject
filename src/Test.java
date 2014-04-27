import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import sun.audio.*;

import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class Test extends Canvas implements Runnable 
{

    private static final long serialVersionUID = 1L;
    public static int WIDTH = 960;
    public static int HEIGHT = 960;
    public boolean running = true;
    static Keyboard key = new Keyboard();
    public boolean state = true;
    
    Map<Integer,String> questions = new TreeMap<Integer,String>();
    public int[] pixels;
    public int[] background = load("field.png");
    public int[] enter = load("Enter.png");
    public int[] player0 = load("player0.png");
    public int[] player1 = load("player1.png");
    public int[] player3 = load("player3.png");
    public int[] question = load("Notecard.png");
    public int[] baseball = load("baseball.png");
    public int[] second = load("Background.png");
    
    ArrayList<Integer> names = new ArrayList<Integer>();
    String answers = "";
    
    int missed = 0;
    int strikes = 0;
    int balls = 0;
    int outs = 0;
    int fouls = 0;
    long timeAnswer = System.currentTimeMillis();
    int inning = 0;
    int runs = 0;
    public int pos = 960;
    
    public BufferedImage img;
    public static JFrame frame;
    public static int count= 0;
    private Thread thread;
    public static long time = System.currentTimeMillis();

    public static void main(String[] arg) 
    {
        MainScreen scr = new MainScreen();
    }

    public void init() 
    {
    	Test wind = new Test();
    	frame = new JFrame("WINDOW");
        frame.add(wind);
        frame.add(this);
        frame.setVisible(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.addKeyListener(key);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        thread = new Thread(this);
        thread.start();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setAutoRequestFocus(true);
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    }

    public void run() 
    {
    	long time = System.currentTimeMillis();
    	
    	InputStream in;
    	AudioStream as = null;
		try
			{
				in = new FileInputStream("Take Me out to the Ball Game.wav");
				as = new AudioStream(in);
				AudioPlayer.player.start(as);
			}
		catch (IOException e1)
			{
				e1.printStackTrace();
			}
    	
    	
        while (running) 
        {
        	if(System.currentTimeMillis()-(time + 10) >= 0)
        		{
        			time = System.currentTimeMillis();
        			if(update() == 'E' && state)
        			{
        			state = false;
        			AudioPlayer.player.stop(as);
        			pos = 900;
        			if(names.size()==0 || outs == 3)
        			{
        				names = new ArrayList<Integer>();
            			for(int i = 0; i< 10; i++)
            			{
            				names.add(i);
            			}
            			try 
            			{
            				inning++;
    						Scanner scan = new Scanner(new File("Natural Questions/"+inning+"/Answers.txt"));
    						answers = scan.next();
    					}
            			
            			catch (FileNotFoundException e) 
            			{
    						e.printStackTrace();
    					}
        			}
        			
        			
        		}
        		}
        	render();
        }     
    }

    public void render() 
    {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) 
        {
            createBufferStrategy(3);
            return;
        }        
        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
        count++;
        frame.setTitle("                                                                                                        OUTS: " + outs + " STRIKES: " + strikes + " INNING: " + inning + " RUNS: " + runs);
        
        g.dispose();
        bs.show();

    }

	private void drawPicture(int x, int y, int w, int h, int[] pix)
	{
		for(int c = 0; c < w; c++)
			{
				for(int r = 0; r < h; r++)
					{
					if(pix[c+r*w] != -65281)
						pixels[c+x+(r+y)*WIDTH] = pix[c+r*w];
					}
			}
	}

    private void draw()
		{
    	if(state)
    	{
    		drawPicture(0, 0, 960,960, background);
    		//drawDots(bases covered)
    		if(System.currentTimeMillis()%1000 >= 500)
			drawPicture(290, 300, 400, 80, enter);
    	}
    	
    	else
    	{
    		int random = (int) (Math.random()*names.size());
    		char a = answers.charAt(random);
    		answers = answers.substring(0, random) + answers.substring(random+1);
    		System.out.println("Natural Questions/"+inning+"/"+names.get(random)+".png");
    		int[] question = load("Natural Questions/"+inning+"/"+names.get(random)+".png");
    		
    		names.remove(names.get(random));
    		System.out.println(Arrays.toString(names.toArray()));
    		
    		drawPicture(0,0,960,960, second);
    		drawPicture(0,510,256,256, player0);
    		drawPicture(0,100,960,200, question);

    		long time = System.currentTimeMillis();
    		while(pos>100)
    		{
    			
    			drawPicture(0,0,960,960, second);
    			drawPicture(0,100,960,200, question);
    			drawPicture(0,510,256,256, player0);
    			drawPicture(pos,692,28,28,baseball);
    			if(System.currentTimeMillis()-500>time)
    			{
    				try
        				{
        					InputStream in = new FileInputStream("nes-11-08.wav");
        					AudioStream as = new AudioStream(in);
        					AudioPlayer.player.start(as);
        				}
        			catch (IOException e1)
        				{
        					e1.printStackTrace();
        				}
    				pos-=50;
    				time = System.currentTimeMillis();
    			}
    			render();
    			char c = key.update();
    			if(c==a)
    			{
    				System.out.println("got it");
    				hit(true);
    				state = true;
    				break;
    			}
    			else if(c!='E' && c!=' ')
    			{
    				System.out.println(c);
    				hit(false);
    				state = true;
    				break;
    			}
    		}
    		state = true;
    	}
	}
    
	private void hit(boolean answer)
		{
			try 
				{
					Thread.sleep(500);
					drawPicture(0,0,960,960, second);
					drawPicture(pos-50,692,28,28,baseball);
					drawPicture(0,510,256,256,player1);
					render();
					try
	    				{
	    					InputStream in = new FileInputStream("nes-10-10.wav");
	    					AudioStream as = new AudioStream(in);
	    					AudioPlayer.player.start(as);
	    				}
	    			catch (IOException e1)
	    				{
	    					e1.printStackTrace();
	    				}
					Thread.sleep(500);
					drawPicture(0,0,960,960, second);
					drawPicture(pos-100,692,28,28,baseball);
					drawPicture(0,510,256,256,player3);
					render();
					try
	    				{
	    					InputStream in = new FileInputStream("nes-10-10.wav");
	    					AudioStream as = new AudioStream(in);
	    					AudioPlayer.player.start(as);
	    				}
	    			catch (IOException e1)
	    				{
	    					e1.printStackTrace();
	    				}
					System.out.println("working");
					Thread.sleep(500);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			if(answer)
				{
					try
						{
							drawPicture(0,0,960,960, second);
							drawPicture(0,510,256,256, player0);
							drawPicture(100,100,400,80,enter);
							Thread.sleep(1000);
						}
					catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					
					pos = 960;
					
					while(pos > 50)
						{
							drawPicture(0,0,960,960, second);
							drawPicture(100,100,400,80,enter);
			    			drawPicture(0,510,256,256, player0);
			    			drawPicture(pos,692,28,28,baseball);
			    			if(System.currentTimeMillis()-40>time)
			    			{
			    				int i = (int) (Math.random()*20) -10;
			    				pos-=50 + i;
			    				time = System.currentTimeMillis();
			    				try
			        				{
			        					InputStream in = new FileInputStream("nes-11-08.wav");
			        					AudioStream as = new AudioStream(in);
			        					AudioPlayer.player.start(as);
			        				}
			        			catch (IOException e1)
			        				{
			        					e1.printStackTrace();
			        				}
			    			}
			    			render();
			    			System.out.println(pos);
			    			char c = key.update();
			    			if(c=='E')
			    			{
			    				if(pos <= 360 && pos >= 291)
			    					{
			    						slow(pos);
			    						strikes = 0;
			    						System.out.println("single " + pos);
			    						break;
			    					}
			    				else if(pos <=290 && pos >=250)
			    					{
			    						slow(pos);
			    						System.out.println("home " + pos);
			    						strikes = 0;
			    						break;
			    					}
			    				else if(pos <= 249 && pos >= 184)
			    					{
			    						slow(pos);
			    						System.out.println("double " + pos);
			    						strikes = 0;
			    						break;
			    					}
			    				else if(pos <= 248 && pos >=0 || pos <= 960 && pos >= 361)
			    					{
			    						slow(pos);
			    						System.out.println("strike " + pos);
			    						strikes++;
			    						if(strikes == 3)
			    							{
			    								outs++;
			    								strikes = 0;
			    								break;
			    							}
			    						pos = 960;
			    						try
											{
												Thread.sleep(1000);
											}
										catch (InterruptedException e)
											{
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
			    					}
			    				
			    			}
			    			
						}
				}
			else
				{
					outs++;
					missed++;
					if(missed > 20)
						{
							//harriet kills
							//load lose screen with sound
						}
					//incriment outs
					//if number missed > 20 harriet kills roy
				}
			
		}

	private void slow(int pos2)
		{
			try
				{
					Thread.sleep(500);
				}
			catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			drawPicture(0,0,960,960, second);
			drawPicture(pos-50,692,28,28,baseball);
			drawPicture(0,510,256,256,player1);
			render();
			try
				{
					InputStream in = new FileInputStream("nes-10-10.wav");
					AudioStream as = new AudioStream(in);
					AudioPlayer.player.start(as);
				}
			catch (IOException e1)
				{
					e1.printStackTrace();
				}
			try
				{
					Thread.sleep(500);
				}
			catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			drawPicture(0,0,960,960, second);
			drawPicture(pos-100,692,28,28,baseball);
			drawPicture(0,510,256,256,player3);
			render();
			try
				{
					InputStream in = new FileInputStream("nes-10-10.wav");
					AudioStream as = new AudioStream(in);
					AudioPlayer.player.start(as);
				}
			catch (IOException e1)
				{
					e1.printStackTrace();
				}
			System.out.println("working");
			try
				{
					Thread.sleep(500);
				}
			catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			
		}

	private char update()
		{
			draw();
			key.update();
			if(key.A)
			return 'A';
			if(key.D)
			return 'D';
			if(key.S)
			return 'S';
			if(key.F)
			return 'F';
			if(key.enter)
			return 'E';
			return ' ';
		}
    
    private int[] load(String path)
    	{
    		int[] pix;
    		try
    			{
    				BufferedImage image = ImageIO.read(new File(path));
    				int w = image.getWidth();
    				
    				int h = image.getHeight();
    				System.out.println(w + " " + h);
    				pix = new int[w*h];
    				image.getRGB(0, 0, w, h, pix, 0, w);
    				System.out.println(pix[0] + " " + pix[1]);
    				return pix;
    			}
    			catch(IOException e)
    			{
    				e.printStackTrace();
    			}
    		int[] j = new int[0];
    		return j;
    		
    	}
}
