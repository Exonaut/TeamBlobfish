package com.entity.menu;

public class ResponseDescriptionTee {

  public Content[] content;

  @Override
  public String toString() {

    return this.content[0].dish.description;

  }
}