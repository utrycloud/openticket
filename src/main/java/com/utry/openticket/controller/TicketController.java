package com.utry.openticket.controller;

/**
 * @Description : 工单controller
 * @author : LVDING
 * @version : 1.0
 * @date : 2018/07/26
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utry.openticket.dto.TicketValueDTO;
import com.utry.openticket.dto.TicketDTO;
import com.utry.openticket.dto.TicketFieldDTO;
import com.utry.openticket.model.TicketTypeDO;
import com.utry.openticket.model.TicketValueDO;
import com.utry.openticket.service.*;
import com.utry.openticket.util.DateUtils;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TicketController {

	@Autowired
	private TicketFieldService ticketFieldService;
	@Autowired
	private TicketService ticketService;
	@Autowired
	private TicketTypeService ticketTypeService;
	@Autowired
	private FieldTypeValueService fieldTypeValueService;
	@Autowired
	private TicketValueService ticketValueService;

	/**
	 *
	 * 功能描述 : 打开table_future页面
	 *
	 * @param :
	 *            Model
	 * @return :
	 * @auther : LVDING
	 * @date : 2018-07-26
	 */
	@RequestMapping("index")
	public String index(Model model, @RequestParam(defaultValue = "需求") String ticketType) {
		List<TicketTypeDO> ticketTypeList = ticketTypeService.getTicketTypeList();
		ticketService.getTicketList(ticketType);
		model.addAttribute("ticketTypeList", ticketTypeList);
		model.addAttribute("ticketType", ticketType);
		return "/tables";
	}

	@RequestMapping("index2")
	public String index2(Model model) {
		List<TicketTypeDO> ticketTypeList = ticketTypeService.getTicketTypeList();
		model.addAttribute("ticketTypeList", ticketTypeList);
		return "/table_future";
	}

	/**
	 *
	 * 功能描述 : 获得工单的自定义列
	 *
	 * @param :
	 *            String ticketType 工单类型
	 * @return : List<TicketFieldDO> 自定义列List
	 * @auther : LVDING
	 * @date : 2018-07-26
	 */
	@RequestMapping("getColumn")
	public @ResponseBody List<TicketFieldDTO> getTicketColumn(@RequestParam String ticketType) {
		List<TicketFieldDTO> ticketFieldList = ticketFieldService.getColumn(ticketType);
		return ticketFieldList;
	}

	/**
	 *
	 * 功能描述 : 获得基础工单信息
	 *
	 * @param :
	 *            String ticketType 工单类型
	 * @return : List<TicketDTO> 基础工单List
	 * @auther : LVDING
	 * @date : 2018-07-26
	 */
	@RequestMapping("getTicket")
	public @ResponseBody List<Map<String, String>> getTicket(@RequestParam String ticketType) {
		List<TicketDTO> ticketList = ticketService.getTicketList(ticketType);
		List<Map<String, String>> result = new ArrayList<>();
		for (TicketDTO ticket : ticketList) {
			Map<String, String> ticketMap = new LinkedHashMap<>();
			ticketMap.put("id", ticket.getId().toString());
			ticketMap.put("createTime", DateUtils.getContentFormetDate(ticket.getCreateTime()));
			ticketMap.put("createUser", ticket.getCreateUser());
			ticketMap.put("ticketType", ticket.getTicketType());
			int ticketId = ticket.getId();
			List<TicketValueDTO> ticketValueList = ticketValueService.getTicketValueList(ticketId);
			for (TicketValueDTO ticketValue : ticketValueList) {
				ticketMap.put(ticketValue.getFieldName(), ticketValue.getValue());
			}
			result.add(ticketMap);
		}
		return result;
	}

	/**
	 *
	 * 功能描述 : 跳转到添加工单页面
	 *
	 * @param :
	 *            Model
	 * @return :
	 * @auther : LVDING
	 * @date : 2018-07-26
	 */
	@RequestMapping("addTicket")
	public String addFieldsPage(@RequestParam String ticketType, Model model) {
		List<TicketFieldDTO> ticketFieldList = ticketFieldService.getColumn(ticketType);
		for (TicketFieldDTO t : ticketFieldList) {
			t.setSelectValueList(fieldTypeValueService.getFieldTypeValue(t.getId()));
		}
		model.addAttribute("ticketType", ticketType);
		model.addAttribute("ticketFieldList", ticketFieldList);

		return "/form_future";
	}

	/**
	 * 导出excel表格 选择导出全部
	 * @param request
	 * @param response
	 * @param ticketType 导出什么类型的表格 可以传递参数
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("exportXLs")
	public String exportXLs(HttpServletRequest request, HttpServletResponse response,@RequestParam(defaultValue = "需求") String ticketType) throws IOException {
		//得到所有的结果
		List<Map<String, String>> resultList= getTicket(ticketType);
		// 创建hssfworkbook Excel的文档对象
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建新的sheet
		HSSFSheet sheet = wb.createSheet("用户信息");
		// 设置第一行
		HSSFRow row = sheet.createRow(0);
		// 设置属性 但是我现在也不知道有几行 所以要去数据库查
		List<TicketFieldDTO> ticketFieldDTOs=getTicketColumn(ticketType);
		//现在只需要一个TicketFieldDTO的name属性 并且需要其他的默认属性 新建一个lsit<String>对象
		List<String> nameList=new ArrayList<String>();
		List<String> fileldNameList=new ArrayList<String>();
		fileldNameList.add("id");
		fileldNameList.add("createTime");
		fileldNameList.add("createUser");
		nameList.add("序号");
		nameList.add("创建时间");
		nameList.add("创建用户");
		for (TicketFieldDTO ticketFieldDTO : ticketFieldDTOs) {
			nameList.add(ticketFieldDTO.getName());
			fileldNameList.add(ticketFieldDTO.getFieldName());
		}
		//现在得到了所有的属性列 设置列名字
		for (int i=0;i<nameList.size();i++) {
			row.createCell(i).setCellValue(nameList.get(i));
		}
		
		//便利
		for(int i = 0;i<resultList.size();i++){
			HSSFRow rows = sheet.createRow(sheet.getLastRowNum() + 1);
			Map<String,String> map = resultList.get(i);
			//用来跟踪map的一个变量 因为该map的类型是linkedHashMap所以遍历取值时是可以保证顺序的
			for(int j=0;j<map.size()-1;j++){
				rows.createCell(j).setCellValue(map.get(fileldNameList.get(j)));
			}
		}
		// 输出Excel文件
		// 获取输出流
		OutputStream output = response.getOutputStream();
		response.reset();
		// 设置分区中文名
		String filename = ticketType+"信息";
		// 设置响应的编码
		response.setContentType("application/x-download");// 下面三行是关键代码，处理乱码问题
		response.setCharacterEncoding("utf-8");
		// 设置浏览器响应头对应的Content-disposition
		response.setHeader("Content-disposition",
				"attachment;filename=" + new String(filename.getBytes("gbk"), "iso8859-1") + ".xls");
		// wb输出
		wb.write(output);
		output.close();
		return null;
	}

	/**
	 *
	 * 表单渲染页面
	 *
	 * @param :
	 *            Model
	 * @return :
	 * @auther : robotbird
	 * @date : 2018-11-26
	 */
	@RequestMapping("form")
	public String renderForm(@RequestParam String ticketType, Model model) {
		List<TicketFieldDTO> ticketFieldList = ticketFieldService.getColumn(ticketType);
		for (TicketFieldDTO t : ticketFieldList) {
			t.setSelectValueList(fieldTypeValueService.getFieldTypeValue(t.getId()));
			// System.out.println(t);
		}
		model.addAttribute("ticketType", ticketType);
		model.addAttribute("ticketFieldList", ticketFieldList);
		return "/forms";
	}

	/**
	 *
	 * 功能描述 : 添加工单 自定义列
	 * 
	 * @param :
	 *            jsonObj(TicketDTO) 工单对象
	 * @return : String 结果
	 * @auther : LVDING
	 * @date : 2018-07-26
	 */
	@RequestMapping("saveTicket")
	@ResponseBody
	public String saveTicket(@RequestBody String jsonObj) {
		JSONObject jsonObject = JSON.parseObject(jsonObj);
		JSONArray jsonArray=(JSONArray) jsonObject.get("ticketValueList");
		jsonObject.put("ticketValueList", takeOutRepetition(jsonArray));
		TicketDTO ticket = (TicketDTO) JSON.parseObject(jsonObject.toString(), TicketDTO.class);
		String ticketType = ticket.getTicketType();
		ticket.setCreateUser("张三");
		List<TicketValueDO> ticketValueList = ticket.getTicketValueList();
		ticketService.saveTicket(ticket, ticketValueList);
		return "success";
	}

	/**
	 *
	 * 功能描述 : 删除工单 自定义列
	 * 
	 * @param :
	 *            Integer id 工单编号
	 * @return : String 结果
	 * @auther : LVDING
	 * @date : 2018-07-26
	 */
	@RequestMapping("deleteTicket")
	@ResponseBody
	public String deleteTicket(@RequestParam Integer id) {
		ticketService.deleteTicket(id);
		return "success";
	}

	/**
	 *
	 * 功能描述 : 跳转到编辑工单页面
	 *
	 * @param :
	 *            Model
	 * @return :
	 * @auther : LVDING
	 * @date : 2018-08-01
	 */
	@RequestMapping("updateTicketPage")
	public String updateTicket(@RequestParam int id, @RequestParam String ticketType, Model model) {
		List<TicketFieldDTO> ticketFieldList = ticketFieldService.getUpdateColumn(id, ticketType);
		for (TicketFieldDTO t : ticketFieldList) {
			t.setSelectValueList(fieldTypeValueService.getFieldTypeValue(t.getId()));
		}
		model.addAttribute("ticketId", id);
		model.addAttribute("ticketFieldList", ticketFieldList);
		return "/table_update";
	}

	/**
	 *
	 * 功能描述 : 添加工单 自定义列
	 * 
	 * @param :
	 *            jsonObj(TicketDTO) 工单对象
	 * @return : String 结果
	 * @auther : LVDING
	 * @date : 2018-08-01
	 */
	@RequestMapping("updateTicket")
	@ResponseBody
	public String updateTicket(@RequestBody String jsonObj) {
		TicketDTO ticket = (TicketDTO) JSON.parseObject(jsonObj, TicketDTO.class);
		int id = ticket.getId();
		List<TicketValueDO> ticketValueList = ticket.getTicketValueList();
		for (TicketValueDO ticketValue : ticketValueList) {
			ticketValue.setTicketId(id);
		}
		ticketValueService.updateTicketValueList(ticketValueList);
		return "success";
	}
	
	/**
	 * 将重复的json的key去掉 保留value#间隔
	 * @param jsonArray
	 * @return
	 */
	public static JSONArray takeOutRepetition(JSONArray jsonArray) {

		JSONArray newJsonArray;
		Set<String> sets=new HashSet<String>();
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject=(JSONObject) jsonArray.get(i);
			sets.add((String) jsonObject.get("fieldId"));
		}
		List<String> list=new ArrayList<String>(sets);
		
		newJsonArray = new JSONArray();
		for(int i=0;i<list.size();i++){
			JSONObject object=new JSONObject();
			object.put("fieldId",list.get(i));
			object.put("value", "");
			
			for(int j=0;j<jsonArray.size();j++){
				JSONObject object2=(JSONObject) jsonArray.get(j);
				if(object2.get("fieldId").equals(list.get(i))){
					object.put("value", object.get("value")+"/"+object2.get("value"));
					jsonArray.remove(j);
					j--;
				}
			}
			object.put("value", ((String)object.get("value")).substring(1));
			newJsonArray.add(object);
		}
		
		return newJsonArray;
	}
}
