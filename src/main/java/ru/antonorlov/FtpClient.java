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


    private static  final String server = "78.108.80.75";
    private static  final int port = 21;
    private static  final String user = "f69481";

    private static  final String pass = "";

//    public static final String pathToDownload ="/Users/antonorlov/Documents/StelsParser2014/ftp_download/";
    public static final String pathToDownload ="/home/oav/tmp/StelsParser2014/downloads/";


    public static void downloadFiles(final String remotePath){
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            final boolean isAuth = ftpClient.login(user, pass);
            if(!isAuth){
                throw new IOException("Wrong user or password");
            }

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            download(ftpClient,remotePath);

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }finally {
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

    public static void uploadFiles(final File localFile, final String remotePath){
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            final boolean isAuth = ftpClient.login(user, pass);
            if(!isAuth){
                throw new IOException("Wrong user or password");
            }
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            upload(ftpClient,remotePath, localFile);

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


    public static void main(String[] args) {
//        String server = "78.108.80.75";
//        int port = 21;
//        String user = "f69481";
//        String pass = "hQqgJ6tV";


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

            String bigRemotePath = "velo-lineru/www/data/big";
            System.out.println(ftpClient.printWorkingDirectory());
            uploadFiles(ftpClient,bigRemotePath,files);


            String mediumRemotePath = "velo-lineru/www/data/medium";
            System.out.println(ftpClient.printWorkingDirectory());
            uploadFiles(ftpClient,mediumRemotePath,files);

            String smallRemotePath = "velo-lineru/www/data/small";
            System.out.println(ftpClient.printWorkingDirectory());
            uploadFiles(ftpClient,smallRemotePath,files);


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

    private static void uploadFiles(final FTPClient ftpClient,
            final String remotePath, List<File> files) throws IOException{
        for (File file : files) {
            upload(ftpClient,remotePath,file);
        }

    }
    private static void upload(final FTPClient ftpClient,final String remotePath, final File localFile) throws IOException{
        ftpClient.changeWorkingDirectory(remotePath);
        String firstRemoteFile = localFile.getName();
        InputStream inputStream = new FileInputStream(localFile);

        System.out.println("Start uploading first file["+localFile.getName()+"]");
        boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
        inputStream.close();
        if (done) {
            System.out.println("The first file is uploaded successfully.");
        }
    }

    private static void download(final FTPClient ftpClient, final String remotePath) throws IOException{
        ftpClient.changeWorkingDirectory(remotePath);
        FTPFile[] ftpFiles = ftpClient.listFiles();

        for (FTPFile ftpFile : ftpFiles) {
            if(ftpFile.getName().contains("16.jpg") && ftpFile.getName().startsWith("s")){
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
