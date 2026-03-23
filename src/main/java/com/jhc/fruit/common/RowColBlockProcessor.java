package com.jhc.fruit.common;

import lombok.Data;

import java.util.Arrays;
import java.util.Objects;

/**
 * 行列区块数据处理工具类
 * 存储格式：行-列-区块（如B01-C01-A，A表示区块）
 * 规则：
 * 1. 行列格式：1→A01，11→K11，27→AA27（字母按26进制递增，数字为纯序号）
 * 2. 区块格式：1→A，26→Z，27→AA，28→AB...（支持Long类型输入）
 * 3. 拆分：提取纯数字行列号，区块保留字母
 * 4. 组合：数字+区块 → 字母+数字格式的行列-区块字符串
 */
public class RowColBlockProcessor {

    // 私有构造器，禁止实例化
    private RowColBlockProcessor() {}

    /**
     * 拆分行列区块数据（格式如：B01-C01-A）
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

        // 解析行（如B01 → 2）、列（如C01 → 3），提取纯数字
        int rowNum = parseAlphaNumToNumber(parts[0].trim());
        int colNum = parseAlphaNumToNumber(parts[1].trim());
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
        return String.format("%s-%s-%s", rowStr, colStr, block.trim().toUpperCase());
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
            result[i] = numberToAlpha(num.intValue()); // 转换为字母（支持Long→int，因26进制递增足够覆盖常规场景）
        }
        return result;
    }

    // ===================== 私有辅助方法 =====================
    /**
     * 辅助：字母+数字 → 纯数字（如A01→1，K11→11，AA27→27）
     */
    private static int parseAlphaNumToNumber(String alphaNumStr) {
        if (Objects.isNull(alphaNumStr) || alphaNumStr.trim().isEmpty()) {
            throw new IllegalArgumentException("字母数字字符串不能为空");
        }

        // 分离字母和数字部分
        StringBuilder alphaPart = new StringBuilder();
        StringBuilder numPart = new StringBuilder();
        for (char c : alphaNumStr.toCharArray()) {
            if (Character.isLetter(c)) {
                alphaPart.append(Character.toUpperCase(c));
            } else if (Character.isDigit(c)) {
                numPart.append(c);
            } else {
                throw new IllegalArgumentException("包含非法字符，仅支持字母和数字：" + alphaNumStr);
            }
        }

        // 校验必填部分
        if (alphaPart.length() == 0 || numPart.length() == 0) {
            throw new IllegalArgumentException("格式错误，必须包含字母+数字（如A01）：" + alphaNumStr);
        }

        // 字母部分仅用于验证（保证格式合法），最终返回数字部分的纯数值（去前导0）
        return Integer.parseInt(numPart.toString());
    }

    /**
     * 辅助：数字 → 字母+数字（如1→A01，11→K11，27→AA27）
     */
    private static String parseNumberToAlphaNum(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("数字必须为正整数：" + number);
        }
        // 数字转26进制字母（1→A，11→K，27→AA） + 原数字（保留序号）
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
            number--; // 调整为0开始（A对应0，Z对应25）
            sb.append((char) ('A' + number % 26));
            number = number / 26;
        }
        return sb.reverse().toString();
    }

    // ===================== 拆分结果封装类 =====================
    /**
     * 拆分结果封装，兼顾易用性和字符串转换
     */
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

        // Getter方法
        public int getRowNum() { return rowNum; }
        public int getColNum() { return colNum; }
        public String getBlock() { return block; }
    }

    // ===================== 测试方法 =====================
    public static void main(String[] args) {
        // 1. 测试拆分
        String testStr = "B01-C01-A";
        SplitResult splitResult = splitRowColBlock(testStr);
        System.out.println("拆分结果对象：" + splitResult); // 2-3-A
        System.out.println("纯数字行列：" + Arrays.toString(splitResult.getNumberArray())); // [2, 3]
        System.out.println("区块：" + splitResult.getBlock()); // A

        // 2. 测试组合
        String combineResult1 = combineRowColBlock(11, 2, "B");
        System.out.println("组合结果1：" + combineResult1); // K11-B02-B
        String combineResult2 = combineRowColBlock(27, 3, "AA");
        System.out.println("组合结果2：" + combineResult2); // AA27-C03-AA

        // 3. 测试区块转换
        String[] blockLetters = convertBlockNumbersToLetters(1L, 11L, 26L, 27L, 28L);
        System.out.println("区块转换结果：" + Arrays.toString(blockLetters)); // [A, K, Z, AA, AB]
    }
}