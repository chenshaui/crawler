package com.webmajic.task;

import com.webmajic.pojo.JobInfo;
import com.webmajic.service.JobInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDataPipeline implements Pipeline {
    @Autowired
    private JobInfoServiceImpl jobInfoService;
    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取封装好的招聘对象详情
        JobInfo jobInfo = resultItems.get("jobInfo");
        if (jobInfo != null) {
            this.jobInfoService.save(jobInfo);
        }
    }
}
