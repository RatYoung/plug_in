package test.helloworld.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class TakeFileName {
	public static ArrayList<String> readfile(String filepath) throws FileNotFoundException, IOException {
		ArrayList<String> file_name = new ArrayList<String>();
		try {
                File file = new File(filepath);
                if (!file.isDirectory()) {
                        System.out.println("文件");
                        System.out.println("path=" + file.getPath());
                        System.out.println("absolutepath=" + file.getAbsolutePath());
                        System.out.println("name=" + file.getName());
                } else if (file.isDirectory()) {
                        System.out.println("文件夹");
                        String[] filelist = file.list();
                        for (int i = 0; i < filelist.length; i++) {
                                File readfile = new File(filepath + "\\" + filelist[i]);
                                if (!readfile.isDirectory()) {
                                        System.out.println("path=" + readfile.getPath());
                                        System.out.println("absolutepath=" + readfile.getAbsolutePath());
                                        System.out.println("name=" + readfile.getName());
                                        file_name.add(readfile.getName());
                                } else if (readfile.isDirectory()) {
                                        readfile(filepath + "\\" + filelist[i]);
                                }
                			
                        }
                }

        } catch (FileNotFoundException e) {
                System.out.println("readfile()   Exception:" + e.getMessage());
        }
        return file_name;
		}
}
