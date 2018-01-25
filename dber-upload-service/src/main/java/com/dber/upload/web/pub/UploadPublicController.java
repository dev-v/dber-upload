package com.dber.upload.web.pub;

import com.dber.base.web.vo.Response;
import com.dber.upload.server.Uploader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/18
 */
@RestController
@RequestMapping("/pub/")
public class UploadPublicController {

    private static final Log log = LogFactory.getLog(UploadPublicController.class);

    @Autowired
    private Uploader uploader;

    @RequestMapping("callback")
    public void callback(HttpServletRequest request) throws IOException {
        byte[] content = null;

        try (InputStream in = request.getInputStream()) {
            content = new byte[in.available()];
            in.read(content);
            in.close();
        }

        if (content == null) {
            return;
        }

        uploader.callback(
                request.getHeader("Authorization"),
                request.getContentType(),
                content);
    }

    @RequestMapping("keys/{type}/{bsId}")
    public Response<long[]> keys(@PathVariable("type") int type, @PathVariable("bsId") long bsId) {
        return Response.newSuccessResponse(uploader.getKeys(type, bsId));
    }


    @RequestMapping("url")
    public Response<String> getDownloadUrl() {
        return Response.newSuccessResponse(uploader.getDownloadUrl());
    }

    @RequestMapping("_url")
    public Response<String> getPrivateDownloadUrl() {
        return Response.newSuccessResponse(uploader.getPrivateDownloadUrl());
    }
}
