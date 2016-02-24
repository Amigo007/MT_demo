import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class Clauses {
	List<String> mergewords=new ArrayList<String>();
	List<String> mergepos=new ArrayList<String>();
	List<String> mergeposorig=new ArrayList<String>();
	Set<String> attrtag=new HashSet<String>();
	
	public Clauses(List<String> mergewords,List<String> mergepos,List<String> mergeposorig)
	{
		this.mergewords=mergewords;
		this.mergepos=mergepos;
		this.mergeposorig=mergeposorig;
		attrtag.add("that");
		attrtag.add("who");
		attrtag.add("whom");
		attrtag.add("whose");
		attrtag.add("which");
	//	attrtag.add("as");  //such as....as good as
		attrtag.add("when");
		attrtag.add("where");
		attrtag.add("why");
	}
	
	/**
	 * 被动状语从句Cured by the doctor,....,Given another 5 yuan,..., Provided that 一般在开头
	 */
	List<String> attributeclauses=new ArrayList<String>();
	/**
	 * 找到定语从句的位置
	 * @return
	 */
	List<Integer> attr=new ArrayList<Integer>(); //存储定语从句的位置
	List<Integer> attrclass=new ArrayList<Integer>(); //存储定语从句的类型，是先行词引导的还是动词分词引导的还是介词引导的(1:带有明显标志的情况,NN+who ...，2:NN at which也带有明显标志的情况 3:过去分词的情况，
	List<String> adverbialclauses=new ArrayList<String>(); //存储定语从句的句子
	public int activeAttrClause()
	{ 
		int len=mergepos.size(),pos=0;
		Integer[] dealornot=new Integer[len];
		for(int i=0;i<len;i++)//记录处理数据的位置，判断数据是否已经处理过
		{
			dealornot[i]=0;
		}
		/**
		 * 以下代码主要是找到标记的位置
		 */
		for(int i=0;i<len;i++)//带有明显的从句标志that，when，which之类引导的定语从句
		{
			if((i>=1) && (mergepos.get(i).equals("WDT")==false)) 
			{
				if((mergepos.get(i-1).charAt(0)=='N')/*名词+that之类的*/)
				{
					attr.add(i);
					attrclass.add(1);
				}
				else if(i>=2 && mergepos.get(i-1).equals("IN") &&mergepos.get(i-2).charAt(0)=='N')/*名词+介词+which之类的，如the reason for which you leave...*/ 
				{
					attr.add(i);
					attrclass.add(2);
				}
			}
			
			if(mergepos.get(i).equals("VBN"))//过去分词引导的定语从句
			{
				if(i==0||(i==1&&mergepos.get(0).equals("RB"))|| mergepos.get(i-1).equals(",") )//Written by his mother,the book...
				{
					if(i+2<len && mergepos.get(i+1).equals("IN")&&( mergepos.get(i+2).equals("NN")||mergepos.get(i+2).equals("PRP")) )//在开头的情况Surprised by their experience
					{
						attr.add(i); 
						attrclass.add(3);
					}
				}
				else if((i>0&&i+2<len) && (mergepos.get(i+1).equals("IN") && mergepos.get(i+2).equals("NN")) ||(mergepos.get(i+1).charAt(0)=='R' && mergepos.get(i+2).equals("IN")&& mergepos.get(i+3).equals("NN"))  )//The book evaluated greatly by ....
				{
					attr.add(i);
					attrclass.add(3);
				}
			}
		}
		
		/**
		 * 对位置进行初步的界定
		 */
		int cnt=attrclass.size()-1;
		if(cnt==0)
		{
			System.out.println("sorry,I just see no attr clauses here.");
		}
		else 
		{
			while(cnt!=0)
			{
				String str="";
				if(attrclass.get(cnt)==3)//The book written by his mother there is interesting
				{
					str=str+mergewords.get(cnt)+" ";
					for(int i=cnt+1;i<len&&dealornot[i]==0;i++)
					{
						if(mergepos.get(i).equals("IN")&&i-cnt==1 || (mergepos.get(i).equals("IN")&&mergepos.get(i-1).equals("IN")&&i-cnt>=1))//动词后出现多个介词
						{
							str=str+mergewords.get(i)+" ";
							dealornot[i]=1;
						}
						else if((mergepos.get(i).equals("NN")||mergepos.get(i).equals("PRP"))&&i-cnt>=2)
						{
							str=str+mergewords.get(i)+" ";
							dealornot[i]=1;
						}
						else if(mergepos.get(i).charAt(0)=='R'&&mergepos.get(i-1).charAt(0)=='V'&&i-cnt>=2)
						{
							str=str+mergewords.get(i)+" ";
							dealornot[i]=1;
						}
					}
				}
				else if(attrclass.get(cnt)==2)
				{
					
				}
			}
		}
		
		
		return 0;
	}
	
 
	
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
	
	List<String> sentences=new ArrayList<String>();
	public void sentenjudge(List<String> surfaceSemanticStructure,Set<String> fixedPhraseCombination,HashMap<String, String> nounsurfaceSemantic)
	{
		if(surfaceSemanticStructure.size()==2)
			return;
		int position=0;
		int flag=0;
		int start=0,end=0;
		int len=surfaceSemanticStructure.size();
		String sententce="";
		List<String> surfaceSemanticStructure2=new ArrayList<String>();
		if((position=surfaceSemanticStructure.indexOf("VBN"))!=-1)
		{
			if(position>0)
			{
				if(judgeNoun(surfaceSemanticStructure,position-1))//The book written by him...The money stolean by him
				{
					start=position-1;
					String phrase="be "+surfaceSemanticStructure.get(position)+" ";
					sententce=surfaceSemanticStructure.get(position-1)+" "+phrase;
					position++;
					while(position<len&&surfaceSemanticStructure.get(position).charAt(0)!='v'&&surfaceSemanticStructure.get(position).charAt(0)!='.')//直到出现下一个v词结束
					{
						sententce=" "+sententce+" "+surfaceSemanticStructure.get(position);
						position++;
						
					}
					end=position;
					flag=1;
					System.out.println(sententce);
				}
				else
				{
					
				}
			}
			else if(position==0)//surprised by his voice,we...
			{
				
			}
		}
		else {
			if(surfaceSemanticStructure.contains("vt")||surfaceSemanticStructure.contains("vt,vi"))
			{
				position=surfaceSemanticStructure.indexOf("vt");
				if(position==-1)
				{
					position=surfaceSemanticStructure.indexOf("vt,vi");
				}
				if(judgeNoun(surfaceSemanticStructure,position-1)){
					start=position-1;
					String phrase= surfaceSemanticStructure.get(position)+" ";
					sententce=surfaceSemanticStructure.get(position-1)+" "+phrase;
					if(surfaceSemanticStructure.get(position+1).equals("how"))
					{
						position++;
						while(position<len&&surfaceSemanticStructure.get(position).charAt(0)!='v'&&surfaceSemanticStructure.get(position).charAt(0)!='.')//直到出现下一个v词结束
						{
							sententce=" "+sententce+" "+surfaceSemanticStructure.get(position);
							position++;
							
						}
						end=position;
					}
					flag=1;
					System.out.println(sententce);
				} 
			}
		}
		
		for(int i=0;i<=start;i++)
		{
			surfaceSemanticStructure2.add(surfaceSemanticStructure.get(i));
		}
		for(int i=end;i<len;i++)
		{
			surfaceSemanticStructure2.add(surfaceSemanticStructure.get(i));
		}
		if(flag==1)
		{
			sentenjudge(surfaceSemanticStructure2,fixedPhraseCombination,nounsurfaceSemantic);
		}
	}
	

	List<String> surfaceSemanticStructure=new ArrayList<String>();
	List<String> surfaceSemanticStructurePosition=new ArrayList<String>();
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
	
	
	
}
