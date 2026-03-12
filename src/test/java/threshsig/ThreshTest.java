package threshsig;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import threshsig.GroupKey.Aggregator;;

public class ThreshTest {
  private final int KEYSIZE = 512;
  private final int K = 6;
  private final int L = 13;
  private Dealer d;
  private GroupKey gk;
  private KeyShare[] keys;
  private final byte[] data = new byte[1024];
  private byte[] b;
  private final SigShare[] sigs = new SigShare[K];

  @BeforeEach
  void setUp() {
    (new Random()).nextBytes(data);
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      b = md.digest(data);
    } catch (NoSuchAlgorithmException e) {
      // This should not occur
      assertTrue(false);
    }

    // Initialize a dealer with a keysize
    d = new Dealer(KEYSIZE);

    final long start = System.currentTimeMillis();
    long elapsed;
    // Generate a set of key shares
    d.generateKeys(K, L);

    elapsed = System.currentTimeMillis() - start;
    System.out.println("\tKey Gen total (ms): " + elapsed);

    // This is the group key common to all shares, which
    // is not assumed to be trusted. Treat like a Public Key
    gk = d.getGroupKey();

    // The Dealer has the shares and is assumed trusted
    // This should be destroyed, unless you want to reuse the
    // Special Primes of the group key to generate a new set of
    // shares
    keys = d.getShares();
  }

  @Test
  public void testVerifySignatures() {
    System.out.println("Attempting to verify a valid set of signatures...");
    // Pick a set of shares to attempt to verify
    // These are the indices of the shares
    final int[] S = { 3, 5, 1, L - 1, 10, 0 };

    for (int i = 0; i < S.length; i++)
      sigs[i] = keys[S[i]].sign(b);

    assertTrue(gk
        .verify(b, sigs));

    Aggregator aggregator = gk.starAggregation(b);
    for (int i = 0; i < S.length; i++)
      aggregator.tryAddSigShare(sigs[i]);

    assertTrue(aggregator.verify());

    assertTrue(gk.verify(b, aggregator.getSignature()));
  }

  @Test
  public void testVerifySignaturesAgain() {
    System.out.println("Attempting to verify a different set of shares...");

    // Create k sigs to verify using different keys
    final int[] T = { 8, 9, 7, 6, 1, 12 };
    for (int i = 0; i < K; i++)
      sigs[i] = keys[T[i]].sign(b);

    assertTrue(gk
        .verify(b, sigs));

    Aggregator aggregator = gk.starAggregation(b);
    for (int i = 0; i < T.length; i++)
      aggregator.tryAddSigShare(sigs[i]);

    assertTrue(aggregator.verify());
    assertTrue(gk.verify(b, sigs));

    assertTrue(gk.verify(b, aggregator.getSignature()));
  }

  @Test
  public void testVerifyBadSignature() {
    testVerifySignaturesAgain();
    System.out.println("Attempting to verify signature of corrupted data...");

    sigs[3] = keys[3].sign("corrupt data".getBytes());

    Aggregator aggregator = gk.starAggregation(b);
    aggregator.tryAddSigShares(sigs);

    // only signature with id 7 should fail,
    assertTrue(aggregator.getFailedSigs().contains(sigs[3]));
    assertTrue(aggregator.getFailedSigs().size() == 1);

    assertThrows(ThresholdSigException.class, () -> aggregator.verify());
    assertFalse(gk.verify(b, sigs));

    assertThrows(ThresholdSigException.class, () -> aggregator.getSignature());
  }

  @Test
  public void testVerifyImpersonatingSignatures() {
    testVerifySignaturesAgain();
    System.out.println("Attempting to verify a impersonated signature...");

    sigs[3] = new KeyShare(10 + 1, keys[3].getSecret(), gk).sign(b);

    Aggregator aggregator = gk.starAggregation(b);
    aggregator.tryAddSigShares(sigs);

    // only signature with id 11 should fail,
    assertTrue(aggregator.getFailedSigs().contains(sigs[3]));
    assertTrue(aggregator.getFailedSigs().size() == 1);

    // group signature should also fail
    assertFalse(gk.verify(b, sigs));
    assertThrows(ThresholdSigException.class, () -> aggregator.verify());

    assertThrows(ThresholdSigException.class, () -> aggregator.getSignature());
  }

  @Test
  public void testPerformance() {
    final int RUNS = 20;
    final int[] S = { 3, 5, 1, 2, 10, 7 };

    long start = System.currentTimeMillis(), elapsed;
    for (int i = 0; i < RUNS; i++)
      sigs[i % K] = keys[S[i % K]].sign(b);
    elapsed = System.currentTimeMillis() - start;
    System.out.println("Signing total (" + RUNS + " sigs) (ms): " + elapsed
        + " Average: " + (float) (elapsed / RUNS));

    for (int i = 0; i < K; i++)
      sigs[i] = keys[S[i]].sign(b);

    start = System.currentTimeMillis();
    for (int i = 0; i < RUNS; i++)
      if (!gk.verify(b, sigs))
        System.out.println("Sig Failed to verify correctly");
    elapsed = System.currentTimeMillis() - start;
    System.out.println("Verification total (" + RUNS + " sigs) (ms): "
        + elapsed + " Average: " + (float) (elapsed / RUNS));
  }
}
