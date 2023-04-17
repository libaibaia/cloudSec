package com.common;


import com.domain.Menu;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Integer cId; //id
    private String cName; //name
    private Integer parentId;
    private List<Menu> children;

    public Node(Integer cId, String cName, Integer parentId) {
        this.cId = cId;
        this.cName = cName;
        this.parentId = parentId;
    }

    public Node() {
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public List<Menu> getRootNode(List<Menu> nodes){
        List<Menu> roots = new ArrayList<>();
        nodes.forEach(o->{
            if(o.getPid().equals(0)){
                roots.add(o);
            }
        });
        return roots;
    }
    public Menu buildChildern(Menu node,List<Menu> nodes){
        List<Menu> children = new ArrayList<>();
        nodes.forEach((o)->{
            if(o.getPid().equals(node.getId())){
                children.add(buildChildern(o,nodes)); //递归调用
            }
        });
        node.setChildren(children);
        return node;
    }

    public List<Menu> builTree(List<Menu> nodes){
        List<Menu> roots = new ArrayList<>() ;
        getRootNode(nodes).forEach(o->{
            Menu node = buildChildern(o, nodes);
            roots.add(node);
        });
        return roots;
    }
}
