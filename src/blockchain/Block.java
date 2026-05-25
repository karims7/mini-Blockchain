package blockchain;

import java.util.ArrayList;
import java.util.Date;

/**
 * Block
 *
 * Think of this as one page in a notebook.
 * Each page holds a message, a timestamp, and two fingerprints:
 * one for itself, and one for the page before it.
 * Together, all the pages form the chain.
 */
public class Block {
    public String hash; // this page's own fingerprint.
    public String previousHash; // a fingerprint of the page BEFORE this one. This is the chain part — every
                                // page remembers the page before it
    public String data; // the actual message written on the page.
    public long timeStamp; // when this page was written.
    public int nonce; // random number the miner keeps changing to solve a puzzle.

    /**
     * Block constructor
     * Creates a new block with a message and the previous block's fingerprint.
     * The hash is calculated immediately after so the block is ready to be mined.
     *
     * @param data         the message to store in this block
     * @param previousHash the fingerprint of the block before this one
     */
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    /**
     * calculateHash()
     * Takes everything on this page — the previous fingerprint, the timestamp,
     * the nonce, and the data — and runs it through a math formula (SHA-256)
     * to produce a unique 64-character fingerprint.
     *
     * Important: change even ONE character of the input and the output
     * looks completely different. That is what makes it tamper-proof.
     *
     * @return the calculated hash string for this block
     */
    public String calculateHash() {
        String calculatedHash = StringUtil
                .applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data);
        return calculatedHash;
    }

    /**
     * mineBlock()
     * Before a block is allowed into the chain, it has to solve a puzzle.
     * The puzzle: keep bumping the nonce up by 1 and recalculating the hash
     * until the hash starts with a certain number of zeros (set by difficulty).
     *
     * The nonce itself is not the answer — it is just a knob we keep turning.
     * What we are watching is the hash. Once the hash looks right (e.g.
     * "00000..."),
     * the block is considered mined and gets added to the chain.
     *
     * This is intentionally hard and slow so nobody can spam fake blocks.
     * The higher the difficulty, the more zeros required, the longer it takes.
     * 
     * You can't cheat it. You can't calculate the answer directly.
     * You just have to brute force it — try thousands of times until you get lucky.
     * That's intentional. It makes adding fake blocks expensive (in time and
     * computing power).
     *
     * @param difficulty the number of zeros the hash must start with
     */
    public void mineBlock(int difficulty) {
        String target = StringUtil.getDifficultyString(difficulty);
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }

        System.out.println("Block Mined: " + hash);

    }

}
