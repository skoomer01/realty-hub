package org.example.business.impl.converters;

import org.example.domain.classes.UserInfo;
import org.example.persistance.entity.UserInfoEntity;

public final class UserConverter {
    private UserConverter() {
    }

    public static UserInfo convert(UserInfoEntity user) {
        return UserInfo.builder()
                .id(user.getId().intValue())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .build();
    }
}
