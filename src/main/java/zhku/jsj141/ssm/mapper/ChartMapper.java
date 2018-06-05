package zhku.jsj141.ssm.mapper;

import zhku.jsj141.ssm.po.Chart;

public interface ChartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Chart record);

    int insertSelective(Chart record);

    Chart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Chart record);

    int updateByPrimaryKey(Chart record);
}