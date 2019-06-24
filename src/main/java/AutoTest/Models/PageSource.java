package AutoTest.Models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PageSource {
	public Tree xmlTree;
	public Queue<TreeNode> nodes;//存储待测的节点
	public boolean allVisited;
	
	public PageSource(Tree xmlTree) {
		this.xmlTree = xmlTree;
		Queue<TreeNode> nodes2 = new LinkedList<TreeNode>();
		nodes.offer(xmlTree.getRoot());
		this.nodes = nodes2;
		this.allVisited = false;
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
	public boolean getAllVisited() {
		return allVisited;
	}
	public void setAllVisited(boolean allVisited) {
		this.allVisited = allVisited;
	}
	
	
}
