package com.ll.social.app.gen.controller;

import com.ll.social.app.gen.entity.GenFile;
import com.ll.social.app.gen.service.GenFileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@Controller
@RequestMapping("/download")
@RequiredArgsConstructor
public class FileDownloadController {

    private final GenFileService genFileService;

    @SneakyThrows // try ~ catch 대응
    @GetMapping("/gen/{id}")
    public void download(HttpServletResponse response, @PathVariable Long id) {
        GenFile genFile = genFileService.getById(id).get();

        String path = genFile.getFilePath();

        File file = new File(path);
        response.setHeader("Content-Disposition", "attachment;filename=" + genFile.getOriginFileName());

        FileInputStream fileInputStream = new FileInputStream(path); // 파일 읽어오기
        OutputStream out = response.getOutputStream();

        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = fileInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

}
