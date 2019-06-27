package AutoTest.Services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.xml.sax.SAXException;

import AutoTest.Models.AppSources;
import AutoTest.Models.PageSource;
import AutoTest.Models.Tree;
import AutoTest.Models.TreeNode;
import AutoTest.Models.XmlElement;
import io.appium.java_client.AppiumDriver;

public class PageSourceServiceImpl implements PageSourceService {

	public PageSource xmlToPageSource(String xml) {
		// TODO Auto-generated method stub
		PageSource ret = null;
		
		try {
			//将String转换成org.dom4j.Document
			Document document = DocumentHelper.parseText(xml);
			
			//获取根节点元素对象，转换成TreeNode
			Element root = document.getRootElement();
			XmlElement rootElement = new XmlElement(root.getName(), root.attributeValue("rotation"));
			TreeNode rootNode = new TreeNode(rootElement);
			
			//新建Tree来存储xml的内容
			Tree tree = new Tree(rootNode);
			Queue<TreeNode> nodes = new LinkedList<TreeNode>();
			nodes.offer(rootNode);
			createTree(nodes, root);
			
			//创建返回的PageSource
			ret = new PageSource(tree);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	//创建树，使用queue的数据结构
	private void createTree(Queue<TreeNode> nodes, Element el) {
		// TODO Auto-generated method stub
		while(!nodes.isEmpty()) {
			TreeNode r = nodes.poll();
			List<Element> subEls = el.elements();//el所有的子节点
			ArrayList<TreeNode> listNode = new ArrayList();
			for(int i = 0; i < subEls.size(); i ++) {
				Element tmpE = subEls.get(i);
				XmlElement tmpXE = elementToXmlElement(tmpE);
				TreeNode tmpN = new TreeNode(r, tmpXE);
				listNode.add(tmpN);
				nodes.offer(tmpN);
			}
			r.setNodelist(listNode);
		}
	}

	//将读取的Element值转化成XmlElement
	private XmlElement elementToXmlElement(Element e) {
		XmlElement ret = new XmlElement(
				e.getName(),
				e.attributeValue("index"),
				e.attributeValue("text"),
				e.attributeValue("class"),
				e.attributeValue("package"),
				e.attributeValue("content-desc"),
				e.attributeValue("checkable"),
				e.attributeValue("checked"),
				e.attributeValue("clickable"),
				e.attributeValue("enabled"),
				e.attributeValue("focusable"),
				e.attributeValue("focused"),
				e.attributeValue("scrollable"),
				e.attributeValue("long-clickable"),
				e.attributeValue("password"),
				e.attributeValue("selected"),
				e.attributeValue("bounds"),
				e.attributeValue("resource-id"),
				e.attributeValue("instance")
				);
		
		
		return ret;
	}

	public boolean pageEqual(PageSource p1, PageSource p2) {
		// TODO Auto-generated method stub
		boolean ret = true;
		
		Tree t1 = p1.getXmlTree();
		Tree t2 = p2.getXmlTree();

		Queue<TreeNode> q1 = new LinkedList<TreeNode>();
		Queue<TreeNode> q2 = new LinkedList<TreeNode>();
		q1.offer(t1.getRoot());
		q2.offer(t2.getRoot());
		
		while((!q1.isEmpty())&&(!q2.isEmpty())) {
			//取出节点
			TreeNode n1 = q1.poll();
			TreeNode n2 = q2.poll();
			
			//两个TreeNode的结构相等，则为同一个SourcePage
			if(!n1.getE().getName().equals(n2.getE().getName())) {
				ret = false;
				break;
			}
			else {
				//子节点入队列
				ArrayList<TreeNode> arr1 = n1.getNodelist();
				ArrayList<TreeNode> arr2 = n2.getNodelist();
				for(TreeNode tmpn1 : arr1) {
					q1.offer(tmpn1);
				}
				for(TreeNode tmpn2 : arr2) {
					q2.offer(tmpn2);
				}
			}
		}
		
		return ret;
	}

	public boolean testTreeNodeInQueue(String name, AppSources app, AppiumDriver driver, long startTime, long runtime) {
		// TODO Auto-generated method stub
		if((new Date().getTime() - startTime) > runtime) {
			//超时，则退出
			driver.quit();
			System.out.println("超时退出程序！");
			System.exit(0);
		}
		else {
			//未超时，则继续测试
			String sourceXml = driver.getPageSource();
			
			//如果登录/注册的apk，则需要特殊处理
			/**
			 * 哔哩哔哩：需要登录；
			 * 简诗：需要注册+登录；
			 * */
			name = name.toLowerCase();
			if((name.contains("bilibili"))
					&& (sourceXml.contains("请输入您的哔哩哔哩账号"))) {
				driver.findElement(By.id("com.hotbitmapgg.ohmybilibili:id/et_username")).clear();
				driver.findElement(By.id("com.hotbitmapgg.ohmybilibili:id/et_username")).sendKeys("test");;
				driver.findElement(By.id("com.hotbitmapgg.ohmybilibili:id/et_password")).clear();
				driver.findElement(By.id("com.hotbitmapgg.ohmybilibili:id/et_password")).sendKeys("test");;
				driver.findElement(By.id("com.hotbitmapgg.ohmybilibili:id/btn_login")).click();
			}
			else if((name.contains("jianshi"))
					&& (sourceXml.contains("回归文字的本质，回归美好"))
					&& (sourceXml.contains("邮箱"))) {
				//先注册简诗
				driver.findElement(By.id("com.wingjay.android.jianshi:id/email")).clear();
				driver.findElement(By.id("com.wingjay.android.jianshi:id/email")).sendKeys("test@qq.com");
				driver.findElement(By.id("com.wingjay.android.jianshi:id/password")).clear();
				driver.findElement(By.id("com.wingjay.android.jianshi:id/password")).sendKeys("password123");
				driver.findElement(By.id("com.wingjay.android.jianshi:id/signup")).click();
				
				//登录
				driver.findElement(By.id("com.wingjay.android.jianshi:id/email")).clear();
				driver.findElement(By.id("com.wingjay.android.jianshi:id/email")).sendKeys("test@qq.com");
				driver.findElement(By.id("com.wingjay.android.jianshi:id/password")).clear();
				driver.findElement(By.id("com.wingjay.android.jianshi:id/password")).sendKeys("password123");
				driver.findElement(By.id("com.wingjay.android.jianshi:id/login")).click();
			}
			
			//将未在AppSources当中的PageSource添加进去
			PageSource curP = addPageSourceIfNotInSources(app, sourceXml);
			TreeNode test = curP.getNodes().poll();
			calculateNewQueue(curP);

			//测试该节点：需要测试步骤+修改该节点的XmlElement的visited为true
			XmlElement e = test.getE();
			//对元素进行定位
			//元素属性
			String resource_id = e.getResource_id();
			String _class = e.get_class();
			String text = e.getText();
			String content_desc = e.getContent_desc();
			//定位内容
			String path_id = resource_id;//id
			String path_xpath1 = _class + text;//class+text
			String path_xpath2 = _class + content_desc;//class+content_desc
			//三个定位方法，哪个是唯一的，就使用那种方法，否则返回
			List<WebElement> list1 = driver.findElementsById(path_id);
			List<WebElement> list2 = driver.findElementsByXPath(path_id);
			List<WebElement> list3 = driver.findElementsByXPath(path_id);
			if(list1.size() == 1) {
				//测试WebElement元素
				testEl(name, app, driver, startTime, runtime, list1.get(0), true);
			}
			else if(list2.size() == 1) {
				//测试WebElement元素
				testEl(name, app, driver, startTime, runtime, list2.get(0), true);
			}
			else if(list3.size() == 1) {
				//测试WebElement元素
				testEl(name, app, driver, startTime, runtime, list3.get(0), true);
			}
			else {//无法唯一的定位元素，则全部遍历
				for(int i = 0; i < list1.size(); i++) {//仅仅是遍历list1的，而不是比较厚，遍历元素最小的那个
					//测试WebElement元素
					testEl(name, app, driver, startTime, runtime, list1.get(i), false);
				}
			}
			
			//修改节点为已访问
			e.setVisited(true);
			test.setE(e);
		}
		
		return false;
	}

	//针对不同的TreeNode的name，设计不同的方案
	/**
	 * 如果是更加详细的测试代码，
	 * 那么会针对布局的Node进行更加详细的规划（调整布局内元素的测试顺序等等），
	 * 但是由于我是queue的顺序来进行Node的测试的，于是这些就没有进行考虑。
	 * Node的处理包括如下几个部分（android.widget）：
	 * 	布局方面（滑动）：FrameLayout，LinearLayout，RelativeLayout；
	 * 	可以输入的组件：EditText；
	 * 	列表的组件：AlterDialog；
	 * 	可以按钮的组件：Button，ImageButton，ToggleButton，CheckBox，RadioButton；
	 * 	选择时间的组件：DatePicker，TimePicker；
	 * 	其余组件（可以点击的组件（clickable = "true"），则点击）：
	 * 		AnalogClock，DigitalClock，Chronmeter，ImageView，ProgressBar，TextView，ProgressDialog；
	 * 	PS：还有很多组件，详见：https://www.yiibai.com/android
	 * 
	 * 	已经考虑的组件情况：
	 * 		1：EditText->输入test;
	 * 		2：按钮类型->click;
	 * 		3：...；
	 * 		4：已出错则回退；
	 * */
	private void testEl(String name, AppSources app, AppiumDriver driver, long startTime, long runtime, 
			WebElement element, boolean uniqueOrNot) {
		// TODO Auto-generated method stub
		String tempResource = driver.getPageSource();
		
		//该元素可以输入->sendkey
		if(element.getTagName().equals("android.widget.EditText")) {
			element.sendKeys("test");
		}
		//ToggleButton，CheckBox，RadioButton（该元素可以点击）->click
		else if((element.getTagName().equals("android.widget.ToggleButton"))
				||(element.getTagName().equals("android.widget.CheckBox"))
				||(element.getTagName().equals("android.widget.RadioButton"))) {
			element.click();
		}
		//Button，ImageButton（该元素可以点击）->click
		else if((element.getTagName().equals("android.widget.Button"))
				||element.getTagName().equals("android.widget.ImageButton")) {
			element.click();
		}
		//该元素可以check->click
		else if(element.getAttribute("checkable").equals("true")) {
			element.click();
		}
		//该元素可以点击（）click）->click
		else if(element.getAttribute("clickable").equals("true")) {
			element.click();
		}
		
		//作用前后页面没有跳转
		if(pageEqual(xmlToPageSource(driver.getPageSource()), 
				xmlToPageSource(tempResource))) {
			//如果是for循环进来的元素，还有for循环中元素待检查，则返回
			if(!uniqueOrNot)return;
			//否则就重新测试PageResource中新元素
			else {
				testTreeNodeInQueue(name, app, driver, startTime, runtime);
			}
		}
		//作用前后页面跳转
		else {
			//重新测试PageResource中新元素
			testTreeNodeInQueue(name, app, driver, startTime, runtime);
		}
	}

	public void calculateNewQueue(PageSource p) {
		// TODO Auto-generated method stub
		//对xml树进行层序遍历
		Queue<TreeNode> ret = p.getNodes();
		
		//已经完成一次层序遍历
		if(ret.isEmpty()) {
			ret.offer(p.getXmlTree().getRoot());
			p.setNodes(ret);
		}
		else {
			TreeNode cur = p.getNodes().poll();
			ArrayList<TreeNode> nodes = cur.getNodelist();
			for(TreeNode t : nodes) {
				ret.offer(t);
			}
			p.setNodes(ret);
		}
		
	}

	/*
	 * 层尝试使用深度遍历，但是发现需要判断当前节点的子节点是否已经遍历过，比较麻烦，但是
	 * 也是可以写的。由于时间不够，于是写较为简单的层序遍历，而我个人认为深度遍历更加优秀。
	private ArrayList<Integer> calNext(TreeNode par, TreeNode cur, ArrayList<Integer> path) {
		// TODO Auto-generated method stub
		//path当中仅含有根节点，而根节点是没有par的
		if(path.size() == 0) {
			path.add(0);//存储根节点的第一个元素
		}
		
		//如果有子节点则返回第0个子节点
		if(cur.getNodelist().size() > 0) {
			path.add(0);
			return path;
		}
		//如果有兄弟节点则返回兄弟节点
		else if((par.getNodelist().size() - 1) > path.get(path.size() - 1)) {
			int index = path.size() - 1;
			int indexValueInPar = path.get(index);
			path.remove(index);
			path.add(indexValueInPar + 1);
			return path;
		}
		//返回叔叔节点
		else {
			int index = path.size() - 1;
			path.remove(index);
			cur = par;
			par = par.getParent();
			path = 
		}
			
	}
	*/

	public PageSource addPageSourceIfNotInSources(AppSources sources, String xml) {
		// TODO Auto-generated method stub
		boolean ret = false;
		PageSource p = xmlToPageSource(xml);
		
		ArrayList<PageSource> pageSources = sources.getSources();
		for(PageSource tmpp : pageSources) {
			if(pageEqual(tmpp, p)) {
				ret = true;
				p = tmpp;
				break;
			}
		}
		
		if(!ret) {
			pageSources.add(p);
			sources.setSources(pageSources);
		}

		return p;
	}

	

}
