package com.bsa.giphy.BSAGiphy.utils;

import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    public boolean invalid(String user_id) {
        return user_id.isBlank() || user_id.isEmpty() || user_id.matches(".*[|*?<>:/\"].*");
    }
}
