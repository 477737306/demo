package com.cmit.testing.entity;

import java.io.Serializable;
import java.util.Arrays;

public class DownloadFileDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileName = "";
    private byte[] byteDataArr = new byte[0];

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getByteDataArr() {
        return byteDataArr;
    }

    public void setByteDataArr(byte[] byteDataArr) {
        this.byteDataArr = byteDataArr;
    }

    @Override
    public String toString() {
        return "DownloadFileDto{" +
                "fileName='" + fileName + '\'' +
                ", byteDataArr=" + Arrays.toString(byteDataArr) +
                '}';
    }
}
