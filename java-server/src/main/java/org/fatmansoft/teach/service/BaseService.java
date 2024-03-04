package org.fatmansoft.teach.service;

import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.extend.impl.FSDefaultCacheStore;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.fatmansoft.teach.models.DictionaryInfo;
import org.fatmansoft.teach.models.MenuInfo;
import org.fatmansoft.teach.payload.response.MyTreeNode;
import org.fatmansoft.teach.repository.DictionaryInfoRepository;
import org.fatmansoft.teach.repository.MenuInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * BaseService 系统菜单、数据字典等处理的功能和方法
 */
@Service
public class BaseService {
    @Autowired
    private DictionaryInfoRepository dictionaryInfoRepository;  //数据数据操作自动注入
    @Autowired
    private MenuInfoRepository menuInfoRepository;  //菜单数据操作自动注入
    @Autowired
    private ResourceLoader resourceLoader;  //资源装在服务对象自动注入

    private FSDefaultCacheStore fSDefaultCacheStore = new FSDefaultCacheStore();

    /**
     *  getDictionaryTreeNode 获取数据字典节点树根节点
     * @return MyTreeNode 数据字典树根节点
     */
    public MyTreeNode getDictionaryTreeNode() {
        MyTreeNode  node = new MyTreeNode(null,null,"数据字典");
        node.setPid(null);
        List<MyTreeNode> childList = new ArrayList<MyTreeNode>();
        node.setChildList(childList);
        List<DictionaryInfo> sList = dictionaryInfoRepository.findRootList();
        if(sList == null)
            return node;
        for(int i = 0; i<sList.size();i++) {
            childList.add(getDictionaryTreeNode(node.getId(),sList.get(i)));
        }
        return node;
    }

    /**
     * 获得数据字典的MyTreeNode
     * @param pid  父节点
     * @param d   数据字典数据
     * @return  树节点
     */
    public MyTreeNode getDictionaryTreeNode( Integer pid, DictionaryInfo d) {
        MyTreeNode  node = new MyTreeNode(d.getId(),d.getValue(),d.getLabel());
        node.setPid(pid);
        List<MyTreeNode> childList = new ArrayList<MyTreeNode>();
        node.setChildList(childList);
        List<DictionaryInfo> sList = dictionaryInfoRepository.findByPid(d.getId());
        if(sList == null)
            return node;
        for(int i = 0; i<sList.size();i++) {
            childList.add(getDictionaryTreeNode(node.getId(),sList.get(i)));
        }
        return node;
    }

    /**
     * MyTreeNode getMenuTreeNode(Integer userTypeId) 获得角色的菜单树根节点
     * @param userTypeId 用户类型ID
     * @return MyTreeNode 根节点对象
     */
    public MyTreeNode getMenuTreeNode(Integer userTypeId) {
        MyTreeNode  node = new MyTreeNode(null,null,"菜单");
        node.setPid(null);
        List<MyTreeNode> childList = new ArrayList<MyTreeNode>();
        node.setChildList(childList);
        List<MenuInfo> sList = menuInfoRepository.findByUserTypeId(userTypeId);
        if(sList == null)
            return node;
        for(int i = 0; i<sList.size();i++) {
            childList.add(getMenuTreeNode(userTypeId,node.getId(),sList.get(i)));
        }
        return node;
    }
    /**
     * MyTreeNode getMenuTreeNode(Integer userTypeId) 获得角色的某个菜单的菜单树根节点
     * @param userTypeId 用户类型ID Integer pid 父节点ID MenuInfo d 菜单信息
     *
     * @return MyTreeNode 当前菜单的MyTreeNode对象
     */
    public MyTreeNode getMenuTreeNode(Integer userTypeId, Integer pid, MenuInfo d) {
        MyTreeNode  node = new MyTreeNode(d.getId(),d.getName(),d.getTitle());
        node.setPid(pid);
        List<MyTreeNode> childList = new ArrayList<MyTreeNode>();
        node.setChildList(childList);
        List<MenuInfo> sList = menuInfoRepository.findByUserTypeIdAndPid(userTypeId,d.getId());
        if(sList == null)
            return node;
        for(int i = 0; i<sList.size();i++) {
            childList.add(getMenuTreeNode(userTypeId,node.getId(),sList.get(i)));
        }
        return node;
    }

    /**
     * 将HTML的字符串转换成PDF文件，返回前端的PDF文件二进制数据流数据流
     * @param htmlContent  HTML 字符流
     * @return PDF数据流兑现
     */
    public ResponseEntity<StreamingResponseBody> getPdfDataFromHtml(String htmlContent) {
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, null);
            builder.useFastMode();
            builder.useCacheStore(PdfRendererBuilder.CacheStore.PDF_FONT_METRICS, fSDefaultCacheStore);
            Resource resource = resourceLoader.getResource("classpath:font/SourceHanSansSC-Regular.ttf"); 
            InputStream fontInput = resource.getInputStream();
            builder.useFont(new FSSupplier<InputStream>() {
                @Override
                public InputStream supply() {
                    return fontInput;
                }
            }, "SourceHanSansSC");
            StreamingResponseBody stream = outputStream -> {
                builder.toStream(outputStream);
                builder.run();
            };

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(stream);

        }
        catch (Exception e) {
            return  ResponseEntity.internalServerError().build();
        }
    }

}
