package com.example.demo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU实现1
 */
public class LRUCache1 {

    // 节点对象, 一个节点对象包含上,下节点信息和自身键值对.
    static class Node{
        Node prev;
        Node next;

        String key;
        Object value;

        public Node(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        // 打印节点对象, 格式: 后结点 <--节点--> 前节点
        @Override
        public String toString() {
            //return  key +"="+ value ;

            StringBuilder sb = new StringBuilder(128);
            sb.append(this.next == null ? null: this.next.key);
            sb.append("<--"+ this.key + "-->");
            sb.append(this.prev == null ? null : this.prev.key);
            return sb.toString();
        }
    }

    int limit; //容量上限
    Node head; //头节点
    Node tail; //尾结点
    Map<String, Node> map;
    public LRUCache1(int limit){
        this.limit = Math.max(limit, 2); //上限不能低于2
        this.head = new Node("Head", null);
        this.tail = new Node("Tail", null);
        head.next = tail;
        tail.prev = head;
        this.map = new HashMap<>();
    }


    // 断开节点
    public void unlink(Node node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    // 插入到头节点
    public void toHead(Node node){
        node.prev = this.head;
        node.next = this.head.next;
        this.head.next.prev = node;
        this.head.next = node;
    }

    // 删除节点
    public void remove(String key){
        Node old = this.map.remove(key);
        unlink(old);
    }

    /**---------------------------
     * 查询节点, 并将此的节点移到头部
     */
    public Object get(String key){

        Node node = this.map.get(key);
        if(node == null){
            return null;
        }

        //先断开节点, 再移动到头部
        unlink(node);
        toHead(node);

        return node.value;
    }

    /**--------------
     * 新增/更新 节点  (头插法)
     */
    public void put(String key, Object value){
        // 查询这个节点, 如果不存在做保存,并移到头部
        Node node = this.map.get(key);
        if(node == null){
            node = new Node(key, value);
            this.map.put(key, node);
        }else {
            // 如果存在做更新, 并先断开再移到头部
            node.value = value;
            unlink(node);
        }

        // 移到头部(头插法)
        toHead(node);

        // 如果容量超过limit上限, 则移除最老的节点(尾结点),并断开它的链接
        if (map.size() > limit){
            Node lastNode = this.tail.prev;
            this.map.remove(lastNode.key);

            unlink(lastNode);
        }
    }


    @Override
    public String toString() {
        return map.values().toString();
    }


    public static void main(String[] args) {
        // 缓存大小为5, 加入5个数据(头插法)
        LRUCache1 cache = new LRUCache1(5);
        cache.put("1", 1); System.out.println(cache);
        cache.put("2", 2); System.out.println(cache);
        cache.put("3", 3); System.out.println(cache);
        cache.put("4", 4); System.out.println(cache);
        cache.put("5", 5); System.out.println(cache);

        // 查询key=3，此节点会移动到链表最前
        cache.get("3"); System.out.println(cache);

        // 新增节点时超过上限, 移除最老数据key=1
        cache.put("6", 6); System.out.println(cache);
    }
}