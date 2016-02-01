package ru.antonorlov.img;


import ru.antonorlov.entities.FullBicycle;
import ru.antonorlov.util.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonorlov on 04/01/15.
 */
public class ImageParser {

    List<File> fileList;

    public ImageParser() {
        fileList = new ArrayList<File>();
        getImgURI();
    }


    public List<FullBicycle> appendImgNameToBikes(List<FullBicycle> list) {
        for (FullBicycle fullBicycle : list) {
//            String modelName = fullBicycle.getModel();
//            String[] nodes = modelName.split(Constants.SPACE_CHAR);
            String imgName = fullBicycle.getProductCode() + ".jpg";
            String imgName2 = fullBicycle.getProductCode() + "_2" + ".jpg";
            String imgName3 = fullBicycle.getProductCode() + "_3" + ".jpg";
//            for(int i =0; i< nodes.length; i++){
//                if(i == 0 || i == 1){
//                    imgName = imgName + nodes[i].substring(0,1).toLowerCase();
//                }else {
//                    imgName = imgName +"_"+ nodes[i].toLowerCase();
//                }
//            }
//            imgName = imgName.replaceAll("\"","");
//            imgName = imgName.replaceAll("-ск","sp");
//            imgName = imgName + ".jpg";
            fullBicycle.setImageName(imgName);
            fullBicycle.setImageName2(imgName2);
            fullBicycle.setImageName3(imgName3);
        }

        return list;
    }


    public void renameImages() {
        for (File file : fileList) {
            String name = file.getName().toLowerCase();
            String newName = "s";
            if (name.contains("pilot")) {
                name = name.replaceAll("pilot", "p");
            } else if (name.contains("nav")) {
                name = name.replaceAll("nav", "n");
            } else if (name.contains("miss")) {
                name = name.replaceAll("miss", "m");
            }
            name = name.replaceAll("-", "_");
            newName = newName + name.trim();
            File f = new File(file.getParentFile().getAbsoluteFile() + "/" + newName);
            file.renameTo(f);
        }
    }

    public List<String> getImgURI() {
        File file = new File(Constants.IMAGES_FOLDER);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            getInnerFiles(files[i]);
        }

        for (File s : fileList) {
            System.out.println(s);
        }
        return null;
    }

    private void getInnerFiles(File file) {
        String fileName = file.getName();
        if (fileName.contains(".jpg")) {
            fileList.add(file);
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    getInnerFiles(files[i]);
                }
            }
        }
    }
}
