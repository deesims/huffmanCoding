import java.util.Arrays;
import java.util.Iterator;


public class HuffmanTree {

 public static int EndOfText=((int)'\uffff')+1; //special symbol created to indicate end of text

 HuffmanNode root=null; // root of the Huffman tree
 HuffmanNode[] leafWhereLetterIs;   // array indexed by characters, storing a reference to
 									// the Huffman Node (leaf) in which the character is stored


 // Constructor receives frequency information which is used to call BuildTree
 public HuffmanTree (LetterFrequencies letterFreq) {

	 root=BuildTree (letterFreq.getFrequencies(),letterFreq.getLetters());

 }

 // BuildTree builds the Huffman tree based on the letter frequencies

 private HuffmanNode BuildTree(int[] frequencies,char[] letters) {


	HeapPriorityQueue<HuffmanNode, HuffmanNode> heap =
			new HeapPriorityQueue<HuffmanNode, HuffmanNode>(frequencies.length+1);

    // initialize array leftWhereLetterIs
	leafWhereLetterIs =new HuffmanNode[(int)'\uffff'+2]; // need 2^16+1 spaces
	for (int i=0; i< (int)'\uffff'+2; i++)
		leafWhereLetterIs[i]=null;

	for (int i=0; i<frequencies.length; i++) {
		if (frequencies[i]>0) {
			HuffmanNode node= new HuffmanNode( (int)letters[i], frequencies[i],null,null,null);
			leafWhereLetterIs[(int)letters[i]]=node;
			heap.insert(node,node);
		}
	}
	// creating node for "EndOfText" special symbol
	HuffmanNode specialNode= new HuffmanNode( EndOfText,0,null,null,null);
	leafWhereLetterIs[EndOfText]=specialNode; // last position reserved
	heap.insert(specialNode,specialNode);



	while(heap.size() > 1){
		Entry<HuffmanNode, HuffmanNode> e1 = heap.removeMin();
		Entry<HuffmanNode, HuffmanNode> e2 = heap.removeMin();
		int newFreq = e1.getValue().getFrequency() + e2.getValue().getFrequency();
		HuffmanNode newT = new HuffmanNode('\0', newFreq, null, e1.getValue(), e2.getValue());
		e1.getValue().setParent(newT);
		e2.getValue().setParent(newT);
		heap.insert(newT, newT);
	}

	Entry<HuffmanNode, HuffmanNode> e = heap.removeMin();


	return e.getValue();

 }

private String encodeCharacter(int c) {

	String order = "";
	HuffmanNode charLeaf = leafWhereLetterIs[c];
	HuffmanNode previousNode = charLeaf;
	HuffmanNode current = previousNode.parent();

	while (previousNode != root){
		if(current.leftChild() == previousNode){
			order = "0" + order;
			previousNode = current;
			current = current.parent();
		} else if (current.rightChild() == previousNode){
			order = "1" + order;
			previousNode = current;
			current = current.parent();
		}
	}
	return order;

 }

 public void encodeCharacter (int c, BitFeedOut bfo) {
	 String s=encodeCharacter(c);
	 for (int i=0; i< s.length();i++) bfo.putNext(s.charAt(i));

 }


public int decodeCharacter(Iterator<Byte> bit) {

	 if (root == null) return Integer.MAX_VALUE; // empty tree is not valid when decoding

	 HuffmanNode current = root;
	 while (bit.hasNext()){
		 int value = bit.next().intValue();
		 if (value == 1){
			 if (current.rightChild() != null){
				 current = current.rightChild();
				 if (current.isLeaf()){
					 return current.getLetter();
				 }
			 }
		 } else if (value == 0){
			 if (current.leftChild() != null){
				 current = current.leftChild();
				 if (current.isLeaf()){
					 return current.getLetter();
				 }
			 }
		 }
	 }

	 return current.getLetter();

 }



 // auxiliary methods for printing the codes in the Huffman tree

 void printCodeTable() {
	 System.out.println("**** Huffman Tree: Character Codes ****");
	 if (root!=null)
		 traverseInOrder(root,""); // uses inorder traversal to print the codes
	 else
		 System.out.println("No character codes: the tree is still empty");
	 System.out.println("***************************************");

 }

 // In-order traversal of the Huffman tree keeping track of
 // the paths to leaves so it can print the codeword for each letter
 private void traverseInOrder(HuffmanNode current, String c) {
	 if (current.isLeaf()) {
		if (current.getLetter()!=EndOfText)
		       System.out.println((char)current.getLetter()+":"+c);
		else   System.out.println("EndOfText:"+c);
	 }
	 else {
		 traverseInOrder(current.leftChild(),c+"0");
		 traverseInOrder(current.rightChild(),c+"1");
	 }

 }


 byte[] freqsToBytes() {
    int b=0;
	byte [] treeBytes= new byte[(int)'\uffff'*4];
    for (int i=0;i<'\uffff';i++) {
		if (leafWhereLetterIs[i]!=null) {
			int freq=leafWhereLetterIs[i].getFrequency();
			char letter=(char)leafWhereLetterIs[i].getLetter();
			treeBytes[b++]= (byte)(((int)letter)/256);
			treeBytes[b++]= (byte)(((int)letter)%256);
			treeBytes[b++]= (byte)(freq/256);
			treeBytes[b++]= (byte)(freq%256);
		}
	}
    return Arrays.copyOf(treeBytes, b);
 }

 	public class HuffmanNode implements Comparable<HuffmanNode> {

		int letter; // if the node is a leaf it will store a letter, otherwise it store null
	    int frequency; // stores the sum of the frequencies of all leaves of the tree rooted at this node
		private HuffmanNode parent, left, right; // reference to parent, left and right nodes.

		public HuffmanNode() {
			parent=left=right=null;
			frequency=-1;
		}

		public HuffmanNode(int letter, int frequency, HuffmanNode parent, HuffmanNode left, HuffmanNode right) {
			this.letter= letter;
			this.frequency=frequency;
			this.parent=parent;
			this.left=left;
			this.right=right;
		}


		boolean isLeaf() { return (left==null && right==null);}

		// getter methods

		HuffmanNode leftChild() { return left;}

		HuffmanNode rightChild() { return right;}

		HuffmanNode parent() { return parent;}

		int getLetter() {return letter;}

		int getFrequency() {return frequency;}

		// setter methods

		void setLeftChild(HuffmanNode leftVal) { left=leftVal;	}

		void setRightChild(HuffmanNode rightVal) { right=rightVal;	}

		void setParent(HuffmanNode parentVal) { parent=parentVal;	}

		void setLetter(char letterVal) { letter = letterVal;}

		void setFrequency(int freqVal) { frequency = freqVal; }

		@Override
		public int compareTo(HuffmanNode o) {
			if (this.frequency==o.frequency) {
				return this.letter-o.letter;
			}
			else return this.frequency-o.frequency;

		}

	}



}
