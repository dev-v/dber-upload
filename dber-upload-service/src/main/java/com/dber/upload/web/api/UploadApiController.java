package com.dber.upload.web.api;

import com.dber.base.IClient;
import com.dber.base.enums.DberSystem;
import com.dber.base.enums.ImgType;
import com.dber.base.util.BaseKeyUtil;
import com.dber.base.web.vo.Response;
import com.dber.upload.server.Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * @param system 客户端系统
     * @return
     */
    @RequestMapping("test")
    public Response test(@RequestParam(BaseKeyUtil.auth_params_system) DberSystem system) {
        return Response.newSuccessResponse(system);
    }

    @RequestMapping("downloadToken")
    public Response<String> downloadToken(@ModelAttribute("baseUrl") String baseUrl) {
        return Response.newSuccessResponse(uploader.getDownloadToken(baseUrl));
    }

    @RequestMapping("uploadToken/{type}/{bsId}")
    public Response<String> uploadToken(@PathVariable("type") int type, @PathVariable("bsId") long bsId) {
        return Response.newSuccessResponse(uploader.getUploadToken(ImgType.from(type), bsId));
    }
}
