package com.eipulse.teamproject.webSocket.controller;

import com.eipulse.teamproject.repository.employeerepository.EmployeeRepository;
import com.eipulse.teamproject.service.formapprovalservice.FileService;
import com.eipulse.teamproject.webSocket.entity.MessageEntity;
import com.eipulse.teamproject.webSocket.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    @Autowired
    private MessageService messageService;
    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    private EmployeeRepository empRepository;
    @Autowired
    private FileService fileService;

    @PostMapping("/sendMsg")
    public void sendMsg(@RequestPart(value = "file", required = false) MultipartFile file,
                        @RequestPart(value = "data") MessageEntity messageEntity) throws IOException {
        if(file!=null){
            String path = fileService.uploadImage(file,String.valueOf(messageEntity.getSender()));
            System.out.println(path);
            messageEntity.setFile(path);
        }
        messageService.sendToUser(messageEntity);
    }

    @PostMapping("/getMsg")
    public ResponseEntity<?> getMsg(@RequestBody MessageEntity messageEntity,@RequestParam Integer pageNumber,@RequestParam Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return new ResponseEntity<>(messageService.getMsg(messageEntity,pageable), HttpStatus.OK);
    }
    @GetMapping("/getUsers")
    public ResponseEntity<?> getUsers(@RequestParam String my){
        return new ResponseEntity<>(messageService.getUser(my), HttpStatus.OK);
    }

}
