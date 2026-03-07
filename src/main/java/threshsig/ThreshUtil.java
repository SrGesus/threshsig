package threshsig;

import java.math.BigInteger;
import java.security.SecureRandom;

class ThreshUtil {
  // Constants and variables
  // ............................................................................
  static String DIGEST_ALGO = "SHA-256";

  protected final static BigInteger ZERO = BigInteger.ZERO;

  protected final static BigInteger ONE = BigInteger.ONE;

  protected final static BigInteger TWO = BigInteger.valueOf(2L);

  protected final static BigInteger FOUR = BigInteger.valueOf(4L);

  /** Fermat prime F4. */
  protected final static BigInteger F4 = BigInteger.valueOf(0x10001L);

  /** An arbitrary security parameter for generating secret shares */
  protected final static int L1 = 128;

  private static final SecureRandom random = new SecureRandom();

  protected static SecureRandom getRandom() {
    return random;
  }

  /**
   * Returns the factorial of the given integer as a BigInteger
   * 
   * @return l!
   */
  public static BigInteger factorial(final int l) {
    BigInteger x = BigInteger.valueOf(1l);
    for (int i = 1; i <= l; i++) {
      x = x.multiply(BigInteger.valueOf(i));
    }

    return x;
  }

  /**
   * Compute lagarange interpolation points Reference: Shoup, pg 7.
   * 
   * @param ik    - a point in S
   * @param S     - a set of k points in {0...l}
   * @param delta - the factorial of the group size
   * 
   * @return the Lagarange interpolation of these points at 0
   */
  public static BigInteger lambda(final int ik, final SigShare[] S,
      final BigInteger delta) {
    // lambda(id,l) = PI {id!=j, 0<j<=l} (i-j')/(id-j')
    BigInteger value = delta;

    for (final SigShare element : S) {
      if (element.getId() != ik) {
        value = value.multiply(BigInteger.valueOf(element.getId()));
      }
    }

    for (final SigShare element : S) {
      if (element.getId() != ik) {
        value = value.divide(BigInteger.valueOf((element.getId() - ik)));
      }
    }

    return value;
  }
}
