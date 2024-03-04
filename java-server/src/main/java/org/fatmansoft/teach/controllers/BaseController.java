package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.payload.response.MyTreeNode;
import org.fatmansoft.teach.payload.response.OptionItem;
import org.fatmansoft.teach.payload.response.OptionItemList;
import org.fatmansoft.teach.repository.DictionaryInfoRepository;
import org.fatmansoft.teach.repository.MenuInfoRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.repository.UserTypeRepository;
import org.fatmansoft.teach.service.BaseService;
import org.fatmansoft.teach.util.ComDataUtil;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

/**
 *  BaseController 主要时为前台框架的基本数据管理提供的Web请求服务
 *
 */
// origins： 允许可访问的域列表
// maxAge:准备响应前的缓存持续的最大时间（以秒为单位）。
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/base")

public class BaseController {
    @Value("${attach.folder}")    //环境配置变量获取
    private String attachFolder;  //服务器端数据存储
    @Autowired
    private PasswordEncoder encoder;  //密码服务自动注入
    @Autowired
    private UserRepository userRepository;  //用户数据操作自动注入
    @Autowired
    private MenuInfoRepository menuInfoRepository; //菜单数据操作自动注入
    @Autowired
    private DictionaryInfoRepository dictionaryInfoRepository;  //数据字典数据操作自动注入
    @Autowired
    private BaseService baseService;  //基本数据处理数据操作自动注入
    @Autowired
    private UserTypeRepository userTypeRepository;   //用户类型数据操作自动注入

    /**
     *  获取menu表的新的Id StringBoot 对SqLite 主键自增支持不好  插入记录是需要设置主键ID，编写方法获取新的menu_id
     * @return
     */
    public synchronized Integer getNewMenuId() {
        Integer id = menuInfoRepository.getMaxId();  // 查询最大的id
        if (id == null)
            id = 1;
        else
            id = id + 1;
        return id;
    }
    /**
     *  获取dictionary表的新的Id StringBoot 对SqLite 主键自增支持不好  插入记录是需要设置主键ID，编写方法获取新的id
     * @return
     */

    public synchronized Integer getNewDictionaryId() {
        Integer id = dictionaryInfoRepository.getMaxId();  // 查询最大的id
        if (id == null)
            id = 1;
        else
            id = id + 1;
        return id;
    }

    /**
     *  获取菜单列表
     *  前台请求参数 userTypeId 用户类型主键ＩＤ，如果为空或缺的当前登录用户的类型的ID
     * 返回前端存储菜单数据的 MapList
     * @return
     */

    @PostMapping("/getMenuList")
    public DataResponse getMenuList(@Valid @RequestBody DataRequest dataRequest) {
        Integer userTypeId = dataRequest.getInteger("userTypeId");
        if (userTypeId == null) {
            Integer userId = CommonMethod.getUserId();
            userTypeId = userRepository.findByUserId(userId).get().getUserType().getId();
        }
        List<MenuInfo> mList = menuInfoRepository.findByUserTypeId(userTypeId);
        List dataList = new ArrayList();
        List<MenuInfo> msList;
        int i, j;
        Map m, ms;
        MenuInfo info;
        List sList;
        for (i = 0; i < mList.size(); i++) {
            info = mList.get(i);
            m = new HashMap();
            m.put("id", info.getId());
            m.put("name", info.getName());
            m.put("title", info.getTitle());
            sList = new ArrayList();
            msList = menuInfoRepository.findByUserTypeIdAndPid(userTypeId, info.getId());
            if (msList != null) {
                for (j = 0; j < msList.size(); j++) {
                    info = msList.get(j);
                    ms = new HashMap();
                    ms.put("id", info.getId());
                    ms.put("name", info.getName());
                    ms.put("title", info.getTitle());
                    sList.add(ms);
                }
            }
            m.put("sList", sList);
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    /**
     *  获取所有角色信息的列表
     *  前台请求参数 无
     * 返回前端存储角色信息的OptionItem的List
     * @return
     */

    @PostMapping("/getRoleOptionItemList")
    @PreAuthorize("hasRole('ADMIN')")
    public OptionItemList getRoleOptionItemList(@Valid @RequestBody DataRequest dataRequest) {
        List<UserType> uList = userTypeRepository.findAll();
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (UserType ut : uList) {
            itemList.add(new OptionItem(ut.getId(), null, ut.getName().name()));
        }
        return new OptionItemList(0, itemList);
    }
    /**
     *  获取某个用户类型 userTypeId 菜单树 信息
     *  前台请求参数 无
     * 返回前端某个用户类型 userTypeId 菜单树对象MyTreeNode（这个是一个递归的树节点对象）
     * @return
     */

    @PostMapping("/getMenuTreeNode")
    @PreAuthorize("hasRole('ADMIN')")
    public MyTreeNode getMenuTreeNode(@Valid @RequestBody DataRequest dataRequest) {
        Integer userTypeId = dataRequest.getInteger("userTypeId");
        if (userTypeId == null)
            userTypeId = 1;
        return baseService.getMenuTreeNode(userTypeId);
    }
    /**
     *  添加新一个菜单
     *  前台请求参数 userTypeId 菜单所属的用户类型 pid 菜单所属的父菜单的pid title 菜单的标题
     * 返回前端先创建的菜单的对象 MyTreeNode
     * @return
     */

    @PostMapping("/newMenuTreeNode")
    @PreAuthorize("hasRole('ADMIN')")
    public MyTreeNode newMenuTreeNode(@Valid @RequestBody DataRequest dataRequest) {
        Integer userTypeId = dataRequest.getInteger("userTypeId");
        Integer pid = dataRequest.getInteger("pid");
        String title = dataRequest.getString("title");
        MenuInfo m = new MenuInfo();
        m.setPid(pid);
        m.setId(getNewMenuId());
        m.setTitle(title);
        m.setUserTypeId(userTypeId);
        menuInfoRepository.save(m);
        MyTreeNode node = new MyTreeNode(m.getId(), m.getName(), m.getTitle());
        node.setChildList(new ArrayList());
        return node;
    }
    /**
     *  删除菜单
     *  前台请求参数 id 要删除的菜单的主键 menu_id
     * 返回前端 操作正常
     * @return
     */
    @PostMapping("/deleteMenuTreeNode")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse deleteMenuTreeNode(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Optional<MenuInfo> op = menuInfoRepository.findById(id);
        if (op.isPresent())
            menuInfoRepository.delete(op.get());
        return CommonMethod.getReturnMessageOK();
    }
    /**
     *  保存菜单信息
     *  前台请求参数 id 要修改菜单的主键 menu_id  name 菜单名字  title 菜单标题
     * 返回前端 操作正常
     * @return
     */

    @PostMapping("/saveMenuTreeNode")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse saveMenuTreeNode(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        String name = dataRequest.getString("name");
        String title = dataRequest.getString("title");
        Optional<MenuInfo> op = menuInfoRepository.findById(id);
        if (op.isPresent()) {
            MenuInfo m = op.get();
            m.setTitle(title);
            m.setName(name);
            menuInfoRepository.save(m);
        }
        return CommonMethod.getReturnMessageOK();
    }

    /**
     *  获取某个数据字典树表信息
     *  前台请求参数 无
     * 返回前端 数据字典数节点对象 MyTreeNode（这个是一个递归的树节点对象）
     * @return
     */
    @PostMapping("/getDictionaryTreeNode")
    @PreAuthorize("hasRole('ADMIN')")
    public MyTreeNode getDictionaryTreeNode(@Valid @RequestBody DataRequest dataRequest) {
        return baseService.getDictionaryTreeNode();
    }
    /**
     *  添加新一个地点
     *  前台请求参数   pid 字典所属的父字典的pid  label 菜单的标题
     * 返回前端先创建字典的对象 MyTreeNode
     * @return
     */

    @PostMapping("/newDictionaryTreeNode")
    @PreAuthorize("hasRole('ADMIN')")
    public MyTreeNode newDictionaryTreeNode(@Valid @RequestBody DataRequest dataRequest) {
        Integer pid = dataRequest.getInteger("pid");
        String label = dataRequest.getString("label");
        DictionaryInfo m = new DictionaryInfo();
        m.setPid(null);
        m.setId(getNewDictionaryId());
        m.setLabel(label);
        dictionaryInfoRepository.save(m);
        MyTreeNode node = new MyTreeNode(m.getId(), m.getValue(), m.getLabel());
        node.setChildList(new ArrayList());
        return node;
    }

    /**
     *  删除字典
     *  前台请求参数 id 要删除的字典的主键 id
     * 返回前端 操作正常
     * @return
     */
    @PostMapping("/deleteDictionaryTreeNode")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse deleteDictionaryTreeNode(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        Optional<DictionaryInfo> op = dictionaryInfoRepository.findById(id);
        if (op.isPresent())
            dictionaryInfoRepository.delete(op.get());
        return CommonMethod.getReturnMessageOK();
    }
    /**
     *  保存字典信息
     *  前台请求参数 id 要修改菜单的主键 id  value 地点值  label 字典名
     * 返回前端 操作正常
     * @return
     */

    @PostMapping("/saveDictionaryTreeNode")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse saveDictionaryTreeNode(@Valid @RequestBody DataRequest dataRequest) {
        Integer id = dataRequest.getInteger("id");
        String value = dataRequest.getString("value");
        String label = dataRequest.getString("label");
        Optional<DictionaryInfo> op = dictionaryInfoRepository.findById(id);
        if (op.isPresent()) {
            DictionaryInfo m = op.get();
            m.setLabel(label);
            m.setValue(value);
            dictionaryInfoRepository.save(m);
        }
        return CommonMethod.getReturnMessageOK();
    }

    /**
     *  获取某种数据类型的数据字典列表
     *  前台请求参数  code 数据类型的值
     * 返回前端存储数据字典信息的OptionItem的List
     * @return
     */

    @PostMapping("/getDictionaryOptionItemList")
    public OptionItemList getDictionaryOptionItemList(@Valid @RequestBody DataRequest dataRequest) {
        String code = dataRequest.getString("code");
        List<DictionaryInfo> dList = dictionaryInfoRepository.getDictionaryInfoList(code);
        OptionItem item;
        List<OptionItem> itemList = new ArrayList();
        for (DictionaryInfo d : dList) {
            itemList.add(new OptionItem(d.getId(), d.getValue(), d.getLabel()));
        }
        return new OptionItemList(0, itemList);
    }

    /**
     *  获取服务器端的图片文件的数据
     *  前台请求参数  文件路径
     * 返回前端图片数据的二进制数据留
     * @return
     */
    @PostMapping("/getImageByteData")
    public ResponseEntity<StreamingResponseBody> getImageByteData(@Valid @RequestBody DataRequest dataRequest) {
        String fileName = dataRequest.getString("fileName");
        try {
            File file = new File(attachFolder + fileName);
            int len = (int) file.length();
            byte data[] = new byte[len];
            FileInputStream in = new FileInputStream(file);
            in.read(data);
            in.close();
            MediaType mType = new MediaType(MediaType.APPLICATION_OCTET_STREAM);
            StreamingResponseBody stream = outputStream -> {
                outputStream.write(data);
            };
            return ResponseEntity.ok()
                    .contentType(mType)
                    .body(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.internalServerError().build();
    }

    /**
     *  上传文件服务
     *  前台请求参数  uploader 信息  remoteFile 服务器文件路径  fileName 前端上传的文件名
     * 返回前端 正常操作信心和异常操作信息
     * @return
     */
    @PostMapping(path = "/uploadFile")
    public DataResponse uploadFile(@RequestBody byte[] barr,
                          @RequestParam(name = "uploader") String uploader,
                           @RequestParam(name = "remoteFile") String remoteFile,
                         @RequestParam(name = "fileName") String fileName)  {
        try {
            OutputStream os = new FileOutputStream(new File(attachFolder + remoteFile));
            os.write(barr);
            os.close();
            return CommonMethod.getReturnMessageOK();
        }catch(Exception e){
            return CommonMethod.getReturnMessageError("上传错误");
        }
    }

    /**
     *  修改登录用户的密码
     *  前台请求参数  oldPassword 用户的旧密码  newPassword 用户的新密码
     * 返回前端 正常操作
     * @return
     */
    //  修改密码
    //Student页面的列表里点击删除按钮则可以删除已经存在的学生信息， 前端会将该记录的id 回传到后端，方法从参数获取id，查出相关记录，调用delete方法删除
    @PostMapping("/updatePassword")
    @PreAuthorize(" hasRole('ADMIN') or  hasRole('STUDENT') or  hasRole('TEACHER')")
    public DataResponse updatePassword(@Valid @RequestBody DataRequest dataRequest) {
        String oldPassword  = dataRequest.getString("oldPassword");  //获取oldPassword
        String newPassword  = dataRequest.getString("newPassword");  //获取newPassword
        Optional<User> op = userRepository.findByUserId(CommonMethod.getUserId());
        if(op.isPresent())
            return CommonMethod.getReturnMessageError("账户不存在！");  //通知前端操作正常
        User u = op.get();
        if(!encoder.matches(oldPassword, u.getPassword())) {
            return CommonMethod.getReturnMessageError("原始密码不正确！");
        }
        u.setPassword(encoder.encode(newPassword));
        userRepository.save(u);
        return CommonMethod.getReturnMessageOK();  //通知前端操作正常
    }

    /**
     *  上传Html字符串流， 用于生成html字符流和PDF数据流的源HTML， 保存的内存MAP中
     *  前台请求参数  html 前端传过来的 html字符串
     * 返回前端 字符串保存的MAP的key,用于下载html，PDF的主键
     * @return
     */

    @PostMapping("/uploadHtmlString")
    @PreAuthorize(" hasRole('ADMIN') ")
    public DataResponse uploadHtmlString(@Valid @RequestBody DataRequest dataRequest) {
        String str  = dataRequest.getString("html");
        String html = new String(Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8)));
        System.out.println(html);
        int htmlCount = ComDataUtil.getInstance().addHtmlString(html);
        return CommonMethod.getReturnData(htmlCount);
    }

    /**
     *  获取Html页面数据，
     *  前台请求参数  htmlCount 获取原始的前端传送后端保存的Html的主键
     * 返回前端 html页面， 前端WebPage可以直接访问的页面
     * @return
     */

    @GetMapping("/getHtmlPage")
    public ResponseEntity<StreamingResponseBody> htmlGetBaseHtmlPage(HttpServletRequest request) {
        String htmlCountStr = request.getParameter("htmlCount");
        Integer htmlCount = Integer.parseInt(htmlCountStr);
        String html = ComDataUtil.getInstance().getHtmlString(htmlCount);
        MediaType mType = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        try {
            byte data[] = html.getBytes();
            StreamingResponseBody stream = outputStream -> {
                outputStream.write(data);
            };
            return ResponseEntity.ok()
                    .contentType(mType)
                    .body(stream);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     *  获取PDF 数据，用于前端PDFView显示，
     *  前台请求参数  htmlCount 获取原始的前端传送后端保存的Html的主键
     * 返回前端 PDF数据的二进制数据流， 系统将html转换为PDF格式数据
     * @return
     */

    @PostMapping("/getPdfData")
    public ResponseEntity<StreamingResponseBody> getPdfData(@Valid @RequestBody DataRequest dataRequest) {
        Integer htmlCount = dataRequest.getInteger("htmlCount");
        String content = ComDataUtil.getInstance().getHtmlString(htmlCount);
        String head= "<!DOCTYPE html><html><head><style>html { font-family: \"SourceHanSansSC\", \"Open Sans\";}</style><meta charset='UTF-8' /><title>Insert title here</title></head><body>";
        content = head + content+"</body></html>";
        content = CommonMethod.removeErrorString(content,"&nbsp;","style=\"font-family: &quot;&quot;;\"");
        return baseService.getPdfDataFromHtml(content);
    }

}
