package place.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import place.param.LoginRequest;
import place.param.LoginResponse;
import place.util.JwtUtil;

import javax.validation.Valid;

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

}
