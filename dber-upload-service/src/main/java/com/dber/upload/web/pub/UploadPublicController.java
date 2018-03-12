package com.dber.upload.web.pub;

import com.dber.base.enums.ImgType;
import com.dber.base.entity.Response;
import com.dber.upload.server.Uploader;
import com.dber.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

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
  public String callback(HttpServletRequest request, @RequestBody String content) throws IOException {
    if (Util.isBlank(content)) {
      return content;
    }

    return uploader.callback(request.getHeader("Authorization"), request.getContentType(), content);
  }

  @RequestMapping(value = "keys/{type}/{bsId}", method = RequestMethod.GET)
  public Response<long[]> keys(@PathVariable("type") int type, @PathVariable("bsId") long bsId) {
    return Response.newSuccessResponse(uploader.getKeys(ImgType.from(type), bsId));
  }

  @RequestMapping(value = "keys/{type}", method = RequestMethod.GET)
  public Response<long[]> keys(@PathVariable("type") int type, long[] bsIds) {
    return Response.newSuccessResponse(uploader.getKeys(ImgType.from(type), bsIds));
  }

  /**
   * 公共空间地址
   * 私有空间地址只允许通过imgType+bsId的形式获取
   *
   * @return
   */
  @RequestMapping(value = "downloadUrl/{imgType}", method = RequestMethod.GET)
  public Response<String> getDownloadUrl(@PathVariable("imgType") int imgType) {
    return Response.newSuccessResponse(uploader.getDownloadUrl(ImgType.from(imgType)));
  }

  @RequestMapping(value = "uploadUrl/{imgType}", method = RequestMethod.GET)
  public Response<String> getBucketUploadUrl(@PathVariable("imgType") int imgType) {
    return Response.newSuccessResponse(uploader.getUploadUrl(ImgType.from(imgType)));
  }
}
