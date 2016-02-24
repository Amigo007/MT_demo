import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import edu.stanford.nlp.ling.CoreAnnotations.SRL_ID;
  
public class FindFixedPhraseandPrepmatch {
	
	/**
	 * fixedphrase�����洢�̶�����
	 * allprepmatchnoun�����洢����+���+���ʵĴ��䣬key�ǽ�ʣ�val�ǽ�ʶ�Ӧ�������뱻���ε��������'with'->'cat,claw'
	 * allprepmatchverb�����洢����+���+���ʵĴ��䣬key�ǽ�ʣ�val�ǽ�ʶ�Ӧ�������뱻���ε��������'with'->'scratch,claw'
	 */
	Map<String,String> fixedphrase=new HashMap<String,String>();
	Map<String,Set<String>> allprepmatchnoun=new HashMap<String,Set<String>>();
	Map<String,Set<String>> allprepmatchverb=new HashMap<String,Set<String>>();
	Map<String,Set<String>> allwhmatch=new HashMap<String, Set<String>>();
	Map<String,Set<String>> allverbprop=new HashMap<String, Set<String>>();
	/**
	 * fixphrasesearch���Է������Ѱ�ҹ̶����飬��ĳ�����ʿ�ͷ�Ĵ��������˵��������ͦ�ٵģ����Կ���ͨ����ͷ�������ҵ����еĴ��飬Ȼ��
	 * �ڱ����̶��������ԭ�����в��ҵ��ʡ�fixedphrasesearch��,keyΪ���ʵĿ�ͷ��valueΪ��key��ͷ��Ӧ�����й̶������list����:key='a',val=<'a number of','a bunch of'...>
	 */
	Map<String,List<String>> fixedphrasesearch=new HashMap<String, List<String>>(); 
	
	public FindFixedPhraseandPrepmatch() {
		String fixed="data\\phrases\\phrases.txt";
		readTxtFile(fixed,"utf-8",1,"");
		
		String prepnouns="data\\prep match\\noun";
		File file = new File(prepnouns);   
		File[] nounfile = file.listFiles();   
		for(int i=0;i<nounfile.length;i++)
		{
//			System.out.println(nounfile[i].getName().substring(0,nounfile[i].getName().length()-4));
			readTxtFile(nounfile[i].getAbsolutePath(),"utf-8",2,nounfile[i].getName().substring(0,nounfile[i].getName().length()-4));
		}
		
		String prepverbs="data\\prep match\\verb";
		File file1=new File(prepverbs);
		File[] verbfile = file1.listFiles(); 
		for(int i=0;i<verbfile.length;i++)
		{
			System.out.println(verbfile[i].getName());
			readTxtFile(verbfile[i].getAbsolutePath(),"utf-8",3,verbfile[i].getName().substring(0,verbfile[i].getName().length()-4));
		}	 
		
		
		String whmatch="data\\wh match";
		File file2=new File(whmatch);
		File[] whmatchfile = file2.listFiles(); 
		for(int i=0;i<whmatchfile.length;i++)
		{
			System.out.println(whmatchfile[i].getName());
			readTxtFile(whmatchfile[i].getAbsolutePath(),"utf-8",4,whmatchfile[i].getName().substring(0,whmatchfile[i].getName().length()-4));
		}	 
		String verbprop="data\\verb modifier";
		File file3=new File(verbprop);
		File[] verbpropfile = file3.listFiles(); 
		for(int i=0;i<verbpropfile.length;i++)
		{
			System.out.println(verbpropfile[i].getName());
			readTxtFile(verbpropfile[i].getAbsolutePath(),"utf-8",5,verbpropfile[i].getName().substring(0,verbpropfile[i].getName().length()-4));
		}
	}
	
	
	public void readTxtFile(String filePath,String code,int type,String prep){
		 try {
			 Set<String> prepmatch=new HashSet<String>();
		     String encoding=code;
		     File file=new File(filePath); 
		     if(file.isFile() && file.exists()){ //�ж�txt�ļ��Ƿ����
		         InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//���ǵ������ʽ
		         BufferedReader bufferedReader = new BufferedReader(read);
		         String lineTxt = "";
		         while((lineTxt = bufferedReader.readLine()) != null)
		         {
		             if(type==1)
		             {
		            	 String[] name=lineTxt.split(",");
		            	 String firstword=name[0].split(" ")[0];
		            	 if(fixedphrasesearch.containsKey(firstword)==false)
		            	 {
		            		 List<String> list=new ArrayList<String>();
		            		 list.add(name[0]);
		            		 fixedphrasesearch.put(firstword,list);
		            	 }
		            	 else {
		            		 List<String> list=new ArrayList<String>();
		            		 list=fixedphrasesearch.get(firstword);
		            		 list.add(lineTxt);
		            		 fixedphrasesearch.put(firstword,list);
						}
		            	fixedphrase.put(name[0], name[1]);
		             }
		             else if(type==2){ 
		            	 prepmatch.add(lineTxt);
					} 
		             else if(type==3){
		            	 prepmatch.add(lineTxt);
					} 
		             else if(type==4)
		             {
		            	 prepmatch.add(lineTxt);
		             }
		             else if(type==5)
		             {
		            	 prepmatch.add(lineTxt);
		             }
		         }
		         if(type==2)
		         {
		        	 allprepmatchnoun.put(prep,prepmatch);
		         }
		         else if(type==3)
		         {
		        	 allprepmatchverb.put(prep, prepmatch);
		         }
		         else if(type==4)
		         {
		        	 allwhmatch.put(prep, prepmatch);
		         }
		         else if(type==5)
		         {
		        	 allverbprop.put(prep, prepmatch);
		         }
		         read.close();
		     }
		     }
		     catch (Exception e) { 
		            System.out.println("��ȡ�ļ����ݳ���");
		            e.printStackTrace();
		        }
		    }
	
	/*
	 * type=1ʱ���жϴ���phrase�Ƿ��ڹ̶�������
	 * type=2ʱ���жϹ̶��Ľ�ʴ���phrase�Ƿ���ȷ(����+���+���ʵĴ���)
	 * type=3ʱ���жϹ̶��Ľ�ʴ���phrase�Ƿ���ȷ(����+���+���ʵĴ���)
	 * @param phrase
	 * @param type
	 * @return
	 */
	public String isIn(String phrase,int type,String prep)
	{
		if(type==1)
		{
			if(fixedphrase.containsKey(phrase))
				return fixedphrase.get(phrase);
		}
		else if(type==2)
		{
			 Set<String> prepmatch=new HashSet<String>();
			 prepmatch=allprepmatchnoun.get(prep); //����+���+���ʵĴ���
			 if(prepmatch!=null&&prepmatch.contains(phrase))
			 {
				 return "yes";
			 }
		}
		else if(type==3)
		{
			 Set<String> prepmatch=new HashSet<String>();
			 prepmatch=allprepmatchverb.get(prep); //����+���+���ʵĴ���
			 if(prepmatch!=null&&prepmatch.contains(phrase))
			 {
				 return "yes";
			 }
		}
		else if(type==4)
		{
			 Set<String> whmatch=new HashSet<String>();
			 whmatch=allwhmatch.get(prep); //����+���+���ʵĴ���
			 if(whmatch.contains(phrase))
			 {
				 return "yes";
			 }
		}
		else if(type==5)
		{
			 Set<String> verbprop=new HashSet<String>();
			 verbprop=allverbprop.get(prep); //����+���+���ʵĴ���
			 if(verbprop.contains(phrase))
			 {
				 return "yes";
			 }
		}
		return null;
	}
	
	/**
	 * �ж��Ƿ��������ཻ��������򱨴����򷵻�true
	 * @param left
	 * @param right
	 * @return
	 */
	public boolean isCross(Integer[] left,Integer[] right)
	{
		int len=left.length;
		for(int i=0;i<len;i++)
		{
			int l=left[i];
			int r=right[i];
			for(int j=i+1;j<len;j++)
			{
				if( (left[j]<=l&&right[j]>=r) || (left[j]>=r) )
				{
					continue;
				}
				else {
					return false;
				}
			}
		}
		return true;
	}
	/*
	 * ��ȡ���п��е����ε�����,�������ֻ������ʣ����ʺͶ��ʣ���Ҫ�����жϽ�ʵ��������
	 * @param tags
	 * @param words
	 */
	public void getSimplePrepModifysequence(String[] words,String[] tags)
	{
		int len=words.length;
		int num=0;
		List<Integer> candidate=new ArrayList<Integer>(); //�����洢������Ҫ���ʺͶ��ʵ���������Щ������Ҫ�������α�ǲ���
		Map<Integer, List<Integer>> allmodify=new HashMap<Integer, List<Integer>>();
		for(int i=0;i<len;i++)
		{
			if(tags[i].charAt(0)!='I')
			{
				num++;
				candidate.add(i);
			}
		}
		
		Map<Integer, List<Integer>> wordandindex=new HashMap<Integer, List<Integer>>();

		for(int i=candidate.size()-1;i>=1;i--)
		{
			List<Integer> list=new ArrayList<Integer>();
			if(tags[candidate.get(i)].charAt(0)=='V')
			{
				continue;
			}
			if(i>0&&tags[candidate.get(i)].charAt(0)=='N'&&tags[candidate.get(i-1)].charAt(0)=='V')
			{
//				list.add(1);
				list.add(i-1);
				allmodify.put(i, list);
				continue;
			}
			for(int j=i-1;j>=0;j--)
			{
				String str=words[candidate.get(j)]+","+words[candidate.get(i)];
				String prep=words[candidate.get(i)-1];
				if(tags[candidate.get(j)].charAt(0)=='V')
				{
					if(isIn(str,3,prep)=="yes")
					{
						if(allmodify.containsKey(candidate.get(i)))
						{
							list=allmodify.get(candidate.get(i));
							list.add(candidate.get(j));
							allmodify.put(candidate.get(i), list);
						}
						else {
							list.add(candidate.get(j));
							allmodify.put(candidate.get(i), list);
						}
					}
				}
				else
				{
					if(isIn(str,2,prep)=="yes")
					{
						if(allmodify.containsKey(candidate.get(i)))
						{
							list=allmodify.get(candidate.get(i));
							list.add(candidate.get(j));
							allmodify.put(candidate.get(i), list);
						}
						else {
							list.add(candidate.get(j));
							allmodify.put(candidate.get(i), list);
						}
				}
				}
			}			
		}
		
		List<List<Integer>> allleft=new ArrayList<List<Integer>>();
		List<List<Integer>> allright=new ArrayList<List<Integer>>();
		for(int i1=0;i1<num;i1++)
		{
			List<Integer> list1=new ArrayList<Integer>();  
			if(allmodify.containsKey(candidate.get(i1)))
			{
				List<Integer> listleft=new ArrayList<Integer>();
				List<Integer> listright=new ArrayList<Integer>();
				list1=allmodify.get(candidate.get(i1));
				for(int j=0;j<list1.size();j++)
				{
					listleft.add(list1.get(j));
					listright.add(candidate.get(i1));
					System.out.print(" ( ");
					System.out.print(list1.get(j));
					System.out.print(candidate.get(i1));
					System.out.print(" ) ");
				}
				allleft.add(listleft);
				allright.add(listright);
				System.out.println();
			}
		}
		getAllSequence(words,allleft,allright,new ArrayList<Integer>(),new ArrayList<Integer>(),0,allleft.size());	
	}
	
    List<String> allsequence=new ArrayList<String>();
	public void getAllSequence(String[] words,List<List<Integer>> allleft,List<List<Integer>> allright,List<Integer> l,List<Integer> r,int start,int num)
	{
		if(start==num)
		{
			Integer[] count=new Integer[words.length];
			for(int i=0;i<words.length;i++)
				count[i]=0;
			Map<Integer,String> tmp=new HashMap<Integer, String>();
			if(isCross((Integer[])l.toArray(new Integer[l.size()]),(Integer[])r.toArray(new Integer[r.size()]) ))
			{
				for(int i=0;i<l.size();i++)
				{
					count[l.get(i)]++;
					if(tmp.containsKey(l.get(i))){
						String[] all=tmp.get(l.get(i)).split(",");
						for(int j=0;j<all.length;j++)
 						if(all[j].equals(words[r.get(i)]))
						{
							return ;
						}
						tmp.put(l.get(i), tmp.get(l.get(i))+","+words[r.get(i)]);
					}
					else {
						tmp.put(l.get(i),words[r.get(i)]);	
					}
				}
				int prio=0;
			    for(int i=0;i<words.length;i++)
			    {
			    	if(count[i]>1)
			    	prio=prio+count[i]-1;
			    }
				String str=Integer.toString(prio);
				for(int i=0;i<l.size();i++)
				{
					str=str+"(" +Integer.toString(l.get(i))+" , "+Integer.toString(r.get(i))+") ";
//					System.out.print("(");
//					System.out.print(l.get(i));
//					System.out.print(" , ");
//					System.out.print(r.get(i));
//					System.out.print(") ");
				}
				allsequence.add(str);
				System.out.println(str);
			}
		
			
				return ;
		}
		List<Integer> tmp1=allleft.get(start);
		List<Integer> tmpr=allright.get(start);
		for(int i=0;i<tmp1.size();i++)
		{
			l.add(tmp1.get(i));
			r.add(tmpr.get(i));
			getAllSequence(words,allleft,allright,l,r,start+1,num);
			l.remove(start);
			r.remove(start);
		}
		
	}
	
	
	/**
	 * ��ԭ��ֻ���й�POS��ע�Ĳ��ֲ��ù̶������Լ�POSת��������������
	 * ���㷨��˼·�ǣ�
	 *    �Ծ����е�ÿһ�����ʽ��в��ң��ж��Ƿ�����Ըõ���Ϊ��ͷ�Ĵ��飬������ڣ��򷵻�һ���Ըõ���Ϊ��ͷ���ʵ��б�
	 *    ���б��е����й̶�������б�������ÿһ������;��ӽ��бȽϣ���߲��������ڼ����ж��Ӽ��ķ�ʽ���в����������������Ϊ����
	 *   ��1���򵥵Ĵ��飬�м�û���κ�...��(...)����ֱ�ӽ���һһƥ��
	 *   ��2���м����...��(...)�Ĵ��飬ͬ������һһƥ�䣬�����(...)�����ж�POS���ԣ���������ݴʻ��߸��ʣ����������ɨ�裬����ͽ������index�����һ��
	 *		  �����...,ͬ���ж�POS����,��������ݴʻ��߸��ʻ������ʻ������ʻ����޶���������е�index������һ�񣬷���ֹͣ
	 *   ��3������������NNS֮��ģ����ھ����н���ɨ�����֪������NNSΪֹ
	 * @param tags
	 * @param sentence
	 */
	public String newsentence="";
	public String newtag="";
	public void transfer(String tags,String sentence,String[] mergeposall,String[] mergewords,String[] mergeposparts)
	{
		List<String> newsen=new ArrayList<String>();
		List<String> newtags=new ArrayList<String>();
		String[] words=sentence.split(" ");
		String[] tag=tags.split(" ");
		Map<String, Integer> mergewordspos=new HashMap<String, Integer>();//�����洢ÿһ�����ʶ�Ӧ�Ŀ��Ժϲ�����ͨ���������λ��
		for(int i=0;i<mergewords.length;i++)
		{
			String[] str=mergewords[i].split(" ");
			for(int j=0;j<str.length;j++)
			{
				mergewordspos.put(str[j],i);
			}
		} 
		for(int i=0;i<words.length;i++) //���������е�ÿһ������
		{ 
			List<String> list=new ArrayList<String>(); //�洢ÿһ�����ʿ�ͷ�Ĵ���
			if(fixedphrasesearch.containsKey(words[i]))
			{
				list=fixedphrasesearch.get(words[i]);
				String save="";//�����洢�����ı�֮��...����Ӧ��POS
				boolean mark=false;
				for(int j=0;j<list.size();j++) //����ÿһ������
				{
					String[] name=list.get(j).split(",")[0].split(" "); //��������Ϊ����
					int k=1,start=i+1; //start�Ǿ����е�ǰ���ʵ�λ�ã�k�ǵ�ǰ������ɨ�赽�ĵ��ʶ�Ӧ��λ��
					while(k<name.length&&start<words.length)//������;��Ӷ�û�б�������ʱ
					{
						if(words[start].charAt(0)<'a'||words[start].charAt(0)>'z')//������ӽ�β�����ߵ�����ӵ��жϲ����磺I have a brother,an American,...
						{
							break;
						}
						
						if(name[k].equals("(...)"))//(...)��ʾ����˼�ǿ��������ݴʻ��߸��ʻ��߿գ���ʲô��û�е������
						{
							if((tag[start].charAt(0)=='J'||tag[start].charAt(0)=='R'))//Ϊ���ݴʻ��߸��ʵ����
							{
								save=tag[start];
								if(k+1<name.length&&words[start].equals(name[k+1]))//�������������벿����ͬ���������������������񣬾���������һ��
								{
									start++;
									k=k+2;
								}
								else //������������ݴʻ��߸��ʣ������������
								{ 
									start++;
								}
							}
							else //���Ϊ�գ�����鵱ǰ���ʵ�index������һ��
							{
								k++;
							}
						}
						else if(name[k].equals("..."))//���������ݴʻ��߸��ʻ��������Լ����ǵ����Ҳ����ʲô��û��
						{ 
							int t=mergewordspos.get(words[start]); //��ǰ����������mergepos�е�λ��
							String wordsstr=mergewords[t];//��õ�ǰ����֮ǰ��ϵĴ���ı���			
							String posstr=mergeposall[t]; //��ö�Ӧ�Ĵ����ÿ�����ʵ�POS
							String[] tmpp=posstr.split(" ");//��ö�Ӧ�Ĵ����ÿ�����ʵ�POS
							String[] tmp=wordsstr.split(" ");//��õ�ǰ����֮ǰ��ϵĴ���ı��ݵ�ÿ������
							int flag=-1; //������¼���������·ָ��λ��
							for(int l=0;l<tmp.length;l++)//�м�Ͽ��ˣ�����both beautiful and nice���Ὣԭ�ȵ�and��beautiful��nice�ֿ�
							{
								if(tmp[l].equals(name[k+1]))
								{
									flag=l;//flag��Ӧand��λ��
									start=l+start+1; //��һ��start��ʼ��λ��.�����nice��Ӧ��λ��
									k=k+2;
									save=tag[start-2];//�洢name[k+1]��Ӧ�ĵ��ʵ�ǰһ����POS��־�������beautiful
									break;///////
								}
							}
							if(flag!=-1)//��Ҫ��ֵ����������Ҫ��ֵ����
							{ 
								String newstr="";
								String newpos="";
								for(int l=flag+1;l<tmp.length;l++)
								{
									newpos=newpos+tmpp[l]+" ";
									newstr=newstr+tmp[l]+" ";
								}
								mergeposall[t]=newpos;
								mergewordspos.put(newstr, t);
								mergewordspos.remove(wordsstr);
							}
							else
							{
								if(name[k+1].equals(words[start+tmp.length]))
								{
									save=mergeposparts[t];
									k++;
									start=start+tmp.length;
								}
								else {
									break;
								}
							}
						}
						else if(name[k].charAt(0)=='?')//Ҫ�������ʣ������Ҫ����������ų�,��a number of cats��This is a number of my sister's��a number of�ǲ�ͬ��
						{
							if(mergeposparts[mergewordspos.get(words[start])].equals(name[k].substring(1)))
							{
								start=start+mergeposall[mergewordspos.get(words[start])].split(" ").length;
								k++;
							} 
							else
							{
								break;
							}
						}
						else if(name[k].equals(words[start]))
						{
							k++;
							start++;
						}
						else 
						{
							break;
						}
					}

					if(k==name.length)
					{
						String str="";
						for(int m=i;m<start;m++)
						{ 
							str=str+words[m]+" ";
						}
						newsen.add(str);
						String postosave=""; 						
						String newpos=fixedphrase.get(list.get(j).split(",")[0]);
						String[] poss=newpos.split(" ");
						
						for(int m=0;m<poss.length;m++)
						{
							if(poss[m].equals("..."))
							{
								postosave=postosave+save+" ";
							}
							else {
								postosave=postosave+poss[m]+" ";
							}
						} 
						newtags.add(postosave);
						i=start-1;
						mark=true;
						break;
					}
				}
				if(mark==true)
				{
					continue;
				}
				else 
				{ 
					newsen.add(words[i]);
					newtags.add(tag[i]);
				}
			}
			else 
			{
				newsen.add(words[i]);
				newtags.add(tag[i]);
			}
		}
		 
		String newsentence="";
		String newtagss="";
		String newsenpos="";
		for(int i=0;i<newsen.size();i++)
		{
			newsentence=newsentence+newsen.get(i)+" *** ";
			newtagss=newtagss+newtags.get(i)+" *** ";
			if(newsen.get(i).charAt(newsen.get(i).length()-1)==' ')
			{
//				System.out.println(newsen.get(i).substring(0,newsen.get(i).length()-2));
			newsenpos=newsenpos+newsen.get(i).substring(0,newsen.get(i).length()-1)+"_"+newtags.get(i)+"\t";
			}
			else {
				newsenpos=newsenpos+newsen.get(i)+"_"+newtags.get(i)+"\t";
			}
		}
		this.newsentence=newsentence;
		this.newtag=newtagss;
		this.senpos=newsenpos;
		System.out.println(newsentence);
		System.out.println(newtagss);
	}
	
	public String getnewSentence()
	{
		return newsentence;
	}
	public String getnewTag()
	{
		return newtag;
	}
	String senpos="";
	public String getSentencePOS()
	{
		return senpos;
	}
	
	public void show()
	{
		int cnt=0;
		Iterator iter = fixedphrase.keySet().iterator();
		while (iter.hasNext()) {
		cnt++;
		if(cnt==100)
			break;
		String key = (String) iter.next();
		String val = fixedphrase.get(key);
		System.out.println(key+"  "+val);
		}
		Iterator iter1 = allprepmatchnoun.keySet().iterator();
		while (iter1.hasNext()) {
			cnt++;
			if(cnt==200)
				break;
			String key = (String) iter1.next();
			Set<String> val = allprepmatchnoun.get(key);
			for(String value : val){  
	            System.out.println(key+"  "+value);  
	        }  			
		}
		Iterator iter2 = allprepmatchverb.keySet().iterator();
		while (iter2.hasNext()) {
			cnt++;
			if(cnt==300)
				break;
			String key = (String) iter2.next();
			Set<String> val = allprepmatchverb.get(key);
			for(String value : val){  
	            System.out.println(key+"  "+value);  
	        }  			
		}
	}
	
	Integer[] index;
	private void Initialindex(int len)
	{
		allsentence.clear();
		allsentence=new ArrayList<String>();
		index=new Integer[len];
		for(int i=0;i<len;i++)
		{
			index[i]=-1;
		}
	}
	List<String> allsentence=new ArrayList<String>();
	public int getAllClauses(int type,int start,String[] mergeWords,String[] mergePos,String[] words,String[] Pos)
	{
		List<String> mainwords=new ArrayList<String>();
		List<String> mainpos=new ArrayList<String>();
		List<String> subsentence=new ArrayList<String>();
		List<Integer> mainwordsPos=new ArrayList<Integer>();
		int len=mergePos.length;
		if(type==-1)
		{
			Initialindex(len);
		}
		if(type==1)//NN+IN+WH������The place I live
		{
			mainwords.add(mergeWords[start]);
			subsentence.add(words[start]);
			mainpos.add(mergePos[start]);
			int cnt=0;
			for(int i=start+1;i<=len;i++)
			{
				if(index[i]==-1)
				{
					if(cnt==2)//Ѱ����һ�ο�ʼ�ĵط�
					{
						start=i;
						break;
					}
					cnt++;
					mainwords.add(mergeWords[i]);
					mainpos.add(mergePos[i]);
					subsentence.add(words[i]);
				}
			}
		}
		int nextpos=-1;
		for(int i=start;i<len;i++)
		{
			int change=0;
			if(mergePos[i].charAt(0)=='N')
			{
				mainwords.add(mergeWords[i]);
				mainpos.add(mergePos[i]);
				subsentence.add(words[i]);
				index[i]=-1;
				if(mergePos[i+1].charAt(0)=='I'&&mergePos[i+2].charAt(0)=='W')//���1:The place in which ...
				{
					change=1;
					nextpos=getAllClauses(1, i, mergeWords, mergePos, words, Pos);
				}				 
			}
			else if(mergePos[i].charAt(0)=='V'  && type==1)
			{
				int mainlen=mainpos.size();
				int verbnum=0;
				for(int j=mainlen-1;j>=0;j--)
				{
					if(mainpos.get(j).charAt(0)=='V')
					{
						for(int k=0;k<subsentence.size();k++)
							System.out.print(subsentence.get(k)+" ");
						System.out.println();
						verbnum++;
						return i;
					}
				}  
			}
			if(change==1)
			{
				i=nextpos-1;
			}
			else 
			{
				index[i]=1;
				mainpos.add(mergePos[i]);
				subsentence.add(words[i]);
			}
		}
		for(int k=0;k<subsentence.size();k++)
			System.out.print(subsentence.get(k)+" ");
		System.out.println();
		return 0;
	}
	
	@SuppressWarnings("unused")
	public int getAllClauses1(int type,int start,String mainw,String mainp,String w,String[] mergeWords,String[] mergePos,String[] words,String[] Pos)
	{
		List<String> mainwords=new ArrayList<String>();
		List<String> mainpos=new ArrayList<String>();
		List<String> subsentence=new ArrayList<String>();
		List<Integer> mainwordsPos=new ArrayList<Integer>();
		int len=mergePos.length;
		if(type==-1)
		{
			Initialindex(len);
			mainwords.add(mergeWords[0]);
			mainpos.add(mergePos[0]);
			subsentence.add(words[0]);
			index[0]=1;
			start=1;
		}
		else 
		{
			mainwords.add(mainw); 
			mainpos.add(mainp);
			subsentence.add(w);
		}
		if(type==1)//NN+IN+WH������The place I live
		{
			int cnt=0;
			for(int i=start;i<=len;i++)
			{
				if(index[i]==-1)
				{
					if(cnt==2)//Ѱ����һ�ο�ʼ�ĵط�
					{
						start=i;
						break;
					}
					cnt++;
					mainwords.add(mergeWords[i]);
					mainpos.add(mergePos[i]);
					subsentence.add(words[i]);
					index[i]=1;
				}
			}
		}
		if(type==2)//NN+IN+WH������The place in which I live
		{
			int cnt=0;
			for(int i=start;i<=len;i++)
			{
				if(index[i]==-1)
				{
					if(cnt==2)//Ѱ����һ�ο�ʼ�ĵط�
					{
						start=i;
						break;
					}
					cnt++;
					mainwords.add(mergeWords[i]);
					mainpos.add(mergePos[i]);
					subsentence.add(words[i]);
					index[i]=1;
				}
			}
		}
		if(type==3)
		{
			int cnt=0;
			for(int i=start;i<len;i++)
			{
				if(index[i]==-1)
				{
					if(cnt==1)//Ѱ����һ�ο�ʼ�ĵط�
					{
						start=i;
						break;
					}
					cnt++;
					mainwords.add(mergeWords[i]);
					mainpos.add(mergePos[i]);
					subsentence.add(words[i]);
					index[i]=1;
				}
			}
		}
		if(type==4)
		{
			int cnt=0;
			for(int i=start;i<len;i++)
			{
				if(index[i]==-1)
				{
					if(cnt==2)//Ѱ����һ�ο�ʼ�ĵط�
					{
						start=i;
						break;
					}
					cnt++;
					mainwords.add(mergeWords[i]);
					mainpos.add(mergePos[i]);
					subsentence.add(words[i]);
					index[i]=1;
				}
			}
		}
		if(type==5)//The place which I used to live is so beautiful
		{
			int cnt=0;
			for(int i=start;i<len;i++)
			{
				if(index[i]==-1)
				{
					if(cnt==2)//Ѱ����һ�ο�ʼ�ĵط�
					{
						start=i;
						break;
					}
					cnt++;
					mainwords.add(mergeWords[i]);
					mainpos.add(mergePos[i]);
					subsentence.add(words[i]);
					index[i]=1;
				}
			}
		}
		if(type==6)
		{
			int cnt=0;
			for(int i=start;i<len;i++)
			{
				if(index[i]==-1)
				{
					if(cnt==1)//Ѱ����һ�ο�ʼ�ĵط�
					{
						start=i;
						break;
					}
					cnt++;
					mainwords.add(mergeWords[i]);
					mainpos.add(mergePos[i]);
					subsentence.add(words[i]);
					index[i]=1;
				}
			}
		}
		if(type==7)
		{
			int cnt=0;
			for(int i=start;i<len;i++)
			{
				if(index[i]==-1)
				{
					if(cnt==2)//Ѱ����һ�ο�ʼ�ĵط�
					{
						start=i;
						break;
					}
					cnt++;
					mainwords.add(mergeWords[i]);
					mainpos.add(mergePos[i]);
					subsentence.add(words[i]);
					index[i]=1;
				}
			}
		}
		int nextpos=-1;
		for(int i=start;i<len;i++)
		{
			int change=0;
			int length=mainwords.size();
			if(mainpos.get(length-1).charAt(0)=='N')
			{
				if(mergePos[i].charAt(0)=='I'&&mergePos[i+1].charAt(0)=='W')//���1:The place in which ...
				{
					change=1;
				    if(isIn(mainwords.get(length-1),4,"where")=="yes"||isIn(mainwords.get(length-1),4,"when")=="yes")
					{
				    	nextpos=getAllClauses1(1, i, mainwords.get(length-1),mainpos.get(length-1),subsentence.get(length-1),mergeWords, mergePos, words, Pos);
					}
				    else
				    {
				    	for(int k=0;k<subsentence.size();k++)
							System.out.print(subsentence.get(k)+" ");
						System.out.println();
						return i;
					}
				}				 
				else if(mergePos[i].equals("VBN")&&mergePos[i+1].charAt(0)=='I')//���1:The book written by ...
				{
					change=1;
					nextpos=getAllClauses1(2, i, mainwords.get(length-1),mainpos.get(length-1),subsentence.get(length-1),mergeWords, mergePos, words, Pos);
				}
				else if(mergePos[i].equals("VBG"))//���1:The book written by ...
				{
					change=1;
					nextpos=getAllClauses1(3, i, mainwords.get(length-1),mainpos.get(length-1),subsentence.get(length-1),mergeWords, mergePos, words, Pos);
				}
				else if(mergePos[i].charAt(0)=='I'&&mergePos[i+1].charAt(0)=='N')//boy in a blue shirt
				{ 
					for(int j=length-1;j>=0;j--)
					{
						if(mainpos.get(j).charAt(0)=='N' &&isIn(mainwords.get(j)+","+mergeWords[i+1], 2,mergeWords[i])=="yes")
						{ 
							change=1;
							nextpos=getAllClauses1(4, i, mainwords.get(j),mainpos.get(j),subsentence.get(j),mergeWords, mergePos, words, Pos);
							break;
						}
						else if(mainpos.get(j).charAt(0)=='V' &&isIn(mainwords.get(j)+","+mergeWords[i+1], 3,mergeWords[i])=="yes")//The boy playing basketball with his friends there is...
						{ 
							change=1;
							mainwords.add(mergeWords[i]);
							mainpos.add(mergePos[i]);
							subsentence.add(words[i]);
							mainwords.add(mergeWords[i+1]);
							mainpos.add(mergePos[i+1]);
							subsentence.add(words[i+1]);
							index[i]=1;
							index[i+1]=1;
							nextpos=i+2;
							break;
						}
					}
				}
				else if(mergePos[i].charAt(0)=='W')
				{
					int flag=0;
					int length1=mainwords.size()-1;
					for(int j=length1;j>=0;j--)//The book which is from ...
					{
						if(isIn(mainwords.get(j), 4, mergeWords[i])=="yes")//�������ε������
						{
							flag=1;
							change=1;
							nextpos=getAllClauses1(5, i, mainwords.get(j),mainpos.get(j),subsentence.get(j),mergeWords, mergePos, words, Pos);
							break;
						}
					}
					if(flag==0&&mergeWords[i].equals("who")==false&&mergeWords[i].equals("which")==false&&mergeWords[i].equals("whose")==false&&mergeWords[i].equals("that")==false)//Ҳ�п��ܳ��ֲ������ε�������Ǿ�Ҫ�����ó����ˣ�����put the drug where children can not reach
					{
						;//nextpos=getAllClauses1(6, i, mainwords.get(length1),mainpos.get(length1),subsentence.get(length1),mergeWords, mergePos, words, Pos);
						//break;
					}
				}
				else if(mergePos[i].charAt(0)=='N'&&mergePos[i+1].charAt(0)=='V'&&mergePos[i+1].equals("VBG")==false&&mergePos[i+1].equals("VBN")==false)
				{
					int flag=-1;
					for(int j=1;j<length-1;j++)//�ж϶��ʵ��������
					{
						if(mainpos.get(j).charAt(0)=='V')
						{
							for(int k=3;k>=1;k--)
							{
								if(isIn(mainwords.get(j),5,String.valueOf(k))=="yes")
								flag=k;
								break;
							}
						}
					}
					if(flag<3)//��ʾ����Ŀ϶���������give the book my mom bought for me����the book my friend bought for me֮��ľ���
					{
						change=1;
						nextpos=getAllClauses1(7, i, mainwords.get(length-1),mainpos.get(length-1),subsentence.get(length-1),mergeWords, mergePos, words, Pos);
						//break;
					}
					else //���ܻ���ֱ���Ӿ�,���磺I tell my friends my mom loves me*******������д
					{ 
						int mark=0;
						for(int j=i+2;j<len;j++)
						{ 
							if((j+1<len) && (mergePos[j].charAt(0)=='I') && (mergePos[j+1].charAt(0)=='N'||mergePos[j+1].charAt(0)=='P'))
							{ 
								j++;
								continue;
							}
							else if(j+1<len&&mergePos[j].charAt(0)=='N'&&mergePos[j+1].charAt(0)=='V'&&mergePos[j+1].equals("VBG")==false&&mergePos[j+1].equals("VBN")==false)
							{
								mark=1;
								break;
							}
						}
						if(mark!=1)//�����˱���Ӿ�,���磺I tell my friends my mom loves me******* 
						{
							mainpos.add("Z");
							mainwords.add("that");
						    subsentence.add("that");
							mainwords.add(mergeWords[i]);
							mainpos.add(mergePos[i]);
						    subsentence.add(words[i]);
							mainwords.add(mergeWords[i+1]);
							mainpos.add(mergePos[i+1]);
						    subsentence.add(words[i+1]);
						    index[i]=1;
						    index[i+1]=1;
						    change=1;
							nextpos=i+2;
							//continue;
						}
						else
						{
							change=1;
							nextpos=getAllClauses1(7, i, mainwords.get(length-1),mainpos.get(length-1),subsentence.get(length-1),mergeWords, mergePos, words, Pos);
						}
					}
				}
			}
			else if(mainpos.get(length-1).charAt(0)=='P'||mainpos.get(length-1).charAt(0)=='I')
			{
				if((mergePos[i].charAt(0)=='N'||mergePos[i].charAt(0)=='P') &&(mergePos[i+1].charAt(0)=='V'&&mergePos[i+1].equals("VBG")==false&&mergePos[i+1].equals("VBN")==false))
				{
					int mark=0;
					for(int j=i+2;j<len;j++)
					{ 
						if((j+1<len) && (mergePos[j].charAt(0)=='I') && (mergePos[j+1].charAt(0)=='N'||mergePos[j+1].charAt(0)=='P'))
						{ 
							j++;
							continue;
						}
						else if(j+1<len&&mergePos[j].charAt(0)=='N'&&mergePos[j+1].charAt(0)=='V'&&mergePos[j+1].equals("VBG")==false&&mergePos[j+1].equals("VBN")==false)
						{
							mark=1;
							break;
						}
					}
					if(mark!=1)
					{
						mainpos.add("Z");
						mainwords.add("that");
					    subsentence.add("that");
						mainwords.add(mergeWords[i]);
						mainpos.add(mergePos[i]);
					    subsentence.add(words[i]);
						mainwords.add(mergeWords[i+1]);
						mainpos.add(mergePos[i+1]);
					    subsentence.add(words[i+1]);
					    index[i]=1;
					    index[i+1]=1;
					    change=1;
						nextpos=i+2;
					}
					else
					{
						change=1;
						nextpos=getAllClauses1(7, i, mainwords.get(length-1),mainpos.get(length-1),subsentence.get(length-1),mergeWords, mergePos, words, Pos);
					}
				}
			}
			else if(mainpos.get(length-1).charAt(0)=='V')
			{ 
				if((mergePos[i].charAt(0)=='N'||mergePos[i].charAt(0)=='P') &&(mergePos[i+1].charAt(0)=='V'&&mergePos[i+1].equals("VBG")==false&&mergePos[i+1].equals("VBN")==false))
				{
					int mark=0;
					for(int j=i+2;j<len;j++)
					{ 
						if((j+1<len) && (mergePos[j].charAt(0)=='I') && (mergePos[j+1].charAt(0)=='N'||mergePos[j+1].charAt(0)=='P'))
						{ 
							j++;
							continue;
						}
						else if(j+1<len&&mergePos[j].charAt(0)=='N'&&mergePos[j+1].charAt(0)=='V'&&mergePos[j+1].equals("VBG")==false&&mergePos[j+1].equals("VBN")==false)
						{
							mark=1;
							break;
						}
					}
					if(mark!=1) 
					{
						mainpos.add("Z");
						mainwords.add("that");
					    subsentence.add("that");
						mainwords.add(mergeWords[i]);
						mainpos.add(mergePos[i]);
					    subsentence.add(words[i]);
						mainwords.add(mergeWords[i+1]);
						mainpos.add(mergePos[i+1]);
					    subsentence.add(words[i+1]);
					    index[i]=1;
					    index[i+1]=1;
					    change=1;
						nextpos=i+2;
						//continue;
					}
					else
					{
						change=1;
						nextpos=getAllClauses1(7, i, mainwords.get(length-1),mainpos.get(length-1),subsentence.get(length-1),mergeWords, mergePos, words, Pos);
					}
				}	
			}
			if(type==1)
			{
				int mainlen=mainpos.size();
				if(mergePos[i].charAt(0)=='V')
				{	
					for(int j=mainlen-1;j>=0;j--)
					{
						if(mainpos.get(j).charAt(0)=='V')
						{
							for(int k=0;k<subsentence.size();k++)
								System.out.print(subsentence.get(k)+" ");
							System.out.println();
							return i;
						}
					}
				}
			}
			else if(type==2)
			{
				int mainlen=mainpos.size();
				if(mergePos[i].charAt(0)=='V')
				{
					for(int j=mainlen-1;j>=0;j--)
					{
						if(mainpos.get(j).charAt(0)=='Z')
							break;
						if(mainpos.get(j).charAt(0)=='V')
						{
							for(int k=0;k<subsentence.size();k++)
								System.out.print(subsentence.get(k)+" ");
							System.out.println();
							return i;
						}
					}  
				}
				else if(mergePos[i].charAt(0)=='N')
				{
					if(mainpos.get(mainlen-1).charAt(0)=='N')
					{
						for(int k=0;k<subsentence.size();k++)
							System.out.print(subsentence.get(k)+" ");
						System.out.println();
						return i;
					}  
				}
				else if(mergePos[i].charAt(0)=='I'&&mergePos[i+1].charAt(0)!='N')
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i;

				}
				else if(mergePos[i].charAt(0)<='A'||mergePos[i].charAt(0)>='Z')
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i;
				}
			}
			else if(type==3)
			{
				int mainlen=mainpos.size();
				if(mergePos[i].charAt(0)=='V')
				{
					for(int j=mainlen-1;j>=0;j--)
					{
						if(mainpos.get(j).charAt(0)=='Z') ///��˵
							break;
						if(mainpos.get(j).charAt(0)=='V')
						{
							for(int k=0;k<subsentence.size();k++)
								System.out.print(subsentence.get(k)+" ");
							System.out.println();
							return i;
						}
					}  
				}
				else if(mergePos[i].charAt(0)=='N')
				{
					if(mainpos.get(mainlen-1).charAt(0)=='N')
					{
						for(int k=0;k<subsentence.size();k++)
							System.out.print(subsentence.get(k)+" ");
						System.out.println();
						return i;
					}  
				}
				else if(mergePos[i].charAt(0)=='I'&&mergePos[i+1].charAt(0)!='N')
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i;

				}
				else if(mergePos[i].charAt(0)<='A'||mergePos[i].charAt(0)>='Z')
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i;
				}
			}
			else if(type==4)
			{
				int mainlen=mainpos.size();
				if(mergePos[i].charAt(0)=='V')
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i;
					 
				}
				else if(mergePos[i].charAt(0)=='N')
				{
					if(mainpos.get(mainlen-1).charAt(0)=='N')
					{
						for(int k=0;k<subsentence.size();k++)
							System.out.print(subsentence.get(k)+" ");
						System.out.println();
						return i;
					}  
				}
				else if(mergePos[i].charAt(0)=='I'&&mergePos[i+1].charAt(0)!='N')
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i;

				}
				else if(mergePos[i].charAt(0)<='A'||mergePos[i].charAt(0)>='Z')
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i;
				}
			}
			else if(type==5||type==7)
			{
				int verb=0,noun=0,mainlen=mainpos.size();
				for(int j=mainlen-1;j>=2;j--)
				{
					if(mainpos.get(j).charAt(0)=='V')
					{
						if(mainpos.get(j-1).charAt(0)=='V')
						{
							continue;
						}
						verb++;
					}
				}
				if(verb>0&&mainpos.get(mainlen-1).equals("CC")&&mergePos[i].charAt(0)=='V')//which is cool and is worth 100 yuan
				{
					;
				}
				else if(mainpos.get(mainlen-1).charAt(0)=='V' && (mergePos[i].equals("VBD")||mergePos[i].equals("VBN")||mergePos[i].equals("VBG")))//be made
				{
					 ;
				}
				else if(mergePos[i].charAt(0)=='V'&&verb>0)//�������Ѿ���һ������
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i; 
				}
				else if(mergePos[i].charAt(0)=='W')
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i;
				}
				else if(mergePos[i].charAt(0)=='N')
				{
					if(mainpos.get(mainlen-1).charAt(0)=='N'||mainpos.get(mainlen-1).charAt(0)=='P')
					{
						for(int k=0;k<subsentence.size();k++)
							System.out.print(subsentence.get(k)+" ");
						System.out.println();
						return i;
					}  
				}
				else if(mergePos[i].charAt(0)=='I'&&mergePos[i+1].charAt(0)!='N'&&mergePos[i+1].equals("PRP")==false)
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i;

				}
				else if(mergePos[i].charAt(0)<='A'||mergePos[i].charAt(0)>='Z')
				{
					for(int k=0;k<subsentence.size();k++)
						System.out.print(subsentence.get(k)+" ");
					System.out.println();
					return i;
				}
			}
			else if(type==6)
			{}
			if(change==1)
			{
				i=nextpos-1;
			}
			else 
			{
				index[i]=1;
				if(mergePos[i].charAt(0)=='W'||mergePos[i].charAt(0)=='I'||mergePos[i].charAt(0)=='V'||mergePos[i].charAt(0)=='N'||mergePos[i].equals("CC")||mergePos[i].charAt(0)=='P')
				{
					mainwords.add(mergeWords[i]);
					mainpos.add(mergePos[i]);
				}
				subsentence.add(words[i]);
			}
		}
		for(int k=0;k<subsentence.size();k++)
			System.out.print(subsentence.get(k)+" ");
		System.out.println();
		return 0;
	}
}
