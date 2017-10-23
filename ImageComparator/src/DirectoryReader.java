import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

public class DirectoryReader {
//	por que hay dos main
	public static void main(String[] args) {

		File f = new File("C:\\");
		String [] names = f.list();
		System.out.println(Arrays.toString(names));
	}

}
