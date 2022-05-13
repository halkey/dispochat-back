package au.dispochat.file.controller;

import au.dispochat.file.controller.dto.FileResponse;
import au.dispochat.file.entity.File;
import au.dispochat.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    @Autowired
    private final FileService fileService;

    @PostMapping("/upload")
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        File newFile = fileService.saveFile(file);
        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/file/download/").
                path(newFile.getFileId()).toUriString();
        return new FileResponse(newFile.getFileId(), newFile.getFileName(), newFile.getFileType(), fileUri);
    }

    @PostMapping("/uploadMultipleFiles")
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<FileResponse> response = new ArrayList<>();
        for (var file : files) {
            response.add(uploadFile(file));
        }
        return response;
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws FileNotFoundException {
        File file = fileService.getFile(fileId);
        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFileName() + "\"").
                body(new ByteArrayResource(file.getFileData()));
    }

    @GetMapping("/allFiles")
    public List<File> getListFiles() {
        return fileService.getListOfFiles();
    }

}
