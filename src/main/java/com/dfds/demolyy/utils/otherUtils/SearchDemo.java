package com.dfds.demolyy.utils.otherUtils;

import java.util.Arrays;

public class SearchDemo {
    /**------------------------------------------------------
     * 二分查找
     * 在999个元素查找, 需要比较的次数最多不超过多少次
     * log2 (999) , 结果有小数进1 , Windows计算器默认是求 log10, 转为 log10 (999) / log10 (2) 计算
     */
    public static int binarySearch(int[] arr, int findNum){
        int l = 0;
        int r = arr.length - 1;
        while (l <= r){
            // int mid  = (l+r)/2;	  // l+r如果超出int最大范围, 中间索引会出现负数
            // int mid = l+ (r-l)/2;  // 解决办法1: 把加法转为减法
            int mid = (l+r) >>> 1;    // 解决办法2: 转为无符号位移运算, 而且除法运算效率更高

            if(arr[mid] < findNum){
                l = mid + 1; // 左边界+1继续找
            }else if(arr[mid] > findNum){
                r = mid - 1; // 右边界-1继续找
            }else {
                return mid;  // 找到返回索引
            }
        }
        return -1; 			// 未找到返回-1
    }

    /**------------------------------------------------------
     * 冒泡排序
     * 优化1: 内层循环比较到 总次数-已比较轮数, 表示上次排好序的不用再比较
     * 优化2: 没发生过交换直接退出, 表示已经排好序
     */
    public static void bubbleSort1(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {          //总冒泡轮数
            boolean swapped = false;  //是否发生了交换

            for (int j = 0; j < arr.length - 1 - i; j++) {  //一轮冒泡
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }

            System.out.println("第 "+ i + "轮冒泡: "+ Arrays.toString(arr));
            // 如果没发生交换,表示已排好, 不需要再进行比较
            if(!swapped){
                break;
            }
        }
    }
    /**-------------------------------------------------------
     * 冒泡排序2: 结合了冒泡1的两种优化思想
     * 内层循环比较到 最后发生交换的位置
     * 最后发生交换的位置为0时 退出, 表示已排好序
     */
    public static void bubbleSort2(int[] arr) {
        int n = arr.length-1;

        while (true){
            int last = 0;

            for (int i = 0; i < n; i++) {
                if (arr[i] > arr[i + 1]) {
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    last = i;  //记录最后交换的位置, 表明后面的都已经排好序
                }
            }

            n = last; //下一次只用比较到最后一次位置即可, 等于0时表示已全部排好序
            if(n == 0){
                break;
            }
        }
    }

    /**-----------------------------------------------------
     * 选择排序: 选择一个数,依次与后面的数进行比较, 每次比较出最小的数, 每轮比较完把最小的数放到最左边
     */
    public static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {      //外层循环: 要比较的开始数字arr[i]
            int minIndex = i; // 代表最小元素的索引
            for (int j = minIndex + 1; j < arr.length; j++) {  //内层循环: arr[i]后的每个数arr[j]
                if (arr[minIndex] > arr[j]) {
                    minIndex = j;  //把最小元素的索引赋给minIndex, 本轮判断完才进行交换,一轮只交换一次
                }
            }

            // 把最小元素 与 选择比较的数 交换
            if(minIndex != i){
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
            System.out.println(Arrays.toString(arr));
        }
    }

    /**------------------------------------------------------
     * 插入排序: 将数组分为两个区域, 已排序区和未排序区, 每一轮从未排序区取出第一个元素, 按顺序插入到已排序区
     * 待插入元素比较时, 遇到比自己小的元素就表示找到了插入位置, 无需进行后续比较
     * 开始时已经记录了待插入的元素, 插入时可以直接移动元素, 而不是交换元素
     */
    public static void insertSort(int[] arr){
        // i表示待插入元素的索引
        for (int i = 1; i < arr.length; i++) {
            int temp = arr[i];  // 记录待插入元素的值
            int j = i-1;        // 已排序区的元素索引

            while (j >=0){
                // 待插入的元素依次跟排序区元素比较, 如果小, 则所有大元素向右挪动给temp元素腾出位置
                // 如果大, 则退出比较, 把待插入元素插入到arr[j+1]的位置
                if(temp < arr[j]){
                    arr[j+1] = arr[j];
                }else {
                    break;
                }

                j--;
            }

            arr[j+1] = temp;
            System.out.println(Arrays.toString(arr));
        }
    }


    /**--------------------------------------
     * 递归快速排序
     */
    public static void quickSort(int arr[], int l, int h){
        if(l >= h){
            return ;
        }

        //int p = partitionSingle(arr, l, h);  //方式1: 单边分区
        int p = partitionDouble(arr, l, h);    //方式2: 双边分区

        quickSort(arr, l, p-1);          //左边分区的范围确定
        quickSort(arr, p+1, h);          //右边分区的范围确定
    }
    /**------------------------------------------------
     * 方式1: 单边分区
     * 返回值代表了基准点匀速所在的正确索引, 用它确定下一轮分区的边界
     */
    public static int partitionSingle(int[] arr, int l, int h){
        int pv = arr[h]; //基准点元素
        int i = l;

        for(int j=l; j<h; j++){
            if(arr[j] < pv){
                swap(arr, i, j);
                i++;
            }
        }

        // 基准点 与
        swap(arr, h, i);
        return i;
    }
    /**--------------------------------------------
     * 方式2: 双边分区
     */
    public static int partitionDouble(int[] arr, int l, int h){
        int pv = arr[l];  //基准点元素, 通常选第一个元素
        int i = l; //l指向区间左端
        int j = h; //h指向区间右端

        while (i < j){
            // j 从右找小的
            while (i < j && arr[j] > pv){
                j--;
            }

            // i 从左找大的
            while (i < j && arr[i] <= pv){
                i++;
            }

            swap(arr, i, j);
        }

        swap(arr, l, j);
        return j;
    }
    /**----------------------------------
     * 交换
     */
    public static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
