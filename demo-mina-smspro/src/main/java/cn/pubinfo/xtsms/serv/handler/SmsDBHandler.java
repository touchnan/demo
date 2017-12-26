/*
 * cn.pubinfo.xtsms.serv.handler.SmsDBHandler.java
 * Jun 17, 2014 
 */
package cn.pubinfo.xtsms.serv.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;

import cn.pubinfo.xtsms.queue.QueueManager;
import cn.pubinfo.xtsms.serv.DB;
import cn.pubinfo.xtsms.sms.SmsDto;

/**
 * Jun 17, 2014
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class SmsDBHandler implements SmsHandler {
    private DB db;

    public SmsDBHandler(DB db) {
        super();
        this.db = db;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.handler.SmsHandler#reportSent(java.util.Collection)
     */
    @Override
    public void reportSent(Collection<SmsDto> smses) {
//        Object[][] params = new Object[smses.size()][2];
//        int i = 0;
//        for (SmsDto sms : smses) {
//            params[i] =
//                    new Object[] { sms.getSendto(), sms.getContent(), sms.getSubmitDate(), sms.getSendtime(),
//                            sms.getMsgId() };
//            i++;
//        }
//        db.batchUpdate("INSERT INTO smsreport (`sendto`,`content`,`submittime`,`sendtime`,`msgId`) VALUES (?,?,?,?,?)",
//                params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.handler.SmsHandler#backup2Send(java.util.Collection)
     */
    @Override
    public void backup2Send(Collection<SmsDto> smses) {
        Object[][] params = new Object[smses.size()][2];
        int i = 0;
        for (SmsDto sms : smses) {
            params[i] = new Object[] { sms.getSendto(), sms.getContent(), sms.getSubmitDate() };
            i++;
        }
        db.batchUpdate("INSERT INTO smsouttmp (`sendto`,`content`,`submittime`) VALUES (?,?,?)", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.handler.SmsHandler#loadBackup2Send()
     */
    @Override
    public void loadBackup2Send() {
        List<SmsDto> smses =
                db.find("SELECT sendto,content,submittime FROM smsouttmp", new ResultSetHandler<List<SmsDto>>() {
                    @Override
                    public List<SmsDto> handle(ResultSet rs) throws SQLException {
                        List<SmsDto> l = new ArrayList<SmsDto>();
                        while (rs.next()) {
                            int i = 1;
                            SmsDto dto = new SmsDto();
                            dto.setSendto(rs.getString(i++));
                            dto.setContent(rs.getString(i++));
                            dto.setSubmitDate(rs.getDate(i++));
                            l.add(dto);
                        }
                        return l;
                    }

                });

        if (!smses.isEmpty()) {
            QueueManager.sendQueue.addAll(smses);
            db.update("TRUNCATE TABLE smsouttmp");// 清空
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.handler.SmsHandler#reportStatus(java.util.Collection)
     */
    @Override
    public void reportStatus(Collection<SmsDto> smses) {
        Object[][] params = new Object[smses.size()][2];
        int i = 0;
        for (SmsDto sms : smses) {
            params[i] =
                    new Object[] { sms.getSendto(), sms.getContent(), sms.getSubmitDate(), sms.getSendtime(),
                            sms.getMsgId() };
            i++;
        }
        db.batchUpdate("INSERT INTO smsreport (`sendto`,`content`,`wtime`,`nstat`,`msgId`) VALUES (?,?,?,?,?)", params);

    }

    /*
     * (non-Javadoc)
     * 
     * @see cn.pubinfo.xtsms.serv.handler.SmsHandler#reportReceive(java.util.Collection)
     */
    @Override
    public void reportReceive(Collection<SmsDto> smses) {
        Map<String, Long> users =
                db.find2("SELECT contactuserphone, id FROM tuser", new ResultSetHandler<Map<String, Long>>() {
                    @Override
                    public Map<String, Long> handle(ResultSet rs) throws SQLException {
                        Map<String, Long> l = new HashMap<String, Long>();
                        while (rs.next()) {
                            int i = 1;
                            l.put(rs.getString(i++), rs.getLong(i++));
                        }
                        return l;
                    }

                });

        Object[][] params = new Object[smses.size()][2];
        int i = 0;
        for (SmsDto sms : smses) {
            params[i] =
                    new Object[] { sms.getVfrom(), sms.getContent(), users.get(sms.getSendto()), sms.getSendtime() };
            i++;
        }
        db.batchUpdate("INSERT INTO smsin (`vfrom`,`content`,`userId`,`wtime`) VALUES (?,?,?,?)", params);

    }

}
