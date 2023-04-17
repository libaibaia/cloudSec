package com.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单和权限规则表
 * @TableName menu
 */
@TableName(value ="menu")
public class Menu implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 上级菜单
     */
    private Integer pid;

    /**
     * 类型:menu_dir=菜单目录,menu=菜单项,button=页面按钮
     */
    private Object type;

    /**
     * 标题
     */
    private String title;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单类型:tab=选项卡,link=链接,iframe=Iframe
     */
    private Object menuType;

    /**
     * Url
     */
    private String url;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 缓存:0=关闭,1=开启
     */
    private Integer keepalive;

    /**
     * 扩展属性:none=无,add_rules_only=只添加为路由,add_menu_only=只添加为菜单
     */
    private Object extend;

    /**
     * 备注
     */
    private String remark;

    /**
     * 权重(排序)
     */
    private Integer weigh;

    /**
     * 状态:0=禁用,1=启用
     */
    private Object status;

    /**
     * 更新时间
     */
    private Integer updatetime;

    /**
     * 创建时间
     */
    private Integer createtime;
    @TableField(exist = false)
    private List<Menu> children;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    /**
     * ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 上级菜单
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 上级菜单
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 类型:menu_dir=菜单目录,menu=菜单项,button=页面按钮
     */
    public Object getType() {
        return type;
    }

    /**
     * 类型:menu_dir=菜单目录,menu=菜单项,button=页面按钮
     */
    public void setType(Object type) {
        this.type = type;
    }

    /**
     * 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 规则名称
     */
    public String getName() {
        return name;
    }

    /**
     * 规则名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 路由路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 路由路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 菜单类型:tab=选项卡,link=链接,iframe=Iframe
     */
    public Object getMenuType() {
        return menuType;
    }

    /**
     * 菜单类型:tab=选项卡,link=链接,iframe=Iframe
     */
    public void setMenuType(Object menuType) {
        this.menuType = menuType;
    }

    /**
     * Url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 组件路径
     */
    public String getComponent() {
        return component;
    }

    /**
     * 组件路径
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * 缓存:0=关闭,1=开启
     */
    public Integer getKeepalive() {
        return keepalive;
    }

    /**
     * 缓存:0=关闭,1=开启
     */
    public void setKeepalive(Integer keepalive) {
        this.keepalive = keepalive;
    }

    /**
     * 扩展属性:none=无,add_rules_only=只添加为路由,add_menu_only=只添加为菜单
     */
    public Object getExtend() {
        return extend;
    }

    /**
     * 扩展属性:none=无,add_rules_only=只添加为路由,add_menu_only=只添加为菜单
     */
    public void setExtend(Object extend) {
        this.extend = extend;
    }

    /**
     * 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 权重(排序)
     */
    public Integer getWeigh() {
        return weigh;
    }

    /**
     * 权重(排序)
     */
    public void setWeigh(Integer weigh) {
        this.weigh = weigh;
    }

    /**
     * 状态:0=禁用,1=启用
     */
    public Object getStatus() {
        return status;
    }

    /**
     * 状态:0=禁用,1=启用
     */
    public void setStatus(Object status) {
        this.status = status;
    }

    /**
     * 更新时间
     */
    public Integer getUpdatetime() {
        return updatetime;
    }

    /**
     * 更新时间
     */
    public void setUpdatetime(Integer updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 创建时间
     */
    public Integer getCreatetime() {
        return createtime;
    }

    /**
     * 创建时间
     */
    public void setCreatetime(Integer createtime) {
        this.createtime = createtime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Menu other = (Menu) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPid() == null ? other.getPid() == null : this.getPid().equals(other.getPid()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()))
            && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()))
            && (this.getMenuType() == null ? other.getMenuType() == null : this.getMenuType().equals(other.getMenuType()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getComponent() == null ? other.getComponent() == null : this.getComponent().equals(other.getComponent()))
            && (this.getKeepalive() == null ? other.getKeepalive() == null : this.getKeepalive().equals(other.getKeepalive()))
            && (this.getExtend() == null ? other.getExtend() == null : this.getExtend().equals(other.getExtend()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getWeigh() == null ? other.getWeigh() == null : this.getWeigh().equals(other.getWeigh()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getUpdatetime() == null ? other.getUpdatetime() == null : this.getUpdatetime().equals(other.getUpdatetime()))
            && (this.getCreatetime() == null ? other.getCreatetime() == null : this.getCreatetime().equals(other.getCreatetime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPid() == null) ? 0 : getPid().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        result = prime * result + ((getMenuType() == null) ? 0 : getMenuType().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getComponent() == null) ? 0 : getComponent().hashCode());
        result = prime * result + ((getKeepalive() == null) ? 0 : getKeepalive().hashCode());
        result = prime * result + ((getExtend() == null) ? 0 : getExtend().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getWeigh() == null) ? 0 : getWeigh().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getUpdatetime() == null) ? 0 : getUpdatetime().hashCode());
        result = prime * result + ((getCreatetime() == null) ? 0 : getCreatetime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", pid=").append(pid);
        sb.append(", type=").append(type);
        sb.append(", title=").append(title);
        sb.append(", name=").append(name);
        sb.append(", path=").append(path);
        sb.append(", icon=").append(icon);
        sb.append(", menuType=").append(menuType);
        sb.append(", url=").append(url);
        sb.append(", component=").append(component);
        sb.append(", keepalive=").append(keepalive);
        sb.append(", extend=").append(extend);
        sb.append(", remark=").append(remark);
        sb.append(", weigh=").append(weigh);
        sb.append(", status=").append(status);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", createtime=").append(createtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}