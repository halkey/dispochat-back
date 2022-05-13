package au.dispochat.file.service;

import au.dispochat.file.entity.File;
import au.dispochat.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;


    public File saveFile(MultipartFile file) throws IOException {

        File newFile = new File(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())), file.getContentType(), file.getBytes());
        return fileRepository.save(newFile);
    }

    @Transactional
    public File getFile(String fileId) throws FileNotFoundException {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException("Dosya bulunamadı!"));
        fileRepository.deleteById(fileId);
        return file;
    }

    @Transactional
    public List<File> getListOfFiles() {
        return fileRepository.findAll();
    }

}
