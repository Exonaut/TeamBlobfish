package com.entity.menu;

/**
 * TODO Spielecke This type ...
 *
 */
public class ResponseDescriptionFood {

  private Content[] content;

  public String getFoodDescription(String food) {

    for (int i = 0; i < this.content.length; i++) {
      if (this.content[i].dish.getName().toLowerCase().replaceAll(" ", "").equals(food)) {
        return this.content[i].dish.getDescription();

      }

    }

    return "Das haben wir nicht im Angebot";
  }

}
