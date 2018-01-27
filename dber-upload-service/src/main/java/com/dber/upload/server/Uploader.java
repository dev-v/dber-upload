package com.dber.upload.server;

import com.dber.base.enums.ImgType;
import com.dber.upload.api.entity.UploadToken;

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

    /**
     * 获取Bucket下载地址
     *
     * @return
     */
    String getDownloadUrl(ImgType imgType);

    /**
     * 获取上传token
     *
     * @param imgType
     * @param bsId
     * @return
     */
    UploadToken getUploadToken(ImgType imgType, long bsId);

    /**
     * 覆盖上传token
     *
     * @param imgType
     * @param bsId
     * @return
     */
    UploadToken coverUploadToken(ImgType imgType, long bsId, long id);

    /**
     * 获取上传地址
     *
     * @param imgType
     * @return
     */
    String getUploadUrl(ImgType imgType);

    /**
     * 根据原始url获取下载私有空间数据的url
     *
     * @param baseUrl
     * @return
     */
    String getDownloadUrl(String baseUrl);

    /**
     * 获取下载私有空间数据的地址
     *
     * @param imgType
     * @param bsId
     * @return
     */
    String[] getDownloadUrls(ImgType imgType, long bsId);

    /**
     * 获取文件key集合
     *
     * @param imgType
     * @param bsId
     * @return
     */
    long[] getKeys(ImgType imgType, long bsId);

    /**
     * 从可疑攻击对象集合中移除可疑攻击对象
     *
     * @param imgType
     * @param bsId
     * @return
     */
    int removeAttack(ImgType imgType, long bsId);

    /**
     * 上传完成后的回源请求
     *
     * @param authorization
     * @param contentType
     * @param content
     */
    String callback(String authorization, String contentType, String content);

    int del(ImgType imgType, long bsId, long id);
}
