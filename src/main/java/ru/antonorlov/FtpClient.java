package ru.antonorlov;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by antonorlov on 26/01/16.
 */

public class FtpClient {

    public static final String pathToDownload ="/Users/antonorlov/Documents/StelsParser2014/ftp_download/";

    public static void main(String[] args) {

        String server = "78.108.80.75";
        int port = 21;
        String user = "f69481";
        String pass = "hQqgJ6tV";


        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            File uploadDir = new File("/Users/antonorlov/Documents/StelsParser2014/img_to_upload/");

            List<File> files = Arrays.asList(uploadDir.listFiles());

//            for (File file : files) {
//                System.out.println(file.getName());
//            }

            ftpClient.changeWorkingDirectory("velo-lineru/www/data/big");
            System.out.println(ftpClient.printWorkingDirectory());
            uploadFiles(ftpClient,files);

            ftpClient.changeWorkingDirectory("../medium");
            System.out.println(ftpClient.printWorkingDirectory());
            uploadFiles(ftpClient,files);

            ftpClient.changeWorkingDirectory("../small");
            System.out.println(ftpClient.printWorkingDirectory());
            uploadFiles(ftpClient,files);


        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void uploadFiles(final FTPClient ftpClient, List<File> files) throws IOException{
        for (File file : files) {
            upload(ftpClient,file);
        }

    }
    private static void upload(final FTPClient ftpClient, final File localFile) throws IOException{
        // APPROACH #1: uploads first file using an InputStream
        String firstRemoteFile = localFile.getName();
        InputStream inputStream = new FileInputStream(localFile);

        System.out.println("Start uploading first file["+localFile.getName()+"]");
        boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
        inputStream.close();
        if (done) {
            System.out.println("The first file is uploaded successfully.");
        }
    }

    private static void download(final FTPClient ftpClient) throws IOException{
        ftpClient.changeWorkingDirectory("velo-lineru/www/data/big");
        FTPFile[] ftpFiles = ftpClient.listFiles();

        for (FTPFile ftpFile : ftpFiles) {
            if(ftpFile.getName().contains("15.jpg")){
                System.out.println(ftpFile);
                File downloadFile = new File(pathToDownload + ftpFile.getName());
                OutputStream outputStream1
                        = new BufferedOutputStream(new FileOutputStream(downloadFile));
                boolean success = ftpClient.retrieveFile(ftpFile.getName(), outputStream1);
                outputStream1.close();
                System.out.println("Downloaded");
            }
        }
    }

}
