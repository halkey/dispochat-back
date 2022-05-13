package au.dispochat.file.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileResponse {

    String fileId;
    String fileName;
    String fileType;
    String fileUri;

    public FileResponse(String fileId, String fileName, String fileType, String fileUri) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileUri = fileUri;
    }
}
