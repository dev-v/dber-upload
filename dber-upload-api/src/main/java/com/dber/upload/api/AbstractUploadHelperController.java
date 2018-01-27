package com.dber.upload.api;

import com.dber.base.entity.Response;
import com.dber.base.enums.ImgType;
import com.dber.base.login.LoginCheckController;
import com.dber.upload.api.entity.DownloadUrlRequest;
import com.dber.upload.api.entity.UploadToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

/**
 * <li>修改记录: ...</li>
 * <pre>
 *     内容摘要:
 *     一个获取token等的工具controller接口辅助类，
 *     对文件进行上传、覆盖、删除、私有空间下载操作可以使用本辅助类进行方便的集成
 *     也可以使用client进行集成
 * </pre>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/27
 */
@RequestMapping("/token/")
public abstract class AbstractUploadHelperController extends LoginCheckController {

    @Autowired
    IUploadClient uploadClient;

    /**
     * 获取上传token
     * 如果不需要该服务请在子类中覆盖该方法实现
     *
     * @param imgType
     * @param bsId
     * @return
     */
    @RequestMapping(value = "uploadToken/{imgType}/{bsId}", method = RequestMethod.GET)
    public Response<UploadToken> uploadToken(@PathVariable("imgType") int imgType, @PathVariable("bsId") long bsId) {
        if (validAuth(getAccountId(), imgType, bsId)) {
            return Response.newSuccessResponse(uploadClient.getUploadToken(ImgType.from(imgType), bsId).getResponse());
        }
        return null;
    }

    /**
     * 获取覆盖上传token
     * 如果不需要该服务请在子类中覆盖该方法实现
     *
     * @param imgType
     * @param bsId
     * @param id
     * @return
     */
    @RequestMapping(value = "coverToken/{imgType}/{bsId}/{id}", method = RequestMethod.GET)
    public Response<UploadToken> coverToken(@PathVariable("imgType") int imgType, @PathVariable("bsId") long bsId,
                                            @PathVariable("id") long id) {
        if (validAuth(getAccountId(), imgType, bsId)) {
            return Response.newSuccessResponse(
                    uploadClient.getCoverUploadToken(ImgType.from(imgType), bsId, id).getResponse());
        }
        return null;
    }

    /**
     * 获取私有空间下载地址
     * 如果不需要该服务请在子类中覆盖该方法实现
     *
     * @return
     */
    @RequestMapping(value = "downloadUrls")
    public Response<Collection<String>> downloadUrls(DownloadUrlRequest request) {
        if (validAuth(getAccountId(), request.getType(), request.getBsId())) {
            return Response.newSuccessResponse(uploadClient.getDownloadUrls(request).getResponse());
        }
        return null;
    }

    /**
     * 删除数据
     * 如果不需要该服务请在子类中覆盖该方法实现
     *
     * @param type
     * @param bsId
     * @param id
     * @return
     */
    @RequestMapping(value = "del/{type}/{bsId}/{id}", method = RequestMethod.GET)
    public Response<Integer> del(@PathVariable("type") int type, @PathVariable("bsId") long bsId,
                                 @PathVariable("id") long id) {
        if (validAuth(getAccountId(), type, bsId) && !ImgType.MATRIX_CODE.is(type)) {
            return Response.newSuccessResponse(uploadClient.del(id, ImgType.from(type), bsId).getResponse());
        }
        return null;
    }

    /**
     * 实现业务权限验证逻辑
     * 仅在权限验证通过时返回true 其它任何情况请返回false或抛出异常
     *
     * @param accountId 当前登录用户账户id
     * @param imgType   业务类型
     * @param bsId      业务id
     * @return
     */
    protected abstract boolean validAuth(int accountId, int imgType, long bsId);
}