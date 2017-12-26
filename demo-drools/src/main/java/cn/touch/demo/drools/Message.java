/**
 * 
 */
package cn.touch.demo.drools;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Jun 29, 2017.
 */
public class Message {
	public static final int HELLO = 0;
	public static final int GOODBYE = 1;

	private String message;

	private int status;

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
