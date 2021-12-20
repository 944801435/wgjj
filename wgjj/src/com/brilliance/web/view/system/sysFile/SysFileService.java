package com.brilliance.web.view.system.sysFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brilliance.base.common.BaseDAO;
import com.brilliance.base.model.SysFile;

@Service
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SysFileService {

	@Autowired
	private BaseDAO baseDAO;
	
	public SysFile findById(String fileId){
		return (SysFile) baseDAO.findById(SysFile.class, fileId);
	}
	
}