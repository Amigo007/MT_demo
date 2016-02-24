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
	 * ����״��Ӿ�Cured by the doctor,....,Given another 5 yuan,..., Provided that һ���ڿ�ͷ
	 */
	List<String> attributeclauses=new ArrayList<String>();
	/**
	 * �ҵ�����Ӿ��λ��
	 * @return
	 */
	List<Integer> attr=new ArrayList<Integer>(); //�洢����Ӿ��λ��
	List<Integer> attrclass=new ArrayList<Integer>(); //�洢����Ӿ�����ͣ������д������Ļ��Ƕ��ʷִ������Ļ��ǽ��������(1:�������Ա�־�����,NN+who ...��2:NN at whichҲ�������Ա�־����� 3:��ȥ�ִʵ������
	List<String> adverbialclauses=new ArrayList<String>(); //�洢����Ӿ�ľ���
	public int activeAttrClause()
	{ 
		int len=mergepos.size(),pos=0;
		Integer[] dealornot=new Integer[len];
		for(int i=0;i<len;i++)//��¼�������ݵ�λ�ã��ж������Ƿ��Ѿ������
		{
			dealornot[i]=0;
		}
		/**
		 * ���´�����Ҫ���ҵ���ǵ�λ��
		 */
		for(int i=0;i<len;i++)//�������ԵĴӾ��־that��when��which֮�������Ķ���Ӿ�
		{
			if((i>=1) && (mergepos.get(i).equals("WDT")==false)) 
			{
				if((mergepos.get(i-1).charAt(0)=='N')/*����+that֮���*/)
				{
					attr.add(i);
					attrclass.add(1);
				}
				else if(i>=2 && mergepos.get(i-1).equals("IN") &&mergepos.get(i-2).charAt(0)=='N')/*����+���+which֮��ģ���the reason for which you leave...*/ 
				{
					attr.add(i);
					attrclass.add(2);
				}
			}
			
			if(mergepos.get(i).equals("VBN"))//��ȥ�ִ������Ķ���Ӿ�
			{
				if(i==0||(i==1&&mergepos.get(0).equals("RB"))|| mergepos.get(i-1).equals(",") )//Written by his mother,the book...
				{
					if(i+2<len && mergepos.get(i+1).equals("IN")&&( mergepos.get(i+2).equals("NN")||mergepos.get(i+2).equals("PRP")) )//�ڿ�ͷ�����Surprised by their experience
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
		 * ��λ�ý��г����Ľ綨
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
						if(mergepos.get(i).equals("IN")&&i-cnt==1 || (mergepos.get(i).equals("IN")&&mergepos.get(i-1).equals("IN")&&i-cnt>=1))//���ʺ���ֶ�����
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
					while(position<len&&surfaceSemanticStructure.get(position).charAt(0)!='v'&&surfaceSemanticStructure.get(position).charAt(0)!='.')//ֱ��������һ��v�ʽ���
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
						while(position<len&&surfaceSemanticStructure.get(position).charAt(0)!='v'&&surfaceSemanticStructure.get(position).charAt(0)!='.')//ֱ��������һ��v�ʽ���
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
	
	
	
}
