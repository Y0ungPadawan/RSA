import java.io.Serializable;
import java.util.Random;


public class BigNumber implements Serializable {
	     /**
	 * 
	 */
		 private static final long serialVersionUID = 1L;
		 int sign;
		 long[] bits;
		 static int BASE = 4;
		 
		 /*
		  *  CONSTRUCTORS
		  */
		 BigNumber(){
			 this.sign = 1;
			 this.bits = new long[0];
		 }
		 
		 BigNumber(byte value){
			 this((short)value);
		 }
		 
		 BigNumber(short value){
			this.sign =  (value < 0) ? -1 : 1;
			this.bits = new long[1];
			this.bits[0] = (long)value;
		 }
		
		 BigNumber(int value){
			 this((long)value);
		 }
		 
		 BigNumber(long value){
			 this.sign =  (value < 0) ? -1 : 1;
			 this.bits = new long[1];
			 this.bits[0] = value;
			 this.normalization();
		 }
		 
		 BigNumber(String arg0){
			 this.sign = arg0.startsWith("-") ? -1 : 1;
			 arg0 = arg0.replace("-", "0");
			 this.bits = new long[arg0.length()];
			 for(int i = 0; i < this.bits.length; i++){
				 this.bits[i] = Character.getNumericValue(arg0.charAt(i));
			 }
			 this.bits = fragmentation(this.bits);
			 this.normalization();
		 } 
		 
		 BigNumber(BigNumber arg0){
			 this.bits = new long[arg0.bits.length];
			 this.sign = arg0.sign;
			 for (int i=0; i<arg0.bits.length; i++){
				 this.bits[i] = arg0.bits[i];
			 }
		 }
		 /*
		  * END CONSTRUCTORS
		  */
		 
		 
		/*
		 * ïåðåâîä ÷èñëà â ñèñòåìó ñ÷èñëåíèÿ ïî îñíîâàíèþ  pow(10, base). 
		 * Êîðî÷å ðàçáèâàåò íà ôðàãìåíòû ïî base öèôð
		 * Ðàçâîðà÷èâàåò ÷èñëî, ñòàâÿ ìëàäøèé ðàçðÿä â ïåðâûé ýëåìåíò ìàññèâà
		 */
		private long[] fragmentation(long[] input_number){
			 int size = (int) Math.ceil(input_number.length / (float) BASE);
			 long[] result = new long[size];
			 int point = input_number.length-1;
			
			 for (int i=0; i<size; i++){
				 result[i] = input_number[point];
				 int pow = 1;
				 
				 for(int j=point-1; j > point-BASE && j>=0; j--){
					 result[i] += input_number[j] * Math.pow(10, pow++);
				 }
				 point = point - BASE;
			 }
			 return result;	 
		 }
		
		
		/*
		  *íîðìàëèçàöèÿ ÷èñëà.
		  * ÷èñëî íîðìàëèçóåòñÿ ïî îñíàâèþ pow(10, base)
		  */
		private void normalization(){
			 int divinc = 0; // ïåðåìåííàÿ êîòîðàÿ ñóììèðóåòüñÿ ê ñòàðøåìó ïîðÿäêó, åñëè òåêóùèé ðîçðÿä ÷èñëî î÷àçàëñÿ áîëüøå îñíîâàíèÿ 
			 int divdec = 0; // ïåðåìåííàÿ êîòîðàÿ ñóììèðóåòüñÿ ê ñòàðøåìó ïîðÿäêó, ïðè ñëîæåíèè ÷èñåë, îäíî èç êîòîðûõ îòðèöàòåëüíîå
			 long temp;
			 for (int i = 0; i < this.bits.length; i++){
				 temp = this.bits[i]+ divinc + divdec;
				 if(temp>-1){
					 this.bits[i] = (int) (temp % Math.pow(10, BASE)) ;
					 divinc = (int) (temp/Math.pow(10, BASE));
					 divdec = 0;
				 }
				 if (temp  < 0){
					divdec = -1;
					this.bits[i] =  (int)Math.pow(10, BASE) + temp;
					divinc = 0;
				 }
			 }
		/* 
		 * åñëè ýòà ïåðåìåííàÿ áîëüøå íîëÿ, çíà÷èò íàäî äîáàâèòü åù¸ îäèí ðîçðÿä ÷èñëà è çàïèñàòü òóäà çíà÷åíèå divinc
		 * ÍÀÏÐÈÌÅÐ åñëè äîáàâèòü 9+9  òî â ðåçóëüòàòå áóäåò 8 à 1 ïîéäåò â ñòàðøèé ðîçðÿä ÷èëñà. Â èòîãå ïîëó÷àåòüñÿ 18
		 */	
			 if (divinc > 0){
				 long[] arr = new long[this.bits.length+1]; 
				 System.arraycopy(this.bits, 0, arr, 0, this.bits.length);
				 arr[arr.length-1] = divinc;
				 this.bits = arr;
				 this.normalization();
			 }
			 
			 int cutindex = -1;
			 for (int i =  this.bits.length-1; i > -1 ; i--){
				 if (this.bits[i] > 0){
					 cutindex = i;
					 break;
				 }
			 }
			 if (cutindex > -1){
				 long[] arr = new long[cutindex+1];
				 System.arraycopy(this.bits, 0, arr, 0, arr.length);
				 this.bits = arr;	 
				 }
			 
			 int count = 0;
			 for(int i = this.bits.length-1; i>-1; i--){
				 if (this.bits[i]==0)
					 count++;
				 else
					 break;
			 }
			 if (count>0){
				 long[] tempArr = new long[this.bits.length==count? 1 : this.bits.length-count];
				 System.arraycopy(this.bits, 0, tempArr, 0, tempArr.length);
				 this.bits = tempArr;
			 }
		 }
		
		/* 
		 * ñðàâíèâàåò òåêóùåå áîëüøîå ÷èñëî ñ áîëüøèì ÷èñëîì  arg0
		 * åñëè òåêóùåå áîëüùå arg0 âîçâðàùàåò 1, åñëè ìåíüøå - -1,
		 * åñëè îíè ðàâíû - 0
		 */
		 int compareTo(BigNumber arg0){
			 if (this.sign < arg0.sign){
				 return -1;
			 } else{
				 if(this.sign > arg0.sign){
					 return 1;
				 } 
				//êîãäà çíàêè ÷èñåë ðàâíû
				 else{ 
					 // êîãäà ÷èñëà îòðèöàòåëüíû
					  if (this.sign == -1){    
						  if (this.bits.length > arg0.bits.length){
							  return -1;
						  } else{
							  if (this.bits.length < arg0.bits.length){
								  return 1;
							  } else{
								 for (int i = this.bits.length-1; i>-1; i--){
									 long a1 = this.bits[i];
									 long a2 = arg0.bits[i];
									 if (a1 > a2){
										 return -1;
									 } else if(a1 < a2) return 1;
								 } 
								 return 0; 
							  }
						   }
					  }	
					 // êîãäà ÷èñëà ïîëîæèòåëüíû
					  else { 
						 if (this.bits.length < arg0.bits.length){
							 return -1;
						 } else{	
							 if (this.bits.length > arg0.bits.length){
								 return 1;
							 } else {
								 for (int i = this.bits.length-1; i>-1; i--){
									 long a1 = this.bits[i];
									 long a2 = arg0.bits[i];
									 if (a1 < a2){
										 return -1;
									 } else if(a1 > a2) return 1;
								 } 
								 return 0;
							 } 
					    } 
					  }
				 }
			 }
		 }
		 
		 int compareToByAbs(BigNumber arg0){
			 BigNumber a1 = new BigNumber(this);
			 a1.sign = 1;
			 BigNumber a2 = new BigNumber(arg0);
			 a2.sign = 1;
			 return a1.compareTo(a2);
		 }
		 
		 
		 
		 /*
		  * ñóììèðîâàíèå äâîõ áîëüøèõ ÷èñåë. 
		  * arg0 - ÷èñëî êîòîðîå ñóììèðóåòüñÿ ê òåêóùåìó
		  * ìåòîä âîçâðàùàåò íîâûé îáúåêò êëàññà BigNumber â êîòîðîì è íàõîäèòñÿ ñóììà.
		  */
		 BigNumber add(BigNumber arg0){
			 BigNumber result = new BigNumber(this);
			 if (this.bits.length <  arg0.bits.length){
					return result = arg0.add(this);
				}
			 else{ 
				 if (this.sign == arg0.sign){
						for (int i = 0; i < arg0.bits.length; i++){
							result.bits[i] = this.bits[i] + arg0.bits[i];
						}
				 } 
				 else{
						 if (this.compareToByAbs(arg0) < 0){
							 return result = arg0.add(this);
						 }
						 else{
							 for (int i = 0; i < arg0.bits.length; i++){
								 result.bits[i] = this.bits[i] - arg0.bits[i];								 
							 }
						 }
				 }
				 result.normalization();
				 return result; 
			 }
		 }
		 
		 BigNumber add(int arg0){
			BigNumber result = new BigNumber(this);
			result.bits[0]+=arg0;
			//if (this.bits[0]>Math.pow(10, BASE))
				 result.normalization();
			return result;
		 }
		
		 /*
		  * Âû÷èòàíèå arg0 ñ òåêóùåãî áîëüøîãî ÷èñëà.
		  * Ìåòîä âîçâðàùàåò îáúåêò òèïà BigNumber 
		  */
		 BigNumber subtract(BigNumber arg0){
			 BigNumber temp = new BigNumber (arg0);
			 temp.sign *=-1;
			 return this.add(temp);
		 }
		 
		 BigNumber subtract(int arg0){
			 return this.subtract(new BigNumber(arg0));
		 }
		 
		 /*
		  * Ñäâèã ÷èñëà íà count ðàçðÿäîâ âëåâî áåç ïîòåðè ñòàðøèõ ðàçðÿäîâ.
		  * Ôàêòè÷åñêè óìíîæåíèå íà 10^base*count.
		  */
		 BigNumber shiftLeft(int count){
			 BigNumber result = new BigNumber(this);
			 long[] arr = new long[this.bits.length + count];
			 result.bits = arr;			 
			 System.arraycopy(this.bits, 0, result.bits, count, this.bits.length);
			 return result;
		 }
		 
		 
		 /*
		  * Âûðåçàåò ÷àñòü ðàçðÿäîâ ñ ïîçèöèè start(âêëþ÷èòåëüíî) ïî ïîçèöèþ edn(âêëþ÷èòåëüíî)
		  * ñ ÷èñëà â ôðàãìåíòèðîâàíîì âèäå. Íà÷àëî - ñòàðøèé ðîçðÿä
		  */
		 BigNumber cut(int start, int end){
			 BigNumber result;
			 if(start > end){
				 return result = this.cut(end, start);
			 }
			 else{
				 result = new BigNumber(this);
				 result.bits = new long[this.bits.length - (end - start) - 1];
				 start = this.bits.length - 1 - start;
				 end = this.bits.length - 1 - end;
				 int i_ = 0;
				 int i = 0;
				 while(i<this.bits.length){
					if ((i < end) || (i > start))
						result.bits[i_++] = this.bits[i];
					i++;
				}
				 return result;  
			 }
			
		 }
		 
		 
		 /*
		  * Ìåäîò óâåëè÷èâàåò òåêóùåå ÷èñëî íà 1. 
		  * ìåòîä ÌÎÄÈÔÈÖÈÐÓÅÒ this ÎÁÚÅÊÒ
		  */
		 BigNumber inc(){
			 bits[0]++;
			 if (this.bits[0]>Math.pow(10, BASE))
				 this.normalization();
			 return this;
		 }
		 
		 BigNumber dec(){
			 BigNumber result = new BigNumber(this);
			 result.bits[0]-=1;
			 if (result.bits[0]<0)
			 result.normalization();
			 return result;
		 }
		
		 
		/*
		 * óìíîåíèå òåêóùåãî ÷èñëà íà arg0 ìåòîäîì Êàðàöóáû
		 * XY =  acT^2 + ((a+b)(c+d) - ac - bd)T + bd 
		 * X = aT + b
		 * Y = cT + d
		 */
		 
		 BigNumber multiply(BigNumber arg0){
			 
			 BigNumber result = new BigNumber(); 
			 int sign1;
			 int sign2;
			 sign1 = this.sign;
			 this.sign = 1;
			 sign2 = arg0.sign;
			 arg0.sign = 1;
			 result = this.multi(arg0);
			 result.sign = sign1*sign2;
			 this.sign = sign1;
			 arg0.sign = sign2;
			 return result;
		 }
		 
		
		 
		 private BigNumber multi(BigNumber arg0){
			 
			  int argLen = arg0.bits.length;
			  int thisLen = this.bits.length;
			  BigNumber result = new BigNumber(); 
			  result.bits = new long[thisLen+argLen];
			  result.sign = this.sign == arg0.sign ? 1:-1;
		  
			  if ((thisLen == 1)||(argLen == 1)){
				  if (thisLen < argLen){
					  return result = arg0.multi(this);
				  }
				  else{
					  for (int i = 0; i < thisLen; i++){
						  result.bits[i] = this.bits[i] * arg0.bits[0];
					  }
					  result.normalization();
					  return result;
				  }
			  }
			  else{
				 BigNumber m1 = new BigNumber(this);
				 BigNumber m2 = new BigNumber(arg0);
				 int T = (m1.bits.length+1)/2;
				 BigNumber a = new BigNumber(m1.cut(T, m1.bits.length-1));
				 BigNumber b = new BigNumber(m1.cut(0,T-1));
				 BigNumber c;
				 BigNumber d;
				 if (m2.bits.length < m1.bits.length - T+1){
					  c = new BigNumber("0");
					  d = new BigNumber(m2);
				 }
				 else{
					  int temp = T-(m1.bits.length - m2.bits.length);
					  c = new BigNumber(m2.cut(temp, m2.bits.length-1));
					  d = new BigNumber(m2.cut(0, temp - 1));					 
				 }
				 BigNumber ac = a.multi(c);
				 BigNumber bd = b.multi(d);
				 BigNumber abcd = (a.add(b)).multi(c.add(d));
				 result = ac.shiftLeft((m1.bits.length-T)*2).add(abcd.subtract(ac).subtract(bd).shiftLeft(m1.bits.length-T)).add(bd);
			 }
		 return result;
		 }
		 
		 BigNumber copy(int start, int end){
			 BigNumber result;
			 if(start > end){
				 return result = this.copy(end, start);
			 }
			 else{
				 result = new BigNumber();
				 result.bits = new long[end - start + 1];
				 start = this.bits.length - 1 - start;
				 end = this.bits.length - 1 - end;
				 int i = end;
				 int j = 0;
				 while(i < start+1){
					 result.bits[j++] = this.bits[i++];
				 }
				 return result;
			 }
		 }
		 BigNumber div(BigNumber arg0){
			 return this.division(arg0, false);
		 }
		 
		 BigNumber div(int arg0){
			 return this.div(new BigNumber(arg0));
		 }
		 
		 BigNumber mod(BigNumber arg0){
			 BigNumber result = new BigNumber();
			 result = this.division(arg0, true);
			 result.sign = this.sign;
			 return result;
		 }
		 
		 BigNumber mod(int arg0){
			if (arg0==2){
				return new BigNumber(this.bits[0] & 1);
			}
			 return this.mod(new BigNumber(arg0));
		 }
		 
		 boolean isEqual(BigNumber arg0){
			 return this.compareTo(arg0) == 0 ? true : false;
		 }
		 
		 
		 boolean isEqual(int arg0){
			 if ((arg0==0)&&(this.bits.length==1))
				 return (this.bits[0]== 0 ? true: false);
			 BigNumber temp = new BigNumber(arg0);
			 if ((this.sign == temp.sign)&&(this.bits.length == temp.bits.length)){
				for (int i = 0; i<this.bits.length; i++){
					if (this.bits[i]!=temp.bits[i])
						return false;
				}
				return true;
			 } else return false;

		 }
		 
		// äåëåíèå . Ôëàã óêàçûâàåò ÷òî âåðíóòü, îòñàòîê èëè öåëóþ ÷àñòü.
		 private BigNumber division(BigNumber arg0, boolean flag){
			 
			 BigNumber result = new BigNumber();
			 
			 if (this.sign == arg0.sign){
				 result.sign = 1;
			 } else result.sign = -1;
			 
			 BigNumber divPart = new BigNumber(0);
			 BigNumber modPart = new BigNumber(arg0);
			 modPart.sign = 1;
			 int i = 0;
			 BigNumber partialQuotient = new BigNumber();
			 while(i < this.bits.length){
				 for (int j = i; j < this.bits.length;j++){
					partialQuotient = partialQuotient.shiftLeft(1);
					partialQuotient = partialQuotient.add(this.copy(i++, j));
					divPart = divPart.shiftLeft(1);
				 	if (partialQuotient.compareTo(arg0)>-1){
				 		i = j + 1;
				 		break;
				 	}
				 	
				 }
				 modPart = new BigNumber(partialQuotient);		
				 if (partialQuotient.compareTo(arg0) == -1){
			    	 break;
			     }
			     
				 while (partialQuotient.compareTo(arg0)>-1){
					 partialQuotient = partialQuotient.subtract(arg0);
					 divPart = divPart.add(1);
				 } 
				 modPart = new BigNumber(partialQuotient);
			 }
			 result.bits = divPart.bits;
			 if (flag){
				 return modPart;
			 }
			 else{
				 return result;
			 }
		 }
		 
		 // @Override
		 public String toString(){
			int inc = 0;
			if(this.sign < 0){
				inc = 1;
			}
			StringBuilder result = new StringBuilder(BASE * bits.length+inc);
			if (inc == 1 ){result.append("-");}
			int num;
			for (int i = bits.length-1; i>=0; i--){
			    num = (int)Math.log10(bits[i])+1;
			    if (i!=bits.length-1)
			    if (num < BASE){
			    	for (int j=0; j< BASE - num; j++)
			    		result.append("0");
			    }
			    if (bits[i] == 0)
			    	for (int j=0; j<BASE; j++)
			    	result.append("0");
			    else
				result.append( bits[i] );
			}
			return result.toString(); 
		 }
		 // returns length/number of digits
		 int getLength(){
			 int length = this.bits.length-1;
			 long temp = this.bits[length];
			 int count = 1;
			 while((temp = temp/10)!=0){
				count++; 
			 }
			 return BASE * length + count;
		 }
		 
		 
		 BigNumber pow(int power){
			 BigNumber base = new BigNumber(this);
			 BigNumber result = new BigNumber(1);
			 	while (power != 0){
			 		if (power % 2 !=0){
			 			result = result.multiply(base);
			 			power--;
			 		} 
			 		base = base.multiply(base);
			 		power /= 2;
			 	}
			 return result;
		 }
		 
		 BigNumber powByModule(int power, BigNumber module){
			 BigNumber base = new BigNumber(this);
			 BigNumber result = new BigNumber(1);
			 while ((power!=0)){
				 if (power%2 ==0){
					 power /= 2;
					 base = base.multiply(base).mod(module);
				 } else{ 
					 power--;
					 result = result.multiply(base).mod(module);
				 }
			 }
			 return result;	 
		 }
		 
		 static BigNumber powByModule(int intBase, int power, BigNumber module){
			 BigNumber base = new BigNumber(intBase);
			 BigNumber result = new BigNumber(1);
			 while ((power!=0)){
				 if (power%2 ==0){
					 power /= 2;
					 base = base.multiply(base);
					 if (base.compareTo(module) == 1){
						 base = base.mod(module);
					 }
				 } else{ 
					 power--;
					 result = result.multiply(base);
					 if (result.compareTo(module) == 1){
						 result = result.mod(module);
					 }
				 }
			 }
			 return result;	 
		 }
		 
		 BigNumber powByModule(BigNumber power, BigNumber module){
			 BigNumber base = new BigNumber(this);
			 BigNumber result = new BigNumber(1);
			 while (!power.isEqual(0)){
				 if (power.mod(2).isEqual(0)){
					 power = power.div(2);
					 base = base.multiply(base);
					 if (base.compareTo(module) == 1)
					     base = base.mod(module);
				 } else{ 
					 power = power.dec();
					 result = result.multiply(base);
					 if (result.compareTo(module) == 1)
						result=result.mod(module);
				 }
			 }
			 return result;
			 
		 }
		 static boolean miller_rabin(BigNumber arg0, int raund, int begin){
			 
			 //Êàíîíè÷åêîå ðàçëîæåíèå ÷èñëà arg0 ê âèäó arg0-1 = 2^s*t
			    int s = 0;
			    BigNumber t = arg0.dec();
			    while (t.mod(2).isEqual(0)){
			    	t = t.div(2);
			    	s++;	
			    }
				for (int i = 0; i< raund; i++){
					BigNumber a = new BigNumber(i+begin);
					BigNumber x = a.powByModule(t, arg0);;
		    		if (x.isEqual(1)||x.isEqual(arg0.subtract(1)))
		    			continue;
		    			for (int j = 0; j < s - 1; j++){
		    				x = x.powByModule(2, arg0);
		    				if (x.isEqual(1))
		    					return false;
		    				if (x.isEqual(arg0.dec()))
		    					break;
		    			}
		    			if (!x.isEqual(arg0.dec()))
		    			return false;
		    	}
			 return true;
		 }
		 
		 
		 static BigNumber getRandomBigNumber(BigNumber arg0){
			 	BigNumber temp = new BigNumber(arg0.subtract(1));
			 	int length = temp.bits.length;
			 	Random rnd = new Random();
			 	temp.bits[temp.bits.length-1] = rnd.nextInt((int)temp.bits[temp.bits.length-1]);
			 	for (int i = length-2; i >0; i--){
			 			do{
			 				temp.bits[i] = rnd.nextInt((int) Math.pow(10, BASE)-1);
			 			}while (temp.compareTo(arg0)>0);
			 		}
			 	temp.bits[0] |= 1;
			 	if (temp.compareTo(arg0)!=-1)
			 		temp = temp.subtract(2);
			 return temp;
		 }
		 
		 static BigNumber getPrimeNumber(int bitLength){
			 BigNumber standart = new BigNumber(new BigNumber(2).pow(bitLength));
			 BigNumber test = BigNumber.getRandomBigNumber(standart);
			 while(true){
				 while(!BigNumber.miller_rabin(test, 1,3)){
				 System.out.println(test);
					 test = test.subtract(2);
				 }
				 if (BigNumber.miller_rabin(test, 4,4)){
					 return test;
				 }
				 else test  = test.subtract(2);
			 }
		 }
		 
}
