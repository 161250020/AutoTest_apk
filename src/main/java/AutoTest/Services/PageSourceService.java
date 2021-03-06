package AutoTest.Services;

import java.util.ArrayList;
import java.util.Queue;

import AutoTest.Models.AppSources;
import AutoTest.Models.PageSource;
import AutoTest.Models.TreeNode;
import io.appium.java_client.AppiumDriver;

public interface PageSourceService {
	
	/**
	 * 将app的xml转换成PageSource后返回
	 * */
	public PageSource xmlToPageSource(String xml);
	
	/**
	 * 判断两个PageSource是否指代同一个界面
	 * */
	public boolean pageEqual(PageSource p1, PageSource p2);
	
	/**
	 * 测试当前PageSource的待测节点们：nodes的第一个节点;
	 * 会调用calculateNewQueue同时更新nodes;
	 * PS:name为特殊的apk，需要注册登录的特殊操作；
	 * */
	public void testTreeNodeInQueue(String name, AppSources app, AppiumDriver driver, long startTime, long runtime);

	/**
	 * 计算获得PageSource待测节点：nodes，
	 * 记得同时计算PageSource的allVisited参数的值，
	 * 如果allVisited为true，则依旧循环访问；
	 * 下一个节点的顺序：按照层次遍历来进行
	 * */
	public PageSource calculateNewQueue(PageSource p);
	
	/**
	 * 返回：AppSources，
	 * 如果不包含，则将当前PageSource添加到AppSources sources当中
	 * */
	public Object[] addPageSourceIfNotInSources(AppSources sources, String xml);
}
