package com.dber.upload.server;

import com.dber.base.enums.ImgType;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/24
 */
public interface Uploader {
    String getUploadToken(ImgType imgType, long bsId);

    int removeAttack(ImgType imgType, long bsId);

    String getDownloadToken(String baseUrl);

    String getDownloadUrl();

    String getPrivateDownloadUrl();

    void callback(String authorization, String contentType, byte[] content);

    long[] getKeys(int type, long bsId);
}
