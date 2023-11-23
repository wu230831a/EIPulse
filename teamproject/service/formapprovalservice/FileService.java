package com.eipulse.teamproject.service.formapprovalservice;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    @Value("${upload.path}")
    private String uploadPath;
    private String connectStr = "DefaultEndpointsProtocol=https;AccountName=eipulseimages;AccountKey=J3OLfPQvTNjhsavqjIZpktnTy8hCx12b3u6t+IdLYwIJ7i/nzSGJuLgF7Do5/SPoBi5+PkLamBDF+AStcEcIBQ==;EndpointSuffix=core.windows.net";
    private BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();;

    //本地
    public void saveFile(List<MultipartFile> files,Integer empId) throws IOException {
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String directoryPath = uploadPath + "/" + empId;
                File directory = new File(directoryPath);

                if (!directory.exists()) {
                    directory.mkdirs(); // 這裡會建立整個路徑，包括中間可能不存在的目錄
                }

                String filePath = directoryPath + "/" + file.getOriginalFilename();
                file.transferTo(new File(filePath));
            }
        }
    }

    //雲端
    public String uploadFormImage(List<MultipartFile> files) {
        // 獲取 Blob 容器的引用
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("images/form");
        // 獲取 Blob 的引用
        for (MultipartFile file : files) {
            BlobClient blobClient = containerClient.getBlobClient(file.getOriginalFilename());
            try {
                // 上傳文件到 Blob
                blobClient.upload(file.getInputStream(), file.getSize(), true);
            } catch (IOException e) {
                e.printStackTrace();
                return "Error occurred while uploading the file.";
            }
        }

        // 返回 Blob 的 URL
//        System.out.println(blobClient.getBlobUrl());
        return null;
    }


    public String uploadImage(MultipartFile file,String path) {
        // 獲取 Blob 容器的引用
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("images");
        // 獲取 Blob 的引用
        BlobClient blobClient = containerClient.getBlobClient(file.getOriginalFilename());
        try {
            // 上傳文件到 Blob
            blobClient.upload(file.getInputStream(), file.getSize(), true);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while uploading the file.";
        }

        // 返回 Blob 的 URL
//        System.out.println(blobClient.getBlobUrl());
        return blobClient.getBlobUrl();
    }

}
