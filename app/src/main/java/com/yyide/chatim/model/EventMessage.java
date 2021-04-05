package com.yyide.chatim.model;

/**
 * Created by Administrator on 2019/4/4.
 */

public class EventMessage {

   private String code;
   private String message;

   public EventMessage(String code, String message) {
      this.code = code;
      this.message = message;
   }

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}