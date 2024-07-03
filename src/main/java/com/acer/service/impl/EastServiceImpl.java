package com.acer.service.impl;

import com.acer.config.EastElasticsearchConfig;
import com.acer.model.po.FaceDevice;
import com.acer.service.EastService;
import com.acer.utils.DateTimeSplit;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询东区ES抓拍
 */
@Service
@RequiredArgsConstructor
public class EastServiceImpl implements EastService {

    private final EastElasticsearchConfig client;

    @Value("${east-es.es-person}")
    private String person;

    /**
     * 查询东区人脸抓拍数据
     * @param faceDevice 设备详情
     * @param num 查询抓拍数据数量
     * @param startDateTimeStr 起始时间
     * @param endDateTimeStr 结束时间
     */
    @Override
    public FaceDevice saveDeviceCount(FaceDevice faceDevice, Integer num,String startDateTimeStr,String endDateTimeStr) {
        try {
            // 字符串转数字
            ArrayList<Integer> startDateList = DateTimeSplit.dateStringSplit(startDateTimeStr);
            ArrayList<Integer> endDateList = DateTimeSplit.dateStringSplit(endDateTimeStr);

            // 设备ID和日期范围
            LocalDate startDate = LocalDate.of(startDateList.get(0), startDateList.get(1), startDateList.get(2));
            LocalDate endDate = LocalDate.of(endDateList.get(0), endDateList.get(1), endDateList.get(2));

            // 存储每天的count结果
            Map<LocalDate, Long> countsByDay = new HashMap<>();

            // 循环查询每一天的数据
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                // 构建日期范围查询
                String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("deviceId", faceDevice.getDeviceCode()))
                        .must(new RangeQueryBuilder("shotTime")
                                .gte(formattedDate + "T00:00:00")
                                .lte(formattedDate + "T23:59:59")
                                .format("yyyy-MM-dd'T'HH:mm:ss"));

                // 构建搜索请求
                SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(boolQuery);
                SearchRequest searchRequest = new SearchRequest(this.person).source(sourceBuilder);

                // 发送请求并解析结果
                SearchResponse response = client.client().search(searchRequest, RequestOptions.DEFAULT);
                long count = response.getHits().getTotalHits().value;

                // 存储结果
                countsByDay.put(currentDate, count);
                if (count <= num) {
                    countsByDay.clear();
                    return null;
                }

                // 移动到下一天
                currentDate = currentDate.plusDays(1);
            }

            // 输出结果示例
            if (countsByDay.size() > 0) {
                int i = 0;
                for (Map.Entry<LocalDate, Long> entry : countsByDay.entrySet()) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(faceDevice.getDeviceName());
                    buffer.append(" - ");
                    buffer.append(faceDevice.getDeviceCode());
                    buffer.append(" - ");
                    buffer.append(entry.getKey());
                    buffer.append(": ");
                    buffer.append(entry.getValue());
                    buffer.append("-计数：");
                    buffer.append(++i);
                    System.out.println(buffer.toString());
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return faceDevice;
    }

    @Override
    public FaceDevice saveDeviceCount1(FaceDevice faceDevice, Integer num, String startDateTimeStr, String endDateTimeStr) {
        try {
            // 字符串转数字
            ArrayList<Integer> startDateList = DateTimeSplit.dateStringSplit(startDateTimeStr);
            ArrayList<Integer> endDateList = DateTimeSplit.dateStringSplit(endDateTimeStr);

            // 设备ID和日期范围
            LocalDate startDate = LocalDate.of(startDateList.get(0), startDateList.get(1), startDateList.get(2));
            LocalDate endDate = LocalDate.of(endDateList.get(0), endDateList.get(1), endDateList.get(2));


            // 循环查询每一天的数据
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                // 构建日期范围查询
                String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("deviceId", faceDevice.getDeviceCode()))
                        .must(new RangeQueryBuilder("shotTime")
                                .gte(formattedDate + "T00:00:00")
                                .lte(formattedDate + "T23:59:59")
                                .format("yyyy-MM-dd'T'HH:mm:ss"));

                // 构建搜索请求
                SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().query(boolQuery);
                SearchRequest searchRequest = new SearchRequest(this.person).source(sourceBuilder);

                // 发送请求并解析结果
                SearchResponse response = client.client().search(searchRequest, RequestOptions.DEFAULT);
                long count = response.getHits().getTotalHits().value;

                // 存储结果
                /**
                 * 逻辑：如果count小于0，表示设备没有抓拍
                 */
                if (count <= num) {
                    StringBuffer buffer = new StringBuffer("没有抓拍设备：");
                    buffer.append(faceDevice.getDeviceName());
                    buffer.append(" - ");
                    buffer.append(currentDate);
                    System.out.println(buffer.toString());
                }

                // 移动到下一天
                currentDate = currentDate.plusDays(1);
            }


        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return faceDevice;
    }
}
