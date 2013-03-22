package com.example.taximap;

import android.app.Application;

public class UserState extends Application {

  private Integer uid;
  private String type;

  public Integer getuid(){
    return uid;
  }
  public void setuid(Integer x){
    uid = x;
  }
  
  public String gettype(){
	return type;
  }
  public void settype(String x){
	type = x;
  }
}