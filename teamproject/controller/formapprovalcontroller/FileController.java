package com.eipulse.teamproject.controller.formapprovalcontroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
@RestController
@CrossOrigin
public class FileController {

    @Value("${upload.path}")
    private String uploadPath;
    @GetMapping("/download/{empId}/{fileNames:.+}")
    public ResponseEntity<byte[]>[] getImages1(@PathVariable Integer empId, @PathVariable String fileNames) throws IOException {

        List<ResponseEntity<byte[]>> responseEntities = new ArrayList<>();

        String[] fileNameArray = fileNames.split(",");
        for (String fileName : fileNameArray) {

                String filePath = uploadPath + "/" + empId + "/" + fileName;
                byte[] photoFile = getFileAsByteArray(filePath);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);

                ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(photoFile, headers, HttpStatus.OK);
                responseEntities.add(responseEntity);

        }
        ResponseEntity<byte[]>[] responseArray = new ResponseEntity[responseEntities.size()];
        return responseEntities.toArray(responseArray);
    }



    @GetMapping("/chats/{roomId}/{empId}/{fileName}")
    public ResponseEntity<?> getImages(@PathVariable Integer roomId, @PathVariable String empId, @PathVariable String fileName) throws IOException {
        String filePath = uploadPath + "/chats/" + roomId + "/" + empId + "/" + fileName;
        byte[] photoFile = getFileAsByteArray(filePath);
        String base64Image = Base64.getEncoder().encodeToString(photoFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(base64Image, headers, HttpStatus.OK);
    }

    public byte[] getFileAsByteArray(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        // 根據檔案路徑取得byte[]
        byte[] fileData = Files.readAllBytes(path);

        return fileData;
    }

    @GetMapping("/privateChats/{folder}/{sender}/{fileName}")
    public ResponseEntity<?> getPrivateImages(@PathVariable Integer folder, @PathVariable String sender, @PathVariable String fileName) throws IOException {
        String filePath = uploadPath + "/privateChats/" + folder + "/" + sender + "/" + fileName;
        byte[] photoFile = getFileAsByteArray(filePath);
        String base64Image = Base64.getEncoder().encodeToString(photoFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(base64Image, headers, HttpStatus.OK);
    }

}
