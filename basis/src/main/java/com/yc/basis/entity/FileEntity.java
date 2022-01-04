package com.yc.basis.entity;


import android.net.Uri;

import com.yc.basis.utils.DataUtils;

import java.io.Serializable;

public class FileEntity implements Serializable {
    public long sizePro, size;
    //file  文件夹  1图片 2视频  3文件  4音频  0其它
    public String id = "", name, time, type = "";
    public String url;//下载路径
    public Uri uri;

    public boolean equals(Object obj) {//这个比较要和哈希比较的 字段数一致
        if (!(obj instanceof FileEntity)) {
            return false;
        }
        if (DataUtils.isEmpty(this.id)) {
            return this.url.equals(((FileEntity) obj).url);
        }
        return this.id.equals(((FileEntity) obj).id);
    }

    public int hashCode() {//哈希比较哪些内容  可以根据自己的需要  加减字段
        if (DataUtils.isEmpty(this.id)) {
            return this.url.hashCode();
        }
        return this.id.hashCode();
    }

}
