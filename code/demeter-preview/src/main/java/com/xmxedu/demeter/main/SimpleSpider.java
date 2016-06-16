package com.xmxedu.demeter.main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

/**
 * 简单地调用selenium来驱动chromedriver来进行任务爬取
 * @version 1.0.0
 */
public class SimpleSpider {
    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "demeter-preview/chromedriver");
        WebDriver driver = new ChromeDriver();

        try {

            driver.get(
                "http://www.neitui.me/index.php?name=neitui&handle=lists&fr=search&keyword=&kcity=%E6%9D%AD%E5%B7%9E");

            Thread.sleep(2000);
            System.out.println("Page title is: " + driver.getTitle());

            String content = driver.getPageSource();
            System.out.println(content);

            Document jsoup = Jsoup.parse(content);
            Element body = jsoup.body();
            Elements elements = body.getAllElements();
            Element first = elements.first();
            System.out.println(first.data());

            List<WebElement> jd = driver.findElements(By.cssSelector("#joblist > div.content.commentjobs.brjobs.topjobs > ul > li"));

            Thread.sleep(2000);
            for (WebElement we : jd) {
                WebElement jobDetail = we.findElement(By.cssSelector("div.jobnote-l > a"));
                String url = jobDetail.getAttribute("href");
                System.out.println(url);
            }

            //Close the browser
            driver.quit();
        }catch (Exception e){
            e.printStackTrace();
            driver.quit();
        }
    }
}
