package ru.antonorlov;

import ru.antonorlov.img.ImageResizeHelper;

import java.io.File;

/**
 * Created by antonorlov on 29/01/16.
 */
public class ImageProcessor {

    public static final String IMAGES_DIR = "/Users/antonorlov/Documents/StelsParser2014/img_to_upload";
    public static final String IMAGES_RESIZED_DIR = "/Users/antonorlov/Documents/StelsParser2014/resized_img/";

    public static void main(String[] args) {

        File imgOrigDir = new File(IMAGES_DIR);
        File imgResizedDir = new File(IMAGES_RESIZED_DIR);


        //создаем необходимые директории

        createIfNotExists(imgResizedDir);
        File smallResizeDir = new File(IMAGES_RESIZED_DIR + "/small");
        createIfNotExists(smallResizeDir);
        File mediumResizeDir = new File(IMAGES_RESIZED_DIR + "/middle");
        createIfNotExists(mediumResizeDir);

        for(File img : imgOrigDir.listFiles()){
            ImageResizeHelper.resizeImage(img.getAbsolutePath(), smallResizeDir.getAbsolutePath() + "/" + img.getName(), ImageResizeHelper.VeloLineImgSize.SMALL);
            ImageResizeHelper.resizeImage(img.getAbsolutePath(), mediumResizeDir.getAbsolutePath() +"/" + img.getName(), ImageResizeHelper.VeloLineImgSize.MEDIUM);
        }


        for(File f: smallResizeDir.listFiles()){
            FtpService.getInstance().uploadFile(f,FtpService.SMALL_REMOTE_PATH);
        }

        for(File f: mediumResizeDir.listFiles()){
            FtpService.getInstance().uploadFile(f,FtpService.MEDIUM_REMOTE_PATH);
        }



    }

    private static void createIfNotExists(File dir){
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println(dir.getName() + " created");
            }
        } else {
            System.out.println(dir.getName() + " already exists");
        }
    }
}
