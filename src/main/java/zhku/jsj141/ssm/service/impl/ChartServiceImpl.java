package zhku.jsj141.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhku.jsj141.ssm.mapper.ChartMapper;
import zhku.jsj141.ssm.po.Chart;
import zhku.jsj141.ssm.service.ChartService;
@Service("chartService")
public class ChartServiceImpl implements ChartService {
	@Autowired
	private ChartMapper chartMapper;
	public int deleteByPrimaryKey(Integer id) throws Exception {
		return chartMapper.deleteByPrimaryKey(id);
	}

	public int insert(Chart record) throws Exception {
		return chartMapper.insert(record);
	}

	public int insertSelective(Chart record) throws Exception {
		return chartMapper.insertSelective(record);
	}

	public Chart selectByPrimaryKey(Integer id) throws Exception {
		return chartMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(Chart record) throws Exception {
		return chartMapper.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(Chart record) throws Exception {
		return chartMapper.updateByPrimaryKey(record);
	}

}
