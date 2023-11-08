package com.kangui.talentsharehub.global.file;

import lombok.Getter;

@Getter
public class UploadFile {

    private final String uploadFileName;
    private final String storeFileName;
    private final String fileUrl;

    public UploadFile(String uploadFileName, String storeFileName, String fileUrl) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fileUrl = fileUrl;
    }
}
