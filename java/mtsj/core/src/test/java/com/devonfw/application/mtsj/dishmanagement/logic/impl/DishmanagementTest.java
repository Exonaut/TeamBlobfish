package com.devonfw.application.mtsj.dishmanagement.logic.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.dishmanagement.common.api.to.CategoryEto;
import com.devonfw.application.mtsj.dishmanagement.common.api.to.DishCto;
import com.devonfw.application.mtsj.dishmanagement.common.api.to.DishSearchCriteriaTo;
import com.devonfw.application.mtsj.dishmanagement.logic.api.Dishmanagement;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Tests for {@link Dishmanagement} component.
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
public class DishmanagementTest extends ApplicationComponentTest {

  @Inject
  private Dishmanagement dishmanagement;

  /**
   * This test gets all the available dishes using an empty SearchCriteria object
   */
  @Test
  public void findAllDishes() {

    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();
    criteria.setCategories(categories);
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "price"));
    criteria.setPageable(pageable);
    Page<DishCto> result = this.dishmanagement.findDishCtos(criteria);
    assertThat(result).isNotNull();
  }

  /**
   * This test filters all the available dishes that match the SearchCriteria object
   */
  @Test
  public void filterDishes1() {

    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();
    criteria.setCategories(categories);
    criteria.setSearchBy("Thai Spicy Basil Fried Rice");
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "id"));
    criteria.setPageable(pageable);
    Page<DishCto> result = this.dishmanagement.findDishCtos(criteria);

    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);
    assertThat(result.getContent().get(0).getDish().getName()).isEqualTo("Thai Spicy Basil Fried Rice");
  }

  /**
   * This test filters all the available dishes that match the SearchCriteria object
   */
  @Test
  public void filterDishes2() {

    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();
    criteria.setCategories(categories);
    criteria.setSearchBy("Garlic Paradise Salad");
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "id"));
    criteria.setPageable(pageable);
    Page<DishCto> result = this.dishmanagement.findDishCtos(criteria);

    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);
    assertThat(result.getContent().get(0).getDish().getName()).isEqualTo("Garlic Paradise Salad");
  }

  /**
   * This test filters all the available dishes that match the SearchCriteria object
   */
  @Test
  public void filterDishes3() {

    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();
    criteria.setCategories(categories);
    criteria.setSearchBy("Thai green chicken curry");
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "id"));
    criteria.setPageable(pageable);
    Page<DishCto> result = this.dishmanagement.findDishCtos(criteria);

    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);
    assertThat(result.getContent().get(0).getDish().getName()).isEqualTo("Thai green chicken curry");
  }

  /**
   * This test filters all the available dishes that match the SearchCriteria object
   */
  @Test
  public void filterDishes4() {

    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();
    criteria.setCategories(categories);
    criteria.setSearchBy("Thai Peanut Beef");
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "id"));
    criteria.setPageable(pageable);
    Page<DishCto> result = this.dishmanagement.findDishCtos(criteria);

    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);
    assertThat(result.getContent().get(0).getDish().getName()).isEqualTo("Thai Peanut Beef");
  }

  /**
   * This test filters all the available dishes that match the SearchCriteria object
   */
  @Test
  public void filterDishes5() {

    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();
    criteria.setCategories(categories);
    criteria.setSearchBy("Thai Thighs Fish/Prawns");
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "id"));
    criteria.setPageable(pageable);
    Page<DishCto> result = this.dishmanagement.findDishCtos(criteria);

    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);
    assertThat(result.getContent().get(0).getDish().getName()).isEqualTo("Thai Thighs Fish/Prawns");
  }

  /**
   * This test filters all the available dishes that match the SearchCriteria object
   */
  @Test
  public void filterDishes6() {

    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();
    criteria.setCategories(categories);
    criteria.setSearchBy("Beer");
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "id"));
    criteria.setPageable(pageable);
    Page<DishCto> result = this.dishmanagement.findDishCtos(criteria);

    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);
    assertThat(result.getContent().get(0).getDish().getName()).isEqualTo("Beer");
  }

  /**
   * This test filters all the available dishes that match the SearchCriteria object
   */
  @Test
  public void filterDishes7() {

    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();
    criteria.setCategories(categories);
    criteria.setSearchBy("Tea");
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "id"));
    criteria.setPageable(pageable);
    Page<DishCto> result = this.dishmanagement.findDishCtos(criteria);

    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);
    assertThat(result.getContent().get(0).getDish().getName()).isEqualTo("Tea");
  }

  /**
   * Tests to find a category by id
   */
  @Test
  public void findCategoryById() {

    Long categoryId = (long) 1;
    CategoryEto category = this.dishmanagement.findCategory(categoryId);
    assertThat(category).isNotNull();
  }

  /**
   * Tests to find dishes by category name
   */
  @Test
  public void findDishesByCategoryName1() {

    String categoryName = "Main Dishes";
    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();

    criteria.setCategories(categories);
    criteria.setSearchBy("");
    PageRequest pageable = PageRequest.of(0, 8, Sort.by(Direction.DESC, "price"));
    criteria.setPageable(pageable);

    Page<DishCto> result = this.dishmanagement.findDishesByCategory(criteria, categoryName);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);

  }

  /**
   * Tests to find dishes by category name
   */
  @Test
  public void findDishesByCategoryName2() {

    String categoryName = "Starter";
    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();

    criteria.setCategories(categories);
    criteria.setSearchBy("");
    PageRequest pageable = PageRequest.of(0, 8, Sort.by(Direction.DESC, "price"));
    criteria.setPageable(pageable);

    Page<DishCto> result = this.dishmanagement.findDishesByCategory(criteria, categoryName);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);

  }

  /**
   * Tests to find dishes by category name
   */
  @Test
  public void findDishesByCategoryName3() {

    String categoryName = "Vegan";
    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();

    criteria.setCategories(categories);
    criteria.setSearchBy("");
    PageRequest pageable = PageRequest.of(0, 8, Sort.by(Direction.DESC, "price"));
    criteria.setPageable(pageable);

    Page<DishCto> result = this.dishmanagement.findDishesByCategory(criteria, categoryName);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);

  }

  /**
   * Tests to find dishes by category name
   */
  @Test
  public void findDishesByCategoryName4() {

    String categoryName = "Rice";
    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();

    criteria.setCategories(categories);
    criteria.setSearchBy("");
    PageRequest pageable = PageRequest.of(0, 8, Sort.by(Direction.DESC, "price"));
    criteria.setPageable(pageable);

    Page<DishCto> result = this.dishmanagement.findDishesByCategory(criteria, categoryName);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);

  }

  /**
   * Tests to find dishes by category name
   */
  @Test
  public void findDishesByCategoryName5() {

    String categoryName = "Curry";
    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();

    criteria.setCategories(categories);
    criteria.setSearchBy("");
    PageRequest pageable = PageRequest.of(0, 8, Sort.by(Direction.DESC, "price"));
    criteria.setPageable(pageable);

    Page<DishCto> result = this.dishmanagement.findDishesByCategory(criteria, categoryName);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);

  }

  /**
   * Tests to find dishes by category name
   */
  @Test
  public void findDishesByCategoryName6() {

    String categoryName = "Drinks";
    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();

    criteria.setCategories(categories);
    criteria.setSearchBy("");
    PageRequest pageable = PageRequest.of(0, 8, Sort.by(Direction.DESC, "price"));
    criteria.setPageable(pageable);

    Page<DishCto> result = this.dishmanagement.findDishesByCategory(criteria, categoryName);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);

  }

  /**
   * Tests to find dishes by category name
   */
  @Test
  public void findDishesByCategoryName7() {

    String categoryName = "Vegetarian";
    DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
    List<CategoryEto> categories = new ArrayList<>();

    criteria.setCategories(categories);
    criteria.setSearchBy("");
    PageRequest pageable = PageRequest.of(0, 8, Sort.by(Direction.DESC, "price"));
    criteria.setPageable(pageable);

    Page<DishCto> result = this.dishmanagement.findDishesByCategory(criteria, categoryName);
    assertThat(result).isNotNull();
    assertThat(result.getContent().size()).isGreaterThan(0);

  }

  /**
   * Tests to find dishes by category name with empty token
   */
  @Test
  public void findDishesByCategoryNameWithEmptyToken() {

    try {
      String categoryName = "";
      DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
      List<CategoryEto> categories = new ArrayList<>();

      criteria.setCategories(categories);
      criteria.setSearchBy("");
      PageRequest pageable = PageRequest.of(0, 8, Sort.by(Direction.DESC, "price"));
      criteria.setPageable(pageable);

      Page<DishCto> result = this.dishmanagement.findDishesByCategory(criteria, categoryName);
      assertThat(result).isNull();

    } catch (Exception e) {
      IllegalArgumentException iae = new IllegalArgumentException();
      assertThat(e.getClass()).isEqualTo(iae.getClass());
    }
  }

  /**
   * Tests to find dishes by category name that does not exist
   */
  @Test
  public void findDishesByCategoryNameNotExist() {

    try {
      String categoryName = "Dessert";
      DishSearchCriteriaTo criteria = new DishSearchCriteriaTo();
      List<CategoryEto> categories = new ArrayList<>();

      criteria.setCategories(categories);
      criteria.setSearchBy("");
      PageRequest pageable = PageRequest.of(0, 8, Sort.by(Direction.DESC, "price"));
      criteria.setPageable(pageable);

      Page<DishCto> result = this.dishmanagement.findDishesByCategory(criteria, categoryName);
      assertThat(result).isNull();

    } catch (Exception e) {
      IllegalArgumentException iae = new IllegalArgumentException();
      assertThat(e.getClass()).isEqualTo(iae.getClass());
    }
  }

}
