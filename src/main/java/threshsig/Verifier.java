package threshsig;

import java.math.BigInteger;

public class Verifier {
  private BigInteger z;

  private BigInteger c;

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
