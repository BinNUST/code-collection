package com.zb.algo;

/**
 * 排序算法
 * see https://www.cnblogs.com/onepixel/articles/7674659.html
 */
public class Sort {
    public static void main(String[] args) {
        int[] unSorted = new int[]{1, 23, 67, 6, 45, 99, 78, 12, 89, 34, 56};
        System.out.println("before:");
        printArray(unSorted);

        /*int[] sorted = bubbleSort(unSorted);
        System.out.println("bubble sort after:");
        printArray(sorted);*/

        /*int[] sorted = selectionSort(unSorted);
        System.out.println("selection sort after:");
        printArray(sorted);*/

        int[] sorted = insertionSort(unSorted);
        System.out.println("insertion sort after:");
        printArray(sorted);
    }

    private static void printArray(int[] arr) {
        for (int a : arr) {
            System.out.print(a);
            System.out.print("\t");
        }
        System.out.println();
    }

    /**
     * 冒泡排序 Bubble Sort
     * @param arr
     * @return
     */
    public static int[] bubbleSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (arr[j] > arr[j+1]) { // 相邻元素两两比较
                    int temp = arr[j+1]; // 元素交换
                    arr[j+1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
        return arr;
    }

    /**
     * 选择排序 Selection Sort
     * @param arr
     * @return
     */
    public static int[] selectionSort(int[] arr) {
        int len = arr.length;
        int minIndex, temp;
        for (int i = 0; i < len - 1; i++) {
            minIndex = i;
            for (int j = i + 1; j < len; j++) {
                if (arr[j] < arr[minIndex]) { // 寻找最小的数
                    minIndex = j; // 保存最小数的索引
                }
            }
            temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
        return arr;
    }

    /**
     * 插入排序 Insertion Sort
     * @param arr
     * @return
     */
    public static int[] insertionSort(int[] arr) {
        int len = arr.length;
        int preIndex, current;
        for (int i = 1; i < len; i++) {
            preIndex = i - 1;
            current = arr[i];
            while (preIndex >= 0 && arr[preIndex] > current) {
                arr[preIndex+1] = arr[preIndex];
                preIndex--;
            }
            arr[preIndex+1] = current;
        }
        return arr;
    }
}
