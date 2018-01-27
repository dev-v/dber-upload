package com.dber.upload.api;

import com.dber.base.enums.ImgType;
import com.dber.base.result.Result;
import com.dber.upload.api.entity.DownloadUrlRequest;
import com.dber.upload.api.entity.UploadToken;

import java.util.Collection;

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
     * 请务必进行业务数据权限验证
     *
     * @param imgType
     * @param bsId
     * @return
     */
    Result<UploadToken> getUploadToken(ImgType imgType, long bsId);

    /**
     * 覆盖上传token
     * 请务必进行业务数据权限验证
     *
     * @param imgType
     * @param bsId
     * @return
     */
    Result<UploadToken> getCoverUploadToken(ImgType imgType, long bsId, long id);

    /**
     * 请务必进行业务数据权限验证
     * 获取私有空间下载地址
     *
     * @return
     */
    Result<Collection<String>> getDownloadUrls(DownloadUrlRequest request);

    /**
     * 删除数据
     * 请自行校验数据权限
     * 参数会用于校验数据准确性
     *
     * @return
     */
    Result<Integer> del(long id, ImgType type, long bsId);
}
