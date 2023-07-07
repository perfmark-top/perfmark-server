package io.github.perfmarktop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.perfmarktop.vo.CpuVO;
import io.github.perfmarktop.vo.GpuVO;
import io.github.perfmarktop.vo.param.SortParam;

import java.util.List;

/**
 * GPU 接口方法
 * @author LukeZhang
 * @date 2023/7/5 10:08
 */
public interface GpuService {
    /**
     * 指定字段排序
     * @param page 分页字段
     * @param sortParam 排序字段
     * @return 分页信息
     */
    public IPage<GpuVO> getSortedEntities(Page<GpuVO> page, SortParam sortParam, String key);

    /**
     * 获取多个id的GPU详情信息
     * @param ids id列表
     * @return GPU详情列表
     */
    public List<GpuVO> getRankCpu(List<String> ids);
}
