package des;

import java.util.ArrayList;
import java.util.*;

public class DES {

    private int totalRound;
    private final int[][] sbox = {{0, 2, 1, 3, 1, 0, 3, 2, 1, 3, 2, 0, 3, 0, 2, 1},
    {1, 3, 0, 2, 0, 2, 3, 1, 0, 3, 0, 2, 2, 1, 0, 3}};
    private final byte[] initial_permutation = {1, 4, 6, 8, 3, 5, 2, 7};
    private final byte final_permutation[] = {1, 7, 5, 2, 6, 3, 8, 4};
    private final byte expansion_table[] = {4, 1, 2, 3, 2, 3, 4, 1};
    private final byte permutation_4bits[] = {2, 1, 4, 3};
    private final byte shiftTimes[] = {2, 1};
    private ArrayList<String> subkey = new ArrayList<>();

    public static void main(String[] args) {
       
        DES des = new DES();
         new NewJFrame().setVisible(true);
      /*  Scanner scanner = new Scanner(System.in);
        System.out.println("enter the plain text");
        String plain = scanner.nextLine();
        System.out.println("enter the plain key");
        String key = scanner.nextLine();
        String cipher1 = des.Encrypyion(plain, key);
        System.out.println(cipher1);
        String plain1 = des.Decrypyion(cipher1, key);
        System.out.println(plain1);
      */
    }

    public String XOR(String left, String right) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < left.length(); i++) {
            stringBuilder.append((left.charAt(i) - '0') ^ (right.charAt(i) - '0'));
        }
        return stringBuilder.toString();
    }

    public String permutation(String recived, byte[] table) {
        StringBuilder build = new StringBuilder();
        for (byte index : table) {
            build.append(recived.charAt(index - 1));
        }
        return build.toString();
    }

    public void keys(String key) {

        //key=permutation(key, table);
        String preLeft = key.substring(0, key.length() / 2);
        String preRight = key.substring(key.length() / 2);
        for (int i = 0; i < 2; i++) {
            String newleft = shiftleft(preLeft, shiftTimes[i]);
            String newRifht = shiftleft(preRight, shiftTimes[i]);
            String NewKey = newleft + newRifht;
            //NewKey=permutation(NewKey, table);
            subkey.add(NewKey);
            preLeft = newleft;
            preRight = newRifht;
        }

    }

    public String shiftleft(String prev, int shiftTime) {
        return prev = prev.substring(shiftTime) + prev.substring(0, shiftTime);

    }

    public String sboxs(String newRigh) {
        int tbv, fmbv, c, sBoxValue;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i += 4) {
            String fourBit = newRigh.substring(i, i + 4);
            String c1 = fourBit.substring(0, 1);
            String c6 = fourBit.substring(3);
            tbv = Integer.parseInt((c1 + c6), 2);
            fmbv = Integer.parseInt(fourBit.substring(1, 3), 2);
            c = tbv * 4 + fmbv;
            sBoxValue = sbox[i / 4][c];
            String y = Integer.toBinaryString(sBoxValue);
            while (y.length() < 2) {
                y = "0" + y;
            }
            builder.append(y);

        }
        return builder.toString();
    }

    public String Encrypyion(String plain, String key) {
        plain = permutation(plain, initial_permutation);
        String Left = plain.substring(0, plain.length() / 2);
        String Right = plain.substring(plain.length() / 2);
        String cipher;
        keys(key);
        String k;
        for (int i = 0; i < 2; i++) {
            String Exp_Right = permutation(Right, expansion_table);
            k = subkey.get(i);
            String xor_Exp_Right = XOR(k, Exp_Right);
            String R_Sbox = sboxs(xor_Exp_Right);
            String perm_R_Sbox = permutation(R_Sbox, permutation_4bits);
            String final_right = XOR(Left, perm_R_Sbox);
            Left = Right;
            Right = final_right;
        }
        cipher = Right + Left;
        cipher = permutation(cipher, final_permutation);

        return (cipher);
    }
   public String Decrypyion(String cipher, String key) {
        cipher = permutation(cipher, initial_permutation);
        String Left = cipher.substring(0, cipher.length() / 2);
        String Right = cipher.substring(cipher.length() / 2);
        String plain;
        keys(key);
        String k;
        for (int i = 1; i >= 0; i--) {
            String Exp_Right = permutation(Right, expansion_table);
            k = subkey.get(i);
            String xor_Exp_Right = XOR(k, Exp_Right);
            String R_Sbox = sboxs(xor_Exp_Right);
            String perm_R_Sbox = permutation(R_Sbox, permutation_4bits);
            String final_right = XOR(Left, perm_R_Sbox);
            Left = Right;
            Right = final_right;
        }
        plain = Right + Left;
        plain = permutation(plain, final_permutation);

        return (plain);
    }
}
