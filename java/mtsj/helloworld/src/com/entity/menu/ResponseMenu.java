package com.entity.menu;

public class ResponseMenu {

  public Content[] content;

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    for (Content c : this.content) {
      sb.append(c.dish.name + ", ");
    }
    return sb.toString();
  }

}