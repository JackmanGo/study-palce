package place.param;

import java.time.LocalDateTime;
import java.util.Date;

public class FormData {

    private Long id;
    private String title;
    private String description;
    private String imagePath;
    private Date createTime;
    private String ipAddress;
    private String userAgent;

    // 构造方法
    public FormData() {
        this.createTime = new Date();
    }

    public FormData(String title, String description, String imagePath) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.createTime = new Date();
    }

    // Getter和Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}