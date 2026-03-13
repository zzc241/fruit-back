package com.jhc.fruit.common;

import com.jhc.fruit.entity.FruitPrice;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PriceUtil {

    // ===================== 核心反爬配置（重点优化）=====================
    private static final String CNHNB_URL = "https://www.cnhnb.com/hangqing/ganju/";
    // 最大重试次数（降低，避免加重服务器负担）
    private static final int MAX_RETRY = 2;
    // 基础重试间隔（延长）
    private static final long BASE_RETRY_INTERVAL = 3000;
    // 请求间隔（大幅延长，核心优化点）
    private static final long MIN_REQUEST_INTERVAL = 5000;  // 最小5秒
    private static final long MAX_REQUEST_INTERVAL = 8000;  // 最大8秒
    // 长休眠间隔（延长）
    private static final long LONG_SLEEP_BASE = 15000;      // 基础15秒
    private static final long LONG_SLEEP_RANDOM = 5000;     // 随机+5秒
    // 每爬取N个品种长休眠（减少频率）
    private static final int LONG_SLEEP_THRESHOLD = 3;      // 每3个品种就休眠
    // 请求超时（延长）
    private static final int REQUEST_TIMEOUT = 30000;

    // 浏览器指纹池（更丰富）
    private static final List<String> USER_AGENTS = Arrays.asList(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Edge/119.0.0.0",
            "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:121.0) Gecko/20100101 Firefox/121.0"
    );

    // 代理配置（关键：如需使用代理，替换为你的代理IP和端口）
    private static final boolean USE_PROXY = false;          // 是否启用代理
    private static final String PROXY_HOST = "127.0.0.1";    // 代理IP
    private static final int PROXY_PORT = 1080;              // 代理端口

    // 会话维持
    private static final Map<String, String> COOKIES = new HashMap<>();
    private static long LAST_REQUEST_TIME = 0;

    /**
     * 获取指定水果的实时收购价格（防拦截增强版）
     */
    public static List<FruitPrice> getFruitPrice(String fruitName) {
        List<FruitPrice> priceList = new ArrayList<>();

        if ("柑桔".equals(fruitName)) {
            log.info("开始爬取柑桔价格数据（防拦截模式）");
            priceList = getCitrusPriceFromCNHNB2();
        } else {
            log.info("非柑桔品种，返回空数据");
        }

        return priceList;
    }

    /**
     * 从惠农网爬取柑桔价格（核心防拦截逻辑）
     */
    public static List<FruitPrice> getCitrusPriceFromCNHNB2() {
        List<FruitPrice> allPriceList = new ArrayList<>();
        Date currentDate = new Date();

        try {
            // 1. 解析品种列表（带防拦截）
            List<Variety> varietyList = parseCitrusVarietiesWithAntiCrawl();
            if (varietyList.isEmpty()) {
                log.warn("未解析到任何柑桔品种，爬取终止");
                return allPriceList;
            }
            log.info("成功解析到 {} 个柑桔品种", varietyList.size());

            // 2. 逐个爬取（大幅降低频率）
            for (int i = 0; i < varietyList.size(); i++) {
                Variety variety = varietyList.get(i);

                // 频率控制：强制随机长间隔
                controlRequestFrequency(true);

                // 每3个品种强制长休眠（核心优化）
                if (i > 0 && i % LONG_SLEEP_THRESHOLD == 0) {
                    long longSleep = LONG_SLEEP_BASE + (long) (Math.random() * LONG_SLEEP_RANDOM);
                    log.info("已爬取{}个品种，强制长休眠{}毫秒", i, longSleep);
                    TimeUnit.MILLISECONDS.sleep(longSleep);
                }

                List<FruitPrice> singleVarietyPrices = new ArrayList<>();
                boolean crawlSuccess = false;

                // 重试机制（减少重试次数）
                for (int retry = 0; retry < MAX_RETRY; retry++) {
                    try {
                        singleVarietyPrices = crawlSingleVarietyPrice(variety, currentDate);
                        crawlSuccess = true;
                        break;
                    } catch (Exception e) {
                        long retryInterval = BASE_RETRY_INTERVAL * (retry + 1) + (long) (Math.random() * 2000);
                        log.error("爬取品种【{}】第{}次失败: {}，{}毫秒后重试",
                                variety.getName(), retry + 1, e.getMessage(), retryInterval);

                        // 重试前额外休眠
                        TimeUnit.MILLISECONDS.sleep(retryInterval);
                    }
                }

                if (!crawlSuccess) {
                    log.error("品种【{}】多次重试后仍爬取失败，跳过", variety.getName());
                    // 失败后强制长休眠，避免连续失败
                    TimeUnit.MILLISECONDS.sleep(10000 + (long) (Math.random() * 5000));
                    continue;
                }

                log.info("品种【{}】爬取成功，共{}条数据", variety.getName(), singleVarietyPrices.size());
                allPriceList.addAll(singleVarietyPrices);
            }

        } catch (Exception e) {
            log.error("爬取惠农网数据总流程失败: " + e.getMessage(), e);
            return allPriceList;
        }

        log.info("所有品种爬取完成，总计{}条柑桔价格数据", allPriceList.size());
        return allPriceList;
    }

    /**
     * 解析柑桔品种列表（防拦截）
     */
    private static List<Variety> parseCitrusVarietiesWithAntiCrawl() {
        List<Variety> varietyList = new ArrayList<>();

        for (int retry = 0; retry < MAX_RETRY; retry++) {
            try {
                Connection connection = buildAntiCrawlConnection(CNHNB_URL);
                Connection.Response response = connection.execute();
                COOKIES.putAll(response.cookies());

                Document doc = response.parse();
                Elements varietyElements = doc.select(".spec-breed-area li.sub-row:contains(品种) .third-cate-item");

                for (Element element : varietyElements) {
                    String varietyName = element.text().trim();
                    String varietyUrl = element.attr("href").trim();

                    if (!varietyName.isEmpty() && !"不限".equals(varietyName) && !varietyUrl.isEmpty()) {
                        if (!varietyUrl.startsWith("http")) {
                            varietyUrl = "https://www.cnhnb.com" + varietyUrl;
                        }
                        varietyList.add(new Variety(varietyName, varietyUrl));
                    }
                }

                log.info("品种列表解析成功，获取{}个品种", varietyList.size());
                break;
            } catch (Exception e) {
                long retryInterval = BASE_RETRY_INTERVAL * (retry + 1);
                log.error("解析柑桔品种列表第{}次失败: {}，{}毫秒后重试",
                        retry + 1, e.getMessage(), retryInterval);
                try {
                    TimeUnit.MILLISECONDS.sleep(retryInterval);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        return varietyList;
    }

    /**
     * 爬取单个品种价格（核心请求逻辑）
     */
    private static List<FruitPrice> crawlSingleVarietyPrice(Variety variety, Date currentDate) throws Exception {
        List<FruitPrice> priceList = new ArrayList<>();

        // 构建防拦截连接
        Connection connection = buildAntiCrawlConnection(variety.getUrl());

        // 执行请求
        Connection.Response response = connection.execute();

        // 检查状态码（详细日志）
        if (response.statusCode() != 200) {
            log.error("品种【{}】请求失败，状态码：{}", variety.getName(), response.statusCode());
            throw new RuntimeException("HTTP请求失败，状态码：" + response.statusCode());
        }

        // 更新Cookie
        COOKIES.putAll(response.cookies());

        Document doc = response.parse();
        Elements priceItems = doc.select(".quotation-content-list .market-list-item");

        if (priceItems.isEmpty()) {
            log.info("品种【{}】未找到价格数据", variety.getName());
            return priceList;
        }

        for (Element item : priceItems) {
            Elements cells = item.select("span");
            if (cells.size() >= 6) {
                FruitPrice price = new FruitPrice();
                price.setFruitName(variety.getName());

                // 价格日期
                String dateStr = cells.get(0).text().trim();
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    price.setPriceDate(sdf.parse(dateStr));
                } catch (Exception e) {
                    price.setPriceDate(currentDate);
                    log.warn("品种【{}】日期解析失败，使用当前日期: {}", variety.getName(), dateStr);
                }

                // 产地
                price.setOrigin(cells.get(2).text().trim());

                // 价格处理
                String priceStr = cells.get(3).text().trim().replace("元/斤", "");
                if (!priceStr.isEmpty()) {
                    try {
                        price.setPrice(new BigDecimal(priceStr));
                    } catch (NumberFormatException e) {
                        log.warn("品种【{}】价格格式错误，跳过该条数据: {}", variety.getName(), priceStr);
                        continue;
                    }
                }

                // 单位/市场/时间
                price.setUnit("斤");
                price.setMarket("惠农网");
                price.setCreateTime(LocalDateTime.now());
                price.setUpdateTime(LocalDateTime.now());

                priceList.add(price);
            }
        }

        return priceList;
    }

    /**
     * 构建防拦截连接（支持代理）
     */
    private static Connection buildAntiCrawlConnection(String url) {
        // 随机User-Agent
        String randomUserAgent = USER_AGENTS.get(new Random().nextInt(USER_AGENTS.size()));

        // 构建连接
        Connection connection = Jsoup.connect(url)
                .method(Connection.Method.GET)
                .userAgent(randomUserAgent)
                .timeout(REQUEST_TIMEOUT)
                .followRedirects(true)
                .maxBodySize(0)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)  // 关键：忽略HTTP错误，获取完整响应
                // 模拟真实浏览器请求头
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Referer", "https://www.cnhnb.com/")  // 固定站内Referer，降低风险
                .header("Connection", "keep-alive")
                .header("Upgrade-Insecure-Requests", "1")
                .header("Sec-Fetch-Dest", "document")
                .header("Sec-Fetch-Mode", "navigate")
                .header("Sec-Fetch-Site", "same-origin")
                .cookies(COOKIES);

        // 启用代理（关键：解决IP封禁问题）
        if (USE_PROXY) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
            connection.proxy(proxy);
            log.info("使用代理IP：{}:{}", PROXY_HOST, PROXY_PORT);
        }

        return connection;
    }

    /**
     * 严格控制请求频率
     */
    private static void controlRequestFrequency(boolean forceLongWait) throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastRequest = currentTime - LAST_REQUEST_TIME;

        // 计算等待时间（强制长间隔）
        long baseWait = forceLongWait ? MAX_REQUEST_INTERVAL : MIN_REQUEST_INTERVAL;
        long waitTime = baseWait + (long) (Math.random() * (MAX_REQUEST_INTERVAL - MIN_REQUEST_INTERVAL));

        if (timeSinceLastRequest < waitTime) {
            long actualWait = waitTime - timeSinceLastRequest;
            TimeUnit.MILLISECONDS.sleep(actualWait);
        }

        LAST_REQUEST_TIME = System.currentTimeMillis();
    }

    /**
     * 内部辅助类：存储品种名称和URL
     */
    private static class Variety {
        private String name;
        private String url;

        public Variety(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }
}