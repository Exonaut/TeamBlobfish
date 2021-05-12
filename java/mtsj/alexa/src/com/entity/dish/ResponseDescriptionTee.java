package com.entity.dish;

public class ResponseDescriptionTee {

  private Content[] content;

  @Override
  public String toString() {

    return this.content[0].dish.getDescription().replace("Darjeeling Green Tea,", "Darjeeling Green Tea und");

  }
}