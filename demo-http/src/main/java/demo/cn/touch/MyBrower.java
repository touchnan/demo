/*
 * demo.cn.touch.MyBrower.java
 * Nov 11, 2013 
 */
package demo.cn.touch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;

/**
 * Nov 11, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class MyBrower {
    private BasicCookieStore cookieStore = new BasicCookieStore();
    private HttpConnector conn = new HttpConnector(cookieStore);
    
    private String sitePath = "http://www.lvhe-cn.com/greenweb/";
    
    private String loginUrl = sitePath+"login.aspx?returnUrl=/greenweb/index.aspx";
    private String homeUrl =  sitePath+"index.aspx";
    private String orderAddrUrl =  sitePath+"packages_order.aspx";
    private String logoutUrl = sitePath+"LoginOut.aspx";
    private String myOrderUrl = sitePath+"myorder.aspx";

    @Test
    public void autoBookLH() {
        List<String> vagetables = new ArrayList<String>();//喜欢的菜优先级从高到低选择
        vagetables.add("西瓜");
        vagetables.add("桔子");
        vagetables.add("红薯");
        vagetables.add("西红柿");
        vagetables.add("马铃薯");
        vagetables.add("土豆");
        vagetables.add("莲藕");
        vagetables.add("黄瓜");
        vagetables.add("芹菜");
        
//        vagetables.add("甘蓝");
        vagetables.add("结球甘蓝");
        vagetables.add("大娃娃菜");
        vagetables.add("大白菜");
        
        vagetables.add("蘑菇");
        
        
        
        vagetables.add("菠菜");
        vagetables.add("紫番薯");
        vagetables.add("慈菇");
        vagetables.add("茨菰");
        vagetables.add("茭白");

        vagetables.add("韭菜");
        
        
        User u = this.new User("韩成强", "hancq", "1122334455",this.new Pkg("鸡蛋套餐",6), vagetables);
        this.loginLH(u);//登录
        if (!hadOrdered(u)) {//还没下单就下单
            String pkgRelateUrl = this.parsePkgDetailUrl(u);//选择套餐
            this.choosePkgVegetables(u, pkgRelateUrl);//选菜
            System.out.println(this.makeOrder(u));//下单
        }
        conn.get(logoutUrl);//退出
//        System.out.println(conn.get("http://www.lvhe-cn.com/greenweb/myorder.aspx"));//我的订单
//        printCokkies();
    }
    
    private void loginLH(User u) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        HttpGet httpget = new HttpGet(loginUrl);
        String content = conn.get(httpget);
        Document doc = Jsoup.parse(content);
        remainHiddenInput(doc, nvps);
        
        HttpPost httpost = new HttpPost(loginUrl);
        nvps.add(new BasicNameValuePair("tbname", u.getLoginName()));
        nvps.add(new BasicNameValuePair("tbpwd", u.getPasswd()));
        nvps.add(new BasicNameValuePair("ImageButton1.x", "0"));
        nvps.add(new BasicNameValuePair("ImageButton1.y", "0"));
        // nvps.add(new BasicNameValuePair("__EVENTARGUMENT", null));
        // nvps.add(new BasicNameValuePair("__EVENTTARGET", null));
        // nvps.add(new BasicNameValuePair("__EVENTTARGET", null));

        // httpost.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        // httpost.addHeader("Accept-Encoding","gzip, deflate");
        // httpost.addHeader("Accept-Language","en-US,en;q=0.5");
        // httpost.addHeader("Cache-Control","max-age=0");
        // httpost.addHeader("Connection","keep-alive");
        // httpost.addHeader("Host","60.191.115.181");
        // httpost.addHeader("Referer","http://www.lvhe-cn.com/greenweb/login.aspx?returnUrl=%2fgreenweb%2findex.aspx");
        // httpost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.2; rv:25.0) Gecko/20100101 Firefox/25.0");

        // httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        content = conn.post(httpost, nvps);
    }
    
    
    private String parsePkgDetailUrl(User u) {
      //解析套餐的相对地址
        String content = conn.get(homeUrl);
        checkUser(content, u);
        
        Document doc = Jsoup.parse(content);
        Elements es = doc.select("div.h_title");
        for (Element e : es) {
                String eText = e.text();
                if (StringUtils.isNotBlank(eText) && u.matchPkg(eText.trim())) {
                    for (Element child :e.siblingElements()) {
                        String childClass = child.attr("class");
                        if (StringUtils.isNotBlank(childClass)) {
                            if ("order_text".equals(childClass)) {
                                return child.select("a").attr("href");
                            }
                        }
                        
                    }
                }
        }
        return "packages_detail.aspx?tcpcid=162";//默认鸡蛋套餐
    }
    
    private void choosePkgVegetables(User u, String pkgRelateUrl) {
        //选择套餐菜
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        String content = conn.get(sitePath + pkgRelateUrl);
        checkUser(content, u);
        Document doc = Jsoup.parse(content);
        remainHiddenInput(doc, nvps);
        
        
        HttpPost httpost = new HttpPost(sitePath + pkgRelateUrl);
        
        Elements es = doc.select("div.select  li");
        int choosed = 0;
        /*- 
        for (Element e: es) {
            String name = e.text();
            if (StringUtils.isNotBlank(name)) {
                if (u.want(name)) {
                    nvps.add(new BasicNameValuePair(e.select("input[type=checkbox]").attr("name"), "on"));
                    choosed++;
                    if (choosed>=u.getPkg().getLimit()) {
                        break;//自己喜欢 达到限制个数
                    }
                } else {
                    unlikes.add(new BasicNameValuePair(e.select("input[type=checkbox]").attr("name"), "on"));
                }
            }
        }
        */
        for (String likeName : u.likeVagetables) {//喜欢的菜优先级从高到低选择
            for (Element e: es) {
                String name = e.text();
                if (StringUtils.isNotBlank(name)) {
                    if (name.contains(likeName)) {//匹配此优先级的菜
                        nvps.add(new BasicNameValuePair(e.select("input[type=checkbox]").attr("name"), "on"));
                        choosed++;
                        break;
                    }
                }
            }
            if (choosed>=u.maxChoose()) {
                break;//自己喜欢 达到限制个数
            }            
        }
        
        
        if (choosed<u.maxChoose()) {//喜欢的数量太少
            List<NameValuePair> unlikes = new ArrayList<NameValuePair>();
            for (Element e: es) {
                String name = e.text();
                if (StringUtils.isNotBlank(name)) {
                    if (u.want(name)) {
                    } else {
                        unlikes.add(new BasicNameValuePair(e.select("input[type=checkbox]").attr("name"), "on"));
                    }
                }
            }
            
            int up2sys = u.maxChoose()-choosed;
            for (int c=0;c<up2sys && c<unlikes.size(); c++) {
                nvps.add(unlikes.get(c));//剩下的随便挑
            }
        }
        nvps.add(new BasicNameValuePair("ImageButton1.x","48"));
        nvps.add(new BasicNameValuePair("ImageButton1.y","27"));
        
        content = conn.post(httpost, nvps);
    }
    
    //下单
    private String makeOrder(User u) {
        //下单
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        String content = conn.get(orderAddrUrl);
        checkUser(content, u);
        Document doc = Jsoup.parse(content);
        remainHiddenInput(doc, nvps);
        
        String pkg = doc.select("#txTcname").val();
        if (StringUtils.isNotBlank(pkg)) {
             Assert.assertTrue("要下单的套餐不正确", u.matchPkg(pkg.trim()));
        }
        
        HttpPost httpost = new HttpPost(orderAddrUrl);

        nvps.add(new BasicNameValuePair("TbName",StringUtils.isBlank(u.getReceiveTelphone())?doc.select("#TbName").val() : u.getReceiveUser()));
        nvps.add(new BasicNameValuePair("TbTel",StringUtils.isBlank(u.getReceiveTelphone())?doc.select("#TbTel").val():u.getReceiveTelphone()));
        
        Elements es = doc.select("#DropDownList1 option");
        for (Element e: es) {
            String name = e.text();
            if (StringUtils.isNotBlank(name) && name.equals("省信产公司")) {
                nvps.add(new BasicNameValuePair("DropDownList1", e.val()));
                break;
            }
        }
        
        nvps.add(new BasicNameValuePair("ImageButton1.x","84"));
        nvps.add(new BasicNameValuePair("ImageButton1.y","12"));
        
        content = conn.post(httpost, nvps);//提交收货信息
        
        String orderInfo = findOrder(u);
        if (StringUtils.isNotBlank(orderInfo)) {
            //下单成功
            return orderInfo;
        }
        
        System.out.println(content);//如果订单不成功,查看提交信息看下什么情况
        return "未找到这礼拜的订单,可能是还没下单!";
    }
    
    private String findOrder(User u) {
        //成功与否,要验证
        String myOrderContent = conn.get(myOrderUrl);//我的订单
        Document doc = Jsoup.parse(myOrderContent);
        Elements es = doc.select("table tr");
        
        Calendar calendar = Calendar.getInstance();
        for (int day : new int[] {Calendar.MONDAY,Calendar.TUESDAY,Calendar.WEDNESDAY}) {
            calendar.set(Calendar.DAY_OF_WEEK, day);
            String orderDay = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
            for (Element e: es) {
                String orderNumber = e.child(0).text();
                if (StringUtils.isNotBlank(orderNumber) && orderNumber.startsWith(orderDay)) {
                    return u.getName()+"在"+(orderDay+"这天订了"+e.child(1).text())+"["+e.child(4).text()+"] 发货给"+e.child(2).text();
                }
            }            
        }
        
        return "";        
    }
    
    private boolean hadOrdered(User u) {
        String order = findOrder(u);
        if (StringUtils.isNotBlank(order)) {
            System.out.println(order);
            return true;
        }
        return false;
    }
    
    //每个页面都重新验证下用户,为了安全
    private void checkUser(String page, User u) {
      boolean loginSucess = page.contains(u.getName()); 
      if (!loginSucess) {    
          Assert.assertTrue("登录错误，没找到匹配的用户" + u, false);
      }        
    }
    
    private void remainHiddenInput(Document doc, List<NameValuePair> nvps) {
        Elements es = doc.select("input[type=hidden]");
        for (Element e : es) {
            if (StringUtils.isNotBlank(e.val())) {
                nvps.add(new BasicNameValuePair(e.attr("name"), e.val()));
            }
        }        
    }    
    
    private void printCokkies() {
        List<Cookie> cookies = cookieStore.getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
    }
    
    class Pkg {
        private int limit;
        private String pkgName;
        public Pkg(String pkgName, int limit) {
            this.pkgName = pkgName;
            this.limit = limit;
        }
   
        /**
         * @return the limit
         */
        public int getLimit() {
            return limit;
        }

        /**
         * @return the pkgName
         */
        public String getPkgName() {
            return pkgName;
        }
    }
    
    class User {
        private String name;
        private String loginName;
        private String passwd;
        
        private Pkg pkg;
        private List<String> likeVagetables;
        
        private String receiveUser;
        private String receiveTelphone;
        
        
        @SuppressWarnings("unchecked")
        public User(String name, String loginName, String passwd,Pkg pkg, List<String> vagetables) {
            this.name = name;
            this.loginName = loginName;
            this.passwd = passwd;
            this.pkg = pkg;
            this.likeVagetables = vagetables == null ? Collections.EMPTY_LIST: vagetables;
        }
        
        @SuppressWarnings("unchecked")
        public User(String name, String loginName, String passwd,String receiveUser, String receiveTelphone,Pkg pkg, List<String> vagetables) {
            this.name = name;
            this.loginName = loginName;
            this.passwd = passwd;
            this.pkg = pkg;
            this.likeVagetables = vagetables == null ? Collections.EMPTY_LIST: vagetables;
            this.receiveUser = receiveUser;
            this.receiveTelphone = receiveTelphone;
        }        
        
        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the loginName
         */
        public String getLoginName() {
            return loginName;
        }

        /**
         * @return the passwd
         */
        public String getPasswd() {
            return passwd;
        }
        
        public List<String> likeVagetables () {
            return this.likeVagetables;
        }
        
        public boolean want(String vagetableName) {
            for (String v: likeVagetables ) {
                if (vagetableName.contains(v)) {
                    return true;
                }
            }
            return false;
        }
        
        public int maxChoose() {
            return this.pkg.getLimit();
        }
        
        public boolean matchPkg(String pkgName) {
            return pkgName.equals(this.pkg.getPkgName());
        }

        /**
         * @return the receiveName
         */
        public String getReceiveUser() {
            return receiveUser;
        }

        /**
         * @return the receiveTelphone
         */
        public String getReceiveTelphone() {
            return receiveTelphone;
        }

        public String toString() {
            return String.format("{name:%s, loginName:%s, passwd:%s}", name, loginName, passwd);
        }
    }

}
