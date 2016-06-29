import java.io.Serializable;

public class Keys implements Serializable
{
	private static final long serialVersionUID = 1L;
	BigNumber N;
	BigNumber KB;
	
	Keys(BigNumber n, BigNumber kb){
		this.N = n;
		this.KB = kb;
	}
	
	public String toString(){
		return "N = " + this.N + " | KB1 = " + this.KB;
	}
}