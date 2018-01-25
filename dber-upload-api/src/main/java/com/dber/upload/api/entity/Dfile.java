package com.dber.upload.api.entity;

import lombok.Data;

/**
 * <li>文件名称: Dfile.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月20日
 * @author dev-v
 */
@Data
public class Dfile {
	
	/**
	 * 也作为云平台文件存储名称
	 */
	private Long id;

	/**
	 * 
	 */
	private Long bsId;

	/**
	 * 业务类型：
1-店铺图片
3-系统健身运动服务项目图片
4-教练认证头像
5-用户认证头像等
6-店铺二维码
7-教练认证证书
8-体能测试结果
9-身份认证图片
	 */
	private Integer type;

	/**
	 * 原始文件名称
	 */
	private String sfname;

	/**
	 * 状态：1-可用，2-删除，3-过期
	 */
	private Integer status;


}
