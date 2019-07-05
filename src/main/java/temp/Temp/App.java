package temp.Temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.SystemClock;

import AutoTest.Services.*;
import AutoTest.Models.*;
import io.appium.java_client.AppiumDriver;


public class App {

	/**
	 * 所有和AppiumDriver相关的操作都必须写在该函数中
	 * 
	 * @param driver 
	 * @param runtime 
	 * @param startTime 
	 */
	public void test(String appPath, AppiumDriver driver, long startTime, long runtime) {
		try {
			Thread.sleep(6000); // 等待6s，待应用完全启动
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS); // 设置尝试定位控件的最长时间为8s,也就是最多尝试8s
		/*
		 * 余下的测试逻辑请按照题目要求进行编写
		 */
		PageSourceService pss = new PageSourceServiceImpl();
		AppSources app = new AppSources();
		pss.testTreeNodeInQueue(appPath, app, driver, startTime, runtime);
		
		/*
		 * 测试需要用到的代码的部分：
		driver.findElement(By.id("com.hotbitmapgg.ohmybilibili:id/et_username")).clear();
		driver.findElement(By.id("com.hotbitmapgg.ohmybilibili:id/et_username")).sendKeys("test");;
		driver.findElement(By.id("com.hotbitmapgg.ohmybilibili:id/et_password")).clear();
		driver.findElement(By.id("com.hotbitmapgg.ohmybilibili:id/et_password")).sendKeys("test");;
		driver.findElement(By.id("com.hotbitmapgg.ohmybilibili:id/btn_login")).click();
		//哔哩哔哩登录之后的界面的元素获取
		try {
			Thread.sleep(1000); // 等待1s
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		/*
		 * 测试的函数：xmlToPageSource，createTree；
		 * 测试方法：
		 * 1：输出的xml放到notepad当中，统计“<”（num1）和“/>”（num2）的数量，
		 * 2：计算出xml当中node的数量：(num1-1+num2)/2：
		 * 原因：num1当中包含<?的<，所以需要-1，
		 * />的部分不以</结束，不包含在<的里面，所以加上/>的数量之后再除以2（默认每一个node以<开始，以</结束），
		 * 则计算出node的数量
		 *3： 计算出来的数量和后面方法当中打印出来的node的数量相比较，发现二者相同，则表示方法正确；
		String xml = driver.getPageSource();
		System.out.println(xml);
		PageSource p = pss.xmlToPageSource(xml);//根据当前xml生成pagesource
		Queue<TreeNode> nodeAll = new LinkedList<TreeNode>();
		nodeAll.offer(p.getXmlTree().getRoot());
		//打印pagesource里面树节点元素的值
		int i = 0;
		while(!nodeAll.isEmpty()) {
			i ++;
			TreeNode temp = nodeAll.poll();
			ArrayList<TreeNode> arr = temp.getNodelist();
			for(TreeNode node : arr)
				nodeAll.offer(node);
//			System.out.println(temp.getE().getName()
//					+"     "+temp.getE().getRotation()
//					+"     "+temp.getE().getResource_id());
		}
		System.out.println("node个数：" + i);
		//打印pagesource里面nodes的值
		System.out.println("大小：" + p.getNodes().size());
		System.out.println(p.getNodes().peek().getE().getName());
		*/
		
		/*
		 * 测试：calculateNewQueue
		String xml = driver.getPageSource();
		System.out.println(xml);
		PageSource p = pss.xmlToPageSource(xml);//根据当前xml生成pagesource
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		pss.calculateNewQueue(p);
		System.out.println(p.getNodes().size());
		*/
		
		
	}

	public AppiumDriver initAppiumTest(String appPath, String deviceUdid, String port) {
		AppiumDriver driver = null;
		File classpathRoot = new File(System.getProperty("user.dir"));
		File app = new File(appPath);

		// 设置自动化相关参数
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", "");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("deviceName", "Android Emulator");
		//capabilities.setCapability("udid", deviceUdid);
		
		// 设置apk路径
		String apkAbPath = app.getAbsolutePath();
		capabilities.setCapability("app", apkAbPath);
		// 不同的应用需要修改成对应的包名和启动Activity
		String cmd = "aapt d badging " + apkAbPath;
		String appPackage = "";
		String appActivity = "";
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String b = null;
			while((b = in.readLine()) != null) {
				if(b.startsWith("package:")) {
					//包名结束的位置
					int end = b.lastIndexOf("versionCode");
					end -= 2;
					//包名开始的位置
					int start = b.indexOf("name='");
					start += 6;
					appPackage = b.substring(start, end);
					capabilities.setCapability("appPackage", appPackage);
				}
				else if(b.startsWith("launchable-activity:"))
				{
					//找到应用名结束的位置
					int end = b.lastIndexOf("label");
					end -= 3;
					//找到应用名开始的位置
					int start = b.indexOf("name='");
					start += 6;
					appActivity = b.substring(start, end);
					capabilities.setCapability("appActivity", appActivity);
				}
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		capabilities.setCapability("noSign", "true");


		// 设置使用unicode键盘，支持输入中文和特殊字符
		capabilities.setCapability("unicodeKeyboard", "true");
		// 设置用例执行完成后重置键盘
		capabilities.setCapability("resetKeyboard", "true");
		// 初始化
		try {
			String url = "http://127.0.0.1:"+port+"/wd/hub";
			driver = new AppiumDriver(new URL(url), capabilities);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver;
	}

	public void start(String[] args) {
		String appPath = "apk/odyssey.apk";
		AppiumDriver driver = initAppiumTest(appPath, "udid", "4723");
		
		// 对apk进行测试
		long startTime = new Date().getTime();
		long runtime = 360 * 1000;
		test(appPath, driver, startTime, runtime);
	}

	/*
	public static void main(String[] args) {
		App main = new App();
		main.start(args);
		//System.out.println("test");
	}
	*/

}