package com.utry.openticket.model;

/**
 * 附件内容业务实体
 * @author MH
 * 
 */
public class AttachmentDO {
	private Integer id;//附件编号
	private Integer ticketId;//工单编号
	private Integer ticketFileldId;//对象编号
	private String fileName;//文件名称
	private Integer fileSize;//文件大小
	private String fileUploadTime;//文件上传日期
	private String fileUsedTime;//文件最近一次被下载 或者 重新上传的日期
	private String contentType;//文件类型
	private String path;//保存路径
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTicketId() {
		return ticketId;
	}
	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}
	public Integer getTicketFileldId() {
		return ticketFileldId;
	}
	public void setTicketFileldId(Integer ticketFileldId) {
		this.ticketFileldId = ticketFileldId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getFileSize() {
		return fileSize;
	}
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileUploadTime() {
		return fileUploadTime;
	}
	public void setFileUploadTime(String fileUploadTime) {
		this.fileUploadTime = fileUploadTime;
	}
	
	public String getFileUsedTime() {
		return fileUsedTime;
	}
	public void setFileUsedTime(String fileUsedTime) {
		this.fileUsedTime = fileUsedTime;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
