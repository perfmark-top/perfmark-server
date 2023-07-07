package io.github.perfmarktop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import io.github.perfmarktop.mapper.CpuDataMapper;
import io.github.perfmarktop.pojo.CpuData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


@SpringBootTest
public class TableTest {
    @Autowired
    private CpuDataMapper cpuDataMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() {
        // cpuDataMapper.truncateTable();
        String truncateSql = "truncate table cpu_data";
        jdbcTemplate.execute(truncateSql);
    }

}
