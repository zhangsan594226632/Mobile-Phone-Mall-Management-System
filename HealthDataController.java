package com.ai.doctor.controller;

import cn.hutool.core.date.DateUtil;
import com.ai.doctor.beans.HealthData;
import com.ai.doctor.service.HealthDataService;
import com.ai.doctor.utils.Results;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("healthdata")
@Slf4j
@Tag(name = "健康数据接口", description = "健康数据接口") // 类级注解
public class HealthDataController {

    @Autowired
    HealthDataService healthDataService;

    @Operation(summary = "健康数据接口", description = "健康数据接口") // 方法级注解
    @GetMapping("list")
    public Results list(){
        return Results.success(healthDataService.list());
    }

    /**
     * 分页查询
     * @return
     */
    @Operation(summary = "健康数据接口分页查询", description = "健康数据接口分页查询") // 方法级注解
    @GetMapping("selectPage")
    public Results selectPage(@RequestParam("pageNum") Integer pageNum,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("name") String name,
                             @RequestParam(value = "createtime") String createtime) {
        //使用分页插件
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<HealthData> healthDataQueryWrapper = new QueryWrapper<HealthData>().eq("name", name).orderByDesc("id");
        healthDataQueryWrapper.like(true,"createtime",createtime);
        //查询数据库，需要用到like查询
        List<HealthData> list = healthDataService.list(healthDataQueryWrapper);
        //封装一下查询的结果集
        PageInfo<HealthData> pageInfo = PageInfo.of(list);
        return Results.success(pageInfo);
    }

    /**
     * 新增接口
     * @return
     */
    @Operation(summary = "新增指标信息", description = "新增指标信息") // 方法级注解
    @PostMapping("add")
    public Results add(@RequestBody HealthData healthData){
        //获取系统时间
        healthData.setCreatetime(DateUtil.now());
        boolean b = healthDataService.save(healthData);
        if(b){
            return Results.success();
        }else{
            return Results.error();
        }
    }

    /**
     *  更新
     */
    @Operation(summary = "更新指标信息", description = "更新指标信息") // 方法级注解
    @PutMapping("update")
    public Results update(@RequestBody HealthData healthData){
        boolean b = healthDataService.updateById(healthData);
        if(b){
            return Results.success();
        }else{
            return Results.error();
        }
    }


    /**
     * 删除
     */
    @Operation(summary = "根据ID删除指标数据", description = "根据ID删除指标数据") // 方法级注解
    @DeleteMapping("/delete/{id}")
    public Results delete(@PathVariable("id")  Integer id){
        boolean b = healthDataService.removeById(id);
        if(b){
            return Results.success();
        }else{
            return Results.error();
        }
    }
}
