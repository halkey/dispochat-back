package au.dispochat.file.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "FILE")
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String fileId;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] fileData;

    public File(String fileName, String fileType) {
        this.fileName = fileName;
        this.fileType = fileType;

    }

    public File(String fileName, String fileType, byte[] fileData) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileData = fileData;
    }

}
