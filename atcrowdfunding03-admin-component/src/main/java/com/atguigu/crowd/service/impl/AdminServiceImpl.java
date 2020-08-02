package com.atguigu.crowd.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.entity.AdminExample.Criteria;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminMapper adminMapper;

	private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
	@Override
	public void saveAdmin(Admin admin) {
		
		String userPswd = admin.getUserPswd();
		userPswd = CrowdUtil.md5(userPswd);
		admin.setUserPswd(userPswd);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createTime = format.format(date);
		admin.setCreateTime(createTime);
		try {
			adminMapper.insert(admin);
		} catch (Exception e) {
			e.printStackTrace();
			 
			logger.info("异常全类名="+e.getClass().getName());
			
			if(e instanceof DuplicateKeyException) {
				throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
			}
		}
		
		
		// throw new RuntimeException();
		
	}

	@Override
	public List<Admin> getAll() {
		return adminMapper.selectByExample(new AdminExample());
	}

	@Override
	public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

		AdminExample adminExample = new AdminExample();
		
		Criteria criteria = adminExample.createCriteria();
		
		criteria.andLoginAcctEqualTo(loginAcct);
		
		List<Admin> list = adminMapper.selectByExample(adminExample);
		
		if(list==null || list.size()==0) {
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		
		if(list.size()>1) {
			throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQU);
			
		}
		Admin admin = list.get(0);
		
		if(admin==null) {
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		
		String userPswdDB = admin.getUserPswd(); 
		
		String userPswdForm = CrowdUtil.md5(userPswd);
		
		if(!Objects.equals(userPswdForm, userPswdDB)) {
			throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
		}
		return admin;
	}

	@Override
	public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
		
		PageHelper.startPage(pageNum,pageSize);
		List<Admin> list = adminMapper.selectAdminByKeyword(keyword);
		
		return new PageInfo<>(list);
		
	}

	@Override
	public void remove(Integer adminId) {
		
		adminMapper.deleteByPrimaryKey(adminId);
	}

	@Override
	public Admin getAdminById(Integer adminId) {
		// TODO Auto-generated method stub
		return adminMapper.selectByPrimaryKey(adminId);
	}

	@Override
	public void update(Admin admin) {
		// TODO Auto-generated method stub
		try {
			adminMapper.updateByPrimaryKeySelective(admin);
		} catch (Exception e) {
			e.printStackTrace();
			 
			logger.info("异常全类名="+e.getClass().getName());
			
			if(e instanceof DuplicateKeyException) {
				throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
			}
		}
		
		
	}

}
