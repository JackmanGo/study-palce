package place.dao;

import org.apache.ibatis.annotations.*;
import place.param.Location;

import java.util.List;

/**
 * @author wangxi
 * @date 2025/9/6 18:59
 */
@Mapper
public interface LocationMapper {

    List<Location> selectAllLocations();

    int deleteLocationByIdAndUserId(@Param("id") Integer id);

    int insertLocation(Location location);
}
