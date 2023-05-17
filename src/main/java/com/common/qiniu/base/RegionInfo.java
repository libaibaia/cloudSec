package com.common.qiniu.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true, value = {"request_id"})
public class RegionInfo {
    private List<RegionData> data;

    public List<RegionData> getData() {
        return data;
    }

    public void setData(List<RegionData> data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true, value = {"product_features"})
    public static class RegionData {
        private String region_id;
        private String local_name;
        private Boolean is_beta;
        private String provider_region_id;
        private String namespace_name;
        private List<ZoneData> zones;
        private String product_features;

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public String getLocal_name() {
            return local_name;
        }

        public void setLocal_name(String local_name) {
            this.local_name = local_name;
        }

        public Boolean getIs_beta() {
            return is_beta;
        }

        public void setIs_beta(Boolean is_beta) {
            this.is_beta = is_beta;
        }

        public String getProvider_region_id() {
            return provider_region_id;
        }

        public void setProvider_region_id(String provider_region_id) {
            this.provider_region_id = provider_region_id;
        }

        public String getNamespace_name() {
            return namespace_name;
        }

        public void setNamespace_name(String namespace_name) {
            this.namespace_name = namespace_name;
        }

        public List<ZoneData> getZones() {
            return zones;
        }

        public void setZones(List<ZoneData> zones) {
            this.zones = zones;
        }

        public String getProduct_features() {
            return product_features;
        }

        public void setProduct_features(String product_features) {
            this.product_features = product_features;
        }
    }

    public static class ZoneData {
        private String region_id;
        private String zone_id;
        private String local_name;

        public String getRegion_id() {
            return region_id;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public String getZone_id() {
            return zone_id;
        }

        public void setZone_id(String zone_id) {
            this.zone_id = zone_id;
        }

        public String getLocal_name() {
            return local_name;
        }

        public void setLocal_name(String local_name) {
            this.local_name = local_name;
        }
    }
}
