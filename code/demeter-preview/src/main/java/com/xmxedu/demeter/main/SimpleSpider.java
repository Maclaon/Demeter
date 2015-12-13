package com.xmxedu.demeter.main;

import org.openqa.selenium.By;
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
        driver.get(
            "http://www.neitui.me/index.php?name=neitui&handle=lists&fr=search&keyword=&kcity=%E6%9D%AD%E5%B7%9E");

        System.out.println("Page title is: " + driver.getTitle());

        WebElement jobDetials = driver.findElement(By.xpath("//*[@id=\"joblist\"]/div[4]/ul/li[8]"));

        System.out.println(jobDetials.getText());

        List<WebElement> jd = driver.findElements(
            By.cssSelector("#joblist > div.content.commentjobs.brjobs.topjobs > ul > li"));

        for (WebElement we: jd){
            WebElement jobDetail = we.findElement(By.xpath("//*[@id=\"joblist\"]/div[4]/ul/li[1]/div[2]/div[2]/div[1]/a"));
        }

        //Close the browser
        driver.quit();
    }
}
