package com.dber.upload.server;

import com.dber.base.enums.ImgErrorType;
import com.dber.upload.api.entity.Dfile;
import com.dber.upload.api.entity.DfileError;
import lombok.Data;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/24
 */
@Data
public class UploadResult {
    private String token;
    private String hash;
    private long key;
    private String bucket;
    private int fsize;
    private String endUser;
    private String fname;
    private String protocol;
    private String style;
    /**
     * 业务类型
     */
    private int type;

    /**
     * 业务id
     */
    private long bsId;

    public Dfile toFile() {
        Dfile dfile = new Dfile();
        dfile.setId(this.getKey());
        dfile.setBsId(this.getBsId());
        dfile.setSfname(this.getFname());
        dfile.setType(this.getType());
        return dfile;
    }

    public DfileError toErrorFile(ImgErrorType errorType) {
        DfileError dfile = new DfileError();
        dfile.setId(this.getKey());
        dfile.setBsId(this.getBsId());
        dfile.setSfname(this.getFname());
        dfile.setType(this.getType());
        dfile.setErrorType(errorType.getValue());
        return dfile;
    }
}