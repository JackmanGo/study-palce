package place.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import place.dao.LocationMapper;
import place.param.Location;

import java.util.List;

/**
 * @author wangxi
 * @date 2025/9/6 19:03
 */
@Service
public class LocationService {

    @Autowired
    private LocationMapper locationMapper;

    public int addLocation(Location location) {
        return locationMapper.insertLocation(location);
    }

    public List<Location> getLocationsByUserId() {
        // 使用XML配置的方式
        return locationMapper.selectAllLocations();

    }

    public int deleteLocation(Integer id) {
        // 使用XML配置的方式
        return locationMapper.deleteLocationByIdAndUserId(id);
    }
}
