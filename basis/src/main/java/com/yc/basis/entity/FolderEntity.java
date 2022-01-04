package com.yc.basis.entity;

import java.util.ArrayList;
import java.util.Objects;

//把图片按文件分类
public class FolderEntity {
    //文件夹名称
    public String folder;
    //保存的图片
    public ArrayList<FileEntity> entities = new ArrayList<>();

    public FolderEntity() {
    }

    public FolderEntity(String folder) {
        this.folder = folder;
    }

    public FolderEntity(String folder, ArrayList<FileEntity> entities) {
        this.folder = folder;
        this.entities.addAll(entities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FolderEntity)) return false;
        FolderEntity that = (FolderEntity) o;
        return folder.equals(that.folder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(folder);
    }
}
