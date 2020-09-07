package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    TeachplanRepository teachplanRepository;
    //查询课程计划
    public TeachplanNode findTeachplanList(String courseId){
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        return teachplanNode;
    }
    /**
     * 添加课程计划
     */
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan){
        if (teachplan ==null || StringUtils.isEmpty(teachplan.getCourseid())
                ||StringUtils.isEmpty(teachplan.getPname())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //判断父节点是否为空
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)){
            //如果父节点为空，则获取根节点
            parentid=getTeachplanRoot(courseid);
        }
        Optional<Teachplan> optionalParent = teachplanRepository.findById(parentid);
        if (!optionalParent.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Teachplan teachplanParent = optionalParent.get();
        String grade = teachplanParent.getGrade();
        teachplan.setParentid(parentid);
        teachplan.setStatus("0");
        if (grade.equals("1")){
            teachplan.setGrade("2");
        }else if (grade.equals("2")) {
            teachplan.setGrade("3");
        }
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 获取课程的根节点，没有则添加
     * @param courseid
     * @return
     */
    private String getTeachplanRoot(String courseid) {
        Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(courseid);
        if (!optionalCourseBase.isPresent()){
            return null;
        }
        CourseBase courseBase = optionalCourseBase.get();
        //取出父节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        if (teachplanList ==null ||teachplanList.size()==0){//如果没有父节点，则该课程为一级节点
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseid);
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);//保存
            return teachplan.getId();

        }
        return teachplanList.get(0).getId();
    }

}
