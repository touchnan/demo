/**
 * 
 */
package cn.touch.studio.hook;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.CountDownLatch;

/**
 * Created by <a href="mailto:88052350@qq.com">chengqiang.han</a> on Dec 26, 2017.
 *
 */

/*-
 * http://blog.csdn.net/u013256816/article/details/50394923
 * 
 * JDK提供了Java.Runtime.addShutdownHook(Thread hook)方法，可以注册一个JVM关闭的钩子，这个钩子可以在一下几种场景中被调用：
		程序正常退出
		使用System.exit()
		终端使用Ctrl+C触发的中断
		系统关闭
		OutOfMemory宕机
		使用Kill pid命令干掉进程（注：在使用kill -9 pid时，是不会被调用的）  
		widnows下强制kill也不会被调用： taskkill /f /pid pid
 */
public class ShutdownHook {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final CountDownLatch latch = new CountDownLatch(1);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("invoke shutdown hook!");
				latch.countDown();
			}
		});
		System.out.println(" current process id : "+getPid());
		try {
			System.out.println("before to await");
//			TimeUnit.SECONDS.sleep(2);
//			new Thread(()-> latch.countDown()).start();
			latch.await();
			System.out.println("after to await");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("exit");
		System.exit(0);
	}

	/*-
	 查看Java API
		java.lang.management
		提供管理接口，用于监视和管理Java虚拟机以及Java虚拟机在其上运行的操作系统。 
		接口摘要
		
		ClassLoadingMXBean
		用于Java虚拟机的类加载系统的管理接口。
		
		CompilationMXBean
		用于Java虚拟机的编译系统的管理接口。
		
		GarbageCollectorMXBean
		用于Java虚拟机的垃圾回收的管理接口。
		
		MemoryManagerMXBean
		内存管理器的管理接口。
		
		MemoryMXBean
		Java虚拟机内存系统的管理接口。
		
		MemoryPoolMXBean
		内存池的管理接口。
		
		OperatingSystemMXBean
		用于操作系统的管理接口，Java虚拟机在此操作系统上运行。
		
		RuntimeMXBean
		Java虚拟机的运行时系统的管理接口。
		
		ThreadMXBean
		Java虚拟机线程系统的管理接口。
	 */
	private static long getPid() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		String name = runtimeMXBean.getName();
		System.out.println("name = " + name);
		return Integer.valueOf(name.split("@")[0]);
	}

	private static void printAllPid() {
		
	}
}
