// Copyright 2022 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////

package com.google.crypto.tink.util;

import com.google.crypto.tink.SecretKeyAccess;
import com.google.crypto.tink.annotations.Alpha;
import com.google.crypto.tink.internal.Bytes;
import com.google.crypto.tink.subtle.Random;
import com.google.errorprone.annotations.Immutable;

/** A class storing an immutable byte array, protecting the data via {@link SecretKeyAccess}. */
@Alpha
@Immutable
public final class SecretByteArray {
  private final Bytes byteArray;

  private SecretByteArray(Bytes byteArray) {
    this.byteArray = byteArray;
  }

  /**
   * Creates a new SecretByteArray with the contents given in {@code value}.
   *
   * <p>The parameter {@code access} must be non-null.
   */
  public static SecretByteArray copyFrom(byte[] value, SecretKeyAccess access) {
    if (access == null) {
      throw new NullPointerException("SecretKeyAccess required");
    }
    return new SecretByteArray(Bytes.copyFrom(value));
  }

  /**
   * Creates a new SecretByteArray with bytes chosen uniformly at random of length {@code length}.
   */
  public static SecretByteArray randomBytes(int length) {
    return new SecretByteArray(Bytes.copyFrom(Random.randBytes(length)));
  }

  /**
   * Returns a copy of the bytes wrapped by this object.
   *
   * <p>The parameter {@code access} must be non-null.
   */
  public byte[] toByteArray(SecretKeyAccess access) {
    if (access == null) {
      throw new NullPointerException("SecretKeyAccess required");
    }
    return byteArray.toByteArray();
  }

  /** Returns the length of the bytes wrapped by this object. */
  public int size() {
    return byteArray.size();
  }

  /**
   * Returns true if the {@code other} byte array has the same bytes, in time depending only on the
   * length of both SecretByteArray objects.
   */
  public boolean equalsSecretByteArray(SecretByteArray other) {
    byte[] myArray = byteArray.toByteArray();
    byte[] otherArray = other.byteArray.toByteArray();
    if (myArray.length != otherArray.length) {
      return false;
    }
    int res = 0;
    for (int i = 0; i < myArray.length; i++) {
      res |= myArray[i] ^ otherArray[i];
    }
    return res == 0;
  }
}
