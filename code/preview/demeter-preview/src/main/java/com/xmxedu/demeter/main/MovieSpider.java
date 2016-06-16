package com.xmxedu.demeter.main;

import com.google.common.base.Strings;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by xmzheng on 16/5/6.
 */
public class MovieSpider {

    private static String title = "老鸟家园 -飞鸟娱乐 高清电影下载";

    private static String url =
        "http://bbs.hd62.com/viewthread.php?tid=810071&extra=page%3D1%26amp%3Bfilter%3Dtype%26amp%3Btypeid%3D98";

    private static String xpath = "//a[contains(@href, 'attachment.php')]";

    private static WebDriver driver;

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "demeter-preview/chromedriver");
        ChromeOptions options = new ChromeOptions();
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        driver = new ChromeDriver(capabilities);
        String downloadUrl = getDownloadUrl();
        if (Strings.isNullOrEmpty(downloadUrl)){
            return;
        }

        String confirmOne = downloadUrl + "&downconfirm=1";
        driver.get(confirmOne);

        String confirmTwo = downloadUrl + "&downconfirm=2";

        try {
            downloadFile(confirmTwo,"/tmp/my.torrent");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null != driver){
            driver.quit();
        }
    }

    /**
     * 根据打开的url来获取对应的下载链接
     * @return
     */
    private static String getDownloadUrl(){
        if (null == driver){
            return null;
        }

        try {
            driver.get(url);
            WebElement content = driver.findElement(By.xpath(xpath));
            if (null != content) {
                String url = content.getAttribute("href");
                return url;
            }

            return null;
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * 获取当前driver下的所有cookie
     * @return
     */
    private static CookieStore seleniumCookiesToCookieStore(){
        if (null == driver){
            return null;
        }

        Set<Cookie> cookies = driver.manage().getCookies();
        CookieStore cookieStore = new BasicCookieStore();
        for (Cookie seleniumCookie : cookies){
            BasicClientCookie basicClientCookie = new BasicClientCookie(seleniumCookie.getName(),seleniumCookie.getValue());
            basicClientCookie.setDomain(seleniumCookie.getDomain());
            basicClientCookie.setExpiryDate(seleniumCookie.getExpiry());
            basicClientCookie.setPath(seleniumCookie.getPath());
            cookieStore.addCookie(basicClientCookie);
        }

        return cookieStore;
    }


    public static void downloadFile(String downloadUrl, String outputFilePath) throws Exception {

        CookieStore cookieStore = seleniumCookiesToCookieStore();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.setCookieStore(cookieStore);

        HttpGet httpGet = new HttpGet(downloadUrl);
        System.out.println("Downloding file form: " + downloadUrl);
        HttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            Header header = entity.getContentType();
            System.out.println(header);
            File outputFile = new File(outputFilePath);
            InputStream inputStream = entity.getContent();
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, read);
            }
            fileOutputStream.close();
            System.out.println("Downloded " + outputFile.length() + " bytes. " + entity.getContentType());
        }
        else {
            System.out.println("Download failed!");
        }
    }
}
