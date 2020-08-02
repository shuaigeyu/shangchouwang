package com.atguigu.crowd.mvc.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;

@Controller
public class RoleHandler {
	
	@Autowired
	private RoleService roleService;
	
	@ResponseBody
	@RequestMapping("/role/get/page/info.json")
	public ResultEntity<PageInfo<Role>> getPageInfo(
			@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
			@RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
			@RequestParam(value = "keyword",defaultValue = "") String keyword
			
			){
		
		PageInfo<Role> pageInfo;
		try {
			pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);
			
			return ResultEntity.successWithData(pageInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultEntity.failed(e.getMessage());
		}
		
		
		
	}
}
