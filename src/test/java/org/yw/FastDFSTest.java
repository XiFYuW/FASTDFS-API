package org.yw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * @author https://github.com/XiFYuW
 * @date 2019/9/9 14:19
 */
public class FastDFSTest {

    public static void main(String[] args) {
        FastDFSClient fastDFSClient = new FastDFSClient();
        File file = new File("D:\\Compressed\\pic\\1561884770(1).jpg");
        try {
            FastDFSResponse fastDFSResponse = fastDFSClient.uploadTo(new FileInputStream(file),"D:\\Compressed\\pic\\1561884770(1).jpg", new HashMap<>());
            System.err.println(fastDFSResponse);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
