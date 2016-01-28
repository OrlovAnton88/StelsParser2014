package ru.antonorlov;

import java.io.File;

/**
 * Created by antonorlov on 01/12/14.
 */
public class Main {

//    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());
    public static void main(String[] args) {


//        FtpClient.downloadFiles("velo-lineru/www/data/big");

//        ImageResizer.resizeImage("/home/oav/tmp/StelsParser2014/downloads/sarrow1616.jpg",
//                "/home/oav/tmp/StelsParser2014/resize/small/sarrow1616.jpg", ImageResizer.VeloLineImgSize.SMALL);
//
//        ImageResizer.resizeImage("/home/oav/tmp/StelsParser2014/downloads/sarrow1616.jpg",
//                "/home/oav/tmp/StelsParser2014/resize/medium/sarrow1616.jpg", ImageResizer.VeloLineImgSize.MEDIUM);

        File small = new File("/home/oav/tmp/StelsParser2014/resize/medium/sarrow1616.jpg");
        FtpClient.uploadFiles(small,"velo-lineru/www/data/medium");
//        try {
//            LOGGER.info("Starting...");

        //get images
//        ImageParser imageParser = new ImageParser();
//
//        imageParser.getImgURI();

//      List<FullBicycle> bicycles =  PriceParserTwo.getInstance(PRICE_FILE, KNOWN_MODELS_FILE)
//                    .setSheetParams(6, 1, 2, 7,6).getFullBicycles();



//            ImageParser imageParser = new ImageParser();
//            imageParser.appendImgNameToBikes(bicycles);
//            imageParser.renameImages();

//            int i = 0;
//            for (FullBicycle bicycle : bicycles) {
//
//                for (FullBicycle fullBicycle : bicycles) {
//                    if(bicycle.getModel().equals(fullBicycle.getModel())){
//                        i++;
//                    }
//                    if(i>1){
//                        System.out.println(bicycle.getModel());
//                    }
//                }
//                i =0;
//            }


//            CSVEngine.getInstance().writeFullFile(bicycles);
//            System.out.println("bicycles size["+bicycles.size()+"]");
//            CSVEngine.getInstance().writeCodeAndPriceFile(bicycles);


//            for (FullBicycle bicycle : bicycles) {
//                System.out.println(bicycle);
//            }
//
//            CSVEngine.getInstance().writeFullFile(bicycles);
//        } catch (PriceReaderException ex) {
//            LOGGER.error(ex);
//
//        } catch (Exception ex) {
//            LOGGER.error(ex);
//        }
    }
}
