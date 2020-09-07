package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    PageService pageService;
    @Test
    public void testFindPage(){
        int page=0;
        int size=10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);

    }
    @Test
    public void testhtml(){
        String pageHtml = pageService.getPageHtml("5ee0e61eb9e7de7b7ce60ede");
        System.out.println(pageHtml);

    }
}
