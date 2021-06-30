package com.devonfw.application.mtsj.usermanagement.logic.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRepository;

/**
 * Tests for {@link UserRepository}
 *
 */
@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  /**
   * Tests to find a user by email
   */
  @Test
  public void findUserByEmail() {

    String userEmail = "user0@mail.com";
    UserEntity user = this.userRepository.findByEmail(userEmail);
    assertNotNull(user);
    Assertions.assertThat(user.getEmail()).isEqualTo(userEmail);

  }

  /**
   * Tests to find a user by email that is invalid
   */
  @Test
  public void findUserByInvalidEmail() {

    String userEmail = "NotAnemail.com";
    UserEntity user = this.userRepository.findByEmail(userEmail);
    assertNull(user);
  }

  /**
   * Tests to find a user by email that does not exist
   */
  @Test
  public void findUserByEmailNotExist() {

    String userEmail = "tom@mail.com";
    UserEntity user = this.userRepository.findByEmail(userEmail);
    assertNull(user);
  }

  /**
   * Tests to find a user by email with empty token
   */
  @Test
  public void findUserByEmailWithEmptyToken() {

    String userEmail = "";
    UserEntity user = this.userRepository.findByEmail(userEmail);
    assertNull(user);
  }

  /**
   * Tests to find a user by user name
   */
  @Test
  public void findUserByUsername() {

    String username = "user0";
    UserEntity user = this.userRepository.findByUsername(username);
    assertNotNull(user);
    Assertions.assertThat(user.getUsername()).isEqualTo(username);
  }

  /**
   * Tests to find a user by user name that does not exist
   */
  @Test
  public void findUserByUsernamenNotExist() {

    String username = "tom";
    UserEntity user = this.userRepository.findByUsername(username);
    assertNull(user);
  }

  /**
   * Tests to find a user by user name with empty token
   */
  @Test
  public void findUserByUsernameWithEmptyToken() {

    String username = "";
    UserEntity user = this.userRepository.findByUsername(username);
    assertNull(user);
  }
}
