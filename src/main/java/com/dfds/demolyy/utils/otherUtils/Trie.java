package com.dfds.demolyy.utils.otherUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
        root.var = ' ';
    }

    /**
     * 插入trie树
     *
     * @param word
     */
    public void insert(String word) {
        TrieNode ws = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (!ws.children.keySet().contains(c)) {
                ws.children.put(c, new TrieNode(c));
            }
            ws = ws.children.get(c);
        }
        ws.isWord = true;
    }

    /**
     * 查询以关键字开头的数据
     * @param prefix
     * @return
     */
    public List<String> startWith(String prefix) {
        List<String> match = new ArrayList<>();
        TrieNode ws = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (!ws.children.keySet().contains(c)) {
                return match;
            }
            ws = ws.children.get(c);
        }
        List<String> data = getData("", ws);
        for (String s : data) {
            match.add(prefix + s);
        }
        return match;
    }

    public List<String> getData(String prefix, TrieNode trieNode) {
        List<String> list = new ArrayList<>();
        if (trieNode.children == null || trieNode.children.size() == 0) {
            // list.add(prefix + trieNode.var);
            list.add(prefix);
            return list;
        }
        if (trieNode.isWord) {
            list.add(prefix);
            // list.add(prefix + trieNode.var);
        }
        for (Map.Entry<Character, TrieNode> nodeEntry : trieNode.children.entrySet()) {
            TrieNode node = nodeEntry.getValue();
            List<String> data = getData(nodeEntry.getKey()+"", node);
            for (String s : data) {
                list.add(prefix + s);
            }
        }
        return list;
    }


    /**-----------------------------------------------
     * 查找字符串数组中最长公共前缀，若不存在公共前缀返回""
     * 先利用Arrays.sort()为数组排序，再将数组第一个元素和最后一个元素的字符从前往后对比即可
     * @param str 待查找的字符串数组
     * @return 公共前缀
     */
    public static String getSamePreStr(String[] str){
        if (! checkStr(str)){
            return "";
        }
        int len = str.length;
        StringBuilder stringBuilder = new StringBuilder();

        //字符串数组排序（包含数字的话,数字排在前面）
        Arrays.sort(str);
        // 第一个元素和最后一个元素 最短length元素的值
        int num = Math.min(str[0].length(), str[len-1].length());
        //数组第一个元素和最后一个元素的字符从前往后对比
        for (int i = 0; i < num; i++) {
            if (str[0].charAt(i) == str[len-1].charAt(i)){
                stringBuilder.append(str[0].charAt(i));
            }else {
                break;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 判断一个字符串数组是否 为null或包含空串
     * @param str
     * @return
     */
    public static boolean checkStr(String[] str){
        boolean flag  = false;
        if (str != null){
            //遍历str检查元素值，如果字符数组为null或有空串返回true
            for (int i=0; i<str.length; i++){
                if (str[i]!=null && str[i].length()!=0){
                    flag  = true;
                }
                else{
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        // 查询以关键字开头的数据
        Trie t = new Trie();
        t.insert("Lyy程序");
        t.insert("Lyy头条");
        t.insert("ABC旅游");
        t.insert("Lyy项目");
        t.insert("Lyy教育");
        List<String> ret = t.startWith("Lyy");
        System.out.println(ret);

        // 查找字符串数组公共前缀
        String[] str = { "customer", "car", "cast"};
        System.out.println(getSamePreStr(str));

    }
}
