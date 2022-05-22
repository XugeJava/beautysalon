package com.xuge.common;

import com.xuge.entity.Image;
import com.xuge.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * author: yjx
 * Date :2022/5/1414:39
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseData {
  private String code;//状态码
  private String msg;//返回消息
  private Object data;//返回集合对象
  private Long count;//总记录数

  public ResponseData(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public ResponseData(String code, String msg, Object data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public ResponseData(String code, String msg, List<Project> list, Long count) {
    this.code=code;
    this.msg=msg;
    this.data=list;
    this.count=count;
  }
}