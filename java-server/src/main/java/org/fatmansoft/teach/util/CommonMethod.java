package org.fatmansoft.teach.util;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.security.services.UserDetailsImpl;
import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

/**
 * CommonMethod 公共处理方法实例类
 */
public class CommonMethod {
    public static final MediaType exelType = new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    public static DataResponse getReturnData(Object obj, String msg){
        return new   DataResponse(0,obj,msg);
    }
    public static DataResponse getReturnMessage(Integer code, String msg){
        return new   DataResponse(code,null,msg);
    }
    public static  DataResponse getReturnData(Object obj){
        return getReturnData(obj,null);
    }
    public static DataResponse getReturnMessageOK(String msg){
        return getReturnMessage(0, msg);
    }
    public static DataResponse getReturnMessageOK(){
        return getReturnMessage(0, null);
    }
    public static DataResponse getReturnMessageError(String msg){
        return getReturnMessage(1, msg);
    }

    /**
     * Integer getUserId() 获得用户的userId 该static 方法可以在任何的Controller和Service中使用，可以获得当前登录用户的用户ID，可以通过在Web请求服务中使用该方法获取当前请求客户的信息
     * @return
     */
    public static Integer getUserId(){
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails != null)
            return userDetails.getId();
        else
            return null;
    }

    public static String getNextNum3( String num) {
        String str;
        String prefix;
        if(num.length() == 3) {
            str = num;
            prefix= "";
        }
        else {
            str = num.substring(num.length() - 3, num.length());
            prefix = num.substring(0,num.length() - 3);
        }
        int c;
        if(str.charAt(0)=='0') {
            if(str.charAt(1)=='0') {
                c = str.charAt(2)-'0';
            }else {
                c = (str.charAt(1)-'0')*10 + str.charAt(2)-'0';
            }
        }else {
            c = (str.charAt(0)-'0')*100  +(str.charAt(1)-'0')*10 + str.charAt(2)-'0';
        }
        c++;
        if(c < 10) {
            return prefix+"00" + c;
        }else if(c < 100) {
            return prefix+"0" + c;
        }else {
            return prefix+ c;
        }
    }
    public static String getNextNum4( String num) {
        String str;
        String prefix;
        if(num.length() == 4) {
            str = num;
            prefix= "";
        }
        else {
            str = num.substring(num.length() - 4, num.length());
            prefix = num.substring(0,num.length() - 4);
        }
        int c;
        if(str.charAt(0)=='0') {
            if (str.charAt(1) == '0') {
                if (str.charAt(2) == '0') {
                    c = str.charAt(3) - '0';
                } else {
                    c = (str.charAt(2) - '0') * 10 + str.charAt(3) - '0';
                }
            } else {
                c = (str.charAt(1) - '0') * 100 + (str.charAt(2) - '0') * 10 + str.charAt(3) - '0';
            }
        }else {
            c = (str.charAt(0) - '0') * 1000 + (str.charAt(1) - '0') * 100 + (str.charAt(2) - '0') * 10 + str.charAt(3) - '0';
        }
        c++;
        if(c < 10) {
            return prefix+"000" + c;
        }else if(c < 100) {
            return prefix+"00" + c;
        }else if(c < 1000){
            return prefix + "0" + c;
        }else {
            return prefix+ c;
        }
    }
    public static String[] getStrings(Map data,String key){
        Object obj = data.get(key);
        if(obj == null)
            return new String[]{};
        if(obj instanceof String[])
            return (String[])obj;
        return new String[]{};
    }

    public static String getString(Map data,String key){
        Object obj = data.get(key);
        if(obj == null)
            return "";
        if(obj instanceof String)
            return (String)obj;
        return obj.toString();
    }
    public static Boolean getBoolean(Map data,String key){
        Object obj = data.get(key);
        if(obj == null)
            return false;
        if(obj instanceof Boolean)
            return (Boolean)obj;
        if("true".equals(obj.toString()))
            return true;
        else
            return false;
    }
    public static List getList(Map data, String key){
        Object obj = data.get(key);
        if(obj == null)
            return new ArrayList();
        if(obj instanceof List)
            return (List)obj;
        else
            return new ArrayList();
    }
    public static Map getMap(Map data,String key){
        Object obj = data.get(key);
        if(obj == null)
            return new HashMap();
        if(obj instanceof Map)
            return (Map)obj;
        else
            return new HashMap();
    }

    public static Integer getInteger(Map data,String key) {
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof Integer)
            return (Integer)obj;
        String str = obj.toString();
        try {
            return Integer.getInteger(str);
        }catch(Exception e) {
            return null;
        }
    }
    public static Integer getInteger0(Map data,String key) {
        Object obj = data.get(key);
        if(obj == null)
            return 0;
        if(obj instanceof Integer)
            return (Integer)obj;
        String str = obj.toString();
        try {
            return Integer.getInteger(str);
        }catch(Exception e) {
            return 0;
        }
    }
    public static Long getLong(Map data,String key) {
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof Long)
            return (Long)obj;
        String str = obj.toString();
        try {
            return Long.getLong(str);
        }catch(Exception e) {
            return null;
        }
    }

    public static Double getDouble(Map data,String key) {
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof Double)
            return (Double)obj;
        String str = obj.toString();
        try {
            return Double.parseDouble(str);
        }catch(Exception e) {
            return null;
        }
    }
    public static Double getDouble0(Map data,String key) {
        Double d0 = 0d;
        Object obj = data.get(key);
        if(obj == null)
            return d0;
        if(obj instanceof Double)
            return (Double)obj;
        String str = obj.toString();
        try {
            return Double.parseDouble(str);
        }catch(Exception e) {
            return d0;
        }
    }
    public static Date getDate(Map data, String key) {
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof Date)
            return (Date)obj;
        String str = obj.toString();
        return DateTimeTool.formatDateTime(str,"yyyy-MM-dd");
    }
    public static Date getTime(Map data,String key) {
        Object obj = data.get(key);
        if(obj == null)
            return null;
        if(obj instanceof Date)
            return (Date)obj;
        String str = obj.toString();
        return DateTimeTool.formatDateTime(str,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * XSSFCellStyle createCellStyle(XSSFWorkbook workbook, int fontSize) Excl 导出 Cell 属性初始方法
     * @param workbook
     * @param fontSize
     * @return
     */
    public static XSSFCellStyle createCellStyle(XSSFWorkbook workbook, int fontSize) {
        XSSFFont font = workbook.createFont();
        //在对应的workbook中新建字体
        font.setFontName("微软雅黑");
        //字体微软雅黑
        font.setFontHeightInPoints((short) fontSize);
        //设置字体大小
        XSSFCellStyle style = workbook.createCellStyle();
        //新建Cell字体
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * String replaceNameValue(String html, Map<String,String>m) 将用户HTML模板中参数替换为Map里面的值，参数名对应Map中的Key
     * @param html
     * @param m
     * @return
     */
    public static String replaceNameValue(String html, Map<String,String>m) {
        if(html== null || html.length() == 0)
            return html;
        StringBuffer buf = new StringBuffer();
        StringTokenizer sz = new StringTokenizer(html,"$");
        if(sz.countTokens()<=1)
            return html;
        String str,key,value;
        Integer index;
        while(sz.hasMoreTokens()) {
            str = sz.nextToken();
            if(str.charAt(0)== '{') {
                index = str.indexOf("}",1);
                key = str.substring(1,index);
                value = m.get(key);
                if(value== null){
                    value = "";
                }
                buf.append(value+str.substring(index+1,str.length()));
            }else {
                buf.append(str);
            }
        }
        return buf.toString();
    }

    /**
     * String addHeadInfo(String html,String head) 增加HTML中的Head信息 可以修改扩展
     * @param html
     * @param head
     * @return
     */
    public static String addHeadInfo(String html,String head) {
        int index0 = html.indexOf("<head>");
        int index1 = html.indexOf("</head>");
        return html.substring(0,index0+6)+head + html.substring(index1,html.length());
    }

    /**
     * String removeErrorString(String html,String ... subs)  移除 html中 PDF转换无法识别的信息 可以修改扩展
      * @param html
     * @param subs
     * @return
     */
    public static String removeErrorString(String html,String ... subs) {
        if(html== null || html.length() == 0)
            return html;
        StringBuffer buf;
        int index;
        int oldIndex;
        int slen;
        String content = html;
        for(String sub : subs) {
            slen = sub.length();
            buf = new StringBuffer();
            index = 0;
            oldIndex = 0;
            while(index >= 0) {
                index = content.indexOf(sub,oldIndex);
                if(index > 0) {
                    buf.append(content.substring(oldIndex,index));
                    oldIndex = index+slen;
                }else {
                    buf.append(content.substring(oldIndex,content.length()));
                }
            }
            content = buf.toString();
        }
        return content;
    }

}
