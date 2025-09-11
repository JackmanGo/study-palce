package place.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import place.param.CodeRequest;
import place.service.FormDataService;
import place.service.WeChatService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/main")
public class MainController {
    private static final Logger logger= LoggerFactory.getLogger(MainController.class);
    @Autowired
    private WeChatService weChatService;
    @Autowired
    private FormDataService formDataService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitForm(
            @RequestParam(value="appOpenId", required = false) String appOpenId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            HttpServletRequest request) {

        try {
            // 参数验证
            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse(-1, "标题不能为空"));
            }

            // 保存数据
            Long id = formDataService.saveFormData(title, description, imageFile, appOpenId, request);

            // 返回成功响应
            Map<String, Object> data = new HashMap<>();
            data.put("id", id);

            return ResponseEntity.ok(createSuccessResponse("提交成功", data));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(-1, "提交失败: " + e.getMessage()));
        }
    }
    /**
     * 获取OpenID接口
     * @param codeRequest 包含jsCode的请求体
     * @return 包含OpenID的响应
     */
    @PostMapping("/getOpenId")
    public ResponseEntity getOpenId(@RequestBody CodeRequest codeRequest) {
        try {
            String openId = weChatService.getOpenId(codeRequest.getJsCode());
            return ResponseEntity.ok(createSuccessResponse("提交成功", openId));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(-1, "提交失败: " + e.getMessage()));
        }
    }
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is running");
    }

    // 创建成功响应
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    // 创建错误响应
    private Map<String, Object> createErrorResponse(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        return response;
    }
}
