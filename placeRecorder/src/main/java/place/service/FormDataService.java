package place.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import place.dao.FormDataMapper;
import place.param.FormData;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class FormDataService {
    @Autowired
    private FormDataMapper formDataMapper;

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    // 保存表单数据
    @Transactional
    public Long saveFormData(String title, String description, MultipartFile imageFile,
                             HttpServletRequest request) throws IOException {

        String imagePath = null;

        // 处理图片上传
        if (imageFile != null && !imageFile.isEmpty()) {
            imagePath = saveImage(imageFile);
        }

        // 创建FormData对象
        FormData formData = new FormData();
        formData.setTitle(title);
        formData.setDescription(description);
        formData.setImagePath(imagePath);
        formData.setCreateTime(new Date());
        formData.setIpAddress(getClientIp(request));
        formData.setUserAgent(request.getHeader("User-Agent"));

        // 保存到数据库
        formDataMapper.insert(formData);

        return formData.getId();
    }

    // 根据ID查询数据
    public FormData getFormDataById(Long id) {
        return formDataMapper.findById(id);
    }

    // 查询所有数据
    public List<FormData> getAllFormData() {
        return formDataMapper.findAll();
    }

    // 根据标题查询
    public List<FormData> getFormDataByTitle(String title) {
        return formDataMapper.findByTitle(title);
    }

    // 更新数据
    @Transactional
    public boolean updateFormData(FormData formData) {
        return formDataMapper.update(formData) > 0;
    }

    // 删除数据
    @Transactional
    public boolean deleteFormData(Long id) {
        return formDataMapper.delete(id) > 0;
    }

    // 获取数据总数
    public int getFormDataCount() {
        return formDataMapper.count();
    }

    // 保存图片文件（保持不变）
    private String saveImage(MultipartFile imageFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        Path filePath = uploadPath.resolve(uniqueFileName);
        imageFile.transferTo(filePath.toFile());

        return uploadDir + uniqueFileName;
    }

    // 获取客户端IP（保持不变）
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
