package com.lyytest.Websocketdemo.Test2;

import java.util.ArrayList;
import java.util.List;
/**
 * 分组工具类 测试
 */
public class TreeUtilTest {
    public static void main(String[] args) {
        List<TreeSelect> list = new ArrayList<>();

        list.add(new TreeSelect(1L,"一级分组1",0L));
        list.add(new TreeSelect(2L,"一级分组2",0L));
        list.add(new TreeSelect(3L,"一级分组3",0L));
        list.add(new TreeSelect(4L,"一级分组4",0L));
        list.add(new TreeSelect(5L,"一级分组5",0L));
        list.add(new TreeSelect(6L,"一级分组6",0L));

        list.add(new TreeSelect(11L,"二级分组1",1L));
        list.add(new TreeSelect(22L,"二级分组2",2L));
        list.add(new TreeSelect(33L,"二级分组3",3L));
        list.add(new TreeSelect(44L,"二级分组4",4L));
        list.add(new TreeSelect(55L,"二级分组5",5L));
        list.add(new TreeSelect(66L,"二级分组6",6L));

        list.add(new TreeSelect(111L,"三级分组1",11L));
        list.add(new TreeSelect(222L,"三级分组2",22L));
        list.add(new TreeSelect(333L,"三级分组3",33L));
        list.add(new TreeSelect(444L,"三级分组4",44L));
        list.add(new TreeSelect(555L,"三级分组5",55L));
        list.add(new TreeSelect(666L,"三级分组6",66L));

        //List<TreeSelect> treeSelects = TreeUtils.buildTree1(list);
        //List<TreeSelect> treeSelects = TreeUtils.buildTree2(list);
        List<TreeSelect> treeSelects = TreeUtil.buildTree3(list);

        for (TreeSelect treeSelect : treeSelects) {
            System.out.println(treeSelect);
        }
    }
}
