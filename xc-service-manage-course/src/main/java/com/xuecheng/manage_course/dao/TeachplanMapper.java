package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator.
 */
@Mapper
@Repository
public interface TeachplanMapper {
   TeachplanNode selectList(String courseId);
}
