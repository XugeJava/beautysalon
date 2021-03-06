package com.xuge.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xuge.common.HttpClientUtil;
import com.xuge.common.ResponseData;
import com.xuge.common.StringUtil;
import com.xuge.entity.User;
import com.xuge.dao.UserDao;
import com.xuge.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2022-05-14 11:38:25
 */
@Service("userService")
public class UserServiceImpl implements UserService {
  @Resource
  private UserDao userDao;

  /**
   * 通过ID查询单条数据
   *
   * @param id 主键
   * @return 实例对象
   */
  @Override
  public User queryById(Long id) {
    return this.userDao.queryById(id);
  }

  /**
   * 分页查询
   *
   * @param user        筛选条件
   * @param pageRequest 分页对象
   * @return 查询结果
   */
  @Override
  public Page<User> queryByPage(User user, PageRequest pageRequest) {
    long total = this.userDao.count(user);
    return new PageImpl<>(this.userDao.queryAllByLimit(user, pageRequest), pageRequest, total);
  }

  /**
   * 新增数据
   *
   * @param user 实例对象
   * @return 实例对象
   */
  @Override
  public User insert(User user) {
    this.userDao.insert(user);
    return user;
  }

  /**
   * 修改数据
   *
   * @param user 实例对象
   * @return 实例对象
   */
  @Override
  public User update(User user) {
    this.userDao.update(user);
    return this.queryById(user.getId());
  }

  /**
   * 通过主键删除数据
   *
   * @param id 主键
   * @return 是否成功
   */
  @Override
  public boolean deleteById(Long id) {
    return this.userDao.deleteById(id) > 0;
  }

  @Override
  public ResponseData userRegister(User user) {
    //1.获取数据
    String phone = user.getPhone();
    String username = user.getUsername();
    String password = user.getPassword();
    //2.数据校验
    if (StringUtil.isNull(phone)) {
      return new ResponseData("9001", "手机号为空");
    }
    if (StringUtil.isNull(password)) {
      return new ResponseData("9002", "密码为空");
    }
    if (StringUtil.isNull(username)) {
      return new ResponseData("9003", "参数为空");
    }
    try {
      //2.查询数据库，看数据是否存在
      User queryUser = userDao.queryUserByUserName(username);
      if (queryUser != null) {
        return new ResponseData("9004", "用户名已经存在");
      }

      //3.不为空，准备数据，执行插入
      //123456  qianfeng    1qi2an34f56eng
      Md5Hash md5Hash = new Md5Hash(password,"xueg",10);
      String newPassword = md5Hash.toString(); //得到加密之后的密码
      //4.保存
      user.setPassword(newPassword);
      //5.调用dao层进行持久化
      userDao.insert(user);
      return new ResponseData("0","注册成功");

    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseData("9999","注册失败");
    }



  }

  /**
   * 用户登录方法
   * @param phone
   * @param password
   * @param code
   * @return
   */
  @Override
  public ResponseData userLogin(String phone, String password, String code) {
    //1.非空校验
    if(StringUtil.isNull(phone)){
      return new ResponseData("9001","手机号为空");
    }
    if(StringUtil.isNull(password)){
      return new ResponseData("9002","密码为空");
    }
    //2.密码加密
    Md5Hash md5Hash = new Md5Hash(password,"xueg",10);
    String newPassword = md5Hash.toString();

    //3.根据手机号密码查询用户信息  user
    User user = userDao.queryUserByPhoneAndPwd(phone, newPassword);
    if(user==null){ //说明错误
      return new ResponseData("9005","账号密码不匹配");
    }

    try{
      //4.调用微信的接口   请求方式  get  模拟一个get请求
      //https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
      String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx0b515c9b16e4d170&secret=edb7447c3c85844e1b4b063e8dc515d7&js_code="+code+"&grant_type=authorization_code";
      String result = HttpClientUtil.doGet(url);//返回一个字符串包含session_key  和 openid
      System.out.println("result = "+result);

      //5.获取session_key  和 openid   字符串 转 json
      JSONObject jsonResult = (JSONObject)JSONObject.parse(result);
      String session_key = (String)jsonResult.get("session_key");
      String openid = (String)jsonResult.get("openid");
      System.out.println("session_key = "+session_key);
      System.out.println("openid = "+openid);

      //6.生成自定义登录状态
      Md5Hash md5Hash1 = new Md5Hash(session_key,openid,10);
      String token = md5Hash1.toString();

      //7.保存(跟新)   user token、session_key  openid   根据 id去跟新
      user.setOpenid(openid);
      user.setSessionkey(session_key);
      user.setToken(token);
      userDao.update(user);
      return new ResponseData("0","登录成功",token);



    }catch(Exception e){
      e.printStackTrace();
      return new ResponseData("9999","登录失败");

    }



  }
}
