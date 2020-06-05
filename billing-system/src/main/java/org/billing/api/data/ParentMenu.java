package org.billing.api.data;

import java.util.Collection;

public class ParentMenu {

	private Integer id;
	private Collection<ChildMenu> childMenu;
	private String name;
	private String link;
	private String icon;
	private String badge;
	private Integer sequence;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Collection<ChildMenu> getChildMenu() {
		return childMenu;
	}

	public void setChildMenu(Collection<ChildMenu> childMenu) {
		this.childMenu = childMenu;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

}
