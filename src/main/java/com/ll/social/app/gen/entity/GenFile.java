package com.ll.social.app.gen.entity;


import com.ll.social.app.base.AppConfig;
import com.ll.social.app.base.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class GenFile extends BaseEntity {
    private String relTypeCode;

    private long relId;

    private String typeCode;

    private String type2Code;

    private String fileExtTypeCode;

    private String fileExtType2Code;

    private int fileSize;

    private int fileNo;

    private String fileExt;

    private String fileDir;

    private String originFileName;

    public void merge(GenFile other) {
        relTypeCode = other.getRelTypeCode();
        relId = other.getRelId();
        typeCode = other.getTypeCode();
        type2Code = other.getType2Code();
        fileExtTypeCode = other.getFileExtTypeCode();
        fileExtType2Code = other.getFileExtType2Code();
        fileSize = other.getFileSize();
        fileNo = other.getFileNo();
        fileExt = other.getFileExt();
        fileDir = other.getFileDir();
        originFileName = other.getOriginFileName();
    }

    public String getFileName() {
        return getId() + "." + getFileExt();
    }

    public String getUrl() {
        return "/gen/" + getFileDir() + "/" + getFileName();
    }
    public String getDownloadUrl() {
        return "/download/gen/" + getId();
    }

    public String getFilePath() {
        return AppConfig.FILE_DIR_PATH + "/" + getFileDir() + "/" + getFileName();
    }
}
