

public class TestHuffmanWithStrings {

	public static void main(String[] args) {

		String  codedText, decodedText;
		LetterFrequencies fq;

		String [] textsToTest = { // add more strings to create more test cases
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!@#$%^&*()1234567890-=",
				"Does a man know what a man's dream is ?",
				"Foobar went to a bar and ordered a foobar !",
				"@mississippi wanted to know what your # is ?",
				"And last test...1234...any string should work"
		};

		for (String plainText: textsToTest) {
			System.out.println(">>>> Begining Encoding:\n" + plainText);
			codedText= testStringEncode(plainText); // encodes plainText into codedText
			System.out.println("Bits in coded text:\n"+ codedText);
			System.out.println("\n>>>> Begining Decoding:\n" + codedText);
			fq=new LetterFrequencies(plainText);
			decodedText=testStringDecode(codedText,fq); // decoded codedText into decodedText
			System.out.println("Letters in decoded  text:\n"+decodedText);
			if (plainText.equals(decodedText)) // plainText must match decodedText
				System.out.println("RESULT: Correctly encoding-decoding!\n");
			else {
				System.out.println("WRONG: incorrect encoding-decoding");
				System.out.println("Letters in original text:\n"+plainText);
				System.out.println("Letters in decoded  text:\n"+decodedText);
			}

		}

	}

	// This is the encoding of a String of characters using Huffman encoding
	// It test several funcionalities of HuffmanTree
	// It returns a String containing the sequence of bits of the encoding

	public static String testStringEncode(String inputText) {

		LetterFrequencies lf = new LetterFrequencies(inputText); // compute frequencies of letters

		// populate the frequency list
		HuffmanTree huffTree= new HuffmanTree(lf); // create Huffman tree based on letter frequencies

		huffTree.printCodeTable(); //print letters and their Huffman encoding

		BitFeedOutForString outSeq= new BitFeedOutForString();
		// create outSeq (which has interface BitFeedOut) that will receive bits during encoding
		for (int i=0; i< inputText.length();i++)
			huffTree.encodeCharacter(inputText.charAt(i),outSeq); // each character is encoded and sent to outSeq

		huffTree.encodeCharacter(HuffmanTree.EndOfText,outSeq); // the special EndOfText character is encoded at put at the end

		String codedText = outSeq.output(); // obtain codedText from outSeq

        return codedText; // return coded text
	}

	// This is the encoding of a String of characters using Huffman encoding
	// It test several funcionalities of HuffmanTree
	// It returns a String containing the sequence of bits of the encoding

	public static String testStringDecode(String codedText, LetterFrequencies lf) {

		// populate the frequency list
		HuffmanTree huffTree= new HuffmanTree(lf); // using the same know frequencies to create tree (so tree will be the same)
		 										   // Huffman encoding with files would have to
												   // read the frequence info from the header of the encoded file

		BitFeedInForString seq=new BitFeedInForString(codedText); // create BitFeedIn to send encoded bits one by one

		StringBuilder decodedText=new StringBuilder();
		while (seq.hasNext()) {
			 int symbol=huffTree.decodeCharacter(seq); // decode the next character by using a few bits of seq
			 if (symbol == Integer.MAX_VALUE)
				 break;
			 if (symbol!=HuffmanTree.EndOfText)
			     decodedText.append((char) symbol); // keep collecting the decoded characters
			 else break;
		}

		return decodedText.toString(); // return the decoded string
	}


}
