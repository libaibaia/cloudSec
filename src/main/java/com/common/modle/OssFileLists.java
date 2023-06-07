package com.common.modle;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;

import java.io.File;
import java.util.Date;
import java.util.List;

public class OssFileLists {
    @ExcelProperty("文件名")
    private String fileName;

    @ExcelProperty("文件路径")
    private String filePath;
    @ExcelProperty("文件大小")
    private long fileSize;
    @ExcelProperty("修改日期")
    private Date date;

    public OssFileLists() {
    }

    public OssFileLists(String fileName, String filePath, long fileSize, Date date) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.date = date;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static void main(String[] args) {
//        OssFileLists ossFileLists = new OssFileLists("ces","t","d",new Date());
//        List<OssFileLists> objects = new ArrayList<>();
//        objects.add(ossFileLists);
//        String fileName = "test.xlsx";
//        EasyExcel.write(fileName, OssFileLists.class).sheet("test").doWrite(objects);
    }

    public static void createFile(List<OssFileLists> objects, File file){
        EasyExcel.write(file, OssFileLists.class).sheet("文件列表").doWrite(objects);
    }
}
