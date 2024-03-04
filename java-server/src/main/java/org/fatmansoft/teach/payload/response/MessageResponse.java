package org.fatmansoft.teach.payload.response;

/**
 * MessageResponse  消息返回对象 系统框架返回的消息对象
 * String message 返回的消息
 */

public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}