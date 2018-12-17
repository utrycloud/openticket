package com.utry.openticket.controller;

/**
 * @Description : 自定义列controller
 * @author : LVDING
 * @version : 1.0
 * @date : 2018/07/26
 */

import com.alibaba.fastjson.JSON;
import com.utry.openticket.constant.Constant;
import com.utry.openticket.dto.TicketFieldDTO;
import com.utry.openticket.model.FieldTypeDO;
import com.utry.openticket.model.FieldTypeValueDO;
import com.utry.openticket.model.TicketTypeDO;
import com.utry.openticket.service.FieldTypeService;
import com.utry.openticket.service.TicketFieldService;
import com.utry.openticket.service.TicketTypeService;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketFieldController {

    @Autowired
    private FieldTypeService fieldTypeService;
    @Autowired
    private TicketTypeService ticketTypeService;
    @Autowired
    private TicketFieldService ticketFieldService;

    private static Logger logger = LoggerFactory.getLogger(TicketFieldController.class);
    /**
     *
     * 功能描述 : 跳转到添加自定义列页面
     *
     * @param : Model
     * @return :
     * @auther : LVDING
     * @date : 2018-07-26
     */
    @RequestMapping("addField")
    public String addFieldsPage(Model map, String ticketTypeId){
        List<FieldTypeDO> fieldTypeDOList = fieldTypeService.getFieldTypeList();
        //工单类型列表
        List<TicketTypeDO> ticketTypeDOList = ticketTypeService.getTicketTypeList();
        logger.info("fieldTypeDOList" + fieldTypeDOList.toString());
        logger.info("ticketTypeDOList" + ticketTypeDOList);
        map.addAttribute("fieldTypeList", fieldTypeDOList);
        map.addAttribute("ticketTypeList", ticketTypeDOList);
        map.addAttribute("ticketTypeId", ticketTypeId);
        return "/add_field";
    }

    /**
     *
     * 功能描述 : 添加自定义列
     *
     * @param : jsonObj(TicketFieldDTO) 自定义列对象
     * @return : String 结果
     * @auther : LVDING
     * @date : 2018-07-26
     */
    @RequestMapping("saveField")
    @ResponseBody
    public String saveFields(@RequestBody String jsonObj){
        TicketFieldDTO ticketFieldDTO = (TicketFieldDTO)JSON.parseObject(jsonObj,TicketFieldDTO.class);

        //判断加入的是什么样的类型
        if(!Strings.isEmpty(ticketFieldDTO.getSelectType())){
        	//如果不为空才判断
        	switch (ticketFieldDTO.getSelectType()) {
        	//如果是文本 大文本 文件 日期的话 只保存字段
			case Constant.DATE_TYPE:
			case Constant.TEXT_AREA_TYPE:
			case Constant.FILE_TYPE:
			case Constant.TEXT_TYPE:
				ticketFieldService.saveTicketField(ticketFieldDTO);
				break;
			//如果是单选 多选 下拉的话 还要保存可选属性
			case Constant.CHECKBOX_TYPE:
			case Constant.RADIO_TYPE:
			case Constant.SELECT_TYPE:
				List<String> selectValuelist = ticketFieldDTO.getSelectValueList();
	            List<FieldTypeValueDO> fieldTypeValueList = new ArrayList<>();
	            for(String str:selectValuelist){
	                FieldTypeValueDO fieldTypeValueDO = new FieldTypeValueDO();
	                fieldTypeValueDO.setValue(str);
	                fieldTypeValueList.add(fieldTypeValueDO);
	            }
	            ticketFieldService.saveTicketField(ticketFieldDTO,fieldTypeValueList);
				break;
			default:
				break;
			}
        }

        return "success";
    }

}
