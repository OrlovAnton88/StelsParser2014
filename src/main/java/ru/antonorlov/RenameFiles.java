package ru.antonorlov;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by antonorlov on 26/01/16.
 */
public class RenameFiles {


    public static void main(String[] args) {
        File imgDir = new File(FtpService.pathToDownload);

        for (File f : imgDir.listFiles()) {
            System.out.println(f.getName());
            String originalFileName = f.getName();
            String newFileName = originalFileName.replaceAll("15.jpg", "16.jpg");
            try {
                copyFile(f, new File("/Users/antonorlov/Documents/StelsParser2014/img_to_upload/" + newFileName));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
