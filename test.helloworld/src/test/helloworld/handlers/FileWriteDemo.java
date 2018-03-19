package test.helloworld.handlers;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriteDemo {
	public static void main(String[] args) throws IOException{
		FileWriter fw = new FileWriter("/Users/mac/Desktop/java_project/test.helloworld/REQ_1.txt");
		String s = "hello, REQ!";
		fw.write(s, 0, s.length());
		fw.flush();
		fw.close();
	}
}
