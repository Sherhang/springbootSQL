//TestController.java
package com.example.demoMysql.controller;

import com.example.demoMysql.bean.StudentEntity;
import com.example.demoMysql.bean.StudentEntityExample;
import com.example.demoMysql.dao.StudentEntityMapper;
import com.example.demoMysql.service.DemoMysqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod; // RequestMethod.GET
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable; // @PathVariable

import java.util.List;
import java.util.Map;
import java.lang.String;

import com.google.gson.Gson;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Autowired() //用于自动注解
    private JdbcTemplate jdbcTemplate; //这里的类你自己是没有初始化的，但是可以使用，是因为上一句帮你做了初始化和管理的工作。

    @Resource  //一定要紧挨着下面的，告诉mapper地址在哪，controller里面需要这句
    @Autowired() //spring 自动管理类的初始化
    private StudentEntityMapper student_EntityMapper; //注入方法，这个变量是全局的，注意，其它地方再次注入用不一样的名字

    @RequestMapping("/getUsers")    //页面地址，当用户访问页面时就会触发getDbType方法。
    public String getDbType() {
        String sql = "select * from student";//执行SQL查询
        //mysql一行数据对应一个map，所以用List<Map<>>
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);//解析数据，到这里已经完成网页功能。
        //对于每一行数据，打印到本地。
        for (Map<String, Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                //Object key = entry.getKey();
                String key = entry.getKey();
                Object val = entry.getValue();
                System.out.println(key + "-->" + val);
            }
        }
        Gson gson = new Gson();
        return gson.toJson(list);//转化为json格式的字符串
        //return list;
    }

    //用 mybatis 来操作SQL
    //查询指定条件数据
    @RequestMapping("/select")
    public String selectData() {
        StudentEntityExample studentEntityExample = new StudentEntityExample();
        StudentEntityExample.Criteria criteria = studentEntityExample.createCriteria();
        criteria.andAgeBetween(18, 20);
        List<StudentEntity> ret = student_EntityMapper.selectByExample(studentEntityExample);
        Gson gson = new Gson();
        return gson.toJson(ret);
    }

    // 实际上方法都应该用 service 提供
    @Autowired
    private DemoMysqlService demoMysqlService; //注入服务方法；

    //根据 id 查询信息
    @RequestMapping(path = "/selectId/{id}", method = RequestMethod.GET)
    public String selectById(@PathVariable long id) {
        String ret = demoMysqlService.selectById(id);
        System.out.println("ret = " + ret);//本地打印
        return ret;
    }
    //查询表内所有数据
    @RequestMapping("selectAll")
    public String selectAll(){
        return demoMysqlService.selectAll();
    }

    //根据 id 删除数据
    @RequestMapping(path = "/deleteId/{id}", method = RequestMethod.GET)
    public boolean deleteById(@PathVariable long id) {
        return demoMysqlService.deleteById(id);
    }

    //添加一条数据
    @RequestMapping(path = "insertData/{json_str}", method = RequestMethod.POST) //POST只能用postman,不能用浏览器
    public boolean insertData(@PathVariable String json_str){
        return demoMysqlService.insertData(json_str);
    }

    //更新一条数据
    @RequestMapping(path = "updateData/{json_str}", method = RequestMethod.POST)
    public  boolean updateData(@PathVariable String json_str){
        return demoMysqlService.updateData(json_str);
    }

    //分页查询

}

