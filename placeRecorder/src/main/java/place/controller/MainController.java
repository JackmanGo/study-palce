package place.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import place.service.FormDataService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/main")
public class MainController {
    private static final Logger logger= LoggerFactory.getLogger(MainController.class);
    @Autowired
    private FormDataService formDataService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitForm(
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
            Long id = formDataService.saveFormData(title, description, imageFile, request);

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
