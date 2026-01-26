package com.jhc.fruit.common;

import com.jhc.fruit.entity.FruitPrice;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PriceUtil {
    // 中国农产品网柑橘价格
    private static final String CNHNB_URL = "https://www.cnhnb.com/hangqing/cd-2001686-0-0-0/";

    /**
     * 获取指定水果的实时收购价格1
     * @param fruitName 水果名称
     * @return 水果价格信息列表
     */
    public static List<FruitPrice> getFruitPrice(String fruitName) {
        List<FruitPrice> priceList = new ArrayList<>();

        // 如果查询的是柑橘，使用中国农产品网爬取数据
        if ("柑桔".equals(fruitName)) {
            return getCitrusPriceFromCNHNB();
        }

        // 其他水果使用模拟数据
        return getMockFruitPrice(fruitName);
    }

    /**
     * 从中国农产品网爬取柑橘价格
     * @return 柑橘价格信息列表
     */
    private static List<FruitPrice> getCitrusPriceFromCNHNB() {
        List<FruitPrice> priceList = new ArrayList<>();
        Date currentDate = new Date();

        try {
            // 发起GET请求获取页面
            Connection.Response response = Jsoup.connect(CNHNB_URL)
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(10000)
                    .execute();

            // 获取HTML文档
            Document doc = response.parse();

            // 查找价格数据表格（根据中国农产品网页面结构调整选择器）
            Elements priceTables = doc.select(".hq-list-table");
            Element priceTable = priceTables.first();
            if (priceTable == null) {
                System.out.println("未找到价格数据表格");
                return priceList;
            }

            // 获取表格行
            Elements rows = priceTable.select("tr");

            // 跳过表头行
            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cells = row.select("td");

                if (cells.size() >= 6) { // 确保有足够的列
                    FruitPrice price = new FruitPrice();
                    price.setFruitName("柑桔");

                    // 提取地区/产地
                    String origin = cells.get(0).text().trim();
                    price.setOrigin(origin);

                    // 提取品种
                    String variety = cells.get(1).text().trim();

                    // 提取规格
                    String specification = cells.get(2).text().trim();

                    // 提取价格
                    String priceStr = cells.get(3).text().trim();
                    if (!priceStr.isEmpty()) {
                        try {
                            // 处理价格范围，取中间值
                            if (priceStr.contains("-")) {
                                String[] priceRange = priceStr.split("-");
                                double minPrice = Double.parseDouble(priceRange[0]);
                                double maxPrice = Double.parseDouble(priceRange[1]);
                                double avgPrice = (minPrice + maxPrice) / 2;
                                price.setPrice(new BigDecimal(String.format("%.2f", avgPrice)));
                            } else {
                                price.setPrice(new BigDecimal(priceStr));
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("价格格式错误: " + priceStr);
                            continue;
                        }
                    }

                    // 提取单位
                    String unit = cells.get(4).text().trim();
                    price.setUnit(unit);

                    // 提取市场
                    String market = cells.get(5).text().trim();
                    price.setMarket(market);

                    // 提取日期
                    String dateStr = cells.get(6).text().trim();
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        price.setPriceDate(sdf.parse(dateStr));
                    } catch (Exception e) {
                        price.setPriceDate(currentDate); // 如果日期解析失败，使用当前日期
                    }

                    price.setCreateTime(currentDate);
                    price.setUpdateTime(currentDate);

                    priceList.add(price);
                }
            }

        } catch (Exception e) {
            System.out.println("爬取中国农产品网数据失败: " + e.getMessage());
            e.printStackTrace();

            // 爬取失败时返回模拟数据
            return getMockCitrusPrice();
        }

        return priceList;
    }

    /**
     * 获取柑橘的模拟价格数据
     * @return 柑橘价格信息列表
     */
    private static List<FruitPrice> getMockCitrusPrice() {
        List<FruitPrice> priceList = new ArrayList<>();
        Date currentDate = new Date();

        // 模拟柑橘价格数据
        String[] markets = {"浙江杭州果品集团有限公司", "浙江嘉兴水果市场", "浙江温州菜篮子农副产品批发交易市场"};
        String[] origins = {"浙江衢州", "浙江台州", "浙江丽水"};
        double[] prices = {6.8, 7.2, 7.5};

        for (int i = 0; i < markets.length; i++) {
            FruitPrice price = new FruitPrice();
            price.setFruitName("柑桔");
            price.setOrigin(origins[i]);
            price.setPrice(new BigDecimal(String.format("%.2f", prices[i])));
            price.setUnit("元/公斤");
            price.setMarket(markets[i]);
            price.setPriceDate(currentDate);
            price.setCreateTime(currentDate);
            price.setUpdateTime(currentDate);
            priceList.add(price);
        }

        return priceList;
    }

    /**
     * 获取其他水果的模拟价格数据
     * @param fruitName 水果名称
     * @return 水果价格信息列表
     */
    private static List<FruitPrice> getMockFruitPrice(String fruitName) {
        List<FruitPrice> priceList = new ArrayList<>();
        Date currentDate = new Date();

        // 模拟数据
        String[] markets = {"北京新发地农产品批发市场", "上海农产品中心批发市场", "广州江南果菜批发市场"};
        String[] origins = {"山东", "海南", "云南"};

        // 根据水果名称生成不同的模拟价格
        double basePrice = 0;
        if ("苹果".equals(fruitName)) {
            basePrice = 8.5;
        } else if ("香蕉".equals(fruitName)) {
            basePrice = 4.2;
        } else if ("橙子".equals(fruitName)) {
            basePrice = 6.8;
        } else if ("草莓".equals(fruitName)) {
            basePrice = 15.6;
        } else if ("葡萄".equals(fruitName)) {
            basePrice = 12.3;
        } else {
            basePrice = 7.0; // 默认价格
        }

        // 为每个市场生成价格数据
        for (int i = 0; i < markets.length; i++) {
            FruitPrice price = new FruitPrice();
            price.setFruitName(fruitName);
            // 每个市场价格略有不同，添加随机波动
            double marketPrice = basePrice + (Math.random() - 0.5) * 2;
            price.setPrice(new BigDecimal(String.format("%.2f", marketPrice)));
            price.setUnit("元/公斤");
            price.setOrigin(origins[i]);
            price.setMarket(markets[i]);
            price.setPriceDate(currentDate);
            price.setCreateTime(currentDate);
            price.setUpdateTime(currentDate);
            priceList.add(price);
        }

        return priceList;
    }
}