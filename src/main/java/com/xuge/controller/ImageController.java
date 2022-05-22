package com.xuge.controller;

import com.xuge.common.ResponseData;
import com.xuge.entity.Image;
import com.xuge.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Image)表控制层
 *
 * @author makejava
 * @since 2022-05-14 11:38:52
 */
@RestController
@RequestMapping("/image")
@Api(tags={"图片管理"})
@Slf4j
public class ImageController {
    @Cacheable(value = "imageCache", key = "#imagetype" )
    @ApiOperation(value = "获取图片信息",notes = "根据图片类型获取图片信息，轮播图：banner  导航菜单：nav")
    @GetMapping("queryImageByType")
    public ResponseData queryImageByType(String imagetype){
        log.info("imagetype={}",imagetype);
        //调用业务层
        return imageService.queryImageByType(imagetype);
    }
    /**
     * 服务对象
     */
    @Resource
    private ImageService imageService;

    /**
     * 分页查询
     *
     * @param image 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<Image>> queryByPage(Image image, PageRequest pageRequest) {
        return ResponseEntity.ok(this.imageService.queryByPage(image, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<Image> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.imageService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param image 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<Image> add(Image image) {
        return ResponseEntity.ok(this.imageService.insert(image));
    }

    /**
     * 编辑数据
     *
     * @param image 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<Image> edit(Image image) {
        return ResponseEntity.ok(this.imageService.update(image));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Long id) {
        return ResponseEntity.ok(this.imageService.deleteById(id));
    }

}

