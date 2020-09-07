package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 页面发布消费者，根据pageid去gridfs下载页面到服务器上
 */
@Component
public class ConsumerPostPage {
    //什么日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    PageService pageService;
    /**
     * 监听生产者消息，下载页面
     * @param msg
     */
    @RabbitListener(queues="${xuecheng.mq.queue}")
    public void postPage(String msg){
        Map map = JSON.parseObject(msg, Map.class);
        LOGGER.info("receive cms post page:{}",msg.toString());
        //取出页面id
        String pageId = map.get("pageId").toString();
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()){
            LOGGER.error("receive cms post page,cmsPage is null:{}",msg.toString());
            return;
        }
        //将页面保存服务器
        pageService.savePageToServerPath(pageId);
    }

}
