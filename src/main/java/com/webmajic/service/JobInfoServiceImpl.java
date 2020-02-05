package com.webmajic.service;

import com.webmajic.dao.IJobInfoDao;
import com.webmajic.pojo.JobInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class JobInfoServiceImpl implements IJobInfoService {
    @Autowired
    private IJobInfoDao jobInfoDao;

    @Override
    @Transactional
    public void save(JobInfo jobInfo) {
        //判断数据库中是否有已经存在的数据，存在更新
        //根据url和·发布时间查询，结果为空新增，存在则更新

        JobInfo param = new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());
        List<JobInfo> list = this.findAll(param);
        if (list.size() == 0) {
            this.jobInfoDao.saveAndFlush(jobInfo);//保存并更新
        }
    }

    @Override
    public List<JobInfo> findAll(JobInfo jobInfo) {
        Example<JobInfo> example = Example.of(jobInfo);
        List<JobInfo> list = this.jobInfoDao.findAll(example);
        return list;
    }
}
