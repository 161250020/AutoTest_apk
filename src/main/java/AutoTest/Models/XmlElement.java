package AutoTest.Models;

public class XmlElement {
	private String name;
	private String rotation;
	private String index;
	private String text;
	private String _class;
	private String _package;
	private String content_desc;
	private String checkable;
	private String checked;
	private String clickable;
	private String enabled;
	private String focusable;
	private String focused;
	private String scrollable;
	private String long_clickable;
	private String password;
	private String selected;
	private String bounds;
	private String resource_id;
	private String instance;
	private boolean visited;
	
	//根节点的结构
	public XmlElement(String name, String rotation) {
		this.name = name;
		this.rotation = rotation;
	}

	//根节点外节点的结构
	public XmlElement(String name, String index, String text, String _class, String _package, String content_desc,
			String checkable, String checked, String clickable, String enabled, String focusable, String focused,
			String scrollable, String long_clickable, String password, String selected, String bounds,
			String resource_id, String instance) {
		this.name = name;
		this.index = index;
		this.text = text;
		this._class = _class;
		this._package = _package;
		this.content_desc = content_desc;
		this.checkable = checkable;
		this.checked = checked;
		this.clickable = clickable;
		this.enabled = enabled;
		this.focusable = focusable;
		this.focused = focused;
		this.scrollable = scrollable;
		this.long_clickable = long_clickable;
		this.password = password;
		this.selected = selected;
		this.bounds = bounds;
		this.resource_id = resource_id;
		this.instance = instance;
		this.visited = false;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRotation() {
		return rotation;
	}
	public void setRotation(String rotation) {
		this.rotation = rotation;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
	public String get_package() {
		return _package;
	}
	public void set_package(String _package) {
		this._package = _package;
	}
	public String getContent_desc() {
		return content_desc;
	}
	public void setContent_desc(String content_desc) {
		this.content_desc = content_desc;
	}
	public String getCheckable() {
		return checkable;
	}
	public void setCheckable(String checkable) {
		this.checkable = checkable;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getClickable() {
		return clickable;
	}
	public void setClickable(String clickable) {
		this.clickable = clickable;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getFocusable() {
		return focusable;
	}
	public void setFocusable(String focusable) {
		this.focusable = focusable;
	}
	public String getFocused() {
		return focused;
	}
	public void setFocused(String focused) {
		this.focused = focused;
	}
	public String getScrollable() {
		return scrollable;
	}
	public void setScrollable(String scrollable) {
		this.scrollable = scrollable;
	}
	public String getLong_clickable() {
		return long_clickable;
	}
	public void setLong_clickable(String long_clickable) {
		this.long_clickable = long_clickable;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}
	public String getBounds() {
		return bounds;
	}
	public void setBounds(String bounds) {
		this.bounds = bounds;
	}
	public String getResource_id() {
		return resource_id;
	}
	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}

	public boolean getVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	
}
