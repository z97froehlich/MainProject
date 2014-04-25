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

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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
    ArrayList<Integer> names = new ArrayList<Integer>();
    String answers = "";
    
    int strikes = 0;
    int balls = 0;
    int outs = 0;
    int fouls = 0;
    long timeAnswer = System.currentTimeMillis();
    int inning = 1;
    public int pos = 960;
    
    public BufferedImage img;
    public static JFrame frame;
    public static int count= 0;
    private Thread thread;
    public static long time = System.currentTimeMillis();

    public static void main(String[] arg) 
    {
        Test wind = new Test();
        frame = new JFrame("WINDOW");
        frame.add(wind);
        frame.setVisible(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.addKeyListener(key);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wind.init();
    }

    public void init() 
    {
        thread = new Thread(this);
        thread.start();
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    }

    public void run() 
    {
    	long time = System.currentTimeMillis();
    	//
        while (running) 
        {
        	// unlimits the FPS but keeps the updates within 60 per second so no fast motion happens
        	if(System.currentTimeMillis()-(time + 10) >= 0)
        		{
        			time = System.currentTimeMillis();
        			if(update() == 'E' && state)
        			{
        			state = false;
        			pos = 900;
        			if(names.size()==0)
        			{
        				names = new ArrayList<Integer>();
            			for(int i = 0; i< 10; i++)
            			{
            				names.add(i);
            			}
            			try 
            			{
    						Scanner scan = new Scanner(new File("Natural Questions/"+inning+"/Answers.txt"));
    						answers = scan.next();
    					} 
            			catch (FileNotFoundException e) 
            			{
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
        			}
        			
        			
        		}
        		}
        	//System.out.println("render");
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
        //draws stuff to Screen
        
        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
        count++;
        if(count == 100)
        	{
        		double disp = System.currentTimeMillis()-time;
        		disp/=1000.0;
        		disp = 1/disp;
        		disp*=100;
        		frame.setTitle("Frames: " + disp);
                time = System.currentTimeMillis();
                count = 0;
        	}
        
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
			// all the calls for drawing
    	if(state)
    	{
    		drawPicture(0, 0, 960,960, background);
    		//drawDots(bases covered)
			drawPicture(300, 500, 200, 40, enter);
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
    		
    		drawPicture(0,600,256,256, player0);
    		drawPicture(0,100,960,200, question);
    		//drawPicture(pitcher);
    		//drawPicture(background);
    		
    		//drawPicture(answers);
    		//drawPicture(pitcher1);
    		//drawPicture(ball,count);
    		long time = System.currentTimeMillis();
    		while(pos>100)
    		{
    			//drawPicture(background);
    			drawPicture(pos,800,28,28,baseball);
    			if(System.currentTimeMillis()-500>time)
    			{
    				pos-=50;
    				time = System.currentTimeMillis();
    			}
    			render();
    			char c = key.update();
    			if(c==a)
    			{
    				System.out.println("got it");
    				try 
    				{
						Thread.sleep(2000);
						drawPicture(pos-50,800,28,28,baseball);
						drawPicture(0,600,256,256,player1);
						render();
						System.out.println("working");
						Thread.sleep(2000);
						drawPicture(pos-100,800,28,28,baseball);
						drawPicture(0,600,256,256,player3);
						render();
						System.out.println("working");
					} 
    				catch (InterruptedException e) 
    				{
						e.printStackTrace();
					}
    			}
    			else if(c!='E' && c!=' ')
    			{
    				System.out.println(c);
    			}
    		}
    		state = true;
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
