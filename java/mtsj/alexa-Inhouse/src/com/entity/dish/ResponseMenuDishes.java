package com.entity.dish;

public class ResponseMenuDishes {

  public Content[] content;

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < this.content.length; i++) {

      if (i < this.content.length - 1)
        sb.append(this.content[i].dish.name + ", ");
      else
        sb.append(this.content[i].dish.name);
    }

    String str = sb.toString().replaceFirst("", " Menu 1: ");

    String strMenu = "";
    int j = 2;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) == ',') {
        strMenu += ". Menu " + j + ":";
        j++;
      } else
        strMenu += "" + str.charAt(i);
    }

    return strMenu;
  }

}