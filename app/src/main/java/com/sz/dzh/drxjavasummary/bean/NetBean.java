package com.sz.dzh.drxjavasummary.bean;

/**
 * Created by administrator on 2018/12/5.
 * 网络返回必带参数
 */
public class NetBean {

    private String respCode;
    private String respMsg;
    private String sessionId;

    public NetBean() {
    }

    public NetBean(String respCode, String respMsg) {
        this.respCode = respCode;
        this.respMsg = respMsg;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "NetBean{" +
                "respCode='" + respCode + '\'' +
                ", respMsg='" + respMsg + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
