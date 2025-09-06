package place.param;

import java.util.Date;

/**
 * @author wangxi
 * @date 2025/9/6 18:58
 */
public class Location {
    private Integer id;
    private Double lat;
    private Double lng;
    private String description;

    // 构造函数
    public Location() {}

    public Location(Double lat, Double lng, String description) {
        this.lat = lat;
        this.lng = lng;
        this.description = description;
    }

    // Getter和Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
