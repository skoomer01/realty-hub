package org.example.business;


import org.example.domain.LoginRequest;
import org.example.domain.LoginResponse;

public interface ILoginManager {
    LoginResponse login(LoginRequest loginRequest);
}
