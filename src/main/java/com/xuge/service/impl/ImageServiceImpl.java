package com.xuge.service.impl;


import com.xuge.common.ResponseData;
import com.xuge.entity.Image;
import com.xuge.dao.ImageDao;
import com.xuge.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Image)表服务实现类
 *
 * @author makejava
 * @since 2022-05-14 11:38:24
 */
@Service("imageService")
public class ImageServiceImpl implements ImageService {
    @Resource
    private ImageDao imageDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Image queryById(Long id) {
        return this.imageDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param image 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Image> queryByPage(Image image, PageRequest pageRequest) {
        long total = this.imageDao.count(image);
        return new PageImpl<>(this.imageDao.queryAllByLimit(image, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param image 实例对象
     * @return 实例对象
     */
    @Override
    public Image insert(Image image) {
        this.imageDao.insert(image);
        return image;
    }

    /**
     * 修改数据
     *
     * @param image 实例对象
     * @return 实例对象
     */
    @Override
    public Image update(Image image) {
        this.imageDao.update(image);
        return this.queryById(image.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.imageDao.deleteById(id) > 0;
    }

    @Override
    public ResponseData queryImageByType(String imagetype) {
        //为了防止有其他异常情况，需要定义在try中
        try{
            //调用dao层
            List<Image> images = imageDao.queryByImageType(imagetype);

            return new ResponseData("0","请求成功",images);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseData("9999","网络异常");
        }
    }
}
