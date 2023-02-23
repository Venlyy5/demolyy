package com.dfds.demolyy.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * List 分批次
 * 将一个大集合,按BATCH_NUMBER的数量为一组,分成多个子集合
 */
public class ListUtils {
    /**
     * 定义分批次入库的数量
     */
    public static final int BATCH_NUMBER = 20;

    /**
     * 拆分list
     * @param listSize
     * @param originalList
     */
    public static List<List<?>> splitList(int listSize, List<?> originalList) {
        List<List<?>> targetList = new ArrayList<List<?>>();
        // 拆分list
        int count = listSize / BATCH_NUMBER;
        int mod = listSize % BATCH_NUMBER;
        for (int i = 0; i < count; i++) {
            List<?> subList = originalList.subList(i * BATCH_NUMBER, (i + 1) * BATCH_NUMBER);
            targetList.add(subList);
        }
        if (mod != 0) {
            List<?> subList = originalList.subList(count * BATCH_NUMBER, (count * BATCH_NUMBER + mod));
            targetList.add(subList);
        }
        return targetList;
    }

    public static void main(String[] args) {
        ArrayList<Integer> list2 = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            list2.add(i);
        }
        List<List<?>> lists = ListUtils.splitList(68, list2);

        for (List<?> objects : lists) {
            System.out.println(objects);
        }
    }
}