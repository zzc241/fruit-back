package com.jhc.fruit.common;

import com.jhc.fruit.entity.FruitPrice;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
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

        // 如果查询的是柑桔，使用中国农产品网爬取数据
        if ("柑桔".equals(fruitName)) {
            log.info("进行爬取");
            // return getCitrusPriceFromCNHNB2();
        }
        log.info("使用模拟数据");
        // 其他水果使用模拟数据
        // return getMockFruitPrice(fruitName);
        return priceList;
    }

    /**
     * 从中国农产品网爬取柑橘价格
     * @return 柑橘价格信息列表
     */
    /*private static List<FruitPrice> getCitrusPriceFromCNHNB() {
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
                log.info("未找到价格数据表格");
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
                            log.info("价格格式错误: " + priceStr);
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
            log.info("爬取中国农产品网数据失败: " + e.getMessage());
            e.printStackTrace();

            // 爬取失败时返回模拟数据
            return getMockCitrusPrice();
        }

        log.info("爬取成功");
        return priceList;
    }
*/
    /**
     * 从惠农网爬取柑桔价格
     * @return 柑桔价格信息列表
     */
    // private static List<FruitPrice> getCitrusPriceFromCNHNB2() {
    //     List<FruitPrice> priceList = new ArrayList<>();
    //     Date currentDate = new Date();

    //     try {
    //         // 发起GET请求获取页面
    //         Connection.Response response = Jsoup.connect(CNHNB_URL)
    //                 .method(Connection.Method.GET)
    //                 .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
    //                 .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    //                 .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
    //                 .header("Referer", "https://www.cnhnb.com/")
    //                 .timeout(15000)
    //                 .execute();

    //         // 获取HTML文档
    //         Document doc = response.parse();

    //         // 查找价格数据列表（适配惠农网页面结构的选择器）
    //         Elements priceItems = doc.select(".quotation-content-list .market-list-item");
    //         if (priceItems.isEmpty()) {
    //             log.info("未找到价格数据列表");
    //             return priceList;
    //         }

    //         // 遍历每一条价格数据
    //         for (Element item : priceItems) {
    //             Elements cells = item.select("span");
    //             if (cells.size() >= 6) { // 确保有足够的列（时间、产品、产地、价格、升降、走势图）
    //                 FruitPrice price = new FruitPrice();
    //                 price.setFruitName("柑桔");

    //                 // 1. 提取价格日期
    //                 String dateStr = cells.get(0).text().trim();
    //                 try {
    //                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //                     price.setPriceDate(sdf.parse(dateStr));
    //                 } catch (Exception e) {
    //                     price.setPriceDate(currentDate); // 日期解析失败使用当前日期
    //                     log.warn("日期解析失败，使用当前日期: " + dateStr);
    //                 }

    //                 // 3. 提取产地
    //                 String origin = cells.get(2).text().trim();
    //                 price.setOrigin(origin);

    //                 // 4. 提取价格
    //                 String priceStr = cells.get(3).text().trim().replace("元/斤", "");
    //                 if (!priceStr.isEmpty()) {
    //                     try {
    //                         price.setPrice(new BigDecimal(priceStr));
    //                     } catch (NumberFormatException e) {
    //                         log.warn("价格格式错误，跳过该条数据: " + priceStr);
    //                         continue;
    //                     }
    //                 }

    //                 // 5. 提取单位（固定为斤）
    //                 price.setUnit("斤");

    //                 // 6. 提取市场/行情来源
    //                 price.setMarket("惠农网");

    //                 // 设置创建/更新时间
    //                 price.setCreateTime(LocalDateTime.now());
    //                 price.setUpdateTime(LocalDateTime.now());


    //                 priceList.add(price);
    //             }
    //         }

    //     } catch (Exception e) {
    //         log.error("爬取惠农网数据失败: " + e.getMessage(), e);
    //         // 爬取失败时返回模拟数据（可根据需要实现getMockCitrusPrice方法）
    //         return getMockCitrusPrice2();
    //     }

    //     log.info("爬取成功，共获取{}条柑桔价格数据", priceList.size());
    //     return priceList;
    // }

    /**
     * 模拟数据（爬取失败时返回）
     */
    // private static List<FruitPrice> getMockCitrusPrice2() {
    //     List<FruitPrice> mockList = new ArrayList<>();
    //     Date currentDate = new Date();

    //     FruitPrice mock1 = new FruitPrice();
    //     mock1.setFruitName("柑桔");
    //     mock1.setOrigin("湖北宜昌市秭归县");
    //     mock1.setPrice(new BigDecimal("0.47"));
    //     mock1.setUnit("斤");
    //     mock1.setMarket("惠农网");
    //     mock1.setPriceDate(currentDate);
    //     mock1.setCreateTime(LocalDateTime.now());
    //     mock1.setUpdateTime(LocalDateTime.now());

    //     FruitPrice mock2 = new FruitPrice();
    //     mock2.setFruitName("柑桔");
    //     mock2.setOrigin("浙江宁波市象山县");
    //     mock2.setPrice(new BigDecimal("5.10"));
    //     mock2.setUnit("斤");
    //     mock2.setMarket("惠农网");
    //     mock2.setPriceDate(currentDate);
    //     mock2.setCreateTime(LocalDateTime.now());
    //     mock2.setUpdateTime(LocalDateTime.now());

    //     mockList.add(mock1);
    //     mockList.add(mock2);
    //     return mockList;
    // }
}