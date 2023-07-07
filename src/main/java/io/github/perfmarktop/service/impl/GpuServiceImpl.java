package io.github.perfmarktop.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.perfmarktop.mapper.GpuDataMapper;
import io.github.perfmarktop.service.GpuService;
import io.github.perfmarktop.vo.GpuVO;
import io.github.perfmarktop.vo.param.SortParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GpuServiceImpl implements GpuService {
    @Autowired
    private GpuDataMapper gpuDataMapper;

    @Override
    public IPage<GpuVO> getSortedEntities(Page<GpuVO> page, SortParam sortParam, String key) {
        return gpuDataMapper.getSortedGpuData(page,sortParam,key);
    }

    @Override
    public List<GpuVO> getRankCpu(List<String> ids) {
        return gpuDataMapper.getGpuByIds(ids);
    }
}
