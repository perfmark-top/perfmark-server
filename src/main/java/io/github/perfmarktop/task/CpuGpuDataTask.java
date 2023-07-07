package io.github.perfmarktop.task;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.perfmarktop.mapper.CpuDataMapper;
import io.github.perfmarktop.mapper.GpuDataMapper;
import io.github.perfmarktop.pojo.CpuData;
import io.github.perfmarktop.pojo.GpuData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

import static io.github.perfmarktop.constant.Constant.*;
import static io.github.perfmarktop.constant.Constant.GPUHOST;
/**
 * 定时任务获取 CPU GPU数据 并存入数据库中
 * @author LukeZhang
 * @date 2023/7/7 14:28
 */
@Slf4j
@Component
public class CpuGpuDataTask {
    @Autowired
    private CpuDataMapper cpuDataMapper;

    @Autowired
    private GpuDataMapper gpuDataMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Async
    @Scheduled(cron = "0 18 17 * * ?") // 每天凌晨两点执行任务
    @Transactional(rollbackFor = Exception.class)
    public void processGpuData() {
        String gpu = getData("gpu");
        if (gpu.isEmpty()) {
            log.error("获取的数据为："+gpu);
            throw new RuntimeException("获取原始数据失败");
        }
        // 创建ObjectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        List<GpuData> gpuDataList = null;
        try {
            gpuDataList = objectMapper.readValue(gpu, new TypeReference<List<GpuData>>(){});
            log.info("gpuDataList的长度："+gpuDataList.size());
            calculateGpuRanks(gpuDataList);
            log.info("gpuDataList 排序成功！！！");
            // 截断操作
            String truncateSql = "truncate table gpu_data";
            jdbcTemplate.execute(truncateSql);
            log.info("gpu_data表已经被截断");
            Integer integer = 0;
            for (int i = 0; i < gpuDataList.size(); i++) {
                try {
                    if (gpuDataList.get(i).getValue().toUpperCase().equals("NA")) {
                        gpuDataList.get(i).setValue(null);
                    }
                    gpuDataMapper.insert(gpuDataList.get(i));
                    integer++;
                } catch (Exception e) {
                    log.error("GPU数据插入失败,数据为："+gpuDataList.get(i).toString());
                    log.error(e.getMessage());
                }
            }
            log.info("GPU数据插入成功"+"插入了"+integer+"条数据");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @Async
    @Scheduled(cron = "0 27 17 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void processCpuData() throws JsonProcessingException {
        // 获取数据
        String cpu = getData("cpu");
        if (cpu.isEmpty()) {
            log.error("获取的数据为："+cpu);
            throw new RuntimeException("获取原始数据失败");
        }
        // 创建ObjectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        List<CpuData> cpuDataList = null;
        cpuDataList = objectMapper.readValue(cpu, new TypeReference<List<CpuData>>(){});
        log.info("cpuDataList的长度："+cpuDataList.size());
        calculateCpuRanks(cpuDataList);
        log.info("cpuDataList 排序成功！！！");
        // 截断操作
        String truncateSql = "truncate table cpu_data";
        jdbcTemplate.execute(truncateSql);
        log.info("cpu_data表已经被截断");
        Integer integer = 0;
        for (int i = 0; i < cpuDataList.size(); i++) {
            try {
                if (cpuDataList.get(i).getValue().equals("NA")) {
                    cpuDataList.get(i).setValue(null);
                }
                cpuDataMapper.insert(cpuDataList.get(i));
                integer++;
            } catch (Exception e) {
                log.error("CPU数据插入失败,数据为："+cpuDataList.get(i).toString());
                log.error(e.getMessage());
            }
        }
        log.info("CPU数据插入成功"+"插入了"+integer+"条数据");
    }

    // 获取数据方法
    private String getData(String type) {
        URL url = null;
        URL dataUrl = null;
        String host = null;
        String inputLine = null;
        String data = null;
        if ( type.equals("cpu") ) {
            try {
                url = new URL(CPURL);
                dataUrl = new URL(CPUDATAURL + QUERY);
                host = CPUHOST;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else if ( type.equals("gpu") ) {
            try {
                url = new URL(GPUURL);
                dataUrl = new URL(GPUDATAURL + QUERY);
                host = GPUHOST;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            log.error("type传入错误，type只能为cpu或者gpu");
            return "";
        }
        try {

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 获取响应头中的Set-Cookie字段
            String cookie = connection.getHeaderField("Set-Cookie");
            String sessionId = cookie.substring(0, cookie.indexOf(";"));
            log.info("成功获取Cookie：" + sessionId);
            // 创建一个HttpURLConnection对象
            HttpURLConnection con = (HttpURLConnection) dataUrl.openConnection();
            // 设置请求方法为GET
            con.setRequestMethod("GET");
            // 设置请求头中的Cookie
            con.setRequestProperty("Cookie", sessionId);
            con.setRequestProperty("Referer", url.toString());
            con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            con.setRequestProperty("Accept", "*/*");
            con.setRequestProperty("Host", host);
            // 获取响应代码
            int responseCode = con.getResponseCode();
            log.info("Response Code : " + responseCode);
            // 读取响应内容
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // 创建ObjectMapper对象
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.toString());
            JsonNode dataArrayNode = rootNode.get("data");
            data = dataArrayNode.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
    // 三种字段的排序并将排序后的序号填充到对应的字段上
    private void calculateCpuRanks(List<CpuData> cpuDataList) {
        cpuDataList.sort(Comparator.comparing(CpuData::getValue));
        setCpuRanks(cpuDataList, "valueRank");

        cpuDataList.sort(Comparator.comparing(CpuData::getCpumark));
        setCpuRanks(cpuDataList, "cpumarkRank");

        cpuDataList.sort(Comparator.comparing(CpuData::getThread));
        setCpuRanks(cpuDataList, "threadRank");
    }
    private void setCpuRanks(List<CpuData> cpuDataList, String rankField) {
        int rank = 1;
        for (CpuData cpuData : cpuDataList) {
            try {
                Field field = cpuData.getClass().getDeclaredField(rankField);
                field.setAccessible(true);
                field.set(cpuData, rank++);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("排序出现问题");
                e.printStackTrace();
            }
        }
    }
    private void calculateGpuRanks(List<GpuData> gpuDataList) {
        gpuDataList.sort(Comparator.comparing(GpuData::getG2d));
        setGpuRanks(gpuDataList, "g2dRank");

        gpuDataList.sort(Comparator.comparing(GpuData::getG3d));
        setGpuRanks(gpuDataList, "g3dRank");

        gpuDataList.sort(Comparator.comparing(GpuData::getValue));
        setGpuRanks(gpuDataList, "valueRank");
    }
    private void setGpuRanks(List<GpuData> gpuDataList, String rankField) {
        int rank = 1;
        for (GpuData gpuData : gpuDataList) {
            try {
                Field field = gpuData.getClass().getDeclaredField(rankField);
                field.setAccessible(true);
                field.set(gpuData, rank++);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("排序出现问题");
                e.printStackTrace();
            }
        }
    }
}
