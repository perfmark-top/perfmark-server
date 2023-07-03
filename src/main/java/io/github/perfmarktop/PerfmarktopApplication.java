package io.github.perfmarktop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.perfmarktop.exception.SystemException;
import io.github.perfmarktop.mapper.CpuDataMapper;
import io.github.perfmarktop.mapper.GpuDataMapper;
import io.github.perfmarktop.pojo.CpuData;
import io.github.perfmarktop.pojo.GpuData;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.perfmarktop.constant.Constant.*;
import static io.github.perfmarktop.constant.Constant.GPUHOST;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class PerfmarktopApplication {
    @Autowired
    private CpuDataMapper cpuDataMapper;

    @Autowired
    private GpuDataMapper gpuDataMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(PerfmarktopApplication.class, args);
    }
    @Scheduled(cron = "0 0 2 * * ?") // 每天凌晨两点执行任务
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
            // 截断操作
            String truncateSql = "truncate table gpu_data";
            jdbcTemplate.execute(truncateSql);
            log.info("gpu_data表已经被截断");
            // 批量插入
            Integer integer = gpuDataMapper.batchInsert(gpuDataList);
            log.info("gPU数据插入成功"+"插入了"+integer+"条数据");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @Scheduled(cron = "0 0 3 * * ?")
    public void processCpuData() {
        // 获取数据
        String cpu = getData("cpu");
        if (cpu.isEmpty()) {
            log.error("获取的数据为："+cpu);
            throw new RuntimeException("获取原始数据失败");
        }
        // 创建ObjectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        List<CpuData> cpuDataList = null;
        try {
            cpuDataList = objectMapper.readValue(cpu, new TypeReference<List<CpuData>>(){});
            log.info("cpuDataList的长度："+cpuDataList.size());
            // 截断操作
            String truncateSql = "truncate table gpu_data";
            jdbcTemplate.execute(truncateSql);
            log.info("cpu_data表已经被截断");
            Integer integer = cpuDataMapper.batchInsert(cpuDataList);
            log.info("CPU数据插入成功"+"插入了"+integer+"条数据");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

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
            throw  new SystemException("未识别的输入");
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
        } catch (ProtocolException e) {
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
}
