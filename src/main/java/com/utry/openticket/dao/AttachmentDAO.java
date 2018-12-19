package com.utry.openticket.dao;

import org.apache.ibatis.annotations.Mapper;

import com.utry.openticket.model.AttachmentDO;
/**
 * 附件上传的dao
 * @author MH
 *
 */
@Mapper
public interface AttachmentDAO {

	/**
	 * 增加附件数据库信息
	 * @param attachmentDO 要穿入的实体列
	 * @return
	 */
	Integer saveAttachment(AttachmentDO attachmentDO);

	/**
	 * 找到表中的唯一一列
	 * @param attachmentDO 封装了两个唯一约束
	 * @return
	 */
	AttachmentDO getAttachmentByTicket(AttachmentDO attachmentDO);

	/**
	 * 根据工单id删除所有附件表中的数据
	 * @param id
	 */
	void deleteAttachmentByTicketId(Integer id);

	/**
	 * 从要删除的id中获取所有的path
	 * @param id
	 * @return
	 */
	String[] getPathByDelId(Integer id);

	/**
	 * 更新数据
	 * @param attachmentDO
	 */
	void updateAttachment(AttachmentDO attachmentDO);

	/**
	 * 更新文件最近一次被修改或者下载的日期
	 * @param contentFormetDate
	 */
	void updateFileUsedTime(AttachmentDO attachment);

	 
}
