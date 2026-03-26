package com.jhc.fruit.common;

import lombok.Data;

import java.util.Arrays;
import java.util.Objects;

/**
 * 行列区块数据处理工具类
 * 存储格式：区块-行-列（如B01-C01-A，A表示区块）
 * 规则：
 * 1. 行列格式：1→A01，11→K11，27→AA27（字母按26进制递增，数字为纯序号）
 * 2. 区块格式：1→A，26→Z，27→AA，28→AB...（支持Long类型输入）
 * 3. 拆分：提取纯数字行列号，区块保留字母
 * 4. 组合：数字+区块 → 字母+数字格式的行列-区块字符串
 * 5. 新增：区域ID数字转顺序字母（1→A,2→B,26→Z,27→AA）
 */
public class RowColBlockProcessor {

    // 私有构造器，禁止实例化
    private RowColBlockProcessor() {}

    /**
     * 拆分行列区块数据（格式如：B01-C01-A）
     * 【优化】去除字母校验，仅提取纯数字行列号
     * @param rowColBlockStr 行列区块组合字符串（行-列-区块）
     * @return 拆分结果对象，包含：行号(纯数字)、列号(纯数字)、区块字母
     */
    public static SplitResult splitRowColBlock(String rowColBlockStr) {
        // 空值和格式校验
        if (Objects.isNull(rowColBlockStr) || rowColBlockStr.trim().isEmpty()) {
            throw new IllegalArgumentException("行列区块字符串不能为空");
        }
        String[] parts = rowColBlockStr.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("格式错误，正确格式应为：行-列-区块（如B01-C01-A），当前值：" + rowColBlockStr);
        }

        // 【优化】直接提取纯数字，去除字母逻辑
        int rowNum = extractPureNumber(parts[0].trim());
        int colNum = extractPureNumber(parts[1].trim());
        // 区块直接提取（如A），统一转大写
        String block = parts[2].trim().toUpperCase();

        return new SplitResult(rowNum, colNum, block);
    }

    /**
     * 组合行列区块数据为指定格式
     * @param rowNum 行号（正整数，如1→A01，11→K11）
     * @param colNum 列号（正整数，如1→A01，3→C03）
     * @param block 区块字母（如A、AA）
     * @return 组合后的字符串（如1,3,A → A01-C03-A；11,2,B → K11-B02-B）
     */
    public static String combineRowColBlock(int rowNum, int colNum, String block) {
        // 合法性校验
        if (rowNum < 1 || colNum < 1) {
            throw new IllegalArgumentException("行号和列号必须为正整数，当前行：" + rowNum + "，列：" + colNum);
        }
        if (Objects.isNull(block) || block.trim().isEmpty()) {
            throw new IllegalArgumentException("区块字母不能为空（如A、AA）");
        }

        // 数字转字母+数字格式（1→A01，11→K11）
        String rowStr = parseNumberToAlphaNum(rowNum);
        String colStr = parseNumberToAlphaNum(colNum);
        // 组合为 行-列-区块 格式
        return String.format("%s-%s-%s", rowStr, colNum, block.trim().toUpperCase());
    }

    /**
     * 区块转换方法：Long类型区块编号 → 字母编号（1→A，26→Z，27→AA）
     * @param blockNumbers 一组Long类型区块编号（需>0）
     * @return 对应字母编号数组，顺序与输入一致
     */
    public static String[] convertBlockNumbersToLetters(Long... blockNumbers) {
        if (Objects.isNull(blockNumbers) || blockNumbers.length == 0) {
            throw new IllegalArgumentException("区块编号数组不能为空");
        }

        String[] result = new String[blockNumbers.length];
        for (int i = 0; i < blockNumbers.length; i++) {
            Long num = blockNumbers[i];
            if (Objects.isNull(num) || num < 1) {
                throw new IllegalArgumentException("区块编号必须为正整数，第" + (i+1) + "个值：" + num);
            }
            result[i] = numberToAlpha(num.intValue());
        }
        return result;
    }

    // ===================== 新增方法 =====================
    /**
     * 【新增】区域ID数字转顺序字母编号（核心需求）
     * 输入：1→A, 2→B, ...,26→Z,27→AA,28→AB
     * @param areaId 区域ID（正整数）
     * @return 字母编号
     */
    public static String areaIdToLetter(int areaId) {
        if (areaId < 1) {
            throw new IllegalArgumentException("区域ID必须为正整数，当前值：" + areaId);
        }
        return numberToAlpha(areaId);
    }

    /**
     * 【新增】批量区域ID转字母编号
     * @param areaIds 区域ID数组
     * @return 字母数组（顺序与输入一致）
     */
    public static String[] areaIdsToLetters(int... areaIds) {
        if (Objects.isNull(areaIds) || areaIds.length == 0) {
            throw new IllegalArgumentException("区域ID数组不能为空");
        }
        String[] result = new String[areaIds.length];
        for (int i = 0; i < areaIds.length; i++) {
            result[i] = areaIdToLetter(areaIds[i]);
        }
        return result;
    }

    // ===================== 私有辅助方法 =====================

    /**
     * 【新增辅助】从字符串中提取纯数字（去除所有非数字字符）
     */
    private static int extractPureNumber(String str) {
        if (Objects.isNull(str) || str.trim().isEmpty()) {
            throw new IllegalArgumentException("字符串不能为空");
        }
        // 正则匹配所有数字字符
        String numStr = str.replaceAll("[^0-9]", "");
        if (numStr.isEmpty()) {
            throw new IllegalArgumentException("未提取到有效数字：" + str);
        }
        return Integer.parseInt(numStr);
    }

    /**
     * 辅助：数字 → 字母+数字（如1→A01，11→K11，27→AA27）
     */
    private static String parseNumberToAlphaNum(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("数字必须为正整数：" + number);
        }
        String alphaPart = numberToAlpha(number);
        return alphaPart + number;
    }

    /**
     * 核心：数字 → 26进制字母（1→A，26→Z，27→AA，28→AB...）
     */
    private static String numberToAlpha(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("数字必须大于0：" + number);
        }
        StringBuilder sb = new StringBuilder();
        while (number > 0) {
            number--;
            sb.append((char) ('A' + number % 26));
            number = number / 26;
        }
        return sb.reverse().toString();
    }

    // ===================== 拆分结果封装类 =====================
    @Data
    public static class SplitResult {
        private final int rowNum;    // 行号（纯数字）
        private final int colNum;    // 列号（纯数字）
        private final String block;  // 区块字母（如A、AA）

        public SplitResult(int rowNum, int colNum, String block) {
            this.rowNum = rowNum;
            this.colNum = colNum;
            this.block = block;
        }

        // 获取纯数字数组（行、列），方便数值计算
        public int[] getNumberArray() {
            return new int[]{rowNum, colNum};
        }

        // 转换为简洁字符串（便于存储）
        @Override
        public String toString() {
            return String.format("%d-%d-%s", rowNum, colNum, block);
        }

        public int getRowNum() { return rowNum; }
        public int getColNum() { return colNum; }
        public String getBlock() { return block; }
    }

    // ===================== 测试方法 =====================
    public static void main(String[] args) {
        // 1. 测试拆分（已去除字母逻辑）
        String testStr = "B01-C01-A";
        SplitResult splitResult = splitRowColBlock(testStr);
        System.out.println("拆分结果对象：" + splitResult);
        System.out.println("纯数字行列：" + Arrays.toString(splitResult.getNumberArray()));
        System.out.println("区块：" + splitResult.getBlock());

        // 2. 测试组合
        String combineResult1 = combineRowColBlock(11, 2, "B");
        System.out.println("组合结果1：" + combineResult1);
        String combineResult2 = combineRowColBlock(27, 3, "AA");
        System.out.println("组合结果2：" + combineResult2);

        // 3. 测试区块转换
        String[] blockLetters = convertBlockNumbersToLetters(1L, 11L, 26L, 27L, 28L);
        System.out.println("区块转换结果：" + Arrays.toString(blockLetters));

        // 4. 【新增测试】区域ID转字母
        System.out.println("===== 新增功能测试 =====");
        System.out.println("1→" + areaIdToLetter(1));      // A
        System.out.println("3→" + areaIdToLetter(3));      // C
        System.out.println("26→" + areaIdToLetter(26));    // Z
        System.out.println("27→" + areaIdToLetter(27));   // AA
        System.out.println("28→" + areaIdToLetter(28));   // AB

        // 批量转换
        String[] areaLetters = areaIdsToLetters(1,2,3,26,27);
        System.out.println("批量转换结果：" + Arrays.toString(areaLetters));
    }
}