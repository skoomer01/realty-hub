package org.example.business;

import org.example.domain.classes.AccessToken;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
