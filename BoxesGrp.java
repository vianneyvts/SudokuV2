public class BoxesGrp {

	public String type;
	public int index;
	public int completion;

	public BoxesGrp(){
		this.type = "row";
		this.index = 0;
		this.completion = 0;
	}

	public BoxesGrp(String t, int i, int c){
		this.type = t;
		this.index = i;
		this.completion = c;
	}

	public String toString(){
		String res = "";
		res += "the "+type+" "+index+" has "+completion+" filled boxes";
		return res;
	}
}