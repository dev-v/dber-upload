package com.dber.upload.mapper;

import com.dber.base.mapper.IMapper;
import com.dber.upload.api.entity.Dfile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <li>文件名称: IDfileMapper.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月20日
 */
@Repository
public interface IDfileMapper extends IMapper<Dfile> {

  @Select("select max(id) from dfile")
  Long getMaxId();

  @Select("select id from dfile where type=#{type} " +
          "and bsId in <foreach collection=\"bsIds\" item=\"id\" open=\"(\" separator=\",\" close=\")\"> #{id} </foreach> " +
          "and _status=1")
  long[] getIdsByBsIds(@Param("type") int type, @Param("bsIds") long[] bsIds);
}
