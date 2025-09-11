package place.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import place.param.FormDataRes;
import place.param.Location;
import place.param.LoginRequest;
import place.param.LoginResponse;
import place.service.FormDataService;
import place.service.LocationService;
import place.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangxi
 * @date 2025/9/6 16:10
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    //@Autowired
    //private UserService userService;
    @Autowired
    private FormDataService formDataService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // 验证用户凭据
        //if (userService.validateUser(username, password)) {
        if (true) {
            // 生成JWT令牌
            String token = jwtUtil.generateToken(username);

            // 返回成功响应
            LoginResponse response = new LoginResponse(true, "登录成功", token);
            return ResponseEntity.ok(response);
        } else {
            // 返回失败响应
            LoginResponse response = new LoginResponse(false, "用户名或密码错误", null);
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/location/list")
    public ResponseEntity<Map<String, Object>> getLocations() {

        try {
            List<Location> locations = locationService.getLocationsByUserId();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", locations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
    }

    @PostMapping("/location/add")
    public ResponseEntity<Map<String, Object>> addLocation(
            @RequestBody Location location,
            HttpServletRequest request) {
        try {
            int result = locationService.addLocation(location);
            if (result > 0) {
                return ResponseEntity.ok().build();
            } else {
                throw new RuntimeException("位置添加失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("服务器错误: " + e.getMessage());
        }
    }
    @DeleteMapping("/location/{id}")
    public ResponseEntity<Map<String, Object>> deleteLocation(
            @PathVariable Integer id,
            HttpServletRequest request) {

        try {
            int result = locationService.deleteLocation(id);
            if (result > 0) {
                return ResponseEntity.ok().build();
            } else {
                throw new RuntimeException("位置不存在或已删除");
            }
        } catch (Exception e) {
            throw new RuntimeException("删除失败: " + e.getMessage());
        }
    }
    @GetMapping("/records")
    public ResponseEntity<Map<String, Object>> getRecordss(@RequestParam(required = false) String phone,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        try {
            List<FormDataRes> records = formDataService.getRecordsByConditions(phone, startDate, endDate);

            records.forEach(it->it.setImagePath("http://127.0.0.1:8081/place/"+it.getImagePath()));
            return ResponseEntity.ok(createSuccessResponse("提交成功", records));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse(-1, "提交失败: " + e.getMessage()));
        }
    }
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", message);
        response.put("data", data);
        return response;
    }
    private Map<String, Object> createErrorResponse(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        return response;
    }
}
