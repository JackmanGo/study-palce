package place.dao;

import org.apache.ibatis.annotations.*;
import place.param.FormData;

import java.util.List;

@Mapper
public interface FormDataMapper {
    // 插入数据并返回自增ID
    //@Insert("INSERT INTO form_data (title, description, image_path, create_time, ip_address, user_agent) " +
            //"VALUES (#{title}, #{description}, #{imagePath}, #{createTime}, #{ipAddress}, #{userAgent})")
    //@Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FormData formData);

    // 根据ID查询
    @Select("SELECT * FROM form_data WHERE id = #{id}")
    FormData findById(Long id);

    // 查询所有数据
    @Select("SELECT * FROM form_data ORDER BY create_time DESC")
    List<FormData> findAll();

    // 根据标题模糊查询
    @Select("SELECT * FROM form_data WHERE title LIKE CONCAT('%', #{title}, '%') ORDER BY create_time DESC")
    List<FormData> findByTitle(String title);

    // 更新数据
    @Update("UPDATE form_data SET title = #{title}, description = #{description}, image_path = #{imagePath} WHERE id = #{id}")
    int update(FormData formData);

    // 删除数据
    @Delete("DELETE FROM form_data WHERE id = #{id}")
    int delete(Long id);

    // 统计总数
    @Select("SELECT COUNT(*) FROM form_data")
    int count();
}
