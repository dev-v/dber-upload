package com.dber.upload.api.entity;

import lombok.Data;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/26
 */
@Data
public class UploadToken {
    private String token;
    private String key;
}
