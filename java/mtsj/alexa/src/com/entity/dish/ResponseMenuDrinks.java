package com.entity.dish;

public class ResponseMenuDrinks {

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

    return sb.toString().replaceFirst(",", " und");
  }

}
