package com.devonfw.application.mtsj.general.common.base;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.devonfw.application.mtsj.general.common.api.datatype.SecondFactor;
import com.devonfw.application.mtsj.general.common.api.security.BasicAccountCredentials;
import com.devonfw.application.mtsj.general.common.api.security.LoginDataUsernameAndPassword;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.service.impl.UsermanagementRestServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Login Filter for Json Web Token
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(JWTLoginFilter.class);

  private UserDetailsService userDetailsService;

  private UsermanagementRestServiceImpl usermanagementRestServiceImpl;

  private PasswordEncoder passwordEncoder;

  /**
   * The constructor.
   *
   * @param url the login url
   * @param authManager the {@link AuthenticationManager}
   */
  public JWTLoginFilter(String url, AuthenticationManager authManager, UserDetailsService userDetailsService,
      UsermanagementRestServiceImpl usermanagementRestServiceImpl) {

    super(new AntPathRequestMatcher(url));
    setAuthenticationManager(authManager);
    this.userDetailsService = userDetailsService;
    this.usermanagementRestServiceImpl = usermanagementRestServiceImpl;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException, IOException, ServletException {

    BasicAccountCredentials creds = new ObjectMapper().readValue(req.getInputStream(), BasicAccountCredentials.class);

    if (creds.getEmail() != null) {

      UserEto user = new UserEto();
      user.setEmail(creds.getEmail());
      user.setUsername(creds.getUsername());

      this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

      user.setPassword(this.passwordEncoder.encode(creds.getPassword()));
      user.setUserRoleId((long) 0); // UserRole Costumer
      user.setModificationCounter(1);

      this.usermanagementRestServiceImpl.saveUser(user); // user is written to the Database

      UserDetails userDetails = this.userDetailsService.loadUserByUsername(creds.getUsername()); // if user does not
                                                                                                 // exist, then break
                                                                                                 // here
      ValidationService.validateCredentials(creds);
      return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
          creds.getPassword(), userDetails.getAuthorities())); // user is logged in

    } else {

      UserDetails userDetails = this.userDetailsService.loadUserByUsername(creds.getUsername()); // if user does not
                                                                                                 // exist, then break
                                                                                                 // here
      LoginDataUsernameAndPassword credsLog = new LoginDataUsernameAndPassword();
      credsLog.setUsername(creds.getUsername());
      credsLog.setPassword(creds.getPassword());

      ValidationService.validateCredentials(credsLog); // Login data is checked
      return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
          creds.getPassword(), userDetails.getAuthorities()));
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
      Authentication auth) throws IOException, ServletException {

    if (auth.getDetails() != SecondFactor.NONE) {
      TokenAuthenticationService.addAllowedHeader(res);
      TokenAuthenticationService.addRequiredAuthentication(res, auth);
    } else {
      TokenAuthenticationService.addAllowedHeader(res);
      TokenAuthenticationService.addAuthentication(res, auth);
      TokenAuthenticationService.addRequiredAuthentication(res, auth);
    }
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse res,
      AuthenticationException failed) {

    LOG.info("Authentication was unsuccessful");
    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
