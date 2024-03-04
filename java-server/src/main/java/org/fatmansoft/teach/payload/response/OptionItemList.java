package org.fatmansoft.teach.payload.response;

import java.util.List;
/**
 OptionItemList 发挥前端的OptionItemList集合对象
 Integer code 返回代码 0 正确 1 错误
 */
public class OptionItemList {
    private Integer code = 0;
    private List<OptionItem> itemList;

    public OptionItemList(Integer code,List<OptionItem> itemList ){
        this.code = code;
        this.itemList = itemList;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<OptionItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<OptionItem> itemList) {
        this.itemList = itemList;
    }

}
