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
			if(xml.contains("&#"))
				xml = xml.replaceAll("&#", "");//删除geeknews当中的无法识别的特殊符号，记住要重新为xml赋值，不然xml就没有改变！
			Document document = DocumentHelper.parseText(xml);
			
			//获取根节点元素对象，转换成TreeNode
			Element root = document.getRootElement();
			XmlElement rootElement = new XmlElement(root.getName(), root.attributeValue("rotation"));
			TreeNode rootNode = new TreeNode(rootElement);
			
			//新建Tree来存储xml的内容
			Tree tree = new Tree(rootNode);
			Queue<Element> elements = new LinkedList<Element>();
			Queue<TreeNode> nodes = new LinkedList<TreeNode>();
			elements.offer(root);
			nodes.offer(rootNode);
			createTree(elements, nodes);
			
			//创建返回的PageSource
			ret = new PageSource(xml, tree);
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	//创建树，使用queue的数据结构
	private void createTree(Queue<Element> elements, Queue<TreeNode> nodes) {
		// TODO Auto-generated method stub
//		int j = 0;
//		int k = 0;//获得的el+子el的数量
		while(!elements.isEmpty()) {
//			j ++;
			Element e = elements.poll();
			TreeNode r = nodes.poll();
			
			List<Element> subEls = e.elements();//el所有的子节点
//			k+=subEls.size();
//			k-=1;
//			System.out.println("element name:" + e.getName()+"    "+"sub element num:"+subEls.size());
			ArrayList<TreeNode> listNode = new ArrayList();
			for(int i = 0; i < subEls.size(); i ++) {
				Element tmpE = subEls.get(i);
				XmlElement tmpXE = elementToXmlElement(tmpE);
				TreeNode tmpN = new TreeNode(r, tmpXE);
				listNode.add(tmpN);
				elements.offer(tmpE);
				nodes.offer(tmpN);
			}
//			if(subEls.size() == 0)
//				System.out.println("sub els can be empty");
			r.setNodelist(listNode);
		}
//		System.out.println("create tree node num:" + j);
//		System.out.println("get sub el num:" + k);
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

	//原代码：比较结构相同；现代码：比较xml的长度相同；
	/*
	boolean ret = true;
	
	Tree t1 = p1.getXmlTree();
	Tree t2 = p2.getXmlTree();

	Queue<TreeNode> q1 = new LinkedList<TreeNode>();
	Queue<TreeNode> q2 = new LinkedList<TreeNode>();
	q1.offer(t1.getRoot());
	q2.offer(t2.getRoot());
	int depth = 0;//del
	while((!q1.isEmpty())&&(!q2.isEmpty())) {
		depth ++;//del
		if(q1.size() == q2.size()) {
			//取出节点
			TreeNode n1 = q1.poll();
			TreeNode n2 = q2.poll();
			
			//两个TreeNode的结构相等，则为同一个SourcePage
			if(!n1.getE().getName().equals(n2.getE().getName())) {
				System.out.println("p140:" + n1.getE().getName());
				System.out.println("p141:" + n2.getE().getName());
				System.out.println("p139:" + n1.getE().getBounds());
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		else {
			System.out.println("p162: not equal");
			System.out.println("p164: depth:" + depth);
			ret = false;
			break;
		}
	}
	*/
	public boolean pageEqual(PageSource p1, PageSource p2) {
		// TODO Auto-generated method stub
		if(p1.getXml().length() == p2.getXml().length())
			return true;
		else
			return false;
	}

	public void testTreeNodeInQueue(String name, AppSources app, AppiumDriver driver, 
			long startTime, long runtime) {
		// TODO Auto-generated method stub
		while((new Date().getTime() - startTime) <= runtime) {
			long time = new Date().getTime() - startTime;
			System.out.println("time: " + time);
			try {
				Thread.sleep(500); // 等待0.5s
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
			else if((name.contains("gudong"))
					&& (sourceXml.contains("查看介绍文章"))) {
				driver.findElement(By.xpath("//android.widget.Button[@text='已了解']")).click();
			}
			else if((name.contains("gudong"))
					&& (sourceXml.contains("导入导出"))) {//页面没有可以处理的元素
				driver.navigate().back();//回退上一页
			}
			else if(sourceXml.contains("已停止运行")) {//已停止运行则重启
				driver.findElement(By.xpath("//android.widget.Button[@text='确定']"));
				driver.resetApp();
				try {
					Thread.sleep(6000); // 等待6s，待应用完全启动
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if((name.contains("seeweather"))
					&& (sourceXml.contains("鼓励下作者"))) {//重启app
				driver.closeApp();
				driver.launchApp();
				try {
					Thread.sleep(1000); // 等待1s
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if((sourceXml.toLowerCase().contains("github"))
					|| (sourceXml.toLowerCase().contains("email"))) {
				driver.navigate().back();
			}
			
			//将未在AppSources当中的PageSource添加进去
			Object[] o = addPageSourceIfNotInSources(app, sourceXml);
			app = (AppSources) o[0];
			System.out.println("p219: app size:" + app.getSources().size());
			PageSource curP = (PageSource) o[1];
			curP = calculateNewQueue(curP);
			TreeNode test = curP.getNodes().peek();
			System.out.println("p223: queue size:" + curP.getNodes().size());

			//测试该节点：需要测试步骤+修改该节点的XmlElement的visited为true
			XmlElement e = test.getE();
			curP.getNodes().poll();
			//对元素进行定位
			//元素属性---不常见的---根元素：hierarchy
			int countPassNoAct = 0;
			while(!testOrNot(e)) {
				countPassNoAct ++;
				System.out.println("pass no action!");
//				testTreeNodeInQueue(name, app, driver, 
//						 startTime, runtime);
				curP = calculateNewQueue(curP);
				test = curP.getNodes().peek();
				e = test.getE();
				curP.getNodes().poll();
				
				if(countPassNoAct >= 50) {//超过50个没有可以操作的元素，为了以免页面出错，跳不出来，就返回上一级，下次再测
					driver.navigate().back();
					break;
				}
			}
			//元素属性---常见的元素则会被测试
			String resource_id = e.getResource_id();
			String _className = e.getName();
			String _class = e.get_class();
			String index = e.getIndex();
			String text = e.getText();
			String content_desc = e.getContent_desc();
			//定位内容
			String path_id = resource_id;//id
			String path_xpath1 = "//" + _className + "[@text='" + text + "']";//class+text
			String path_xpath2 = "//" + _className + "[@content-desc='" + content_desc + "']";//class+content_desc
			String path_xpath3 = "//" + _className + "[@class='" + _class + "']";//class+class
			String path_xpath4 = "//" + _className + "[@index='" + index + "']";//class+index
			System.out.println("p224" + path_xpath1);
			System.out.println("p256" + path_xpath2);
			System.out.println("p256" + path_xpath3);
			System.out.println("p256" + path_xpath4);
			//三个定位方法，哪个是唯一的，就使用那种方法，否则返回
			List<WebElement> list1 = new ArrayList();
			if(!path_id.equals(""))
				list1 = driver.findElementsById(path_id);
			List<WebElement> list2 = new ArrayList();
			if(!text.equals(""))
				list2 = driver.findElementsByXPath(path_xpath1);
			List<WebElement> list3 = new ArrayList();
			if(!content_desc.equals(""))
				list3 = driver.findElementsByXPath(path_xpath2);
			List<WebElement> list4 = new ArrayList();
			if(!_class.equals(""))
				list4 = driver.findElementsByXPath(path_xpath3);
			List<WebElement> list5 = new ArrayList();
			if(!index.equals(""))
				list5 = driver.findElementsByXPath(path_xpath4);
			
			if(list1.size() == 1) {
				//测试WebElement元素
				System.out.println("test id!");
				testEl(name, app, driver, startTime, runtime, list1.get(0), true);
			}
			else if(list2.size() == 1) {
				//测试WebElement元素
				System.out.println("test xpath1!");
				testEl(name, app, driver, startTime, runtime, list2.get(0), true);
			}
			else if(list3.size() == 1) {
				//测试WebElement元素
				System.out.println("test xpath2!");
				testEl(name, app, driver, startTime, runtime, list3.get(0), true);
			}
			else if(list4.size() == 1) {
				//测试WebElement元素
				System.out.println("test xpath3!");
				testEl(name, app, driver, startTime, runtime, list4.get(0), true);
			}
			else if(list5.size() == 1) {
				//测试WebElement元素
				System.out.println("test xpath4!");
				testEl(name, app, driver, startTime, runtime, list5.get(0), true);
			}
			else {//无法唯一的定位元素，则全部遍历
				System.out.println("test list!");
				if(list1.size() > 0)
					for(int i = 0; i < list1.size(); i++) {
						//测试WebElement元素
						String sourceXml2 = driver.getPageSource();
						if(sourceXml2.equals(sourceXml))
							testEl(name, app, driver, startTime, runtime, list1.get(i), false);
						else
							break;
					}
				else if(list2.size() > 0)
					for(int i = 0; i < list2.size(); i++) {
						//测试WebElement元素
						String sourceXml2 = driver.getPageSource();
						if(sourceXml2.equals(sourceXml))
							testEl(name, app, driver, startTime, runtime, list2.get(i), false);
						else
							break;
					}
				else if(list3.size() > 0)
					for(int i = 0; i < list3.size(); i++) {
						//测试WebElement元素
						String sourceXml2 = driver.getPageSource();
						if(sourceXml2.equals(sourceXml))
							testEl(name, app, driver, startTime, runtime, list3.get(i), false);
						else
							break;
					}
				else if(list4.size() > 0)
					for(int i = 0; i < list4.size(); i++) {
						//测试WebElement元素
						String sourceXml2 = driver.getPageSource();
						if(sourceXml2.equals(sourceXml))
							testEl(name, app, driver, startTime, runtime, list4.get(i), false);
						else
							break;
					}
				else if(list5.size() > 0)
					for(int i = 0; i < list5.size(); i++) {
						//测试WebElement元素
						String sourceXml2 = driver.getPageSource();
						if(sourceXml2.equals(sourceXml))
							testEl(name, app, driver, startTime, runtime, list5.get(i), false);
						else
							break;
					}
				else {//没找到该元素
					System.out.println("can not be found!");
					testTreeNodeInQueue(name, app, driver, 
							 startTime, runtime);
				}
			}
			
			//修改节点为已访问
			e.setVisited(true);
			test.setE(e);
			
		}
		if((new Date().getTime() - startTime) > runtime) {
			//超时，则退出
			driver.quit();
			System.out.println("超时退出程序！");
			System.exit(0);
		}
	}

	//判断是否检测这个节点
	private boolean testOrNot(XmlElement e) {
		boolean ret = false;
		//该元素可以输入->sendkey
		if(!e.getName().equals("hierarchy"))
			if(e.getName().equals("android.widget.EditText")
					||e.getName().equals("android.widget.ToggleButton")
					||e.getName().equals("android.widget.Button")
					||e.getName().equals("android.widget.ImageButton"))
				ret = true;
//			else if(e.getCheckable().equals("true"))
//				ret = true;
			else if(e.getClickable().equals("true"))
				ret = true;
		
		return ret;
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
		/*
		System.out.println("p281" + name);
		try {
			Thread.sleep(500); // 等待0.5s
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String tempResource = driver.getPageSource();
		*/
		
		//该元素可以输入->sendkey
		if(element.getTagName().equals("android.widget.EditText")) {
			element.click();
			element.clear();
			try {
				Thread.sleep(500);
				element.sendKeys("test\n");
				Thread.sleep(500);
				System.out.println("send key!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//ToggleButton，CheckBox，RadioButton（该元素可以点击）->click
		else if((element.getTagName().equals("android.widget.ToggleButton"))
				||(element.getTagName().equals("android.widget.CheckBox"))
				||(element.getTagName().equals("android.widget.RadioButton"))) {
			element.click();
			System.out.println("click 1!");
		}
		//Button，ImageButton（该元素可以点击）->click
		else if((element.getTagName().equals("android.widget.Button"))
				||element.getTagName().equals("android.widget.ImageButton")) {
			element.click();
			System.out.println("click 2!");
		}
//		//该元素可以check->click
//		else if(element.getAttribute("checkable").equals("true")) {
//			element.click();
//			System.out.println("click 3!");
//		}
		//该元素可以点击（）click）->click
		else if(element.getAttribute("clickable").equals("true")) {
			element.click();
			System.out.println("click 4!");
		}
		/*
		//作用前后页面没有跳转
		try {
			Thread.sleep(500); // 等待0.5s
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		*/
	}

	public PageSource calculateNewQueue(PageSource p) {
		// TODO Auto-generated method stub
		/*
		//对xml树进行层序遍历
		Queue<TreeNode> ret = p.getNodes();
		
		//已经完成一次层序遍历
		if(ret.isEmpty()) {
			System.out.println("new queue empty!");
			ret.offer(p.getXmlTree().getRoot());
			p.setNodes(ret);
		}
		else {
			System.out.println("new queue add!");
			TreeNode cur = p.getNodes().peek();
			ArrayList<TreeNode> nodes = cur.getNodelist();
			System.out.println("nodes length:" + nodes.size());
			for(TreeNode t : nodes) {
				ret.offer(t);
			}
			p.setNodes(ret);
		}
		*/
		//对xml树进行层序遍历
		Queue<TreeNode> ret = p.getNodes();
		
		//queue为空，则page的所有节点
		Queue<TreeNode> cur = new LinkedList();
		cur.offer(p.getXmlTree().getRoot());
		Queue<TreeNode> store = new LinkedList();//hierarchy节点不检查，所以就不存了
		if(ret.isEmpty()) {
			while(!cur.isEmpty()) {
				TreeNode curNode = cur.poll();
				ArrayList<TreeNode> nodes = curNode.getNodelist();
				for(TreeNode node : nodes) {
					cur.offer(node);
					store.offer(node);
				}
			}
			p.setNodes(store);
		}
				
		return p;
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
	public Object[] addPageSourceIfNotInSources(AppSources sources, String xml) {
		// TODO Auto-generated method stub
		boolean ret = false;
		PageSource p = xmlToPageSource(xml);
		
		ArrayList<PageSource> pageSources = sources.getSources();
		System.out.println("p395:" + xml);
		System.out.println("p404:size:" + pageSources.size());
		for(PageSource tmpp : pageSources) {
			if(pageEqual(tmpp, p)) {
				System.out.println("406:equal");
				ret = true;
				p = tmpp;
				break;
			}
		}

		System.out.println("p416: equal or not:" + ret);
		if(!ret) {
			System.out.println("396:not equal");
			pageSources.add(p);
			sources.setSources(pageSources);
		}
		Object[] o = new Object[2];
		o[0] = sources;
		o[1] = p;
		return o;
	}

	

}
