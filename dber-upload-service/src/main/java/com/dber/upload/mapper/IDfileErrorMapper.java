package com.dber.upload.mapper;

import com.dber.base.mapper.IMapper;
import com.dber.upload.api.entity.DfileError;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <li>文件名称: IDfileErrorMapper.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月20日
 */
@Repository
public interface IDfileErrorMapper extends IMapper<DfileError> {

    @Select("select max(id) from dfile_error")
    Long getMaxId();
}
