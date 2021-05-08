package com.entity.menu;

public class ResponseDescriptionBeer {

  private Content[] content;

  @Override
  public String toString() {

    return this.content[1].dish.getDescription().replace("Types: Ales", "Ales").replace(" Malts. Styles:", "")
        .replace("&", "und").replace("Strong,", "Strong und");
  }

}
