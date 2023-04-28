package com.ll.social.app.fileupload.entity;


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
}
