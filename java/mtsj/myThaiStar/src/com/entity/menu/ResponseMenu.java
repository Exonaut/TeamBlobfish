package com.entity.menu;

public class ResponseMenu {

  public Content[] content;

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    for (Content c : this.content) {
      sb.append(c.dish.getName() + ", ");
    }
    return sb.toString().replaceFirst(",", " und").replace("Thai Thighs Fish/Prawns, Garlic Paradise Salad,",
        "Thai Thighs Fish/Prawns und Garlic Paradise Salad");
  }

}