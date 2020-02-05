package com.webmajic.service;

import com.webmajic.pojo.JobInfo;

import java.util.List;

public interface IJobInfoService {
    /**
     * 保存信息
     * @param jobInfo
     */
    void save(JobInfo jobInfo);

    /**
     * 查询信息
     * @param jobInfo
     * @return
     */
    List<JobInfo> findAll(JobInfo jobInfo);
}
