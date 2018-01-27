package com.dber.upload.api;

import com.dber.base.AbstractClient;
import com.dber.base.enums.DberSystem;
import com.dber.base.enums.ImgType;
import com.dber.base.result.Result;
import com.dber.base.util.ResultTypeHelper;
import com.dber.upload.api.entity.UploadToken;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/11
 */
@Service
public class UploadClient extends AbstractClient implements IUploadClient {
    public UploadClient() {
        super(DberSystem.UPLOAD);
    }

    @Override
    public Result<UploadToken> getUploadToken(ImgType imgType, long bsId) {
        return clientUtil.get("/api/uploadToken/" + imgType.getValue() + '/' + bsId,
                ResultTypeHelper.getType(UploadToken.class));
    }

    @Override
    public Result<UploadToken> getCoverUploadToken(ImgType imgType, long bsId, long id) {
        return clientUtil.get("/api/coverToken/" + imgType.getValue() + '/' + bsId + '/' + id,
                ResultTypeHelper.getType(UploadToken.class));
    }

    @Override
    public Result<Collection<String>> getDownloadUrls(ImgType imgType, long bsId) {
        return clientUtil.get("/api/downloadUrls/" + imgType.getValue() + '/' + bsId,
                ResultTypeHelper.getCollectionType(String.class));
    }

    @Override
    public Result<Integer> del(long id, ImgType type, long bsId) {
        return clientUtil.get("/api/del/" + type.getValue() + '/' + bsId + '/' + id,
                ResultTypeHelper.getType(Integer.class));
    }
}
