package blockchain;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

/**
 * Blockchain
 *
 * This is the notebook itself — it holds all the blocks, adds new ones,
 * and checks if anyone messed with the chain.
 * This is also the file you actually RUN. The main() method is the starting
 * point.
 *
 * Flow from start to finish:
 *
 * Run the program
 * → Mine block 1 (no previous block, so previous hash = "0")
 * → Mine block 2 (use block 1's fingerprint as previous hash)
 * → Mine block 3 (use block 2's fingerprint as previous hash)
 * → Check the whole chain is valid
 * → Print everything out
 *
 * How the chain links together:
 *
 * Block 1
 * data: "First Block"
 * previousHash: "0"
 * hash: "00000abc123..."
 * ↓
 * Block 2
 * data: "Second Block"
 * previousHash: "00000abc123..." ← baked in from block 1
 * hash: "00000xyz789..."
 * ↓
 * Block 3
 * data: "Third Block"
 * previousHash: "00000xyz789..." ← baked in from block 2
 * hash: "00000def456..."
 */
public class Blockchain {

    /**
     * blockchain
     * The actual chain — a growing list of blocks.
     * Think of it like a binder where you keep adding pages. Each page is a Block.
     */
    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    /**
     * difficulty
     * The puzzle hardness setting.
     * A value of 5 means every block's fingerprint must start with "00000"
     * before it is allowed into the chain.
     * Crank this number up and mining gets slower.
     * Turn it down and it is faster but less secure.
     */
    public static int difficulty = 5;

    /**
     * main()
     * The starting point — this is where everything kicks off when you run the
     * program.
     *
     * It does four things in order:
     * 1. Mines and adds three blocks to the chain
     * 2. Checks if the chain is valid
     * 3. Converts the chain to readable text (JSON)
     * 4. Prints the whole chain
     *
     * The first block gets "0" as its previous hash because nothing came before it.
     * It is called the Genesis Block — the one that started it all.
     *
     * For block 2 and 3, we grab the last block's fingerprint from the list
     * and bake it in as the previous hash. That is how the chain links together.
     */
    public static void main(String[] args) {

        System.out.println("Trying to Mine block 1...");
        addBlock(new Block("First Block", "0"));

        System.out.println("Trying to Mine block 2...");
        addBlock(new Block("Second Block", blockchain.get(blockchain.size() - 1).hash));

        System.out.println("Trying to Mine block 3...");
        addBlock(new Block("Third Block", blockchain.get(blockchain.size() - 1).hash));

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockChainJSON = StringUtil.getJSON(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockChainJSON);
    }

    /**
     * addBlock()
     * Adds a new page to the notebook — but only after it solves the puzzle.
     *
     * Two steps:
     * 1. Make the block mine itself (solve the puzzle) — it cannot get in without
     * doing the work
     * 2. Once it is mined, add it to the list
     *
     * Think of it like a bouncer at a club.
     * You cannot get in until you solve the puzzle. Once you do, you are in.
     *
     * @param newBlock the block to be mined and added to the chain
     */
    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    /**
     * isChainValid()
     * The integrity check — goes through every block and runs three checks on each
     * one.
     * If any check fails, the chain has been tampered with.
     *
     * It starts the loop at block 1 (not 0) because block 0 has no previous block
     * to compare to.
     * Each iteration grabs the current block and the one before it.
     *
     * Check 1 — did anyone change the data?
     * Recalculates the block's fingerprint from scratch and compares it to the
     * stored one.
     * If someone changed even one letter of the data, the fingerprints won't match.
     * Busted.
     *
     * Check 2 — did anyone swap out a block?
     * Every block stores the fingerprint of the block before it.
     * This check makes sure that stored fingerprint actually matches the real
     * previous block.
     * If someone replaced a block in the middle of the chain, this breaks.
     *
     * Check 3 — was it actually mined?
     * Checks that the fingerprint starts with "00000".
     * If it doesn't, the block never did the work and doesn't belong in the chain.
     *
     * hashTarget explained:
     * new String(new char[difficulty]) → builds a string of 5 blank/null
     * characters: " "
     * .replace('\0', '0') → replaces each blank with '0': "00000"
     * This scales automatically — if difficulty = 8, it produces "00000000".
     * We do it this way instead of hardcoding "00000" so it always matches whatever
     * difficulty is set to.
     *
     * @return true if the chain is valid, false if anything looks wrong
     */
    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        // build the target string dynamically based on difficulty
        // e.g. difficulty 5 → "00000"
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        // start at 1 — block 0 has no previous block to compare to
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            // Check 1: did anyone change this block's data?
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            // Check 2: does this block's stored previous hash match the actual previous
            // block?
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            // Check 3: was this block actually mined? does the hash start with enough
            // zeros?
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }

        return true;
    }
}