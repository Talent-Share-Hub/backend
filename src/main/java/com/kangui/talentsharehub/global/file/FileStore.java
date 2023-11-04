package com.kangui.talentsharehub.global.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDirPath; // 파일이 저장될 디렉토리 경로
    @Value("${file.url}")
    private String url; // 파일에 접근할 수 있는 URL

    // MultipartFile 저장 및 url 반환
    public String storeFile(MultipartFile multipartFile, String additionalPath) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename(); // 업로드된 파일의 원래 이름
        String storeFileName = createStoreFileName(originalFileName); // 저장될 파일의 고유한 이름
        multipartFile.transferTo(new File(fileDirPath + additionalPath + storeFileName)); // 파일을 실제 디렉토리에 저장

        return url + storeFileName; // 저장된 파일에 접근할 수 있는 URL 반환
    }

    //
    public boolean deleteFile(String fileUrl) throws MalformedURLException {
        URL url = new URL(fileUrl); // 파일 URL을 가지고 URL 객체 생성

        File fileToDelete = new File(url.getPath()); // URL에서 파일 경로를 추출하여 해당 파일 객체 생성

        if(fileToDelete.exists()) {
            return fileToDelete.delete(); // 파일이 존재하면 삭제하고 성공 여부 반환
        }

        return false; // 파일을 찾지 못하거나 삭제할 수 없음
    }

    // 원래 파일 이름에서 고유한 저장 파일 이름 생성
    private String createStoreFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString(); // 랜덤한 UUID 생성
        String ext = extractExt(originalFileName); // 파일 확장자 추출

        return originalFileName + "-" + uuid + "." + ext; // UUID와 확장자를 조합하여 고유한 파일 이름 생성
    }

    // 파일 이름에서 확장자 추출
    private String extractExt(String originalFileName) {
        int idx = originalFileName.lastIndexOf("."); // 마지막 점의 위치를 찾음
        return originalFileName.substring(idx); // 해당 위치부터 끝까지 확장자로 변환
    }

}
