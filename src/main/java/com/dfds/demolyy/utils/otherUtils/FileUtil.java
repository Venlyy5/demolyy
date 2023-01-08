package com.dfds.demolyy.utils.otherUtils;

/**
 * @Author liuguicheng
 * @create 2019/6/25 10:43
 */
public class FileUtil {
    /**
     * 判断文件大小
     * @param len  文件长度
     * @param size  限制大小
     * @param unit  限制单位（B,K,M,G）
     * @return
     * 如 文件不大于100M
     * (1)FileUtil.checkFileSize( multipartFile.getSize(),100,"M");
     * (2)FileUtil.checkFileSize( file.length(),100,"M")
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }
}
