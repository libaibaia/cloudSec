package com.common.qiniu.base.model.qvm;

public class ImageInfo {
    private String image_id;
    private String description;
    private String product_code;
    private String os_type;
    private String architecture;
    private String os_name;
    private String image_owner_alias;
    private String progress;
    private String usage;
    private String image_version;
    private String status;
    private String image_name;
    private String is_self_shared;
    private String platform;
    private int size;
    private boolean is_support_cloudinit;
    private boolean is_support_io_optimized;
    private boolean is_copied;
    private boolean is_subscribed;
    private DiskDeviceMappings disk_device_mappings;
    private String snapshot_id;
    private String min_disk_size;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getOs_type() {
        return os_type;
    }

    public void setOs_type(String os_type) {
        this.os_type = os_type;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getOs_name() {
        return os_name;
    }

    public void setOs_name(String os_name) {
        this.os_name = os_name;
    }

    public String getImage_owner_alias() {
        return image_owner_alias;
    }

    public void setImage_owner_alias(String image_owner_alias) {
        this.image_owner_alias = image_owner_alias;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getImage_version() {
        return image_version;
    }

    public void setImage_version(String image_version) {
        this.image_version = image_version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getIs_self_shared() {
        return is_self_shared;
    }

    public void setIs_self_shared(String is_self_shared) {
        this.is_self_shared = is_self_shared;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isIs_support_cloudinit() {
        return is_support_cloudinit;
    }

    public void setIs_support_cloudinit(boolean is_support_cloudinit) {
        this.is_support_cloudinit = is_support_cloudinit;
    }

    public boolean isIs_support_io_optimized() {
        return is_support_io_optimized;
    }

    public void setIs_support_io_optimized(boolean is_support_io_optimized) {
        this.is_support_io_optimized = is_support_io_optimized;
    }

    public boolean isIs_copied() {
        return is_copied;
    }

    public void setIs_copied(boolean is_copied) {
        this.is_copied = is_copied;
    }

    public boolean isIs_subscribed() {
        return is_subscribed;
    }

    public void setIs_subscribed(boolean is_subscribed) {
        this.is_subscribed = is_subscribed;
    }

    public DiskDeviceMappings getDisk_device_mappings() {
        return disk_device_mappings;
    }

    public void setDisk_device_mappings(DiskDeviceMappings disk_device_mappings) {
        this.disk_device_mappings = disk_device_mappings;
    }

    public String getSnapshot_id() {
        return snapshot_id;
    }

    public void setSnapshot_id(String snapshot_id) {
        this.snapshot_id = snapshot_id;
    }

    public String getMin_disk_size() {
        return min_disk_size;
    }

    public void setMin_disk_size(String min_disk_size) {
        this.min_disk_size = min_disk_size;
    }
}
