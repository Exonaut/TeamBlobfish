package com.entity.menu;

public class ResponseMenuDrinks {

  public Content[] content;

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < this.content.length; i++) {

      if (i < this.content.length - 1)
        sb.append(this.content[i].dish.getName() + ", ");
      else
        sb.append(this.content[i].dish.getName());
    }

    return sb.toString().replaceFirst(",", " und");
  }

}
