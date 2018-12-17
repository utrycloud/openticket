package com.utry.openticket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utry.openticket.dao.AttachmentDAO;
import com.utry.openticket.model.AttachmentDO;
import com.utry.openticket.util.FileUtil;

/**
 * 文件上传的service
 * @author MH
 *
 */
@Service
public class AttachmentService {

	@Autowired
	private AttachmentDAO attachmentDAO;

	/**
	 * 
	 * @param attachmentDO要加入的实体类
	 * @return 新增加的数据
	 */
	public int saveAttachment(AttachmentDO attachmentDO) {
		// TODO Auto-generated method stub
		return attachmentDAO.saveAttachment(attachmentDO);
	}

	/**
	 * 根据两个id找到表中的唯一一列
	 * @param ticketId 工单id
	 * @param ticketFileldId 列id
	 */
	public AttachmentDO getAttachmentByTicket(Integer ticketId, Integer ticketFileldId) {
		// TODO Auto-generated method stub
		AttachmentDO attachmentDO=new AttachmentDO();
		attachmentDO.setTicketId(ticketId);
		attachmentDO.setTicketFileldId(ticketFileldId);
		attachmentDO= attachmentDAO.getAttachmentByTicket(attachmentDO);
		return attachmentDO;
	}

	/**
	 * 删除附件表中的工单号为id的所有附件 同时删除附件
	 * 为了防止出错加上@Transactional
	 * @param id
	 */
	@Transactional
	public void deleteAttachmentByTicketId(Integer id) {
		// TODO Auto-generated method stub
		//再删除之前现获取文件然后删除
		String[] paths=attachmentDAO.getPathByDelId(id);
		FileUtil.delFileByPath(paths);
		attachmentDAO.deleteAttachmentByTicketId(id);
	}
	
	/**
	 * 更新数据
	 * @param attachmentDO
	 */
	public void updateAttachment(AttachmentDO attachmentDO){
		attachmentDAO.updateAttachment(attachmentDO);
	}
}
