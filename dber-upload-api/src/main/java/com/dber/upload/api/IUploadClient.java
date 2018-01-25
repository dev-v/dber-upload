package com.dber.upload.api;

import com.dber.base.enums.ImgType;
import com.dber.base.result.Result;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author chenw
 * @version 1.0
 * @since 2018/1/15
 */
public interface IUploadClient {
    /**
     * 获取上传token
     *
     * @param imgType
     * @param bsId
     * @return
     */
    Result<String> getUploadToken(ImgType imgType, long bsId);

    /**
     * 获取私有空间下载token
     *
     * @param baseUrl
     * @return
     */
    Result<String> getDownloadToken(String baseUrl);
}
