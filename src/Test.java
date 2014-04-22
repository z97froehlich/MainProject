import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Test extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;
    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    public boolean running = true;
    public int[] pixels;
    public BufferedImage img;
    public static JFrame frame;
    public static int count= 0;
    private Thread thread;
    public static long time = System.currentTimeMillis();

    public static void main(String[] arg) {
        Test wind = new Test();
        frame = new JFrame("WINDOW");
        frame.add(wind);
        frame.setVisible(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wind.init();
    }

    public void init() {
        thread = new Thread(this);
        thread.start();
        img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    }

    public void run() {
        while (running) {
            render();
           /* try {
                thread.sleep(55);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        drawRect(0, 0, 150, 150);
        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
        System.out.println("printing");
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

    private void drawRect(int x, int y, int w, int h) {
    	int col = (int) (346346*Math.random());
        for (int i = x; i < w; i++) {
            for (int j = x; j < h; j++) {
                pixels[i + j * WIDTH] = col;
            }
        }


    }
}
