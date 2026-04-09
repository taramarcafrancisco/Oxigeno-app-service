package com.oxigeno.portal.dto;

import java.util.List;

public class LoginResponse {
  public String token;
  public UserDto user;

  public static class UserDto {
    public Long id;
    public String email;
    public Integer estado;
    public List<String> roles;
  }
}
