package io.github.perfmarktop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.perfmarktop.pojo.GpuData;
import io.github.perfmarktop.vo.CpuVO;
import io.github.perfmarktop.vo.GpuVO;
import io.github.perfmarktop.vo.param.SortParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * GPU 接口
 * @author LukeZhang
 * @date 2023/7/7 14:45
 */
public interface GpuDataMapper extends BaseMapper<GpuData> {
    /**
     * 批量处理
     * @param data Gpu数据
     * @return 插入的总条数
     */
    Integer batchInsert(@Param("data") List<GpuData> data);
    /**
     * 根据排序字段 返回分页数据
     * @param page 分页参数
     * @param sortParam 排序字段 排序方式
     * @return 列表数据
     */
    IPage<GpuVO> getSortedGpuData(Page<GpuVO> page, @Param("sortParam") SortParam sortParam, @Param("key") String key);

    /**
     * 获取单个id或者多个id的GPu列表数据
     * @param ids id
     * @return Gpu数据
     */
    List<GpuVO> getGpuByIds(@Param("ids") List<String> ids);
}
