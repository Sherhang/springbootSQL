//DemoMysqlService.java

package com.example.demoMysql.service;

import com.example.demoMysql.bean.StudentEntity;
import com.example.demoMysql.bean.StudentEntityExample;
import com.example.demoMysql.dao.StudentEntityMapper;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Resource;

import com.google.gson.Gson;

@Service
public class DemoMysqlService {

    //@Resource //在service层不能用这个
    @Autowired(required = false) //方法注入
    private StudentEntityMapper studentEntityMapper;  //这里要和controller里面的名字不一样

    //根据id 查询所有信息 ，不存在返回 nil
    public String selectById(long id) {
        StudentEntityExample studentEntityExample = new StudentEntityExample();
        StudentEntityExample.Criteria criteria = studentEntityExample.createCriteria();
        criteria.andIdEqualTo(id);
        List<StudentEntity> ret = studentEntityMapper.selectByExample(studentEntityExample);
        if (ret.isEmpty())
            return "nil";
        StudentEntity studentEntity = ret.get(0);
        Gson gson = new Gson();
        return gson.toJson(studentEntity);
    }

    //查询表内所有信息, 表为空返回 nil
    public String selectAll(){
        StudentEntityExample studentEntityExample = new StudentEntityExample();
        studentEntityMapper.selectByExample(studentEntityExample);
        List<StudentEntity> ret = studentEntityMapper.selectByExample(studentEntityExample);
        if (ret.isEmpty())
            return "nil";
        Gson gson = new Gson();
        return gson.toJson(ret);
    }

    //根据 id 删除一条数据
    public boolean deleteById(long id) {
        //先查询
        if (selectById(id) == "nil") {
            //DEBUG
            System.out.println("id do not exists!");
            return false;
        }
        int ret = studentEntityMapper.deleteByPrimaryKey(id);// 0 删除失败，1 成功
        System.out.println("delectById ret = " + ret);
        return true;
    }

    //添加一条数据,传入一个json字符串如{"id":4,"name":"David","age":21,"username":"sdhjfh","password":"fjdigfg1","creatime":"2019-09-29"}
    public boolean insertData(String json_str) {
        //解析json字符串，try catch 处理解析失败
        Gson gson = new Gson();
        StudentEntity jsonObject = new StudentEntity();
        try {
            jsonObject = gson.fromJson(json_str, StudentEntity.class);//此时jsonObject就是StudentEntity
        } catch (Exception e) {
            System.out.println("json_str format ERROR");
            return false;
        }
        System.out.println("jsonObject = " + gson.toJson(jsonObject));//json格式错误的话只是会丢掉那部分数据

        //查看主键是否存在
        if (jsonObject.getId() == null) {
            System.out.println("Data Error! id do not exists!");
            return false;
        }
        //查询数据是否已经存在
        if (selectById(jsonObject.getId()) != "nil") {
            System.out.println("id exists!");
            return false;
        }
        int ret = studentEntityMapper.insertSelective(jsonObject); //1成功
        System.out.println("insertData ret = " + ret);
        return true;
    }

    //更新一条数据
    public boolean updateData(String json_str){
        //解析json字符串，try catch 处理解析失败
        Gson gson = new Gson();
        StudentEntity jsonObject = new StudentEntity();
        try {
            jsonObject = gson.fromJson(json_str, StudentEntity.class);//此时jsonObject就是StudentEntity
        } catch (Exception e) {
            System.out.println("json_str format ERROR");
            return false;
        }
        System.out.println("jsonObject = " + gson.toJson(jsonObject));//json格式错误的话只是会丢掉那部分数据

        //查看主键是否存在
        if (jsonObject.getId() == null) {
            System.out.println("Data Error! id do not exists!");
            return false;
        }
        //查询数据是否已经存在, 不存在则返回
        if (selectById(jsonObject.getId()) == "nil") {
            System.out.println("id do not exists!");
            return false;
        }
        int ret = studentEntityMapper.updateByPrimaryKeySelective(jsonObject); //1成功
        System.out.println("updateData ret = " + ret);
        return true;
    }
}
