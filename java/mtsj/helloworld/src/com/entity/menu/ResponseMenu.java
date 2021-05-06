package com.entity.menu;

public class ResponseMenu {
  public Content[] content;

  public Pageable pageable;

  public String totalElements;
  
  @Override
  public String toString() {

      StringBuilder sb = new StringBuilder();
      for (Content c : content) {
          sb.append(c.dish.name + ", ");
      }
      return sb.toString();
  }
  
}