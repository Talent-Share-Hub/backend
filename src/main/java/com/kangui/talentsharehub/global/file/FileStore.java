package com.kangui.talentsharehub.global.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;
    @Value("{file.storage}")
    private String fileStorage;

    public String getFullPath(String fileName, String additionalPath) {
        return fileDir + additionalPath + fileName;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, String additionalPath) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, additionalPath));
            }
        }
        return storeFileResult;
    }

    // MultipartFile 저장 및 url 반환
    public UploadFile storeFile(MultipartFile multipartFile, String additionalPath) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename(); // 업로드된 파일의 원래 이름
        String storeFileName = createStoreFileName(originalFileName); // 저장될 파일의 고유한 이름
        String fileUrl = fileStorage + getFullPath(storeFileName, additionalPath); // 저장된 파일에 접근할 수 있는 URL
        multipartFile.transferTo(new File(getFullPath(storeFileName, additionalPath))); // 파일을 실제 디렉토리에 저장

        return new UploadFile(originalFileName, storeFileName, fileUrl);
    }

    //
    public boolean deleteFile(String storeFileName, String additionalPath) throws MalformedURLException {

        File fileToDelete = new File(getFullPath(storeFileName, additionalPath)); // URL에서 파일 경로를 추출하여 해당 파일 객체 생성

        if(fileToDelete.exists()) {
            return fileToDelete.delete(); // 파일이 존재하면 삭제하고 성공 여부 반환
        }

        return false; // 파일을 찾지 못하거나 삭제할 수 없음
    }

    // 원래 파일 이름에서 고유한 저장 파일 이름 생성
    private String createStoreFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString(); // 랜덤한 UUID 생성
        String ext = extractExt(originalFileName); // 파일 확장자 추출

        return uuid + ext; // 고유한 파일 이름 생성
    }

    // 파일 이름에서 확장자 추출
    private String extractExt(String originalFileName) {
        int idx = originalFileName.lastIndexOf("."); // 마지막 점의 위치를 찾음
        return originalFileName.substring(idx); // 해당 위치부터 끝까지 확장자로 변환
    }

}
