package blockchain;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class Blockchain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;

    public static void main(String[] args) {

        // add our blocks to the blockchain ArrayList:

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

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        // looping through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {

        }

    }

}
