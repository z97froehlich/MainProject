import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Keyboard implements KeyListener
{
	
	private static boolean[] keys  = new boolean[65536];
	public boolean A, S, D, F, escape, enter;
	
	public char update()
	{
		A = keys[KeyEvent.VK_A];
		S = keys[KeyEvent.VK_S];
		D = keys[KeyEvent.VK_D];
		F = keys[KeyEvent.VK_F];
		escape = keys[KeyEvent.VK_ESCAPE];
		enter = keys[KeyEvent.VK_ENTER];
		if(A)
			return 'A';
		if(S)
			return 'S';
		if(D)
			return 'D';
		if(F)
			return 'F';
		if(escape)
			return 'Q';
		if(enter)
			return 'E';
		return ' ';
	}
	
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;		
	}
	
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
	}
	
	public void keyTyped(KeyEvent e)
	{
		
	}
	
}