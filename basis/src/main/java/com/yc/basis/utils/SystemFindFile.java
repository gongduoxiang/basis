package com.yc.basis.utils;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import com.yc.basis.entity.FileEntity;
import com.yc.basis.entity.FolderEntity;
import com.yc.basis.entrance.MyApplication;

import java.io.File;
import java.util.ArrayList;

public class SystemFindFile {

    private static Context context = MyApplication.context;

    /**
     * 查找所有的音频
     */
    public static java.util.List<FileEntity> getAllMusic() {
        java.util.List<FileEntity> entities = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        android.database.Cursor cursor = context.getContentResolver().query(uri, null, null, null, MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                FileEntity fileEntity = new FileEntity();
                fileEntity.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                if (fileEntity.size < 1024) {
                    continue;
                }
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                fileEntity.name = name.substring(name.lastIndexOf("/") + 1);
                fileEntity.url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                fileEntity.time = DataUtils.timeToData(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)) * 1000, "yyyy-MM-dd HH:mm");
                fileEntity.id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)) + "";
                fileEntity.type = "4";
                entities.add(fileEntity);
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return entities;
    }

    /**
     * 查找所有的视频频
     */
    public static java.util.List<FileEntity> getAllVideo() {
        java.util.List<FileEntity> entities = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        android.database.Cursor cursor = context.getContentResolver().query(uri, null, null, null, MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                FileEntity fileEntity = new FileEntity();
                fileEntity.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                if (fileEntity.size < 1024) {
                    continue;
                }
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                fileEntity.name = name.substring(name.lastIndexOf("/") + 1);
                fileEntity.url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                fileEntity.time = DataUtils.timeToData(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)) * 1000, "yyyy-MM-dd HH:mm");
                fileEntity.id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)) + "";
                fileEntity.type = "2";
                entities.add(fileEntity);
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return entities;
    }

    /**
     * 查找所有的图片
     */
    public static ArrayList<FileEntity> getAllImag() {
        ArrayList<FileEntity> entities = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        android.database.Cursor cursor = context.getContentResolver().query(uri, null, null, null, MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                FileEntity fileEntity = new FileEntity();
                if (cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)) < 1024) {
                    continue;
                }
                // 获取图片的路径
                fileEntity.id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)) + "";
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                fileEntity.name = name.substring(name.lastIndexOf("/") + 1);
                fileEntity.url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                //获取图片uri
                fileEntity.uri =  MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(fileEntity.id).build();
                fileEntity.time = DataUtils.timeToData(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)) * 1000, "yyyy-MM-dd HH:mm");
                fileEntity.type = "1";
                entities.add(fileEntity);
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return entities;
    }

    //获取所有图片 按文件夹分类
    public static ArrayList<FolderEntity> getAllImageFolder() {
        ArrayList<FolderEntity> folderEntities = new ArrayList<>();
        ArrayList<FileEntity> entities = getAllImag();
        folderEntities.add(new FolderEntity("全部图片", entities));
        String name;
        int index;
        for (int i = 0; i < entities.size(); i++) {
            /**
             * 根据图片路径，获取图片文件夹名称
             * WeiXin  微信
             * Screenshots  截屏
             * Camera  相机
             */
            name = getFolderName(entities.get(i).url);
            MyLog.d("name  " + name);
            if (DataUtils.isEmpty(name)) {
                continue;
            }
            //展示的时候替换
//            if ("WeiXin".equalsIgnoreCase(name)) {
//                name = "微信";
//            } else if ("Screenshots".equalsIgnoreCase(name)) {
//                name = "截屏";
//            } else if ("Camera".equalsIgnoreCase(name)) {
//                name = "相机";
//            }
            if (folderEntities.contains(new FolderEntity(name))) {//如果这个类型已经存在
                index = folderEntities.indexOf(new FolderEntity(name));
                folderEntities.get(index).entities.add(entities.get(i));
            } else {
                FolderEntity entity = new FolderEntity();
                entity.folder = name;
                entity.entities.add(entities.get(i));
                folderEntities.add(entity);
            }
        }
        return folderEntities;
    }

    /**
     * 根据图片路径，获取图片文件夹名称
     * WeiXin  微信
     * Screenshots  截屏
     * Camera  相机
     */
    private static String getFolderName(String path) {
        if (!DataUtils.isEmpty(path)) {
            String[] strings = path.split(File.separator);
            if (strings.length >= 2) {
                return strings[strings.length - 2];
            }
        }
        return "";
    }

    /**
     * 查找所有的文档
     */
    public static java.util.List<FileEntity> getAllWenDan() {
        java.util.List<FileEntity> entities = new ArrayList<>();
        Uri uri = MediaStore.Files.getContentUri("external");
        String select = getAllWenDanSelect(wjEnd.toArray(new String[wjEnd.size()]));
        android.database.Cursor cursor = context.getContentResolver().query(uri, null, select,
                null, MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                FileEntity fileEntity = new FileEntity();
                fileEntity.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                if (fileEntity.size < 1024) {
                    continue;
                }
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                fileEntity.name = name.substring(name.lastIndexOf("/") + 1);
                fileEntity.url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                fileEntity.time = SizeUtils.getSiezGB(fileEntity.size) + " |  " + DataUtils.timeToData(cursor.getLong(cursor.
                        getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED)) * 1000, "yyyy-MM-dd HH:mm");
                fileEntity.id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)) + "";
                fileEntity.type = "3";
                entities.add(fileEntity);
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return entities;
    }

    //  doc  docx  txt
    private static String getAllWenDanSelect(String[] select) {
        String str = "(";
        for (int i = 0; i < select.length; i++) {
            str += MediaStore.Files.FileColumns.DATA + " LIKE '%." + select[i] + "'";
            if (i < select.length - 1) str += " or ";
        }
        return str + ")";
    }

    private static java.util.List<String> wjEnd = new ArrayList<String>() {
        {
            add("rar");
            add("zip");
            add("txt");
            add("doc");
            add("docx");
            add("xls");
            add("xlsx");
            add("ppt");
            add("pptx");
            add("pdf");
        }
    };

}
