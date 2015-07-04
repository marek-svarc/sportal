package com.clubeek.dao.impl.springjdbctemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class that contains helper methods for tests.
 *
 * @author vitfo
 */
public class Tools {

    public static byte[] getImageAsBytes(String fileName) {
        File file = new File(fileName);
        byte[] fileContent = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            fileContent = new byte[(int) file.length()];
            fis.read(fileContent);
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found! " + fnfe);
        } catch (IOException ioe) {
            System.out.println("Exception while reading file! " + ioe);
        }
        return fileContent;
    }
}
