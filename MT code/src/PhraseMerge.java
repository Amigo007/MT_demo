import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.W3CDomHandler;

import edu.stanford.nlp.ling.Word;


public class PhraseMerge {
	public PhraseMerge() {
		// TODO Auto-generated constructor stub
	}
	
	private String[] words;//用来记录单词
	private String[] Pos;//用来记录每个单词对应的词性
	public String getPos()
	{
		String pos="";
		for(int i=0;i<Pos.length;i++)
		{
			pos=pos+Pos[i]+" ";
		}
		return pos;
	}
	
	public String getSentence() {
		String sen="";
		for(int i=0;i<words.length;i++)
		{
			sen=sen+words[i]+" ";
		}
		return sen; 
	}
	
	/**
	 * 预处理，将词性标注的词进行分割，并分别进行存储
	 * @param str
	 */
	public void Preprocessing(String str,String cutmark)
	{
		//以下操作是把词性和原词区分开来
		String[] wordsPOS=str.split(cutmark);
		words=new String[wordsPOS.length];
		Pos=new String[wordsPOS.length];
		for(int i=0;i<wordsPOS.length;i++)
		{
			 String[] wordPos=wordsPOS[i].split("_");
			 words[i]=wordPos[0];
			 Pos[i]=wordPos[1];
		}  
		String[] copywords=words;
		String[] copypos=Pos;
		//对一些可以合并的短语进行合并，例如形容词+名词就可以进行简单的合并
		Merge(0,copywords,null,copypos,null);
	}
 
	/** CC Coordinating conjunction 并列连词
		CD Cardinal number 基数
		DT Determiner 限定词
		EX Existential there  there 存在句
		FW Foreign word 外来词
		IN Preposition or subordinating conjunction 介词；前置词
		JJ Adjective  形容词
		JJR Adjective, comparative 形同词，比较级
		JJS Adjective, superlative 形容词最高级
		LS List item marker 设定列表项标志
		MD Modal 模态，情态动词
		NN Noun, singular or mass 单数
		NNS Noun, plural 名词，复数
		NNP Proper noun, singular 专有名词
		NNPS Proper noun, plural 专有名词，复数
		PDT Predeterminer 前限定词
		POS Possessive ending 所有格结束词
		PRP Personal pronoun 人称代名词 he,she,it
		PRP$ Possessive pronoun 所有格代名词 his,her
		RB Adverb 副词
		RBR Adverb, comparative 副词，比较级
		RBS Adverb, superlative 副词，最高级
		RP Particle 助词
		SYM Symbol 符号
		TO to  to
		UH Interjection 感叹词
		VB Verb, base form  动词基本形式
		VBD Verb, past tense 动词过去式
		VBG Verb, gerund or present participle 动名词
		VBN Verb, past participle 过去分词
		VBP Verb, non-3rd person singular present 非第三人称单数现在时
		VBZ Verb, 3rd person singular present 第三人称单数现在时
		WDT Wh-determiner Wh限定词
		WP Wh-pronoun 所有格的代词
		WP$ Possessive wh-pronoun  所有格形容词代词
		WRB Wh-adverb Wh开头的副词*/	
	/*
	 * mergewords是进行简单合并之后的单词组合
	 * mergepos是进行简单合并之后的pos词性组合
	 * mergemainpos是合并后只保留了最重要的一个词的词性，类似于head word
	 */
	List<String> mergewords=new ArrayList<String>();
	List<String> mergepos=new ArrayList<String>();
	List<String> mergemainpos=new ArrayList<String>();
	List<String> mergemainwords=new ArrayList<String>();
	public List<String> getmergewords()
	{
		return mergewords;
	}
	public List<String> getmergepos()
	{
		return mergepos;
	}
	public List<String> getmergemainpos()
	{
		return mergemainpos;
	}
	public List<String> getmergemainwords() {
		return mergemainwords;
	}
	/**
	 * 第二步
	 * @param change 判断当前是否还有可以进行合并的简单项，如果有，则进行，否则就退出
	 * @param words 对当前的单词进行组合
	 * @param pos 对当前的pos进行组合，只保留重要的词性部分，其余部分丢弃
	 */
	public void Merge(int change,String[] words,String[] wordstmp,String[] pos,String []postmp)
	{
//		for(int i=0;i<words.length;i++)
//		{
//			System.out.print(words[i]+" , ");
//		}
//		System.out.println();
//		for(int i=0;i<pos.length;i++)
//		{
//			System.out.print(pos[i]+" , ");
//		}
//		System.out.println();
//		if(postmp!=null)
//		for(int i=0;i<postmp.length;i++)
//		{
//			System.out.print(postmp[i]+" , ");
//		}
//		System.out.println();
		int cnt=0;
		int len=pos.length;
		List<String> tmpwords=new ArrayList<String>();
		List<String> tmpmainwords=new ArrayList<String>();
		List<String> tmppos=new ArrayList<String>();
		
		List<String> tmpmergepos=new ArrayList<String>();
		while(cnt<=len-1)
		{
			if(cnt<len-1)
			{
				if(pos[cnt].equals("WRB")&&pos[cnt+1].equals("JJ")) //如how much，how great之类的词
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]);
					tmpmainwords.add(words[cnt+1]);
					if(postmp==null)
					tmppos.add(pos[cnt]+" "+pos[cnt+1]);
					else {
						tmppos.add(postmp[cnt]+" "+postmp[cnt+1]);
					}
					tmpmergepos.add("JJ");///???????
					cnt=cnt+2;
					change=1;
				}
				else if(pos[cnt].charAt(0)=='M'&&pos[cnt+1].equals("VB"))
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]);
					tmpmainwords.add(words[cnt+1]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]);
						} 
					tmpmergepos.add(pos[cnt+1]);
					cnt=cnt+2;
					change=1;
				}
				else if(pos[cnt].charAt(0)=='R'&&(pos[cnt+1].charAt(0)=='C'||pos[cnt+1].charAt(0)==',')&&pos[cnt+2].charAt(0)=='R')//R&R , R,R 副词+连词+副词， 副词+“，”+副词
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]+" "+words[cnt+2]); 
					tmpmainwords.add(words[cnt+2]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]+" "+pos[cnt+2]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]+" "+postmp[cnt+2]);
						}
					tmpmergepos.add(pos[cnt+2]);
					cnt=cnt+3;
					change=1;
				}
				else if(pos[cnt].charAt(0)=='J'&&(pos[cnt+1].charAt(0)=='C'||pos[cnt+1].charAt(0)==',')&&pos[cnt+2].charAt(0)=='J')//JJ+CC/.+JJ  ;  RB+CC/.+RB 形容词+连词+形容词，形容词+","+形容词
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]+" "+words[cnt+2]); 
					tmpmainwords.add(words[cnt+2]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]+" "+pos[cnt+2]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]+" "+postmp[cnt+2]);
						}
					tmpmergepos.add(pos[cnt+2]);
					cnt=cnt+3;
					change=1;
				}
				else if(pos[cnt].charAt(0)=='R'&&pos[cnt+1].charAt(0)=='J') //R+J 副词+形容词
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]); 
					tmpmainwords.add(words[cnt+1]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]);
						}
					tmpmergepos.add(pos[cnt+1]);
					cnt=cnt+2;
					change=1;
				}
				else if(pos[cnt].charAt(0)=='J'&&pos[cnt+1].charAt(0)=='N') //J+N 形容词+名词  
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]); 
					tmpmainwords.add(words[cnt+1]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]);
						}
					tmpmergepos.add(pos[cnt+1]);
					cnt=cnt+2;
					change=1;
				}
				else if((pos[cnt].equals("PDT")||pos[cnt].equals("DT")||pos[cnt].equals("CD")||pos[cnt].equals("WDT"))&&pos[cnt+1].charAt(0)=='N')//DT/数词/That+NN  冠词+名词
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]); 
					tmpmainwords.add(words[cnt+1]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]);
						}
					tmpmergepos.add(pos[cnt+1]);///?????
					cnt=cnt+2; 
					change=1;
				} 
				else if((pos[cnt].equals("PDT")||pos[cnt].equals("DT")||pos[cnt].equals("CD")||pos[cnt].equals("WDT"))&&( pos[cnt+1].equals("VBN")||pos[cnt+1].equals("VBG") ) && (pos[cnt+2].charAt(0)=='N') )//DT/CD+动名词/动词分词+名词
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]+" "+words[cnt+2]); 
					tmpmainwords.add(words[cnt+2]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]+" "+pos[cnt+2]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]+" "+postmp[cnt+2]);
						}
					tmpmergepos.add(pos[cnt+2]);///?????
					cnt=cnt+3; 
					change=1;
				} 
				else if(pos[cnt].charAt(0)=='N'&&pos[cnt+1].equals("POS")) //名词+'s
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]); 
					tmpmainwords.add(words[cnt+1]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]);
						}
					tmpmergepos.add(pos[cnt+1]);
					cnt=cnt+2;
					change=1;
				}
				else if(pos[cnt].equals("POS")&&pos[cnt+1].charAt(0)=='N') //‘s + 名词
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]); 
					tmpmainwords.add(words[cnt+1]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]);
						}
					tmpmergepos.add(pos[cnt+1]);
					cnt=cnt+2;
					change=1;
				}
//				else if(pos[cnt].charAt(0)=='N'&&pos[cnt+1].charAt(0)=='N'&&postmp[cnt+1].charAt(0)=='P'&&postmp[cnt+1].charAt(1)=='O') //新加的NN+‘+NN，例如mother's bag
//				{
//					tmpwords.add(words[cnt]+" "+words[cnt+1]); 
//					tmppos.add(pos[cnt]+" "+pos[cnt+1]);
//					tmpmergepos.add(pos[cnt+1]);
//					cnt=cnt+2;
//					change=1;
//				}
				else if(pos[cnt].equals("PRP$")&&pos[cnt+1].charAt(0)=='N') //PRP$+N  his/her+名词
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]); 
					tmpmainwords.add(words[cnt+1]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]);
						}
					tmpmergepos.add(pos[cnt+1]);
					cnt=cnt+2;
					change=1;
				}
 
				else if(pos[cnt].charAt(0)=='N'&&(postmp==null||postmp[cnt+1].charAt(0)=='N')&&(pos[cnt+1].charAt(0)=='N'))//名词+名词当且仅当后一个名词前没有任何修饰的词，如冠词，形容词等
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]); 
					tmpmainwords.add(words[cnt+1]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]);
						}
					tmpmergepos.add(pos[cnt+1]);
					cnt=cnt+2;
					change=1;
				}
				else if(pos[cnt].charAt(0)=='R'&&pos[cnt+1].charAt(0)=='V') //副词+动词
				{
					tmpwords.add(words[cnt]+" "+words[cnt+1]); 
					tmpmainwords.add(words[cnt+1]);
					if(postmp==null)
						tmppos.add(pos[cnt]+" "+pos[cnt+1]);
						else {
							tmppos.add(postmp[cnt]+" "+postmp[cnt+1]);
						}
					tmpmergepos.add(pos[cnt+1]);
					cnt=cnt+2;
					change=1;
				}
				else 
				{
					tmpwords.add(words[cnt]); 
					if(wordstmp==null)
					{
						tmpmainwords.add(words[cnt]);
					}
					else {
						tmpmainwords.add(wordstmp[cnt]);
					}
					if(postmp==null)
						tmppos.add(pos[cnt]);
						else {
							tmppos.add(postmp[cnt]);
						}
					tmpmergepos.add(pos[cnt]);
					cnt=cnt+1; 
				}
			}
			else 
			{
				tmpwords.add(words[cnt]);
				if(wordstmp==null)
				{
					tmpmainwords.add(words[cnt]);
				}
				else {
					tmpmainwords.add(wordstmp[cnt]);
				}
//				tmpmainwords.add(words[cnt]);
				if(postmp==null)
					tmppos.add(pos[cnt]);
					else {
						tmppos.add(postmp[cnt]);
					}
				tmpmergepos.add(pos[cnt]);
				cnt=cnt+1;
			}
		}
		if(change==1)
		{
			Merge(0, (String[])tmpwords.toArray(new String[tmpwords.size()]),(String[])tmpmainwords.toArray(new String[tmpmainwords.size()]), (String[])tmpmergepos.toArray(new String[tmpmergepos.size()]),(String[])tmppos.toArray(new String[tmppos.size()]));
		}
		else {
			mergepos=tmppos;
			mergewords=tmpwords;
			mergemainpos=tmpmergepos;
			mergemainwords=tmpmainwords;
			return ; 
		}
	}
	
	public void clear()
	{
		words=null;
		Pos=null;
		mergewords.clear();
		mergepos.clear();
	    mergemainpos.clear();
	    mergemainwords.clear();
	}
	
	
	/**
	 * surfaceSemanticStructure
	 * surfaceSemanticStructurePosition
	 */
	List<String> surfaceSemanticStructure=new ArrayList<String>();
	List<String> surfaceSemanticStructurePosition=new ArrayList<String>();
	

	
	/**
	 * 被动状语从句Cured by the doctor,....,Given another 5 yuan,..., Provided that 一般在开头
	 */
	List<String> attributeclauses=new ArrayList<String>();
	public int passiveAttrClause()
	{
		int flag=0,pos=0;
		int len=mergepos.size();
		for(int i=0;i<len;i++)
		{
			if(mergepos.get(i).equals("VBN"))
			{
				if(i==0)
				{
					if(i+2<len && mergepos.get(i+1).equals("IN")&&(mergepos.get(i+2).equals("NN")||mergepos.get(i+2).equals("PRP")))//在开头的情况Surprised by their experience
					{
						pos=i;//被动情况做状语从句
					}
					else if(i+1<len && mergepos.get(i+1).equals("NN")) //given another 5 yuan
					{
						flag=i;
					} 
				}
				String str="";
				while(mergepos.get(pos).equals(",")==false)
				{
					str=str+ mergewords.get(pos);
					pos++;
				}
				adverbialclauses.add(str);
				System.out.println(str);
/**
 * 这个后续处理
 */
//				else if(mergepos.get(i-1).equals(".")) //前面一个是，...,provided that you are ....
//				{
//					if(i+2<len && mergepos.get(i+1).equals("IN")&&(mergepos.get(i+2).equals("NN")||mergepos.get(i+2).equals("PRP")))//在开头的情况SUrprised by their experience
//					{
//						pos=i;//被动情况做状语从句
//					}
//					else if(i+1<len && mergepos.get(i+1).equals("NN"))
//					{
//						flag=i;
//					} 
//				}
			}
		}
		return flag;
	}
	
	 
	/**
	 * 被动状语从句Cured by the doctor,....,Given another 5 yuan,..., Provided that 一般在开头
	 */
	List<String> adverbialclauses=new ArrayList<String>();
	public int passiveAdverbialClause()
	{
		int flag=0,pos=0;
		int len=mergepos.size();
		for(int i=0;i<len;i++)
		{
			if(mergepos.get(i).equals("VBN"))
			{
				if(i==0)
				{
					if(i+2<len && mergepos.get(i+1).equals("IN")&&(mergepos.get(i+2).equals("NN")||mergepos.get(i+2).equals("PRP")))//在开头的情况Surprised by their experience
					{
						pos=i;//被动情况做状语从句
					}
					else if(i+1<len && mergepos.get(i+1).equals("NN")) //given another 5 yuan
					{
						flag=i;
					} 
				}
				String str="";
				while(mergepos.get(pos).equals(",")==false)
				{
					str=str+ mergewords.get(pos);
					pos++;
				}
				adverbialclauses.add(str);
				System.out.println(str);
/**
 * 这个后续处理
 */
//				else if(mergepos.get(i-1).equals(".")) //前面一个是，...,provided that you are ....
//				{
//					if(i+2<len && mergepos.get(i+1).equals("IN")&&(mergepos.get(i+2).equals("NN")||mergepos.get(i+2).equals("PRP")))//在开头的情况SUrprised by their experience
//					{
//						pos=i;//被动情况做状语从句
//					}
//					else if(i+1<len && mergepos.get(i+1).equals("NN"))
//					{
//						flag=i;
//					} 
//				}
			}
		}
		return flag;
	}
	
 
	public void findSurfaceSemanticStructure(HashMap<String, String> nounsurfaceSemantic)
	{
		Iterator iterator = nounsurfaceSemantic.keySet().iterator();                
		while (iterator.hasNext()) {    
		Object key = iterator.next();    
		System.out.println(key+"  :"+nounsurfaceSemantic.get(key));    
		}           
		
		int len=mergepos.size();
		for(int i=0;i<len;i++)
		{
			if(mergepos.get(i).charAt(0)=='N')//sb,sth,sp,stime,sbsth
			{
				String words=mergewords.get(i);
				String[] word=words.split(" ");
				
				System.out.println(nounsurfaceSemantic.get(word[word.length-1]));
				System.out.println(nounsurfaceSemantic.containsKey(word[word.length-1]));
				String attr=nounsurfaceSemantic.get(word[word.length-1]);
				surfaceSemanticStructure.add(attr);
			}
			else if(mergepos.get(i).charAt(0)=='J')//how
			{
				surfaceSemanticStructure.add("how");
			} 
			else if(mergepos.get(i).charAt(0)=='V'&&mergepos.get(i).equals("VBN")==false&&mergepos.get(i).equals("VBG")==false)//vi,vt
			{
				 
				String attr=nounsurfaceSemantic.get(mergewords.get(i));
				surfaceSemanticStructure.add(attr);
				
			}
			else if(mergepos.get(i).equals("PRP"))
			{ 
				String words=mergepos.get(i);
				if(words.charAt(words.length()-2)=='l'&&words.charAt(words.length()-1)=='f')
				surfaceSemanticStructure.add("PRP1");
			}
			else {
				surfaceSemanticStructure.add(mergepos.get(i));
			}
		} 
		
		for(int i=0;i<len;i++)
		{
			System.out.print(surfaceSemanticStructure.get(i)+"  ");
		}
		System.out.println();
 	}
	
	
	
	public boolean judgeNoun(List<String> surfaceSemanticStructure,int position)
	{
		if(surfaceSemanticStructure.get(position).contains("stime")||surfaceSemanticStructure.get(position).contains("sth")||surfaceSemanticStructure.get(position).contains("sp"))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 先从定语从句开始attributive clause
	 */
	public void judgeClause()
	{
		 
		int len=mergepos.size();
		for(int i=0;i<len;i++)
		{
			int cnt=i;
			if(mergepos.get(i).equals("WDT"))
			{
				List<String> Clause=new ArrayList<String>();
				if(i>0)
				{
					if(mergepos.get(i-1).charAt(0)=='N') //N+that/which 之类引起的定语从句
					{
						Clause.add(mergepos.get(i-1));
						Clause.add(mergepos.get(i));
						if(mergepos.get(cnt+1).charAt(0)=='V')//定语从句中做主语
						{
							Clause.add(mergepos.get(cnt+1));
						}
					}
				}
			}
			else if(mergepos.get(i).equals("WRB"))//动词以及其在句子中的位置
			{
				
			}
		}
		
	}
	
	//记录词性以及其所在的位置
	class PosPosition{
		int pos;
		String word;
		public PosPosition(){}
		
		public PosPosition(int pos,String word)
		{
			this.pos=pos;
			this.word=word;
		}
	}
	
	/*
	 * 因为一个句子中只存在一个动词其他的动词要么是动名词要么是从句中的词。
	 */
	List<PosPosition> verb=new ArrayList<PosPosition>();
	List<PosPosition> prep=new ArrayList<PosPosition>(); 
	List<PosPosition> verbprep=new ArrayList<PosPosition>(); 
	List<PosPosition> tag=new ArrayList<PosPosition>(); //记录中间的词是somewhere还是someplace还是how还是who还是when
	
	public void VerbPrepFind()
	{
		int len=mergepos.size();
		for(int i=0;i<len;i++)
		{
			if(mergepos.get(i).charAt(0)=='V')//动词以及其在句子中的位置
			{
				PosPosition tmp=new PosPosition(i,mergepos.get(i));
				verb.add(tmp);
				verbprep.add(tmp);
			}
			else if(mergepos.get(i).charAt(0)=='I'||mergepos.get(i).charAt(0)=='T')//介词以及其在句子中的位置
			{
				PosPosition tmp=new PosPosition(i,mergepos.get(i));
				verb.add(tmp);
				verbprep.add(tmp);
			}
		}
	}
	
	
	/**
	 * 显示一些词性和他们的词
	 */
	public void print()
	{
		for(int i=0;i<words.length;i++)
		{
			System.out.print(words[i]+" ");
		}
		System.out.println();
		for(int i=0;i<words.length;i++)
		{
			System.out.print(Pos[i]+" ");
		}
		System.out.println();
		
		/**/
		for(int i=0;i<mergewords.size();i++)
		{
			System.out.print(mergewords.get(i)+" "+mergepos.get(i)+" ");
		}
		System.out.println();
		/**/
		for(int i=0;i<mergemainpos.size();i++)
		{
			System.out.print(mergemainpos.get(i)+" ");
		}
		System.out.println();
	}

	
	
	
	public void ClauseFind()
	{
		
	}
	
	
	
}
