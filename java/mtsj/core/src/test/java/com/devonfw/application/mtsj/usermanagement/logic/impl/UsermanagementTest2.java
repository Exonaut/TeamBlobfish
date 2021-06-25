package com.devonfw.application.mtsj.usermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;

/**
 * Tests for {@link UsermanagementImpl}
 *
 */

@SpringBootTest(classes = SpringBootApp.class)
public class UsermanagementTest2 extends ApplicationComponentTest {

  @Inject
  private Usermanagement userManagement;

  private PasswordEncoder passwordEncoder;

  @Override
  public void doSetUp() {

    super.doSetUp();

    this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * Tests to edit the email of user
   */
  @Test
  public void editUserUserEmailTest() {

    String newEmail = "user8@mail.com";
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername("user0");
    userToEdit.setPassword("password");
    userToEdit.setEmail(newEmail);
    userToEdit.setUserRoleId((long) 0);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user).isNotNull();
    assertThat(user.getEmail()).isEqualTo(newEmail);
  }

  /**
   * Tests to edit user name of user
   */
  @Test
  public void editUserUsernameTest() {

    String newUsername = "user8";
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername(newUsername);
    userToEdit.setPassword("password");
    userToEdit.setEmail("user0@mail.com");
    userToEdit.setUserRoleId((long) 0);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user).isNotNull();
    assertThat(user.getUsername()).isEqualTo(newUsername);
  }

  /**
   * Tests to edit user role of user
   */
  @Test
  public void editUserUserRoleTest() {

    Long newUserRole = (long) 2;
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername("user0");
    userToEdit.setPassword("password");
    userToEdit.setEmail("user0@mail.com");
    userToEdit.setUserRoleId(newUserRole);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user).isNotNull();
    assertThat(user.getUserRoleId()).isEqualTo(newUserRole);
  }

  /**
   * Tests to edit password of user
   */
  @Test
  public void editUserUserPasswordTest() {

    String newPassword = "test";
    String oldPassword = this.passwordEncoder.encode("password");
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername("user0");
    userToEdit.setPassword(newPassword);
    userToEdit.setEmail("user0@mail.com");
    userToEdit.setUserRoleId((long) 0);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user).isNotNull();
    assertThat(user.getPassword()).isNotEqualTo(oldPassword);
  }

  /**
   * Tests to edit empty user name of user
   */
  @Test
  public void editUserWithEmptyUsernameTest() {

    String emptyUsername = "";
    String oldPassword = this.passwordEncoder.encode("password");
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername(emptyUsername);
    userToEdit.setPassword(oldPassword);
    userToEdit.setEmail("user0@mail.com");
    userToEdit.setUserRoleId((long) 0);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user.getUsername()).isNotEqualTo(emptyUsername);
  }

  /**
   * Tests to edit user name with identical user name
   */
  @Test
  public void editUserUsernameWithIdenticalUsernameTest() {

    String identicalUsername = "user0";
    String oldPassword = this.passwordEncoder.encode("password");
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername(identicalUsername);
    userToEdit.setPassword(oldPassword);
    userToEdit.setEmail("user0@mail.com");
    userToEdit.setUserRoleId((long) 0);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user.getUsername()).isEqualTo(userToEdit.getUsername());
  }

  /**
   * Tests to edit user email with empty token
   */
  @Test
  public void editUserEmailWithEmptyEmailTest() {

    String emptyEmail = "";
    String oldPassword = this.passwordEncoder.encode("password");
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername("user0");
    userToEdit.setPassword(oldPassword);
    userToEdit.setEmail(emptyEmail);
    userToEdit.setUserRoleId((long) 0);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user.getEmail()).isNotEqualTo(emptyEmail);
  }

  /**
   * Tests to edit user email with identical email
   */
  @Test
  public void editUserEmailWithIdenticaliEmailTest() {

    String identicalEmail = "user0@mail.com";
    String password = this.passwordEncoder.encode("password");
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername("user0");
    userToEdit.setPassword(password);
    userToEdit.setEmail(identicalEmail);
    userToEdit.setUserRoleId((long) 0);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user.getEmail()).isEqualTo(userToEdit.getEmail());
  }

  /**
   * Tests to edit user role of user with value null
   */
  @Test
  public void editUserUserRoleWithEmptyUserRoleTest() {

    Long newUserRole = null;
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername("user0");
    userToEdit.setPassword("password");
    userToEdit.setEmail("user0@mail.com");
    userToEdit.setUserRoleId(newUserRole);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user.getUserRoleId()).isNotEqualTo(newUserRole);
  }

  /**
   * Tests to edit user role of user with invalid user role
   */
  @Test
  public void editUserUserRoleWithIdenticalUserRoleTest() {

    Long identicalUserRole = (long) 0;
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername("user0");
    userToEdit.setPassword("password");
    userToEdit.setEmail("user0@mail.com");
    userToEdit.setUserRoleId(identicalUserRole);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user.getUserRoleId()).isEqualTo(identicalUserRole);
  }

  /**
   * Tests to edit user name and email of user
   */
  @Test
  public void editUserUserNameAndUserEmailTest() {

    String newEmail = "user8@mail.com";
    String newUsername = "user8";
    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername(newUsername);
    userToEdit.setPassword("password");
    userToEdit.setEmail(newEmail);
    userToEdit.setUserRoleId((long) 0);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user).isNotNull();
    assertThat(user.getEmail()).isEqualTo(newEmail);
    assertThat(user.getUsername()).isEqualTo(newUsername);
  }

  /**
   * Tests to edit user name and password of user
   */
  @Test
  public void editUserUserNameAndPasswordTest() {

    String newPassword = "";
    String oldPassword = this.passwordEncoder.encode("password");
    String newUsername = "user8";

    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername(newUsername);
    userToEdit.setPassword(newPassword);
    userToEdit.setEmail("user0@mail.com");
    userToEdit.setUserRoleId((long) 0);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user).isNotNull();
    assertThat(user.getUsername()).isEqualTo(newUsername);
    assertThat(user.getPassword()).isNotEqualTo(oldPassword);
  }

  @Test
  public void editUserUserEmailPasswordUserRoleIdTest() {

    String newPassword = "test";
    Long newUserRoleId = (long) 1;
    String oldPassword = this.passwordEncoder.encode("password");
    String newUserEmail = "user8@mail.com";

    UserEto userToEdit = new UserEto();
    userToEdit.setId((long) 0);
    userToEdit.setUsername("user0");
    userToEdit.setPassword(newPassword);
    userToEdit.setEmail(newUserEmail);
    userToEdit.setUserRoleId(newUserRoleId);

    UserEto user = this.userManagement.editUser(userToEdit);

    assertThat(user).isNotNull();
    assertThat(user.getEmail()).isEqualTo(newUserEmail);
    assertThat(user.getPassword()).isNotEqualTo(oldPassword);
    assertThat(user.getUserRoleId()).isEqualTo(newUserRoleId);
  }
}
