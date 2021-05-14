package com.entity.dish;

/**
 * TODO Spielecke This type ...
 *
 */
public class ResponseDescriptionDishes {

  private Content[] content;

  public String getDishesDescription(String dishName) {

    for (int i = 0; i < this.content.length; i++) {
      if (this.content[i].dish.name.toLowerCase().equals(dishName)) {
        return this.content[i].dish.description.replaceAll("&", "and");

      }

    }
    return "Das haben wir nicht im Angebot";

  }
}
