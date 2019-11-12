package com.web.wps.util;

public class FileUtil {

    // excel
    private static String[] etExts = {"et", "xls", "xlt", "xlsx", "xlsm", "xltx", "xltm", "csv"};

    // word
    private static String[] wpsExts = {"doc", "docx", "txt", "dot", "wps", "wpt", "dotx", "docm", "dotm"};

    // ppt
    private static String[] wppExts = {"ppt", "pptx", "pptm", "pptm", "ppsm", "pps", "potx", "potm", "dpt", "dps"};

    // pdf
    private static String[] pdfExts = {"pdf"};

    public static String getFileTypeCode(String fileType){
        for (String et : etExts){
            if (et.equalsIgnoreCase(fileType)){
                return "s";
            }
        }
        for (String et : wpsExts){
            if (et.equalsIgnoreCase(fileType)){
                return "w";
            }
        }
        for (String et : wppExts){
            if (et.equalsIgnoreCase(fileType)){
                return "p";
            }
        }
        for (String et : pdfExts){
            if (et.equalsIgnoreCase(fileType)){
                return "f";
            }
        }
        return null;
    }

    public static String getFileName(String filePath){
        String[] pathArr = filePath.split("/");
        String fileName ;
        if (pathArr.length>1){
            fileName = pathArr[pathArr.length-1];
        }else {
            fileName = filePath;
        }
        return fileName;
    }

    public static String getFileTypeByPath(String filePath){
        String fileName = getFileName(filePath);
        String[] arr = fileName.split("\\.");
        return arr[arr.length-1];
    }

    public static String getFileTypeByName(String fileName){
        String[] arr = fileName.split("\\.");
        return arr[arr.length-1];
    }

}
