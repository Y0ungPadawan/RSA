import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class RSA {
	public Keys publicKey;
    private Keys privateKey;
	int keySize;
	int[] primery = {	3,  5,  7,  11, 13, 17, 19, 23, 
					 	29, 31, 37, 41, 43, 47, 53, 59, 
					 	61, 67, 71, 73, 79, 83, 89, 97
					 };
	
	RSA(int keySize){
		this.keySize = keySize/2;
		BigNumber P = BigNumber.getPrimeNumber(this.keySize);
		BigNumber Q = BigNumber.getPrimeNumber(this.keySize);
		BigNumber N = P.multiply(Q);
		BigNumber fN= P.subtract(1).multiply(Q.subtract(1));
		Random rnd = new Random();
		BigNumber KB1;
		BigNumber KB2;
		do{
			KB1 = new BigNumber(primery[rnd.nextInt(primery.length)]);
			KB2 = RSA.KB2generator(fN, KB1);
		} while(!KB2.multiply(KB1).mod(fN).isEqual(1));
		this.publicKey = new Keys(N,KB1);
		this.privateKey = new Keys(N,KB2);
		}
	
	static BigNumber[] encrypt(String message, Keys key){
				StringBuilder binaryTemp = new StringBuilder("");
				String temp;
				for (int i = 0; i<message.length(); i++){
					temp = Integer.toBinaryString((int)message.charAt(i));
							for (int j=0; j<11-temp.length(); j++){
								binaryTemp.append("0");	
						}
					binaryTemp.append(temp);
				}
				
				BigNumber N = key.N;
				int KB1 = Integer.parseInt(key.KB.toString());
				
				int point = N.subtract(1).getLength()<31 ? N.subtract(1).getLength()-1:31;
				String[] textBinaryBlocks = new String[(binaryTemp.length()+point-1)/point];
				
				binaryTemp = binaryTemp.reverse();
				
				textBinaryBlocks = binaryTemp.toString().split("(?<=\\G.{"+point+"})");
				Collections.reverse(Arrays.asList(textBinaryBlocks));
				
				int[] blocks = new int[textBinaryBlocks.length];
				BigNumber[] result = new BigNumber[blocks.length];
				for (int i = 0; i< blocks.length; i++){
					blocks[i] = Integer.parseInt(new StringBuilder(textBinaryBlocks[i]).reverse().toString(),2);
					result[i] = BigNumber.powByModule(blocks[i], KB1, N);
				}
				return result;
	}
	
			String decrypt(BigNumber[] message){
				
				BigNumber N = privateKey.N;
				BigNumber KB2 = privateKey.KB;
				BigNumber[] result = new BigNumber[message.length];
				StringBuilder binaryTemp = new StringBuilder("");
				int point = N.subtract(1).getLength()<31 ? N.subtract(1).getLength()-1:31;
				
				String temp;
				
				for (int i = 0; i < message.length; i++){
					result[i] = message[i].powByModule(KB2, N);
					temp = Integer.toBinaryString(Integer.parseInt(result[i].toString()));
					if (!binaryTemp.toString().equals(""))
						for (int j=0; j<point-temp.length(); j++)
							binaryTemp.append("0");
					binaryTemp.append(temp);
				}
				
				binaryTemp = binaryTemp.reverse();
				
				String[] textBinaryBlocks = binaryTemp.toString().split("(?<=\\G.{11})");
				Collections.reverse(Arrays.asList(textBinaryBlocks));
				
				StringBuilder resultString = new StringBuilder("");
				StringBuilder[] tmp = new StringBuilder[textBinaryBlocks.length];
				for (int i = 0; i < tmp.length; i++){
					tmp[i] = new StringBuilder(textBinaryBlocks[i]).reverse();
					resultString.append((char)Integer.parseInt(tmp[i].toString(),2));
				}
				
				return resultString.toString();
		
	}
	
	static BigNumber KB2generator(BigNumber fN, BigNumber KB1){
		
		BigNumber o = new BigNumber(0); 
		BigNumber x, y;
		
		if (KB1.isEqual(0) ){
			return new BigNumber(0);
		}
		BigNumber x2 = new BigNumber(1);
		BigNumber x1 = new BigNumber(0);
		BigNumber y2 = new BigNumber(0);
		BigNumber y1 = new BigNumber(1);
		BigNumber q;
		BigNumber r;
		
		while (KB1.compareTo(o)>0){
			q = fN.div(KB1);
			r = fN.subtract(q.multiply(new BigNumber(KB1)));
			
			x = x2.subtract(q.multiply(x1));
			y = y2.subtract(q.multiply(y1));
		
			fN = new BigNumber(KB1); 
			KB1 = new BigNumber(r);
			x2 = new BigNumber(x1);
			x1 = new BigNumber(x);
			y2 = new BigNumber(y1);
			y1 = new BigNumber(y);			
		}
		
		if (y2.compareTo(o)<0){
			y2 = fN.add(y2);
		}
		return y2;
	}
}
