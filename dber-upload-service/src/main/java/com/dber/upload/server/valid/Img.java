package com.dber.upload.server.valid;

import lombok.Getter;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/24
 */
public class Img {
    @Getter
    private int type;

    @Getter
    private long bsId;

    public Img(int type, long bsId) {
        this.type = type;
        this.bsId = bsId;
    }
}
