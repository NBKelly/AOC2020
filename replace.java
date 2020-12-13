import java.util.Scanner;
public class replace {
    public static void main(String[] argv) {
	Scanner sc = new Scanner(System.in);	    
	if(argv.length != 1) {
	    //do nothing
	    while(sc.hasNextLine())
		System.out.println(sc.nextLine());
	}
	else {
	    //empty line next

	    String rep = argv[0];
	    while(sc.hasNextLine()) {
		String line = sc.nextLine();
		if(line.equals(""))
		    System.out.println(rep);
		else
		    System.out.println(line);
	    }
	}
    }
}
