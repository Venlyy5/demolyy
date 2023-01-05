package com.lyytest.Websocketdemo.Test2;

import java.io.Serializable;
import java.util.List;

public class TreeSelect implements Serializable {

    /**
     * 节点ID
     */
    private Long id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 父ID
     */
    private Long parentId;

    /**
     * 子节点
     */
    private List<TreeSelect> children;

    public TreeSelect() {
    }

    public TreeSelect(Long id, String label, Long parentId) {
        this.id = id;
        this.label = label;
        this.parentId = parentId;
    }

    public TreeSelect(TreeSelect treeSelect) {
        this.id = treeSelect.getId();
        this.label = treeSelect.getLabel();
        this.children = treeSelect.getChildren();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getParentId() { return parentId; }

    public void setParentId(Long parentId) { this.parentId = parentId; }

    public List<TreeSelect> getChildren() {
        return children;
    }

    public void setChildren(List<TreeSelect> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "TreeSelect{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", parentId=" + parentId +
                ", children=" + children +
                '}';
    }
}