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
import com.utry.openticket.dao.UserDAO;
import com.utry.openticket.dto.TicketValueDTO;
import com.utry.openticket.constant.Constant;
import com.utry.openticket.dto.TicketDTO;
import com.utry.openticket.dto.TicketFieldDTO;
import com.utry.openticket.dto.UserDTO;
import com.utry.openticket.model.*;
import com.utry.openticket.service.*;
import com.utry.openticket.util.DateUtils;
import com.utry.openticket.util.FileUtil;

import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(TicketController.class);

    /**
     * 功能描述 : 打开table_future页面
     *
     * @param : Model
     * @return :
     * @auther : LVDING
     * @date : 2018-07-26
     */
    @RequestMapping("index")
    public String index(Model model, @RequestParam(defaultValue = "4") String ticketTypeId
            , @RequestParam(defaultValue = "0") String timeType) {
        List<TicketTypeDO> ticketTypeList = ticketTypeService.getTicketTypeList();
        String ticketTypeName = ticketTypeName(ticketTypeList, ticketTypeId);
        model.addAttribute("ticketTypeList", ticketTypeList);
        model.addAttribute("ticketTypeId", ticketTypeId);
        model.addAttribute("ticketType", ticketTypeName);
        model.addAttribute("type", timeType);
        return "/tables";
    }

    private String ticketTypeName(List<TicketTypeDO> ticketTypeList, String ticketTypeId) {
        String result = "ticketTypeName Error";
        for (TicketTypeDO ticketTypeDO : ticketTypeList) {
            if (ticketTypeDO.getId() == Integer.parseInt(ticketTypeId)) {
                result = ticketTypeDO.getName();
            }
        }
        return result;
    }


    /**
     * 功能描述 : 获得工单的自定义列
     *
     * @param : String ticketType 工单类型
     * @return : List<TicketFieldDO> 自定义列List
     * @auther : LVDING
     * @date : 2018-07-26
     */
    @RequestMapping("getColumn")
    public @ResponseBody
    List<TicketFieldDTO> getTicketColumn(@RequestParam Integer ticketType) {
        List<TicketFieldDTO> ticketFieldList = ticketFieldService.getColumnByTicketId(ticketType);
        return ticketFieldList;
    }

    /**
     * 功能描述 : 获得基础工单信息
     *
     * @param : String ticketType 工单类型
     * @return : List<TicketDTO> 基础工单List
     * @auther : LVDING
     * @date : 2018-07-26
     */
    @RequestMapping("getTicket")
    public @ResponseBody
    List<Map<String, String>> getTicket(@RequestParam String ticketTypeId
            , @RequestParam(defaultValue = "0") String timeType) {
        TimeDO time = new TimeDO();
        if ("1".equals(timeType)) {
            time = getCurrentWeek(time);
        }
        List<TicketDTO> ticketList = ticketService.getTicketList(ticketTypeId, time);
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

    public TimeDO getCurrentWeek(TimeDO timeDO) {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(cal.getTime());
        endCal.add(Calendar.DAY_OF_WEEK, 6);
        endCal.add(Calendar.DATE, 1);
        Date weekEndSta = endCal.getTime();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("开始日期：" + formatter.format(cal.getTime()));
        System.out.println("结束日期：" + formatter.format(weekEndSta));

        timeDO.setStartTime(formatter.format(cal.getTime()));
        timeDO.setEndTime(formatter.format(weekEndSta));
        return timeDO;
    }

    /**
     * 功能描述 : 跳转到添加工单页面
     *
     * @param : Model
     * @return :
     * @auther : LVDING
     * @date : 2018-07-26
     */
    @RequestMapping("addTicket")
    public String addFieldsPage(@RequestParam Integer ticketType, Model model) {
        String ticketName = ticketTypeService.getTicketNameById(ticketType.toString());
        List<TicketFieldDTO> ticketFieldList = ticketFieldService.getColumnByTicketId(ticketType);
        for (TicketFieldDTO t : ticketFieldList) {
            t.setSelectValueList(fieldTypeValueService.getFieldTypeValue(t.getId()));
            if (Constant.CHECKBOX_TYPE.equals(t.getSelectType())) {
                if (Strings.isNotEmpty(t.getDefaultValue())) {
                    String[] values = t.getDefaultValue().split("/");
                    t.setCheckBoxValue(Arrays.asList(values));
                }
            }
        }
        model.addAttribute("ticketType", ticketName);
        model.addAttribute("ticketTypeId", ticketType);
        model.addAttribute("ticketFieldList", ticketFieldList);
        logger.info("ticketType:" + ticketType);
        logger.info("ticketName:" + ticketName);
        logger.info("ticketFieldList:" + ticketFieldList);
        return "/form_future";
    }


    /**
     * 表单渲染页面
     *
     * @param : Model
     * @return :
     * @auther : robotbird
     * @date : 2018-11-26
     */
    @RequestMapping("form")
    public String renderForm(@RequestParam String ticketType, Model model) {
        List<TicketFieldDTO> ticketFieldList = ticketFieldService.getColumn(ticketType);
        for (TicketFieldDTO t : ticketFieldList) {
            t.setSelectValueList(fieldTypeValueService.getFieldTypeValue(t.getId()));
            //System.out.println(t);
        }
        model.addAttribute("ticketType", ticketType);
        model.addAttribute("ticketFieldList", ticketFieldList);
        return "/forms";
    }

    /**
     * 功能描述 : 添加工单
     * 自定义列
     *
     * @param : jsonObj(TicketDTO) 工单对象
     * @return : String 结果
     * @auther : LVDING
     * @date : 2018-07-26
     */
    @RequestMapping("saveTicket")
    @ResponseBody
    public String saveTicket(@RequestBody String jsonObj, HttpSession session) {
        // 为了解决多选框选择的值在 数据库 表中的存储问题----开始
        JSONObject jsonObject = JSON.parseObject(jsonObj);
        JSONArray jsonArray = (JSONArray) jsonObject.get("ticketValueList");
        jsonObject.put("ticketValueList", takeOutRepetition(jsonArray));
        // 为了解决多选框选择的值在 数据库 表中的存储问题----结束
        TicketDTO ticket = (TicketDTO) JSON.parseObject(jsonObject.toString(), TicketDTO.class);
        UserDO login = (UserDO) session.getAttribute("login");
        if (login == null) {
            UserDTO clientUser = new UserDTO();
            clientUser.setUsername("client");
            clientUser.setPassword("123456");
            userService.getUser(clientUser);
            ticket.setCreateUser(clientUser.getUsername());
        } else {
            ticket.setCreateUser(login.getUsername());
        }
        List<TicketValueDO> ticketValueList = ticket.getTicketValueList();
        /**
         * 由于之前单选框和多选框如果都没有选的话ticketValueList得不到值
         * 会造成数据库不存数据 实际上应该存入空值
         * 下面是修改结果
         * 思路是 先去数据库查一下ticketValueList是否包含该工单全部的值 如果不包含 就加上
         */
        List<TicketFieldDTO> ticketFieldDTOs = ticketFieldService.getColumn(jsonObject.getString("ticketType"));
        for (TicketFieldDTO ticketFieldDTO : ticketFieldDTOs) {
            boolean flag = true;
            for (TicketValueDO ticketValueDO : ticketValueList) {
                if (ticketFieldDTO.getId().equals(ticketValueDO.getFieldId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                TicketValueDO ticketValueDO = new TicketValueDO();
                ticketValueDO.setFieldId(ticketFieldDTO.getId());
                ticketValueDO.setValue("");
                ticketValueList.add(ticketValueDO);
            }
        }
        // 判断是否包括文件上传
        if (isHaveFile(ticketFieldDTOs)) {
            JSONObject fileJsonObject = (JSONObject) jsonObject.get("inserts");
            // 有文件就先对事先放弃的文件进行处理
            if (jsonObject.get("giveUpFile") != null) {
                // 判断是否有被放弃的文件 如果有就删除
                Object giveUpFile = jsonObject.get("giveUpFile");
                FileUtil.delFileByPath(handleString(giveUpFile.toString()));
            }
            // 保存的文件名处理
            for (TicketValueDO ticketValueDO : ticketValueList) {
                for (String key : fileJsonObject.keySet()) {
                    JSONObject jsonObject2 = (JSONObject) fileJsonObject.get(key);
                    AttachmentDO attachmentDO = JSON.parseObject(jsonObject2.toJSONString(), AttachmentDO.class);
                    if (attachmentDO.getTicketFileldId().equals(ticketValueDO.getFieldId())) {
                        ticketValueDO.setValue(attachmentDO.getFileName());
                    }
                }
            }
            int ticketId = ticketService.saveTicket(ticket, ticketValueList);
            /**
             * 对处理fileJsonObject 对fileJsonObject遍历取出value转成AttachmentDO
             * 设置ticketId 因为之前没有得到 然后保存
             */
            for (String key : fileJsonObject.keySet()) {
                JSONObject jsonObject2 = (JSONObject) fileJsonObject.get(key);
                AttachmentDO attachmentDO = JSON.parseObject(jsonObject2.toJSONString(), AttachmentDO.class);
                attachmentDO.setTicketId(ticketId);
                attachmentService.saveAttachment(attachmentDO);
            }

        } else {
            ticketService.saveTicket(ticket, ticketValueList);
        }
        return "success";
    }

    /**
     * 功能描述 : 删除工单
     * 自定义列
     *
     * @param : Integer id 工单编号
     * @return : String 结果
     * @auther : LVDING
     * @date : 2018-07-26
     */
    @RequestMapping("deleteTicket")
    @ResponseBody
    public String deleteTicket(@RequestParam Integer id) {
        ticketService.deleteTicket(id);
        attachmentService.deleteAttachmentByTicketId(id);
        return "success";
    }

    /**
     * 功能描述 : 跳转到编辑工单页面
     *
     * @param : Model
     * @return :
     * @auther : LVDING
     * @date : 2018-08-01
     */
    @RequestMapping("updateTicketPage")
    public String updateTicket(@RequestParam int id, @RequestParam String ticketType, Model model) {
        String ticketName = ticketTypeService.getTicketNameById(ticketType);
        List<TicketFieldDTO> ticketFieldList = ticketFieldService.getUpdateColumn(id, ticketName);
        for (TicketFieldDTO t : ticketFieldList) {
            t.setSelectValueList(fieldTypeValueService.getFieldTypeValue(t.getId()));
            // 判断如果是多选框的话 就设置checkBoxValue的值来存放多选的所有结果
            if (Constant.CHECKBOX_TYPE.equals(t.getSelectType())) {
                //如果不为空的话 就取出值 为空就设一个空的默认值
                if (!Strings.isEmpty(t.getValue())) {
                    String[] values = t.getValue().split("/");
                    t.setCheckBoxValue(Arrays.asList(values));
                } else {
                    List<String> list = new ArrayList<String>();
                    list.add("");
                    t.setCheckBoxValue(list);
                }
            }
        }
        model.addAttribute("ticketId", id);
        model.addAttribute("ticketTypeId", ticketType);
        model.addAttribute("ticketType", ticketName);
        model.addAttribute("ticketFieldList", ticketFieldList);
        return "/table_update";
    }

    /**
     * 功能描述 : 添加工单
     * 自定义列
     *
     * @param : jsonObj(TicketDTO) 工单对象
     * @return : String 结果
     * @auther : LVDING
     * @date : 2018-08-01
     */
    @RequestMapping("updateTicket")
    @ResponseBody
    public String updateTicket(@RequestBody String jsonObj) {
        // 为了解决多选框选择的值在 数据库 表中的存储问题 将多选值合成一个并用/分开----开始
        JSONObject jsonObject = JSON.parseObject(jsonObj);
        JSONArray jsonArray = (JSONArray) jsonObject.get("ticketValueList");
        jsonObject.put("ticketValueList", takeOutRepetition(jsonArray));
        TicketDTO ticket = (TicketDTO) JSON.parseObject(jsonObject.toString(), TicketDTO.class);

        int ticketId = ticket.getId();
        List<TicketValueDO> ticketValueList = ticket.getTicketValueList();
        /**
         * 修改 如果修改单选或者多选什么都不选 就不显示这两个的bug
         * 思路是 先去数据库查一下ticketValueList是否包含该工单全部的值 如果不包含 就加上
         */
        List<TicketFieldDTO> ticketFieldDTOs = ticketFieldService.getColumn(jsonObject.getString("ticketType"));
        for (TicketFieldDTO ticketFieldDTO : ticketFieldDTOs) {
            boolean flag = true;
            for (TicketValueDO ticketValueDO : ticketValueList) {
                if (ticketFieldDTO.getId().equals(ticketValueDO.getFieldId())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                TicketValueDO ticketValueDO = new TicketValueDO();
                ticketValueDO.setFieldId(ticketFieldDTO.getId());
                ticketValueDO.setValue("");
                ticketValueList.add(ticketValueDO);
            }
        }
        // 判断是否包括文件上传
        if (isHaveFile(ticketFieldDTOs)) {
            // 1.有文件就先对事先放弃的文件进行处理
            JSONObject fileJsonObject = (JSONObject) jsonObject.get("inserts");
            if (!"".equals(jsonObject.get("giveUpFile")) && jsonObject.get("giveUpFile") != null) {
                // 判断是否有被放弃的文件 如果有就删除
                Object giveUpFile = jsonObject.get("giveUpFile");
                FileUtil.delFileByPath(handleString(giveUpFile.toString()));
            }
            // 2.对之前保存的文件删除 并将新的文件保存到附件上传表
            //储存要被删除的文件的path
            List<String> delList = new ArrayList<String>();
            for (String key : fileJsonObject.keySet()) {
                JSONObject attachmentObject = (JSONObject) fileJsonObject.get(key);
                //这个得到的是新增的attachmentDO
                AttachmentDO attachmentDO = JSON.parseObject(attachmentObject.toJSONString(), AttachmentDO.class);
                //这个得到的是要被替换的attachmentDO
                AttachmentDO delAttachment = attachmentService.getAttachmentByTicket(ticketId, attachmentDO.getTicketFileldId());
                if (delAttachment != null) {
                    //如果这是修改数据的话 就进行更新操作
                    delList.add(delAttachment.getPath());
                    attachmentDO.setId(delAttachment.getId());
                    attachmentDO.setTicketId(ticketId);
                    attachmentService.updateAttachment(attachmentDO);
                } else {
                    //否则就是增加新数据
                    attachmentDO.setTicketId(ticketId);
                    attachmentService.saveAttachment(attachmentDO);
                }
            }
            FileUtil.delFileByPath(delList);
            // 3.对保存的文件名处理 设置ticketId 将文件名字设置为正常的文件名 并更新到属性值表中
            for (TicketValueDO ticketValueDO : ticketValueList) {
                ticketValueDO.setTicketId(ticketId);
                if ("".equals(ticketValueDO.getValue())) {
                    String value = ticketValueService.getValue(ticketValueDO);
                    if (Strings.isNotEmpty(value)) {
                        ticketValueDO.setValue(value);
                    }
                }
                //从文件保存的json对象中取出对象 的实际名字
                for (String key : fileJsonObject.keySet()) {
                    JSONObject attachmentObject = (JSONObject) fileJsonObject.get(key);
                    AttachmentDO attachmentDO = JSON.parseObject(attachmentObject.toJSONString(), AttachmentDO.class);
                    if (attachmentDO.getTicketFileldId().equals(ticketValueDO.getFieldId())) {
                        ticketValueDO.setValue(attachmentDO.getFileName());
                    }
                }
            }
            ticketValueService.updateTicketValueList(ticketValueList);
        } else {
            //如果没有文件上传的话
            for (TicketValueDO ticketValue : ticketValueList) {
                ticketValue.setTicketId(ticketId);
            }
            ticketValueService.updateTicketValueList(ticketValueList);
        }
        return "success";
    }

    /**
     * 判断是否包括文件上传
     *
     * @param ticketFieldDTOs
     * @return
     */
    private boolean isHaveFile(List<TicketFieldDTO> ticketFieldDTOs) {
        // TODO Auto-generated method stub
        boolean flag = false;
        for (TicketFieldDTO ticketFieldDTO : ticketFieldDTOs) {
            if (Constant.FILE_TYPE.equals(ticketFieldDTO.getSelectType())) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 导出excel表格 选择导出全部
     *
     * @param request
     * @param response
     * @param ticketType 导出什么类型的表格 可以传递参数
     * @return
     * @throws IOException
     */
    @RequestMapping("exportXLs")
    public String exportXLs(HttpServletRequest request, HttpServletResponse response, @RequestParam String ticketType
            , @RequestParam(defaultValue = "0") String timeType) throws IOException {
        //得到所有的结果
        String ticketName = ticketTypeService.getTicketNameById(ticketType);
        List<Map<String, String>> resultList = getTicket(ticketType, timeType);
        // 创建hssfworkbook Excel的文档对象
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建新的sheet
        HSSFSheet sheet = wb.createSheet("用户信息");
        // 设置第一行
        HSSFRow row = sheet.createRow(0);
        // 设置属性 但是我现在也不知道有几行 所以要去数据库查
        List<TicketFieldDTO> ticketFieldDTOs = ticketFieldService.getColumn(ticketName);
        //现在只需要一个TicketFieldDTO的name属性 并且需要其他的默认属性 新建一个lsit<String>对象
        List<String> nameList = new ArrayList<String>();
        List<String> fileldNameList = new ArrayList<String>();
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
        for (int i = 0; i < nameList.size(); i++) {
            row.createCell(i).setCellValue(nameList.get(i));
        }

        //便利
        for (int i = 0; i < resultList.size(); i++) {
            HSSFRow rows = sheet.createRow(sheet.getLastRowNum() + 1);
            Map<String, String> map = resultList.get(i);
            //用来跟踪map的一个变量 因为该map的类型是linkedHashMap所以遍历取值时是可以保证顺序的
            for (int j = 0; j < map.size() - 1; j++) {
                rows.createCell(j).setCellValue(map.get(fileldNameList.get(j)));
            }
        }
        // 输出Excel文件
        // 获取输出流
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            response.reset();
            // 设置分区中文名
            String filename = ticketName + "信息";
            // 设置响应的编码
            response.setContentType("application/x-download");// 下面三行是关键代码，处理乱码问题
            response.setCharacterEncoding("utf-8");
            // 设置浏览器响应头对应的Content-disposition
            response.setHeader("Content-disposition",
                    "attachment;filename=" + new String(filename.getBytes("gbk"), "iso8859-1") + ".xls");
            // wb输出
            wb.write(output);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            output.close();
        }
        return null;
    }

    /**
     * @param request
     * @param response
     * @param pagNow   当前页面
     * @param pageSize 页面数据大小
     * @return
     * @throws IOException
     */
    @RequestMapping("exportPageXLs")
    public String exportPageXLs(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(defaultValue = "1") String ticketType,
                                @RequestParam("pageNow") String pagNow,
                                @RequestParam("pageSize") String pageSize) throws IOException {
        // 得到所有的结果
        String ticketName = ticketTypeService.getTicketNameById(ticketType);
        List<Map<String, String>> resultList = getTicketByPage(ticketName, pagNow, pageSize);
        // 创建hssfworkbook Excel的文档对象
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建新的sheet
        HSSFSheet sheet = wb.createSheet("用户信息");
        // 设置第一行
        HSSFRow row = sheet.createRow(0);
        // 设置属性 但是我现在也不知道有几行 所以要去数据库查
        List<TicketFieldDTO> ticketFieldDTOs = ticketFieldService.getColumn(ticketName);
        // 现在只需要一个TicketFieldDTO的name属性 并且需要其他的默认属性 新建一个lsit<String>对象
        List<String> nameList = new ArrayList<String>();
        List<String> fileldNameList = new ArrayList<String>();
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
        // 现在得到了所有的属性列 设置列名字
        for (int i = 0; i < nameList.size(); i++) {
            row.createCell(i).setCellValue(nameList.get(i));
        }

        // 便利
        for (int i = 0; i < resultList.size(); i++) {
            HSSFRow rows = sheet.createRow(sheet.getLastRowNum() + 1);
            Map<String, String> map = resultList.get(i);
            // 用来跟踪map的一个变量 因为该map的类型是linkedHashMap所以遍历取值时是可以保证顺序的
            for (int j = 0; j < map.size() - 1; j++) {
                rows.createCell(j).setCellValue(map.get(fileldNameList.get(j)));
            }
        }
        // 输出Excel文件
        // 获取输出流
        OutputStream output = response.getOutputStream();
        response.reset();
        // 设置分区中文名
        String filename = ticketName + "信息";
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
     * 分页查询 结果
     *
     * @param ticketType
     * @param pagNow
     * @param pageSize
     * @return
     */
    private List<Map<String, String>> getTicketByPage(String ticketType, String pagNow, String pageSize) {
        // TODO Auto-generated method stub
        List<TicketDTO> ticketList = ticketService.getTicketListByPage(ticketType, pagNow, pageSize);
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
     * 将重复的json的key去掉 保留value/间隔
     *
     * @param jsonArray
     * @return
     */
    public static JSONArray takeOutRepetition(JSONArray jsonArray) {

        JSONArray newJsonArray;
        Set<String> sets = new HashSet<String>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            sets.add((String) jsonObject.get("fieldId"));
        }
        List<String> list = new ArrayList<String>(sets);

        newJsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject object = new JSONObject();
            object.put("fieldId", list.get(i));
            object.put("value", "");

            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject object2 = (JSONObject) jsonArray.get(j);
                if (object2.get("fieldId").equals(list.get(i))) {
                    object.put("value", object.get("value") + "/" + object2.get("value"));
                    jsonArray.remove(j);
                    j--;
                }
            }
            object.put("value", ((String) object.get("value")).substring(1));
            newJsonArray.add(object);
        }

        return newJsonArray;
    }

    /**
     * 对字符串操作
     */
    public String[] handleString(String string) {
        string = string.substring(1);
        string = string.substring(0, string.length() - 1);
        return string.replace("\"", "").split(",");
    }

    @RequestMapping("/menu")
    @ResponseBody
    public List<Integer> getRoleTypeId(Integer userId) {
        return userService.getRoleTypeIdByUserId(userId);
    }
}
