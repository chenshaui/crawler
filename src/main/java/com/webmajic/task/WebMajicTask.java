package com.webmajic.task;

import com.webmajic.pojo.JobInfo;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Component
public class WebMajicTask implements PageProcessor {
    private String url = "https://search.51job.com/list/000000,000000,0000,01%252C32,9,99,Java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
    @Override
    public void process(Page page) {
        //获取招聘信息详情的url
        List<Selectable> list = page.getHtml().css("div#resultList div.el").nodes();
        //判断获取的集合是否为空，空为详情页，非空为列表页
        if (list.size() == 0) {
            this.saveJobInfo(page);
        } else {
           /*list.forEach(t -> {
               //获取url
               String jonInfoUrl = t.links().toString();
               //把获取到的url放到任务队列中
               page.addTargetRequest(jonInfoUrl);
           });*/
            for (Selectable selectable : list) {
                //获取url地址
                String jobInfoUrl = selectable.links().toString();
                //把获取到的url地址放到任务队列中
                page.addTargetRequest(jobInfoUrl);
            }
           //获取下一页的url
            String bkUrl = page.getHtml().css("div.p_in li.bk").nodes().get(1).links().toString();
            //把获取到的url放到任务队列中
            page.addTargetRequest(bkUrl);
        }
        String html = page.getHtml().toString();
    }

    /**
     * 解析页面获取招聘的详情信息
     * @param page
     */
    private void saveJobInfo(Page page) {
        //创建招聘详情对象
        JobInfo jobInfo = new JobInfo();
        //解析页面
        Html html = page.getHtml();
        //获取对象封装到对象中
        jobInfo.setCompanyName(html.css("div.cn p.cname a", "text").toString());
        jobInfo.setCompanyAddr(Jsoup.parse(html.css("div.bmsg").nodes().get(1).toString()).text());
        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg").toString()).text());

        jobInfo.setJobName(html.css("div.cn h1", "text").toString());
        List<String> list = html.css("div.cn p.msg", "text").all();
        /*武汉-洪山区    5-7年经验    大专    招5人    02-03发布*/
        String message = list.get(0);
        List<String> list1 = new MessageUtil().message(message);

        jobInfo.setJobAddr(list1.get(0));
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        jobInfo.setUrl(page.getUrl().toString());
        //获取薪资
        String salary = html.css("div.cn strong", "text").toString();
        Integer[] salarys = MathSalary.getSalary(salary);
        jobInfo.setSalaryMax(salarys[0]);
        jobInfo.setSalaryMin(salarys[1]);
        //获取发布时间
        //均已发布结尾，所以使用正则
        jobInfo.setTime(list1.get(4));

        page.putField("jobInfo", jobInfo);
        //System.out.println(1);


    }

    private Site site = Site.me()
             .setCharset("gbk")//设置编码
             .setRetrySleepTime(3000) //设置重试间隔
             .setRetryTimes(3) //设置重试次数
             .setTimeOut(10 * 1000); //设置超时时间
    @Override
    public Site getSite() {
        return site;
    }

   @Autowired
   private SpringDataPipeline pipeline;
    /* initialDelay任务启动后等多久执行隔多久启动
     *    fixedDelay每隔多久执行一次
     * */
    //@Scheduled(initialDelay = 2000, fixedDelay = 100000)
    public void Process() {
        Spider.create(new WebMajicTask())
              .addUrl(url)
              .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
              .thread(10)
              .addPipeline(this.pipeline)
              .run();

    }
}
