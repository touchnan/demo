/*
 * cn.touch.servlet3.MyServlet.java
 * Jan 25, 2014 
 */
package cn.touch.servlet3;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jan 25, 2014
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
@WebServlet(asyncSupported=true,displayName="myserv",urlPatterns={"/app/*"})
public class MyServlet extends HttpServlet {
	
	

    /* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
//		config.getServletContext().set
	}

	/* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getParameter("uid");
//        
        
        System.out.println("deal request");

        if (req.isAsyncSupported()) {
        	final AsyncContext ac = req.startAsync();
        	ac.addListener(new AsyncListener() {
                
                @Override
                public void onTimeout(AsyncEvent event) throws IOException {
                    System.out.println("超时了：");
                    //做一些超时后的相关操作
                }
                
                @Override
                public void onStartAsync(AsyncEvent event) throws IOException {
                    // TODO Auto-generated method stub
                    System.out.println("线程开始");
                }
                
                @Override
                public void onError(AsyncEvent event) throws IOException {
                    System.out.println("发生错误：");
                    event.getThrowable().printStackTrace();
                }
                
                @Override
                public void onComplete(AsyncEvent event) throws IOException {
                    System.out.println("执行完成");
                    //这里可以做一些清理资源的操作
                    
                }
            });        	
        	
//        	ac.setTimeout(5000);
	        ac.start(()->{
	        	try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 try {
					ac.getResponse().getWriter().append("succ").flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 ac.complete();
	        	System.out.println("async done!");
	        });
        } else {
        	try {
    			TimeUnit.SECONDS.sleep(3);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	resp.getWriter().write("succ");
        	System.out.println("sync done!");
        }
        
        System.out.println("end!");
        
//        AsyncContext ac = req.startAsync();
//        AsynQueue.put(ac);
//        System.out.println(uid);
//        resp.getWriter().append("hcq for you!").flush();
    }
    
    
    void doAsyncProcess() {
//    	  final AsyncContext asyncContext = request.startAsync();  
//    	 
//    	  asyncContext.addListener(new AsyncListener() {  
//              @Override  
//              public void onComplete(AsyncEvent event) throws IOException {  
//                  //在这里处理正常结束的逻辑  
//              }  
//
//              @Override  
//              public void onTimeout(AsyncEvent event) throws IOException {  
//                  //在这里处理超时的逻辑  
//              }  
//
//              @Override  
//              public void onError(AsyncEvent event) throws IOException {  
//                  //在这里处理出错的逻辑  
//              }  
//
//              @Override  
//              public void onStartAsync(AsyncEvent event) throws IOException {  
//                  //在这里处理开始异步线程的逻辑  
//              }  
//          });  
//          //设置超时的时间，到了时间以后，会回调onTimeout的方法  
//    	AsyncContext asyncContext = request.startAsync();
//          asyncContext.setTimeout(10000L);  
//          //在这里启动，传入一个Runnable对象，服务器会把此Runnable对象放在线程池里面执行  
//          asyncContext.start(new Runnable() {  
//              @Override  
//              public void run() {  
//                  //在这里做耗时的操作，如果做完，则调用complete方法通知回调，异步处理结束了  
//                  asyncContext.complete();  
//              }  
//          });  
    	
    	
//        AsyncContext asyncContext = request.startAsync();
//        asyncContext.addListener(new AsyncListener() {
//            
//            @Override
//            public void onTimeout(AsyncEvent event) throws IOException {
//                log.info("超时了：");
//                //做一些超时后的相关操作
//            }
//            
//            @Override
//            public void onStartAsync(AsyncEvent event) throws IOException {
//                // TODO Auto-generated method stub
//                log.info("线程开始");
//            }
//            
//            @Override
//            public void onError(AsyncEvent event) throws IOException {
//                log.info("发生错误：",event.getThrowable());
//            }
//            
//            @Override
//            public void onComplete(AsyncEvent event) throws IOException {
//                log.info("执行完成");
//                //这里可以做一些清理资源的操作
//                
//            }
//        });
//        //设置超时时间
//        asyncContext.setTimeout(200);
//        //也可以不使用start 进行异步调用
////        new Thread(new Runnable() {
////            
////            @Override
////            public void run() {
////                编写业务逻辑
////                
////            }
////        }).start();
//        
//        asyncContext.start(new Runnable() {            
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(100);
//                    log.info("内部线程：" + Thread.currentThread().getName());
//                    asyncContext.getResponse().setCharacterEncoding("utf-8");
//                    asyncContext.getResponse().setContentType("text/html;charset=UTF-8");
//                    asyncContext.getResponse().getWriter().println("这是【异步】的请求返回");
//                } catch (Exception e) {
//                    log.error("异常：",e);
//                }
//                //异步请求完成通知
//                //此时整个请求才完成
//                //其实可以利用此特性 进行多条消息的推送 把连接挂起。。
//                asyncContext.complete();
//            }
//        });
//        //此时之类 request的线程连接已经释放了
//        log.info("线程：" + Thread.currentThread().getName());
//    }     	
    }
}
