package com.dber.upload.api;

import com.dber.base.AbstractClient;
import com.dber.base.enums.DberSystem;
import com.dber.base.enums.ImgType;
import com.dber.base.result.Result;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
    public Result<String> getUploadToken(ImgType imgType, long bsId) {
        return clientUtil.get("/api/uploadToken/" + imgType.getValue() + '/' + bsId);
    }

    @Override
    public Result<String> getDownloadToken(String baseUrl) {
        Map<String, String> params = new HashMap<>();
        params.put("baseUrl", baseUrl);
        return clientUtil.post("/api/downloadToken", params);
    }
}
