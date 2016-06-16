package com.xmxedu.demeter.main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by xmzheng on 16/4/30.
 */
public class JianShuSpider {
    public static void main(String[] args){
        String url = "http://www.jianshu.com/p/492903fb7005";

        System.setProperty("webdriver.chrome.driver", "demeter-preview/chromedriver");
        WebDriver driver = new ChromeDriver();

        try {
            driver.get(url);

            Thread.sleep(2000);
            System.out.println("Page title is: " + driver.getTitle());

            WebElement content = driver.findElement(By.className("show-content"));
            if (null != content){
                String con = content.getText();
                System.out.println(con);
            }

            //Close the browser
            driver.quit();
        }catch (Exception e){
            e.printStackTrace();
            driver.quit();
        }
    }
}
