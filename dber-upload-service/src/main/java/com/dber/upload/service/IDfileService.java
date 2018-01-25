package com.dber.upload.service;

import com.dber.base.service.IService;
import com.dber.upload.api.entity.Dfile;

/**
 * <li>文件名称: IDfileService.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月20日
 * @author dev-v
 */
public interface IDfileService extends IService<Dfile> {

    Long getMaxId();
}
