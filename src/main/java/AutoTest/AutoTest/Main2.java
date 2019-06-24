package AutoTest.AutoTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AndroidKeyCode;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;


public class Main2 {
    
    
    /**
     * 所有和AppiumDriver相关的操作都必须写在该函数中
     * @param driver
     * @throws InterruptedException 
     */
    public void test(AppiumDriver driver) throws InterruptedException {
                try {
            Thread.sleep(6000);     //等待6s，待应用完全启动
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS); //设置尝试定位控件的最长时间为8s,也就是最多尝试8s
      /*
         * 余下的测试逻辑请按照题目要求进行编写
         */
        try {
            driver.findElementByXPath("//android.widget.Button[@text='已了解']").click();
            driver.findElementByXPath("//android.widget.Button[@text='确定']").click();
            driver.findElementById("android:id/input").click();
            driver.findElementById("android:id/input").sendKeys("test");
            driver.findElementById("name.gudong.translate:id/tv_clear").click();
            driver.findElementById("android:id/input").sendKeys("test");
            driver.findElementById("name.gudong.translate:id/bt_translate").click();
            driver.findElementById("name.gudong.translate:id/sp_translate_way").click();
            driver.findElementByXPath("//android.widget.CheckedTextView[@text='百度']").click();
            driver.findElementById("name.gudong.translate:id/sp_translate_way").click();
            driver.findElementByXPath("//android.widget.CheckedTextView[@text='谷歌']").click();
            driver.findElementById("name.gudong.translate:id/sp_translate_way").click();
            driver.findElementByXPath("//android.widget.CheckedTextView[@text='金山']").click();
            driver.findElementById("name.gudong.translate:id/sp_translate_way").click();
            driver.findElementByXPath("//android.widget.CheckedTextView[@text='有道']").click();
            driver.findElementById("name.gudong.translate:id/iv_favorite").click();
            driver.findElementById("name.gudong.translate:id/iv_sound").click();
            driver.findElementById("name.gudong.translate:id/iv_paste").click();
            driver.findElementByXPath("//android.widget.ImageView[@content-desc='更多选项']").click();
            Thread.sleep(1000);
            driver.findElementByXPath("//android.widget.TextView[@text='历史记录']").click();
            Thread.sleep(1000);
            driver.findElementByXPath("//android.widget.ImageButton[@content-desc='转到上一层级']").click();
            driver.findElementByXPath("//android.widget.ImageView[@content-desc='更多选项']").click();
            Thread.sleep(1000);
            driver.findElementByXPath("//android.widget.TextView[@text='支持作者']").click();
            Thread.sleep(1000);
            driver.findElementByXPath("//android.widget.Button[@text='关闭']").click();
            driver.findElementByXPath("//android.widget.ImageView[@content-desc='更多选项']").click();
            Thread.sleep(1000);
            driver.findElementByXPath("//android.widget.TextView[@text='去评分']").click();
            driver.findElementByXPath("//android.widget.ImageView[@content-desc='更多选项']").click();
            Thread.sleep(1000);
            driver.findElementByXPath("//android.widget.TextView[@text='关于(1.8.0)']").click();
            Thread.sleep(1000);
            driver.findElementByXPath("//android.widget.ImageButton[@content-desc='转到上一层级']").click();
            driver.findElementByXPath("//android.widget.ImageView[@content-desc='更多选项']").click();
            driver.findElementByXPath("//android.widget.TextView[@text='设置']").click();
            Thread.sleep(1000);
            driver.findElementByXPath("//android.widget.Button[@text='知道了']").click();
            driver.findElementByXPath("//android.widget.TextView[@text='开启划词翻译']").click();
            driver.findElementByXPath("//android.widget.TextView[@text='开启自动发音']").click();
            driver.findElementByXPath("//android.widget.TextView[@text='打开 App 自动翻译粘贴板单词']").click();
            driver.findElementByXPath("//android.widget.TextView[@text='开启定时单词提醒']").click();
            driver.findElementByXPath("//android.widget.TextView[@text='提示显示时间']").click();
            driver.findElementByXPath("//android.widget.CheckedTextView[@text='3秒钟']").click();
            swipeTop2Bottom(driver);
            Thread.sleep(500);
            driver.findElementByXPath("//android.widget.TextView[@text='开启单词联想输入']").click();
            driver.findElementByXPath("//android.widget.ImageButton[@content-desc='转到上一层级']").click();
            Thread.sleep(1000);
            driver.findElementById("name.gudong.translate:id/menu_book").click();
            Thread.sleep(1000);
            driver.findElementById("name.gudong.translate:id/iv_over_flow").click();
            driver.findElementByXPath("//android.widget.TextView[@text='翻译']").click();
            Thread.sleep(1000);
            driver.findElementById("name.gudong.translate:id/menu_book").click();
            driver.findElementById("name.gudong.translate:id/iv_over_flow").click();
            driver.findElementByXPath("//android.widget.TextView[@text='删除']").click();
        } catch (Exception e) {
        }
    }
    
    public void swipeTop2Bottom(AppiumDriver driver){
        int screen_width = driver.manage().window().getSize().width;//screen width
        int screen_height = driver.manage().window().getSize().height; //screen height
        int startx = screen_width/2;
        int starty = screen_height*3/4;
        int endx = startx;
        int endy = screen_height/4;
        driver.swipe(startx, starty, endx, endy, 500);
    }
    
    /**
     * AppiumDriver的初始化逻辑必须写在该函数中
     * @return
     */
    public AppiumDriver initAppiumTest() {
        
        AppiumDriver driver=null;
        File classpathRoot = new File(System.getProperty("user.dir"));
        File appDir = new File(classpathRoot, "apk");
        File app = new File(appDir, "GuDong.apk");
        
        //设置自动化相关参数
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("appPackage", "name.gudong.translate");
        capabilities.setCapability("appActivity", "name.gudong.translate.ui.activitys.MainActivity");
        capabilities.setCapability("noSign", "true");

        //设置apk路径
        capabilities.setCapability("app", app.getAbsolutePath()); 
        
        //设置使用unicode键盘，支持输入中文和特殊字符
        capabilities.setCapability("unicodeKeyboard","true");
        //设置用例执行完成后重置键盘
        capabilities.setCapability("resetKeyboard","true");
        //初始化
        try {
            driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
        return driver;
    }
    
    public void start() {
        try {
            test(initAppiumTest());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        Main2 main = new Main2();
        main.start();
    }
    

}