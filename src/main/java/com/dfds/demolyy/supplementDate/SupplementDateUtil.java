package com.dfds.demolyy.supplementDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class SupplementDateUtil {

    public static final String[] monthAndDay = new String[]{"01","02","03","04","05","06","07","08","09","10","11","12"};

    /**
     * 补全月份并赋值为0
     */
    public static List<DataTrans> supplementDate(List<DataTrans> params){

        List<DataTrans> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //当前年份
        int year = calendar.get(Calendar.YEAR);
        for (DataTrans data:params){
            list.add(data);
            for (String s:monthAndDay){
                DataTrans dataTrans = new DataTrans();
                String date = year+"-"+s;
                if (!date.equals(data.getName())){
                    dataTrans.setName(date);
                    dataTrans.setValue(0);
                    list.add(dataTrans);
                }
            }
        }
        return merge(list);
    }

    /**
     * 集合中相同属性去重、值合并
     */
    public static List<DataTrans> merge(List<DataTrans> list) {
        List<DataTrans> result = list.stream()
                // 表示name为key，接着如果有重复的，那么从DataTrans对象o1与o2中筛选出一个，这里选择o1，
                // 并把name重复，需要将value与o1进行合并的o2, 赋值给o1，最后返回o1
                .collect(Collectors.toMap(DataTrans::getName, a -> a, (o1, o2)-> {
                    o1.setValue(o1.getValue() + o2.getValue());
                    return o1;
                })).values().stream().collect(Collectors.toList());
        return result;
    }


    /**
     * 数据库查询出来的统计数据有时候日期是不连续的.
     * 但是前端展示要补全缺失的日期.
     * 此方法返回一个给定日期期间的所有日期字符串列表,具体在业务逻辑中去判断补全.
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static List<String> completionDate(LocalDateTime startDate, LocalDateTime endDate) {
        //日期格式化
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> dateList = new ArrayList<>();
        //遍历给定的日期期间的每一天
        for (int i = 0; !Duration.between(startDate.plusDays(i), endDate).isNegative(); i++) {
            //添加日期
            dateList.add(startDate.plusDays(i).format(formatter));
        }
        return dateList;
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(3);
        Duration between = Duration.between(localDateTime, LocalDateTime.now());

        System.out.println(between);

        DataTrans dataTrans = new DataTrans("2022-08",0.05);
        DataTrans dataTrans1 = new DataTrans("2022-05",0.8);
        DataTrans dataTrans2 = new DataTrans("2022-05",0.8);
        DataTrans dataTrans3 = new DataTrans("2022-11",0.86);
        List<DataTrans> list = new ArrayList<>();
        list.add(dataTrans);
        list.add(dataTrans1);
        list.add(dataTrans2);
        list.add(dataTrans3);
        List<DataTrans> listResult = supplementDate(list);

        // 根据月份升序排序
        Collections.sort(listResult, Comparator.comparing(DataTrans::getName,(t1, t2) -> t1.compareTo(t2)));
        for (DataTrans trans : listResult) {
            System.out.println(trans);
        }

        // 测试过去7天
        List<String> dateList = completionDate(LocalDateTime.now().minusDays(7), LocalDateTime.now());
        for (String date : dateList) {
            System.out.println(date);
        }




        // =================== completionDate测试
        /*if(null==startDate){
            startDate = getPastDate(7);
        }
        if(null==endDate){
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            endDate = format.format(date);
        }
        List<UserTotalDTO> list = statisticsService.countUserTotal(orgId, regFrom, startDate,endDate);
        //获取日期
        LocalDateTime startDate1 = LocalDateTime.now().minusDays(7);
        LocalDateTime endDat1 = LocalDateTime.now();
        //3.补全为空的日期
        //补全后的结果
        List<Map<String, Object>> result = new ArrayList<>();
        boolean dbDateExist = false;
        List<String> dateList = CompletionDateUtils.completionDate(startDate1, endDat1);

        List<Map<String, Object>>table=new ArrayList<>();

        for (int i = 0; i <list.size() ; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("daytime",list.get(i).getStartDate());
            map.put("total",list.get(i).getCount());
            table.add(map);
        }
        // map.put("list",  table.add(map);

        for (String date : dateList) {
            for (Map<String, Object> row : table) {
                if (row.get("daytime").equals(date)) {
                    //集合已包含该日期
                    dbDateExist = true;
                    result.add(row);
                    break;
                }
            }
            //添加补全的数据到最后结果列表
            if (!dbDateExist) {
                Map<String, Object> temp = new HashMap<>(2);
                temp.put("daytime", date);
                temp.put("total", 0);
                result.add(temp);
            }
            //状态修改为不存在
            dbDateExist = false;
        }*/

    }
}
