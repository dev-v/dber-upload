package com.dber.upload.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dber.base.service.IService;
import com.dber.base.web.controller.AbstractController;
import com.dber.upload.api.entity.Dfile;
import com.dber.upload.service.IDfileService;

/**
 * <li>文件名称: DfileController.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月21日
 * @author dev-v
 */
@RestController
@RequestMapping("/dfile")
public class DfileController extends AbstractController<Dfile> {

	@Autowired
	private IDfileService service;

	@Override
	protected IService<Dfile> getService() {
		return this.service;
	}
}
