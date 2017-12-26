/*
 * cn.pubinfo.xtsms.sms.SmsDto.java
 * Jun 16, 2014 
 */
package cn.pubinfo.xtsms.sms;

import java.util.Date;

/**
 * Jun 16, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class SmsDto {
    private String sendto;
    private String vfrom;
    private String content;
    private String msgId;
    private Date submitDate;
    private Date sendtime;
    private Integer nstat;

    /**
     * @return the sendto
     */
    public String getSendto() {
        return sendto;
    }

    /**
     * @param sendto
     *            the sendto to set
     */
    public void setSendto(String sendto) {
        this.sendto = sendto;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     *            the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the msgId
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * @param msgId
     *            the msgId to set
     */
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    /**
     * @return the submitDate
     */
    public Date getSubmitDate() {
        return submitDate;
    }

    /**
     * @param submitDate
     *            the submitDate to set
     */
    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    /**
     * @return the sendtime
     */
    public Date getSendtime() {
        return sendtime;
    }

    /**
     * @param sendtime
     *            the sendtime to set
     */
    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    /**
     * @return the nstat
     */
    public Integer getNstat() {
        return nstat;
    }

    /**
     * @param nstat
     *            the nstat to set
     */
    public void setNstat(Integer nstat) {
        this.nstat = nstat;
    }

    /**
     * @return the vfrom
     */
    public String getVfrom() {
        return vfrom;
    }

    /**
     * @param vfrom
     *            the vfrom to set
     */
    public void setVfrom(String vfrom) {
        this.vfrom = vfrom;
    }

}
