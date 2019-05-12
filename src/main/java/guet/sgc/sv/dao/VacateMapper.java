package guet.sgc.sv.dao;

import guet.sgc.sv.entity.Vacate;
import guet.sgc.sv.entity.VacateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VacateMapper {
    int countByExample(VacateExample example);

    int deleteByExample(VacateExample example);

    int deleteByPrimaryKey(Long leaveId);

    int insert(Vacate record);

    int insertSelective(Vacate record);

    List<Vacate> selectByExample(VacateExample example);

    Vacate selectByPrimaryKey(Long leaveId);

    int updateByExampleSelective(@Param("record") Vacate record, @Param("example") VacateExample example);

    int updateByExample(@Param("record") Vacate record, @Param("example") VacateExample example);

    int updateByPrimaryKeySelective(Vacate record);

    int updateByPrimaryKey(Vacate record);
}