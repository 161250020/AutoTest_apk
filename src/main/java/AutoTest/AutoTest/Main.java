package AutoTest.AutoTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.SystemClock;

import AutoTest.Services.*;
import AutoTest.Models.*;
import io.appium.java_client.AppiumDriver;


public class Main {

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
	}

	/**
	 * AppiumDriver的初始化逻辑必须写在该函数中
	 * @param port 
	 * @param deviceUdid 
	 * @param appPath 
	 * 
	 * @return
	 */
	public AppiumDriver initAppiumTest(String appPath, String deviceUdid, String port) {
		AppiumDriver driver = null;
		File classpathRoot = new File(System.getProperty("user.dir"));
		File app = new File(classpathRoot, appPath);

		// 设置自动化相关参数
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", "");
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("deviceName", "Android Emulator");
		capabilities.setCapability("udid", deviceUdid);//设置设备号
		
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

	// 输入命令行
	public ArrayList<String> inputCmd(String[] args) {
		ArrayList<String> arr = new ArrayList();
		// 读取jar后面的参数，进行赋值
		arr.add(args[0]);
		arr.add(args[1]);
		arr.add(args[2]);
		arr.add(args[3]);
		
		return arr;
	}

	public void start(String[] args) {
		
		// 输入命令行
		ArrayList<String> arr = new ArrayList();
		if(args.length == 4) {
			arr = inputCmd(args);
		}
		else {
			System.out.println("参数输入错误！");
			System.exit(0);;
		}
		long runtime = Long.parseLong(arr.get(3));
		runtime = runtime * 1000;//转换成毫秒计时
		//从此时开始计时
		long startTime = new Date().getTime();
		
		// 初始化appium
		AppiumDriver driver = initAppiumTest(arr.get(0), arr.get(1), arr.get(2));
		
		/*
		String appPath = "apk/bilibili.apk";
		AppiumDriver driver = initAppiumTest(appPath, "udid", "4723");
		
		// 对apk进行测试
		long startTime = new Date().getTime();
		long runtime = 3600 * 1000;
		*/
		test(arr.get(0), driver, startTime, runtime);
	}

	
	public static void main(String[] args) {
		Main main = new Main();
		main.start(args);
		//System.out.println("test");
	}
	

}