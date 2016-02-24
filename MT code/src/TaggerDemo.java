 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

class TaggerDemo {

  private TaggerDemo() {}

  public static void main(String[] args) throws Exception {
	  
	/*  wordReduction wordreduction=new wordReduction();
		String code = null;
		try {
			code =wordreduction. codeString("dic//dic_ec.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		wordreduction.readTxtFile("dic//dic_ec.txt",code);
		//readTxt.printData();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
       
		while(true)
		{ 
			String word = null;
			try {
				word=br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wordreduction.OriginalWordAndProperty(word);
		}*/
	  
//    if (args.length != 2) {
//      System.err.println("usage: java TaggerDemo modelFile fileToTag");
//      return;
//    }
//    MaxentTagger tagger = new MaxentTagger(args[0]);
//    List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(args[1])));
//    for (List<HasWord> sentence : sentences) {
//      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
//      System.out.println(Sentence.listToString(tSentence, false));
//    }
		//String a = "Sentence Boundary Detection is widely used but often with outdated tools.";
		//String a = "We discuss what makes it difficult, which features are relevant, and present a fully statistical system, now publicly available, that gives the best known error rate on a standard news corpus.";
	 // String a = "A distributed system is a collection of autonnomous computing elements that appears to its user as a single coherent system.";
	  //String a = "He comes up wth an ideal.";
	  //String a = "He plays the guitar as well as you. ";
	 // String a="The lady over there and her friend know the son of the director of the company.";
	 // String a="The car is his.";
	 //String a ="John will go to France and Mary may go to France.";
	 // String a="What you have and which features you like decide who you are!";
	//  String a="The Apple is so beautiful.";
	  //String a="The boy is so strong,so quick,so fast.";
	 // String a="Light travels faster than sound.";
	  //String a="My mother is a beautiful lady.";
//	  String a="We took the picture last year.";
	//  String a="Besides,we have other things to consider.";
	 //String a="We are going too slowly.";
	//  String a="His eyes are wide open.";
	 // String a="He could speak english as fluently as me.";
	//  String a="All boys like playing basketball. "; 
	 // String a="It was quite a nice day.";
	// String a="I need an ice cream.";
	  //  String a="Peter's mother is so beautiful.";
	//String a="The flower that is on the table is my sister's.";  
	  //String a="What an interesting thing! That girl is my sister. I do not know if I am right. It is not correct that he knows this.";
	//  String a="The book which is written by his mother is so great.";
	//  String a="Repeat these steps until none are left.";
	  //String a="I like this boy whose face is full of flowers.";
	//  String a="He likes playing basketball.";
	 //String a="It's not very fair to blame me for that.";
	// String a="The court is 9 of the few institutions in which people have to do some elite thinking.";
	//  String a="I can not tell you if he is my father.";   //???????
	 // String a="The book written by my mother tells me that I need to be a man.";
	//  String a="The boy tell him that the boy who is over there is my son.";
	 // String a="They came to tell us how you met with difficulties. ";
	  //  String a="This is the book I bought on Friday.";
	 //String a="Consumers are starting to realize that the products they buy on the Internet are just as good as those in the shopping malls. "; 
	  //String a="The United States is not as great as I thought. ";
	  
	  HashMap<String, String> fixedPhrase=new HashMap<String, String>();
	    Set<String> fixedPhraseCombination=new HashSet<String>();
		 HashMap<String, String> nounsurfaceSemantic=new HashMap<String, String>();
	  DicPrepare dic=new DicPrepare();
	  dic.readTxtFile("dic//wordattr.txt", "UTF-8", 3);
	  nounsurfaceSemantic=dic.nounsurfaceSemantic;
	  dic.readTxtFile("dic//phrasecombination.txt", "UTF-8",2);
	  fixedPhraseCombination=dic.fixedPhraseCombination;
	  //String a="I like the book written by my mother because it is very interesting.";
	//  String a="I want to give the boy some books.";
		//String a="He often studys all by himself. The book's eye is his."; 
	//  String a="That he says that he would kill the dog one day sounds interesting.";
	 // String a="After all, any regulation is only as good as the sincerity and success of its implementation.";
	//////  String a="Given another book, he can study well.";
	//   String a="The formation of The Community Academy a pilot project directed at expelled students from local school systems has come about as a result of MCCOY's efforts to mobilize community agencies in response to a pressing issue .";
	 // String a = "There have been some broken glasses here.";
//	 / String a="The smiling boy wants the pen bought by his mother.";
	 // String a="I can not catch up with him.";
 	//  String a="There are a lot of lions and an extremely huge number of dogs and an extremely huge number of dogs .";
	  //String a="The man who plays basketball with one finger is talking with my friend with long hair in a blue shirt .";
	 // String a="I want to give the boy with a telescope which is made in China and is worth 100 yuan who is from China a book the beautiful girl bought for him written by my mother .";
	  // String a="The place which is beautiful in which I used to live is quite interesting .";
	 //   String a="The place built by my father in which I used to live is quite interesting .";
	//   String a="I love The boy in a shirt .";
	//  String a="I like the boy kicking basketball with his sister there in a shirt .";
	 // String a="a book written by my mother .";
	     String a="I want to give the boy with a telescope which is made in China and is worth 100 yuan  who is from China a book the beautiful girl buys for him written by my mother .";
	//  String a="I want to tell the boy my mom is the most beautiful woman in the world .";
	 // String a="I tell you the man is humorous in his country .";
	  //   String a="I know that the boy is yours .";
	//*  String a="The idea that you can do this work well without thinking is quite wrong.";
	 // String a="I believe in my broher is right .";
	  //String a="The truth is my brother is right .";
	// String a ="I put the drug where children can not reach .";
	  MaxentTagger tagger =  new MaxentTagger("D:\\Workspace\\MT\\models\\english-bidirectional-distsim.tagger");
	  String tagged = tagger.tagString(a);
	  /*
	   * 固定词组的处理
	   */

	    System.out.println(tagged);
		PhraseMerge merge=new PhraseMerge();
		//merge.Preprocessing("The_DT boy_NN is_VBZ so_RB strong_JJ ,_, so_RB quick_JJ ,_, so_RB fast_JJ ._. ");
		merge.Preprocessing(tagged," ");
		//merge.VerbPrepFind();
		
		List<String> mergewords=new ArrayList<String>();
		List<String> mergepos=new ArrayList<String>();
		List<String> mergemainpos=new ArrayList<String>();
		List<String> mergemainwords=new ArrayList<String>();
		String  tags=merge.getPos();
		String sen=merge.getSentence();
		mergewords=merge.getmergewords();
		mergepos=merge.getmergepos();
		mergemainpos=merge.getmergemainpos();
		
      	merge.print();	

        FindFixedPhraseandPrepmatch fix=new FindFixedPhraseandPrepmatch();
        fix.show();
        
		fix.transfer(tags,sen, (String[])mergepos.toArray(new String[mergepos.size()]),(String[])mergewords.toArray(new String[mergewords.size()]),(String[])mergemainpos.toArray(new String[mergemainpos.size()]));
		
		tagged=fix.getSentencePOS();
		merge.clear();
		System.out.println(tagged);
		merge.Preprocessing(tagged,"\t");
		merge.print();
		
		mergewords=merge.getmergewords();
		mergepos=merge.getmergepos();
		mergemainpos=merge.getmergemainpos();
		mergemainwords=merge.getmergemainwords();
		
		fix.getAllClauses1(-1,0,null,null,null,(String[])mergemainwords.toArray(new String[mergemainwords.size()]),(String[])mergemainpos.toArray(new String[mergemainpos.size()]),(String[])mergewords.toArray(new String[mergewords.size()]),(String[])mergepos.toArray(new String[mergepos.size()]));
		
		List<String> words=new ArrayList<String>();
		words.add("cat");words.add("scratch");words.add("cat");words.add("with");words.add("claw");words.add("with");words.add("claw");

		List<String> tagss=new ArrayList<String>();
		tagss.add("NN");tagss.add("VB");tagss.add("NN");tagss.add("IN");tagss.add("NN");tagss.add("IN");tagss.add("NN");

		fix.getSimplePrepModifysequence((String[])words.toArray(new String[words.size()]),(String[])tagss.toArray(new String[tagss.size()]));
		
		
		
//		merge.findSurfaceSemanticStructure(nounsurfaceSemantic);
//		List<String>  surfaceSemanticStructure=merge.surfaceSemanticStructure;
//		merge.sentenjudge(surfaceSemanticStructure,fixedPhraseCombination,nounsurfaceSemantic);
		 
  } 
}
