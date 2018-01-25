package com.dber.upload.server.valid;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/25
 */
public interface IIDGenerator {

    long next();

    long current();
}
