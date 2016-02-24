
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
 
import edu.stanford.nlp.ling.HasWord; 
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

class DependencyParserDemo {

  /** This example shows a few more ways of providing input to a parser.
   *
   *  Usage: ParserDemo2 [grammar [textFile]]
   */
  public static void main(String[] args) throws IOException {
    String grammar = args.length > 0 ? args[0] : "edu/stanford/nlp/models/lexparser/englishPCFG.caseless.ser.gz";
    String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
    LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
    TreebankLanguagePack tlp = lp.getOp().langpack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

    Iterable<List<? extends HasWord>> sentences;
    if (args.length > 1) {
      DocumentPreprocessor dp = new DocumentPreprocessor(args[1]);
      List<List<? extends HasWord>> tmp =
              new ArrayList<>();
      for (List<HasWord> sentence : dp) {
        tmp.add(sentence);
      }
      sentences = tmp;
    } else {
      // Showing tokenization and parsing in code a couple of different ways.
      String[] sent = { "This", "is", "an", "easy", "sentence", "." };
      List<HasWord> sentence = new ArrayList<>();
      for (String word : sent) {
        sentence.add(new Word(word));
      }
      /**
       * 1
       */
//      String[] sent2 = { "The", "book", "written", "by","my","mother","tells","me","that","I","need","to","be","a","man", "." };
//      String[] tag2 = { "DT", "NN", "VBN", "IN","PRP$", "NN", "VBZ", "PRP","WDT","PRP", "VBP", "TO", "VB", "DT", "NN", "." };
//      List<TaggedWord> sentence2 = new ArrayList<>();
//      for (int i = 0; i < sent2.length; i++) {
//        sentence2.add(new TaggedWord(sent2[i], tag2[i]));
//      }

//      String sent2 = ("This is a slightly longer and more complex " +
//                      "sentence requiring tokenization.");
     // String sent2 = ("I want to play basketball with these children.");
      //String sent2 = ("The book written by his mother tells that Mr Mit often wants to play games with some little kids who live in the street.");
   //  String sent2="I hit the girl with long hair with a hammer.";
   //  String sent2="The boy playing basketball there is my son.";
    //String sent2="I like playing basketball and watching TV.";
      // Use the default tokenizer for this TreebankLanguagePack
      //String sent2="I come up with an idea.";
     // String sent2="Consumers are starting to realize that the products they buy on the Internet are just as good as those in the shopping malls.";
      //String sent2="Your contribution to Goodwill will mean more beautiful than you may know .";
     // String sent2="The boy sitting there with his hands crossed is one of my brothers.";
     // String sent2="There are many apples, pines and so on.";
    //String sent2="The man playing piano with a key is talking with Jesen with long hair in a blue shirt for 100 yuan. ";
    // String sent2="I want to give the boy with a telescope which is made in China and is worth 100 yuan who is from China a book the beautiful girl bought for him written by my mother.";
     // String sent2="The boy kicking basketball with his sister there in a shirt is my son . ";
      String sent2="that he is a good man is a truth.";
      Tokenizer<? extends HasWord> toke =
        tlp.getTokenizerFactory().getTokenizer(new StringReader(sent2));
      List<? extends HasWord> sentence2 = toke.tokenize();
      
      Tree parsee = lp.parse(sentence2);
      parsee.pennPrint();
     

      String[] sent3 = { "It", "can", "can", "it", "." };
      String[] tag3 = { "PRP", "MD", "VB", "PRP", "." }; // Parser gets second "can" wrong without help
      List<TaggedWord> sentence3 = new ArrayList<>();
      for (int i = 0; i < sent3.length; i++) {
        sentence3.add(new TaggedWord(sent3[i], tag3[i]));
      }
      Tree parse = lp.parse(sentence3);
      parse.pennPrint();

      List<List<? extends HasWord>> tmp =
              new ArrayList<>();
      tmp.add(sentence);
      tmp.add(sentence2);
      tmp.add(sentence3);
      sentences = tmp;
    }

    for (List<? extends HasWord> sentence : sentences) {
      Tree parse = lp.parse(sentence);
     // parse.pennPrint();
   //   System.out.println();
      GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
      List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
      System.out.println(tdl);
      System.out.println();

//      System.out.println("The words of the sentence:");
//      for (Label lab : parse.yield()) {
//        if (lab instanceof CoreLabel) {
//          System.out.println(((CoreLabel) lab).toString(CoreLabel.OutputFormat.VALUE_MAP));
//        } else {
//          System.out.println(lab);
//        }
//      }
//      System.out.println();
//      System.out.println(parse.taggedYield());
//      System.out.println();

    }

    // This method turns the String into a single sentence using the
    // default tokenizer for the TreebankLanguagePack.
//    String sent3 = "This is one last test!";
//    lp.parse(sent3).pennPrint();
  }

//  private ParserDemo2() {} // static methods only

}
