package com.xuge.service.impl;

import com.xuge.common.ResponseData;
import com.xuge.entity.Business;
import com.xuge.dao.BusinessDao;
import com.xuge.service.BusinessService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (Business)表服务实现类
 *
 * @author makejava
 * @since 2022-05-14 11:38:23
 */
@Service("businessService")
public class BusinessServiceImpl implements BusinessService {
    @Resource
    private BusinessDao businessDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Business queryById(Long id) {
        return this.businessDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param business 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Business> queryByPage(Business business, PageRequest pageRequest) {
        long total = this.businessDao.count(business);
        return new PageImpl<>(this.businessDao.queryAllByLimit(business, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param business 实例对象
     * @return 实例对象
     */
    @Override
    public Business insert(Business business) {
        this.businessDao.insert(business);
        return business;
    }

    /**
     * 修改数据
     *
     * @param business 实例对象
     * @return 实例对象
     */
    @Override
    public Business update(Business business) {
        this.businessDao.update(business);
        return this.queryById(business.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.businessDao.deleteById(id) > 0;
    }
    //
    @Override
    public ResponseData getBusinessInfoById(Long id) {

        try{
            //调用dao层 根据id查询商家相关的信息返回 Business 对象
            Business busInfoByProId = businessDao.getBusInfoByProId(id);
            return new ResponseData("0","请求成功！",busInfoByProId);
        }catch (Exception e){
            System.out.println(e);
            return new ResponseData("9999","网络异常");
        }
    }
}
