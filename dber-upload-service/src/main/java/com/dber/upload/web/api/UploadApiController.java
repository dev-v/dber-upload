package com.dber.upload.web.api;

import com.dber.base.IClient;
import com.dber.base.entity.Response;
import com.dber.base.enums.ImgType;
import com.dber.upload.api.entity.DownloadUrlRequest;
import com.dber.upload.api.entity.UploadToken;
import com.dber.upload.server.Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: 任何客户端api-server请务必实现IClient标记接口</li>
 * <li>访问需授权</li>
 * <li>本类获取数据时建议从数据源本身获取，redis等缓存建议在api-sdk上面做</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/12
 */
@RestController
@RequestMapping("/api/")
public class UploadApiController implements IClient {

    @Autowired
    private Uploader uploader;

    @RequestMapping(value = "downloadUrls", method = RequestMethod.POST)
    public Response<String[]> downloadUrls(DownloadUrlRequest request) {
        return Response.newSuccessResponse(uploader.getDownloadUrls(request));
    }

    @RequestMapping(value = "uploadToken/{type}/{bsId}", method = RequestMethod.GET)
    public Response<UploadToken> uploadToken(@PathVariable("type") int type, @PathVariable("bsId") long bsId) {
        return Response.newSuccessResponse(uploader.getUploadToken(ImgType.from(type), bsId));
    }

    @RequestMapping(value = "coverToken/{type}/{bsId}/{id}", method = RequestMethod.GET)
    public Response<UploadToken> coverToken(@PathVariable("type") int type, @PathVariable("bsId") long bsId,
                                            @PathVariable("id") long id) {
        return Response.newSuccessResponse(uploader.coverUploadToken(ImgType.from(type), bsId, id));
    }

    @RequestMapping(value = "del/{type}/{bsId}/{id}", method = RequestMethod.GET)
    public Response<Integer> uploadToken(@PathVariable("type") int type, @PathVariable("bsId") long bsId,
                                         @PathVariable("id") long id) {
        return Response.newSuccessResponse(uploader.del(ImgType.from(type), bsId, id));
    }
}