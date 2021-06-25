package com.devonfw.application.mtsj.usermanagement.logic.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRepository;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;

/**
 * Tests for {@link UsermanagementImpl}
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
@ExtendWith(MockitoExtension.class)
public class UsermanagementTest1 extends ApplicationComponentTest {

  @InjectMocks
  private Usermanagement userManagement = new UsermanagementImpl();

  @Mock
  private UserRepository userRepository;

  private PasswordEncoder passwordEncoder;

  @Override
  public void doSetUp() {

    super.doSetUp();

    this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * Tests that the password of user is reset by admin
   */
  @Test
  public void resetPasswordByAdminTest() {

    // given
    UserEntity userSavedInDatabase = new UserEntity();
    UserEto user = new UserEto();
    user.setId((long) 0);
    user.setPassword("test");

    // mocks
    when(this.userRepository.find((long) 0)).thenReturn(userSavedInDatabase);

    // when
    this.userManagement.resetPasswordByAdmin(user);

    // then
    // verify that the method has been called
    verify(this.userRepository).find((long) 0);
    verify(this.userRepository).save(userSavedInDatabase);

  }

}
