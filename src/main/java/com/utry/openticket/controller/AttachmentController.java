package com.utry.openticket.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utry.openticket.constant.FileConstant;
import com.utry.openticket.dto.TicketDTO;
import com.utry.openticket.model.AttachmentDO;
import com.utry.openticket.model.TicketValueDO;
import com.utry.openticket.service.AttachmentService;
import com.utry.openticket.util.DateUtils;
import com.utry.openticket.util.FileUtil;

/**
 * 文件上传下载controller
 * 
 * @author MH
 *
 */
@Controller
public class AttachmentController {

	@Autowired
	AttachmentService attachmentService;


	/**
	 * 实现文件上传
	 * @param ticketFileldId 保存文件的列的唯一id
	 * @param file 上传的文件
	 * @return
	 */
	@RequestMapping("/fileUpload")
	@ResponseBody
	public Object fileUpload(@RequestParam("ticketFileldId") String ticketFileldId,
			@RequestParam("fileName") MultipartFile file) {
		if (file.isEmpty()) {
			return "文件失效";
		}

		AttachmentDO attachmentDO = new AttachmentDO();

		String fileName = file.getOriginalFilename();
		// 生成随机的uuid来实际存放用户提交的附件
		String uuidName = UUID.randomUUID().toString();
		int size = (int) file.getSize();
		String contentType = fileName.substring(fileName.lastIndexOf(".") + 1);
		String path = FileConstant.FILE_LOAD_URL + "/" + uuidName + "." + contentType;
		
		attachmentDO.setTicketFileldId(new Integer(ticketFileldId));
		attachmentDO.setFileName(fileName);
		attachmentDO.setPath(path);
		attachmentDO.setFileSize(size);
		attachmentDO.setFileUploadTime(DateUtils.getContentFormetDate(new Date()));
		attachmentDO.setFileUsedTime(attachmentDO.getFileUploadTime());
		attachmentDO.setContentType(contentType);
		attachmentDO.setTicketId(-1);// 这里先设置为-1 因为实际工单还没生成 当用户点击提交按钮式生成
										// 但是数据库不能存空
		try {
			//先判断父文件是否存在
			File saveFile=new File(path);
			if(!saveFile.getParentFile().exists()){
				saveFile.getParentFile().mkdirs();
			}
			file.transferTo(saveFile); // 保存文件
			return attachmentDO;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "操作失败";
		}
	}

	@RequestMapping("fileDownload")
	@ResponseBody
	public String fileDownload(@RequestParam("mes") String mes, HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		// 首先得到从前台得到的两个id可以唯一确定attachment的主键
		String[] mesStrings = mes.split("/");
		Integer ticketFileldId = Integer.parseInt(mesStrings[0]);
		Integer ticketId = Integer.parseInt(mesStrings[1]);
		// 从数据库获取有文件名字 和 实际存储位置的attachment对象
		AttachmentDO attachment = attachmentService.getAttachmentByTicket(ticketId, ticketFileldId);
		//更新文件最近一次被修改或者下载的日期
		attachment.setFileUsedTime(DateUtils.getContentFormetDate(new Date()));
		attachmentService.updateFileUsedTime(attachment);
		String fileName = attachment.getFileName();// 文件名
		if (fileName != null) {
			// 设置文件路径
			File file = new File(attachment.getPath());
			if (file.exists()) {
				response.setContentType("multipart/form-data;charset=utf-8");// 设置强制下载不打开
				response.setCharacterEncoding("utf-8");
				response.addHeader("Content-Disposition",
						"attachment;fileName=" + new String(fileName.getBytes("gbk"), "iso8859-1"));// 设置文件名
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
					os.flush();
					return "";
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return "下载失败";
	}
}
