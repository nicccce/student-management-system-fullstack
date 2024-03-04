package com.teach.javafxclient.request;
/**
 OptionItemList 发挥前端的OptionItemList集合对象
 Integer code 返回代码 0 正确 1 错误
 */
import java.util.List;

public class OptionItemList {
    private Integer code;
    private List<OptionItem> itemList;

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
