package com.dber.upload.service;

import com.dber.base.mapper.IMapper;
import com.dber.base.service.AbstractService;
import com.dber.upload.api.entity.DfileError;
import com.dber.upload.mapper.IDfileErrorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <li>文件名称: DfileErrorService.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月20日
 */
@Service
public class DfileErrorService extends AbstractService<DfileError> implements IDfileErrorService {

    @Autowired
    private IDfileErrorMapper mapper;

    @Override
    protected IMapper<DfileError> getMapper() {
        return this.mapper;
    }

    @Override
    public Long getMaxId() {
        return mapper.getMaxId();
    }
}
