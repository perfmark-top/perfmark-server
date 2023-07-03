package io.github.perfmarktop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.perfmarktop.pojo.CpuData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface CpuDataMapper extends BaseMapper<CpuData> {
    Integer batchInsert(@Param("data") List<CpuData> data);
}
