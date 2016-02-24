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
	
	private String[] words;//������¼����
	private String[] Pos;//������¼ÿ�����ʶ�Ӧ�Ĵ���
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
	 * Ԥ���������Ա�ע�Ĵʽ��зָ���ֱ���д洢
	 * @param str
	 */
	public void Preprocessing(String str,String cutmark)
	{
		//���²����ǰѴ��Ժ�ԭ�����ֿ���
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
		//��һЩ���Ժϲ��Ķ�����кϲ����������ݴ�+���ʾͿ��Խ��м򵥵ĺϲ�
		Merge(0,copywords,null,copypos,null);
	}
 
	/** CC Coordinating conjunction ��������
		CD Cardinal number ����
		DT Determiner �޶���
		EX Existential there  there ���ھ�
		FW Foreign word ������
		IN Preposition or subordinating conjunction ��ʣ�ǰ�ô�
		JJ Adjective  ���ݴ�
		JJR Adjective, comparative ��ͬ�ʣ��Ƚϼ�
		JJS Adjective, superlative ���ݴ���߼�
		LS List item marker �趨�б����־
		MD Modal ģ̬����̬����
		NN Noun, singular or mass ����
		NNS Noun, plural ���ʣ�����
		NNP Proper noun, singular ר������
		NNPS Proper noun, plural ר�����ʣ�����
		PDT Predeterminer ǰ�޶���
		POS Possessive ending ���и������
		PRP Personal pronoun �˳ƴ����� he,she,it
		PRP$ Possessive pronoun ���и������ his,her
		RB Adverb ����
		RBR Adverb, comparative ���ʣ��Ƚϼ�
		RBS Adverb, superlative ���ʣ���߼�
		RP Particle ����
		SYM Symbol ����
		TO to  to
		UH Interjection ��̾��
		VB Verb, base form  ���ʻ�����ʽ
		VBD Verb, past tense ���ʹ�ȥʽ
		VBG Verb, gerund or present participle ������
		VBN Verb, past participle ��ȥ�ִ�
		VBP Verb, non-3rd person singular present �ǵ����˳Ƶ�������ʱ
		VBZ Verb, 3rd person singular present �����˳Ƶ�������ʱ
		WDT Wh-determiner Wh�޶���
		WP Wh-pronoun ���и�Ĵ���
		WP$ Possessive wh-pronoun  ���и����ݴʴ���
		WRB Wh-adverb Wh��ͷ�ĸ���*/	
	/*
	 * mergewords�ǽ��м򵥺ϲ�֮��ĵ������
	 * mergepos�ǽ��м򵥺ϲ�֮���pos�������
	 * mergemainpos�Ǻϲ���ֻ����������Ҫ��һ���ʵĴ��ԣ�������head word
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
	 * �ڶ���
	 * @param change �жϵ�ǰ�Ƿ��п��Խ��кϲ��ļ������У�����У�������˳�
	 * @param words �Ե�ǰ�ĵ��ʽ������
	 * @param pos �Ե�ǰ��pos������ϣ�ֻ������Ҫ�Ĵ��Բ��֣����ಿ�ֶ���
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
				if(pos[cnt].equals("WRB")&&pos[cnt+1].equals("JJ")) //��how much��how great֮��Ĵ�
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
				else if(pos[cnt].charAt(0)=='R'&&(pos[cnt+1].charAt(0)=='C'||pos[cnt+1].charAt(0)==',')&&pos[cnt+2].charAt(0)=='R')//R&R , R,R ����+����+���ʣ� ����+������+����
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
				else if(pos[cnt].charAt(0)=='J'&&(pos[cnt+1].charAt(0)=='C'||pos[cnt+1].charAt(0)==',')&&pos[cnt+2].charAt(0)=='J')//JJ+CC/.+JJ  ;  RB+CC/.+RB ���ݴ�+����+���ݴʣ����ݴ�+","+���ݴ�
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
				else if(pos[cnt].charAt(0)=='R'&&pos[cnt+1].charAt(0)=='J') //R+J ����+���ݴ�
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
				else if(pos[cnt].charAt(0)=='J'&&pos[cnt+1].charAt(0)=='N') //J+N ���ݴ�+����  
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
				else if((pos[cnt].equals("PDT")||pos[cnt].equals("DT")||pos[cnt].equals("CD")||pos[cnt].equals("WDT"))&&pos[cnt+1].charAt(0)=='N')//DT/����/That+NN  �ڴ�+����
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
				else if((pos[cnt].equals("PDT")||pos[cnt].equals("DT")||pos[cnt].equals("CD")||pos[cnt].equals("WDT"))&&( pos[cnt+1].equals("VBN")||pos[cnt+1].equals("VBG") ) && (pos[cnt+2].charAt(0)=='N') )//DT/CD+������/���ʷִ�+����
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
				else if(pos[cnt].charAt(0)=='N'&&pos[cnt+1].equals("POS")) //����+'s
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
				else if(pos[cnt].equals("POS")&&pos[cnt+1].charAt(0)=='N') //��s + ����
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
//				else if(pos[cnt].charAt(0)=='N'&&pos[cnt+1].charAt(0)=='N'&&postmp[cnt+1].charAt(0)=='P'&&postmp[cnt+1].charAt(1)=='O') //�¼ӵ�NN+��+NN������mother's bag
//				{
//					tmpwords.add(words[cnt]+" "+words[cnt+1]); 
//					tmppos.add(pos[cnt]+" "+pos[cnt+1]);
//					tmpmergepos.add(pos[cnt+1]);
//					cnt=cnt+2;
//					change=1;
//				}
				else if(pos[cnt].equals("PRP$")&&pos[cnt+1].charAt(0)=='N') //PRP$+N  his/her+����
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
 
				else if(pos[cnt].charAt(0)=='N'&&(postmp==null||postmp[cnt+1].charAt(0)=='N')&&(pos[cnt+1].charAt(0)=='N'))//����+���ʵ��ҽ�����һ������ǰû���κ����εĴʣ���ڴʣ����ݴʵ�
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
				else if(pos[cnt].charAt(0)=='R'&&pos[cnt+1].charAt(0)=='V') //����+����
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
	 * ����״��Ӿ�Cured by the doctor,....,Given another 5 yuan,..., Provided that һ���ڿ�ͷ
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
					if(i+2<len && mergepos.get(i+1).equals("IN")&&(mergepos.get(i+2).equals("NN")||mergepos.get(i+2).equals("PRP")))//�ڿ�ͷ�����Surprised by their experience
					{
						pos=i;//���������״��Ӿ�
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
 * �����������
 */
//				else if(mergepos.get(i-1).equals(".")) //ǰ��һ���ǣ�...,provided that you are ....
//				{
//					if(i+2<len && mergepos.get(i+1).equals("IN")&&(mergepos.get(i+2).equals("NN")||mergepos.get(i+2).equals("PRP")))//�ڿ�ͷ�����SUrprised by their experience
//					{
//						pos=i;//���������״��Ӿ�
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
	 * ����״��Ӿ�Cured by the doctor,....,Given another 5 yuan,..., Provided that һ���ڿ�ͷ
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
					if(i+2<len && mergepos.get(i+1).equals("IN")&&(mergepos.get(i+2).equals("NN")||mergepos.get(i+2).equals("PRP")))//�ڿ�ͷ�����Surprised by their experience
					{
						pos=i;//���������״��Ӿ�
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
 * �����������
 */
//				else if(mergepos.get(i-1).equals(".")) //ǰ��һ���ǣ�...,provided that you are ....
//				{
//					if(i+2<len && mergepos.get(i+1).equals("IN")&&(mergepos.get(i+2).equals("NN")||mergepos.get(i+2).equals("PRP")))//�ڿ�ͷ�����SUrprised by their experience
//					{
//						pos=i;//���������״��Ӿ�
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
	 * �ȴӶ���Ӿ俪ʼattributive clause
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
					if(mergepos.get(i-1).charAt(0)=='N') //N+that/which ֮������Ķ���Ӿ�
					{
						Clause.add(mergepos.get(i-1));
						Clause.add(mergepos.get(i));
						if(mergepos.get(cnt+1).charAt(0)=='V')//����Ӿ���������
						{
							Clause.add(mergepos.get(cnt+1));
						}
					}
				}
			}
			else if(mergepos.get(i).equals("WRB"))//�����Լ����ھ����е�λ��
			{
				
			}
		}
		
	}
	
	//��¼�����Լ������ڵ�λ��
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
	 * ��Ϊһ��������ֻ����һ�����������Ķ���Ҫô�Ƕ�����Ҫô�ǴӾ��еĴʡ�
	 */
	List<PosPosition> verb=new ArrayList<PosPosition>();
	List<PosPosition> prep=new ArrayList<PosPosition>(); 
	List<PosPosition> verbprep=new ArrayList<PosPosition>(); 
	List<PosPosition> tag=new ArrayList<PosPosition>(); //��¼�м�Ĵ���somewhere����someplace����how����who����when
	
	public void VerbPrepFind()
	{
		int len=mergepos.size();
		for(int i=0;i<len;i++)
		{
			if(mergepos.get(i).charAt(0)=='V')//�����Լ����ھ����е�λ��
			{
				PosPosition tmp=new PosPosition(i,mergepos.get(i));
				verb.add(tmp);
				verbprep.add(tmp);
			}
			else if(mergepos.get(i).charAt(0)=='I'||mergepos.get(i).charAt(0)=='T')//����Լ����ھ����е�λ��
			{
				PosPosition tmp=new PosPosition(i,mergepos.get(i));
				verb.add(tmp);
				verbprep.add(tmp);
			}
		}
	}
	
	
	/**
	 * ��ʾһЩ���Ժ����ǵĴ�
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
