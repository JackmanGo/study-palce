package place.param;

/**
 * @author wangxi
 * @date 2025/9/6 16:15
 */
public class LoginResponse {
    private String token;
    private String message;
    private boolean success;

    // 构造函数
    public LoginResponse() {}

    public LoginResponse(boolean success, String message, String token) {
        this.success = success;
        this.message = message;
        this.token = token;
    }

    // Getter和Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
