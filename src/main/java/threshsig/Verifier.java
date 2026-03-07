package threshsig;

import java.math.BigInteger;

class Verifier {
  private BigInteger z;

  private BigInteger c;

  // private BigInteger groupVerifier;

  // private BigInteger shareVerifier;

  public Verifier(final BigInteger z, final BigInteger c) {
    this.z = z;
    this.c = c;
  }

  public BigInteger getZ() {
    return z;
  }

  public BigInteger getC() {
    return c;
  }
}
