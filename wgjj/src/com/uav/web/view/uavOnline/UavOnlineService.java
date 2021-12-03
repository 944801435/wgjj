package com.uav.web.view.uavOnline;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.uav.base.model.SysDept;
import com.uav.base.model.SysUser;
import com.uav.base.util.ExcelUtil;
import com.uav.base.util.MapUtil;
import com.uav.web.view.uavOnline.base.Airline;
import com.uav.web.view.uavOnline.base.LngLatPoint;

@Service
@SuppressWarnings("unchecked")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class UavOnlineService {

	@Autowired
	private UavOnlineDao uavOnlineDao;

	public SysUser getSysUserByUserid(String id) {
		return (SysUser) uavOnlineDao.findById(SysUser.class, id);
	}

	public SysDept getSysDeptByDeptId(String id) {
		return (SysDept) uavOnlineDao.findById(SysDept.class, id);
	}

	private List<Airline> airlineList = null;

	public List<Airline> getAirlineList() {
		return airlineList;
	}

	@Autowired
	private ResourceLoader resourceLoader;

	private void initAirline() {
		try {
			Resource resource = resourceLoader.getResource("classpath:Airline.xlsx");
			List<List<Object>> list = ExcelUtil.readExcel(resource.getFile());
			if (list != null && list.size() > 0) {
				airlineList = new ArrayList<Airline>();
				ObjectMapper mapper = new ObjectMapper();
				for (List<Object> row : list) {
					Airline line = new Airline();

					Object name = row.get(0);
					Object array = row.get(1);
					line.setName(name.toString());
					line.setPoints(new ArrayList<LngLatPoint>());
					List<double[]> points = mapper.readValue(array.toString(),
							TypeFactory.defaultInstance().constructCollectionType(List.class, double[].class));
					for (double[] ds : points) {
						double lat = MapUtil.convertToDecimal(ds[0]);
						double lng = MapUtil.convertToDecimal(ds[1]);
						line.getPoints().add(new LngLatPoint(lat, lng));
					}
					airlineList.add(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void init() {
		initAirline();
	}
}