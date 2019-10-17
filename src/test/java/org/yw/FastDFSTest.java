package org.yw;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author https://github.com/XiFYuW
 * @date 2019/9/9 14:19
 */
public class FastDFSTest {

    public static void main(String[] args) {
        File file = new File("D:\\Compressed\\pic\\a02.jpg");
        BigInteger bi = null;
        try {
            System.out.println(DigestUtils.md5Hex(new FileInputStream("D:\\Compressed\\pic\\1561884770(1).jpg")));
            byte[] buffer = new byte[8192];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(bi.toString(16));
    }
}
