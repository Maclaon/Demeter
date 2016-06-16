package com.xmxedu.demeter.main;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 老鸟家园的网站的url文本解析
 *
 * @author xmzheng
 * @version 1.0.0
 */
public class HdMovieParser {

    private final static String singleUrl =
        "http://bbs.hd62.com/viewthread.php?tid=810180&extra=page%3D1%26amp%3Bfilter%3Dtype%26amp%3Btypeid%3D97";
    private static WebDriver driver;

    static Pattern p = Pattern.compile("$+");

    public static void main(String args[]) {
        System.setProperty("webdriver.chrome.driver", "demeter-preview/chromedriver");
        ChromeOptions options = new ChromeOptions();
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new ChromeDriver(capabilities);

        try {
            driver.get(singleUrl);

            WebElement titleWebElement = driver.findElement(By.id("threadtitle"));
            if (null != titleWebElement) {

                WebElement title = titleWebElement.findElement(By.tagName("h1"));
                if (null != title) {
                    System.out.println(title.getText());
                }

                WebElement tags = titleWebElement.findElement(By.className("threadtags"));
                if (null != tags) {
                    System.out.println(tags.getText());
                }
            }

            WebElement contentWebElement = driver.findElement(By.className("t_msgfont"));
            String srcContent = contentWebElement.getAttribute("innerHTML");
            if (Strings.isNullOrEmpty(srcContent)) {
                return;
            }

            String content = srcContent.replace("<br>", "\n");
            content = content.replace("\n", "");
            String[] sp = content.split("<font color=\"blue\">◎(.|\n)*?</font>");
            List<String> values = Lists.newArrayList(sp);
            values = values.subList(1,values.size());


            Map<String,String> infos = Maps.newHashMap();
            Document jsoup = Jsoup.parse(srcContent);
            Elements elements = jsoup.getElementsByAttributeValueMatching("color","blue");
            for (int i = 0; i < elements.size(); i++){
                Element element = elements.get(i);
                System.out.print(element.text() + " : ");

                String v = values.get(i);
                int cut = v.indexOf("<div class=\"quote\">");
                if (-1 != cut){
                    v = v.substring(0,cut);
                }
                v = Jsoup.parse(v).text();
                System.out.println(v);
            }

        } catch (Exception e) {

        }

        if (null != driver) {
            driver.close();
            driver.quit();
        }
    }

    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
