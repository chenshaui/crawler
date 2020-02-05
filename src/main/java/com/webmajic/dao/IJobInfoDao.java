package com.webmajic.dao;

import com.webmajic.pojo.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJobInfoDao extends JpaRepository<JobInfo, Long> {

}
