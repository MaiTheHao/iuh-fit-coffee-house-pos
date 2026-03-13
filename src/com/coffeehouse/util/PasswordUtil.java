package com.coffeehouse.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {
  private PasswordUtil() {}

  private static final int SALT_ROUNDS = 12;

  public static String hashPassword(String plainPassword) {
    if (plainPassword == null || plainPassword.isEmpty()) {
      throw new IllegalArgumentException("Password cannot be null or empty");
    }
    return BCrypt.hashpw(plainPassword, BCrypt.gensalt(SALT_ROUNDS));
  }

  public static boolean verifyPassword(String plainPassword, String hashedPassword) {
    if (plainPassword == null || plainPassword.isEmpty()) {
      throw new IllegalArgumentException("Password cannot be null or empty");
    }
    if (hashedPassword == null || hashedPassword.isEmpty()) {
      throw new IllegalArgumentException("Hashed password cannot be null or empty");
    }
    return BCrypt.checkpw(plainPassword, hashedPassword);
  }
}
