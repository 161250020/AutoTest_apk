package AutoTest.Models;

import java.util.ArrayList;

public class TreeNode {
	private XmlElement e;
	private TreeNode parent;
	private ArrayList<TreeNode> nodelist;
	
	//根节点的节点
	public TreeNode(XmlElement e) {
		this.e = e;
		this.parent = null;
		this.nodelist = new ArrayList<TreeNode>();
	}

	//除根节点外的节点
	public TreeNode(TreeNode parent, XmlElement e) {
		this.parent = parent;
		this.e = e;
		this.nodelist = new ArrayList<TreeNode>();
	}
	
	public XmlElement getE() {
		return e;
	}
	public void setE(XmlElement e) {
		this.e = e;
	}
	public TreeNode getParent() {
		return parent;
	}
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	public ArrayList<TreeNode> getNodelist() {
		return nodelist;
	}
	public void setNodelist(ArrayList<TreeNode> nodelist) {
		this.nodelist = nodelist;
	}
		
}
