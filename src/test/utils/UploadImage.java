package com.uhome.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;


public class UploadImage {

    /**
     * 复制文件到指定路径
     * @param file
     * @param imagePath 临时文件保存路径  例如/home/data/html
     * @return
     */
    public static String copyFile(MultipartFile file,String imagePath){
        if(file != null && !file.isEmpty()) {
            //声明一个临时文件保存路径
            String rootPath = imagePath;
            Calendar date = Calendar.getInstance();
            File dir = new File(rootPath + File.separator + "membermanager" + File.separator + date.get(Calendar.YEAR)
                    + File.separator + (date.get(Calendar.MONTH)+1) + File.separator
                    + date.get(Calendar.DAY_OF_MONTH));
            if(!dir.exists()) {
                dir.mkdirs();
            } //假如路径不存在 生成
            //获取图片
            String filename = file.getOriginalFilename();
            //获取图片后缀
            String suffix  = filename.substring(filename.lastIndexOf("."));
            //获取UUID并转化为String对象
            String uuid = UUID.randomUUID().toString();
            uuid = uuid.replace("-", "");

            //拼接新图片路径
            String path = dir + File.separator + uuid + suffix;
            File tempFile = null;
            //save to the /upload path
            try {
                tempFile =  new File(path);
                tempFile.setWritable(true);
                //将上传的文件保存到临时文件
                file.transferTo(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return path.replaceAll("\\\\","/").replace(rootPath, "");
        }
        return null;
    }
}
