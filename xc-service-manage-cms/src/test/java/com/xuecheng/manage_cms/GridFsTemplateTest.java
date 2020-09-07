package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTemplateTest {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;

    /**
     * testGridFs 上传文件
     * @throws Exception
     */
    @Test
    public void testGridFs() throws Exception{
        File file = new File("e:/index_banner.ftl");
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectId id = gridFsTemplate.store(fileInputStream, "轮播图测试模板01","");
        String fileId = id.toString();
        System.out.println(fileId);
    }
    @Test
    public void queryFile() throws IOException {
        //gridFS存储文件返回的id
        String fileId = "5eef1b09b9e7de21787c5fc8";
        //根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        //获取流中的数据
        String s = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
        System.out.println(s);
    }

    //删除文件
    @Test
    public void testDelFile() throws IOException {
        //根据文件id删除fs.files和fs.chunks中的记录
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5eef1ab7b9e7de1d402516b3")));
    }
}
