import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
public class Test extends Canvas implements Runnable 
{

    private static final long serialVersionUID = 1L;
    public static int WIDTH = 600;
    public static int HEIGHT = 600;
    public boolean running = true;
    public int[] pixels;
    public int[] background = load("baseball.jpg");
    public int[] enter = load("Press Enter.jpg");
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
        			update();
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
        draw();     
        
        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
        count++;
        if(count == 100)
        	{
        		frame.setTitle("Frames: "+ (1000.0/(System.currentTimeMillis()-time)));
                time = System.currentTimeMillis();
                count = 0;
        	}
        
        g.dispose();
        bs.show();

    }



	private void drawPicture(int x, int y, int w, int h, int[] pix)
	{
		for(int c = 0; c < h; c++)
			{
				for(int r = 0; r < w; r++)
					{
						pixels[r+x * (c+(y*w))] = pix[r+x*w];
					}
			}
	}

    private void draw()
		{
			// all the calls for drawing
			drawPicture(0, 0, 600,600, background);
			drawPicture(100, 100, 200, 40, enter);
			
		}
    
	private void update()
		{
			Keyboard.update();
			//check for state change
			
			
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
