package threshsig;

import java.math.BigInteger;

/**
 * Signature Shares Class<BR>
 * Associates a signature share with an id & wraps a static verifier
 * 
 * Reference: "Practical Threshold Signatures",<br>
 * Victor Shoup (sho@zurich.ibm.com), IBM Research Paper RZ3121, 4/30/99<BR>
 * 
 * @author Steve Weis <sweis@mit.edu>
 */
public class SigShare {

  // Constants and variables
  // ............................................................................
  private int id;

  private BigInteger sig;

  private Verifier sigVerifier;

  // Constructors
  // ............................................................................
  public SigShare(final int id, final BigInteger sig, final Verifier sigVerifier) {
    this.id = id;
    this.sig = sig;
    this.sigVerifier = sigVerifier;
  }

  public SigShare(final int id, final byte[] sig) {
    this.id = id;
    this.sig = new BigInteger(sig);
  }

  // Public Methods
  // ............................................................................

  /**
   * Return this share's id. Needed for Lagrange interpolation
   * 
   * @return the id of this key share
   */
  public int getId() {
    return id;
  }

  /**
   * Return a BigInteger representation of this signature
   * 
   * @return a BigInteger representation of this signature
   */
  public BigInteger getSig() {
    return sig;
  }

  /**
   * Return this signature's verifier
   * 
   * @return A verifier for this signaute
   */
  public Verifier getSigVerifier() {
    return sigVerifier;
  }

  /**
   * Return a byte array representation of this signature
   * 
   * @return a byte array representation of this signature
   */
  public byte[] getBytes() {
    return sig.toByteArray();
  }

  @Override
  public String toString() {
    return "Sig[" + id + "]: " + sig.toString();
  }
}
