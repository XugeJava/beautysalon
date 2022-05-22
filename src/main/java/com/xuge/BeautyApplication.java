package com.xuge;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * author: yjx
 * Date :2022/5/1410:44
 **/

@SpringBootApplication
@Slf4j
@MapperScan("com.xuge.dao")
public class BeautyApplication {
  public static void main(String[] args) {
      log.info("===============================================");
      SpringApplication.run(BeautyApplication.class,args);
      log.info("=================项目启动成功========================");
  }
}
