package com.teach.javafxclient.controller.base;

import com.teach.javafxclient.request.DataRequest;
import com.teach.javafxclient.request.DataResponse;
import com.teach.javafxclient.request.HttpRequestUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * PasswordController 登录交互控制类 对应 base/password-panel.fxml
 *  @FXML  属性 对应fxml文件中的
 *  @FXML 方法 对应于fxml文件中的 on***Click的属性
 */
public class PasswordController {
    @FXML
    private TextField oldPasswordField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField confirmPasswordField;

    /**
     * 点击 确认按钮 执行 onSubmitButtonClick方法，请求后台修改密码
     */
    @FXML
    protected void onSubmitButtonClick() {
        DataRequest request= new DataRequest();
        String oldPassword =oldPasswordField.getText();
        String newPassword =newPasswordField.getText();
        String confirmPassword =confirmPasswordField.getText();
        if( oldPassword.length() == 0  || newPassword.length() == 0  || confirmPassword.length() == 0 ) {
            MessageDialog.showDialog("密码输入为空，不能修改！");
            return;
        }
        if(!newPassword.equals(confirmPassword)) {
            MessageDialog.showDialog("新密码和确认密码不同，不能修改！");
            return;
        }
        request.put("oldPassword", oldPassword);
        request.put("newPassword", newPassword);
        request.put("confirmPassword", confirmPassword);
        DataResponse res = HttpRequestUtil.request("",request);
        if(res.getCode() == 0) {
            MessageDialog.showDialog("修改成功！");
        }else {
            MessageDialog.showDialog(res.getMsg());
        }
    }
}
