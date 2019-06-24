package AutoTest.Services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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

	public boolean testTreeNodeInQueue(AppSources app, AppiumDriver driver, PageSource p, long startTime, long runtime) {
		// TODO Auto-generated method stub
		if((new Date().getTime() - startTime) > runtime) {
			//超时，则退出
			
			
			
			
			
		}
		else {
			//未超时，则继续测试
			String sourceXml = driver.getPageSource();
			
			//如果为登录/注册页面，则需要特殊处理
			//为注册页面
			
			//为登录页面
			
			
			
			
			
			//将未在AppSources当中的PageSource添加进去
			PageSource curP = addPageSourceIfNotInSources(app, sourceXml);
			//测试queue里面的元素
			testQueue(curP);
			
		}
		
		return false;
	}

	private void testQueue(PageSource curP) {
		// TODO Auto-generated method stub
		TreeNode test = curP.getNodes().peek();
		calculateNewQueue(curP);
		
		//测试该节点：需要测试步骤+修改该节点的XmlElement的visited为true
		//该元素可以输入->sendkey（不检查页面跳转）
		//该元素可以点击->click（检查页面跳转）
		if(test.getE().getClickable().equals("true")) {
			
		}
	}

	public void calculateNewQueue(PageSource p) {
		// TODO Auto-generated method stub
		//对xml树进行层序遍历
		Queue<TreeNode> ret = p.getNodes();
		
		//已经完成一次层序遍历
		if(ret.isEmpty()) {
			ret.offer(p.getXmlTree().getRoot());
			p.setAllVisited(true);
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
