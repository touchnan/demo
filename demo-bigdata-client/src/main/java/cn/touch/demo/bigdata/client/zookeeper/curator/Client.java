package cn.touch.demo.bigdata.client.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.1.79:2181", retryPolicy);
		client.start();
		
//		client.getData().
		
		

	}
	
	/*-


		client.create().forPath("/my/path", myData);
		
		Recipes
		Distributed Lock
		
		InterProcessMutex lock = new InterProcessMutex(client, lockPath);
		if ( lock.acquire(maxWait, waitUnit) ) 
		{
		    try 
		    {
		        // do some work inside of the critical section here
		    }
		    finally
		    {
		        lock.release();
		    }
		}
		
		Leader Election


		LeaderSelectorListener listener = new LeaderSelectorListenerAdapter()
		{
		    public void takeLeadership(CuratorFramework client) throws Exception
		    {
		        // this callback will get called when you are the leader
		        // do whatever leader work you need to and only exit
		        // this method when you want to relinquish leadership
		    }
		}
		
		LeaderSelector selector = new LeaderSelector(client, path, listener);
		selector.autoRequeue();  // not required, but this is behavior that you will probably expect
		selector.start();

	 */

}
