package com.webmajic.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
@Component
public class ProxyTest implements PageProcessor {
    @Override
    public void process(Page page) {
        System.out.println(page.getHtml().all());
    }
    private Site site = Site.me().setRetrySleepTime(3000) //设置重试间隔
            .setRetryTimes(3) //设置重试次数
            .setTimeOut(10 * 1000); //设置超时时间;

    @Override
    public Site getSite() {
        return site;
    }

    @Scheduled(fixedDelay = 1000)
    public void Process() {
        //创建下载器downloader
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        //给下载器设置代理服务器信息
       httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("101.110.119.70", 80)));
        Spider.create(new ProxyTest())
                .addUrl("http://ip.chinaz.com")
            .setDownloader(httpClientDownloader)//设置下载器，下载器使用了代理
                .run();
    }

}
