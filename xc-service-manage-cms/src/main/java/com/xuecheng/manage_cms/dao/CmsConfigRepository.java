package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * cms配置信息数据访问层
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
