package AutoTest.Models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PageSource {
	private String xml;
	private Tree xmlTree;
	private Queue<TreeNode> nodes;//存储待测的节点
	
	public PageSource(String xml, Tree xmlTree) {
		this.xml = xml;
		this.xmlTree = xmlTree;
		Queue<TreeNode> nodes2 = new LinkedList<TreeNode>();
		nodes2.offer(xmlTree.getRoot());
		this.nodes = nodes2;
	}
	
	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Tree getXmlTree() {
		return xmlTree;
	}
	public void setXmlTree(Tree xmlTree) {
		this.xmlTree = xmlTree;
	}
	public Queue<TreeNode> getNodes() {
		return nodes;
	}
	public void setNodes(Queue<TreeNode> nodes) {
		this.nodes = nodes;
	}
}
