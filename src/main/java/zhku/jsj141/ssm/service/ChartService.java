package zhku.jsj141.ssm.service;

import zhku.jsj141.ssm.po.Chart;

public interface ChartService {
	public int deleteByPrimaryKey(Integer id) throws Exception;

	public int insert(Chart record) throws Exception;

	public int insertSelective(Chart record) throws Exception;

	public Chart selectByPrimaryKey(Integer id) throws Exception;

	public int updateByPrimaryKeySelective(Chart record) throws Exception;

	public int updateByPrimaryKey(Chart record) throws Exception;
}
