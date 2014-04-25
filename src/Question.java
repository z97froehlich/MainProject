import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Question
{
	String text = "";
	String a = "";
	String s = "";
	String d = "";
	String f = "";
	String answer = "";
	public Question(String path) throws IOException 
	{
		Scanner scan = new Scanner(new File(path));
		text = scan.nextLine();
		a = scan.nextLine();
		s = scan.nextLine();
		d = scan.nextLine();
		f = scan.nextLine();
		answer = scan.next();
	}
}
