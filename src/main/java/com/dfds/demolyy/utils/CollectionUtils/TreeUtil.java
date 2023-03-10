package com.dfds.demolyy.utils.CollectionUtils;

import cn.hutool.core.util.ObjectUtil;

import java.util.*;
import java.util.stream.Collectors;

public class TreeUtil {
    /**-------------------------------------------------------
     * 方式1:原始递归构建树
     * @param trees 列表
     * @return 树结构列表
     */
    public static List<TreeSelect> buildTree1(List<TreeSelect> trees)
    {
        List<TreeSelect> returnList = new ArrayList<TreeSelect>();
        List<Long> tempList = new ArrayList<Long>();
        for (TreeSelect dept : trees)
        {
            tempList.add(dept.getId());
        }
        for (Iterator<TreeSelect> iterator = trees.iterator(); iterator.hasNext();)
        {
            TreeSelect treeSelect = (TreeSelect) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(treeSelect.getParentId()))
            {
                recursionFn(trees, treeSelect);
                returnList.add(treeSelect);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = trees;
        }
        return returnList;
    }
    /**
     * 递归列表
     */
    private static void recursionFn(List<TreeSelect> list, TreeSelect t)
    {
        // 得到子节点列表
        List<TreeSelect> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TreeSelect tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private static List<TreeSelect> getChildList(List<TreeSelect> list, TreeSelect t)
    {
        List<TreeSelect> tlist = new ArrayList<TreeSelect>();
        for (TreeSelect n : list) {

            if (ObjectUtil.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild(List<TreeSelect> list, TreeSelect t)
    {
        return getChildList(list, t).size() > 0;
    }



    /**-------------------------------------------------------
     * 方式2:Stream流构建树（原理还是递归）
     */
    public static List<TreeSelect> buildTree2(List<TreeSelect> trees){
        //获取parentId = 0的根节点
        List<TreeSelect> list = trees.stream().filter(item -> item.getParentId() == 0L).collect(Collectors.toList());
        //根据parentId进行分组
        Map<Long, List<TreeSelect>> map = trees.stream().collect(Collectors.groupingBy(TreeSelect::getParentId));
        recursionFnTree(list, map);
        return list;
    }
    /**
     * 递归遍历节点
     */
    public static void recursionFnTree(List<TreeSelect> list, Map<Long, List<TreeSelect>> map) {
        for (TreeSelect treeSelect : list) {
            List<TreeSelect> childList = map.get(treeSelect.getId());
            treeSelect.setChildren(childList);
            if (null != childList && 0 < childList.size()) {
                recursionFnTree(childList, map);
            }
        }
    }

    /**-------------------------------------------------------------
     * 方式3:Stream流升级构建树
     */
    public static List<TreeSelect> buildTree3(List<TreeSelect> trees){
        //获取父节点
        List<TreeSelect> collect = trees.stream().filter(m -> m.getParentId() == 0).map(
                (m) -> {
                    m.setChildren(getChildrenList(m, trees));
                    return m;
                }
        ).collect(Collectors.toList());
        return collect;
    }

    /**
     * 获取子节点列表
     */
    public static List<TreeSelect> getChildrenList(TreeSelect tree, List<TreeSelect> list) {
        List<TreeSelect> children = list.stream().filter(item -> Objects.equals(item.getParentId(), tree.getId())).map(
                (item) -> {
                    item.setChildren(getChildrenList(item, list));
                    return item;
                }
        ).collect(Collectors.toList());
        return children;
    }

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
