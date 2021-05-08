package com.entity.menu;

public class ResponseDescriptionBeer {

  public Content[] content;

  @Override
  public String toString() {

    return this.content[1].dish.description.replace("Types: Ales", "Ales").replace(" Malts. Styles:", "").replace("&",
        "und");
  }

}
