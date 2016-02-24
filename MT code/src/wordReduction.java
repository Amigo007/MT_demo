import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


public class wordReduction {
	
	static Map<String, String> dic=new LinkedHashMap<String, String>();//用来存储简单的词与词性的映射关系
	Map<String, String> goneToOrigin=new LinkedHashMap<String, String>();//用来存储不规则形式的过去式和复数形式以及他们对应的原词汇
	/**
	 * 判断文件的编码格式
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String codeString(String fileName) throws Exception{  
	    BufferedInputStream bin = new BufferedInputStream(  
	    new FileInputStream(fileName));  
	    int p = (bin.read() << 8) + bin.read();  
	    String code = null;        
	    switch (p) {  
	        case 0xefbb:  
	            code = "UTF-8";  
	            break;  
	        case 0xfffe:  
	            code = "Unicode";  
	            break;  
	        case 0xfeff:  
	            code = "UTF-16BE";  
	            break;  
	        default:  
	            code = "GBK";  
	    }  
	    bin.close();
	    return code;  
	}  
	
  
	/**
	 * 按照code编码方式读取filepath下的文件，获取dic和goneToOrigin的结果
	 * @param filePath
	 * @param code
	 */
	 public void readTxtFile(String filePath,String code){
	        try {
	                String encoding=code;
	                File file=new File(filePath); 
	                if(file.isFile() && file.exists()){ //判断txt文件是否存在
	                    InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
	                    BufferedReader bufferedReader = new BufferedReader(read);
	                    String lineTxt = "";
	                    while((lineTxt = bufferedReader.readLine()) != null)
	                    {
	                    	String[] name=lineTxt.split("");     	 
	                    	dic.put(name[0], name[1]);
	                    	String content="";
	                    	for(int i=2;i<name.length;i++)
	                    	{
	                    		content=content+name[i];
	                    	}
	                    	int index=content.indexOf("的过去");
	                    	int index1=content.indexOf("的复数");	
                    		String tmp=""; 
	                    	if(index!=-1)
	                    	{
	                    		String one=(String)content.subSequence(index-1, index);
	                    		while(one.equals(" "))
	                    		{
	                    			index--;
	                    			one=(String)content.subSequence(index-1, index);
	                    		}
	                    		while(one.compareTo("a")>=0&&one.compareTo("z")<=0)
	                    		{
	                    			tmp=(String) content.subSequence(index-1, index)+tmp;
	                    			index--;
	                    			if(index>0){
	                    				one=(String)content.subSequence(index-1, index);
	                    			}
	                    			else {
										break;
									}
	                    		} 
			                   	goneToOrigin.put(name[0], tmp);
	                    	}
	                    	else if(index1!=-1)
	                    	{
	                   			String one=(String)content.subSequence(index1-1, index1);
		                   		while(one.equals(" "))
		                    	{
		                    		index1--;
		                   			one=(String)content.subSequence(index1-1, index1);
		                   		}
		                   		while(one.compareTo("a")>=0&&one.compareTo("z")<=0)
		                    		{
		                    			tmp=(String) content.subSequence(index1-1, index1)+tmp;
		                    			index1--;
		                    			if(index1>0){
		                    				one=(String)content.subSequence(index1-1, index1);
		                    			}
		                    			else {
											break;
										}
	                    		} 
			                   	goneToOrigin.put(name[0], tmp);
	                    	}
	                    }
	                    read.close();
	                }
	        }catch (Exception e) { 
	            System.out.println("读取文件内容出错");
	            e.printStackTrace();
	        }
	    }
		
	 /**
	  * 输出goneToOrigin的情况
	  */
	 public void printData()
		{
			Iterator<Entry<String, String>> iter = goneToOrigin.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
				String key = entry.getKey();
				String val = entry.getValue();
				System.out.println(key+"  "+val);
				}
		} 
	 
	 /**
	  * 输出单词word的原型
	  * @param word
	  */
	 public String OriginalWord(String word)
	 {
		 String str=goneToOrigin.get(word);
		 if(str!=null)//不规则的过去式或者复数形式
		 {
			 return str;
		 } 
		 else {
			str=dic.get(word);//规则的原型
			if(str!=null)
			{
				return word;
			}
	        else //规则的ing形式或者过去式或者复数形式
		    {
	        	if(returnBack(word)==null)
				{
	        		System.out.println("This word is not exist");
	        		return null;
				}
	        	else {
	        		return returnBack(word);
				}
			} 
		} 
	 }
	 
	 /**
	  * 输出单词word的原型以及它的词性
	  * @param word
	  */
	 public void OriginalWordAndProperty(String word)
	 {
		 String str=goneToOrigin.get(word);
		 if(str!=null)//不规则的过去式或者复数形式
		 {
			 System.out.println(str+ " " +dic.get(str));
		 } 
		 else {
			str=dic.get(word);//规则的原型
			if(str!=null)
			{
				System.out.println(word+ " " +str);
			}
	        else //规则的ing形式或者过去式或者复数形式
		    {
	        	if(returnBack(word)==null)
				{
	        		System.out.println("This word is not exist");
				}
	        	else {
	        		System.out.println(returnBack(word)+" "+dic.get(returnBack(word)));
				}
			} 
		} 
	 }
	 
	 /**
	  * 返回词word的原始形态，
	  * @param word 词的当前形态，如第三人称单数等情况
	  * @return
	  */
	 public String returnBack(String word)
	 { 
		 char[] wordchar = word.toCharArray();
		 int pos=wordchar.length;
		 if(pos>2&&wordchar[pos-1]=='s')
		 {
			 if(dic.get(charArraytoString(wordchar,pos-1))!=null)//*s -> * (SINGULAR3)
			 {
				 return charArraytoString(wordchar,pos-1);
			 }
			 else if(wordchar[pos-2]=='e')
			 {
				 if(dic.get(charArraytoString(wordchar,pos-2))!=null)//*es -> * (SINGULAR3)
				 {
					 return charArraytoString(wordchar,pos-2);
				 }
				 else if(pos>3&&wordchar[pos-3]=='i')//*ies -> *y (SINGULAR3)
				 {
					 char c=wordchar[pos-3];
					 wordchar[pos-3]='y';
					 if(dic.get(charArraytoString(wordchar,pos-2))!=null)
                     {
						 return charArraytoString(wordchar,pos-2);
                     }
					 wordchar[pos-3]=c;
				 }
     		 } 
		 }
		 else if(pos>3&&wordchar[pos-1]=='g'&&wordchar[pos-2]=='n'&&wordchar[pos-3]=='i')
		 {
			 String str=charArraytoString(wordchar,pos-3);
			 if(dic.get(str)!=null)//*ing -> *e (VING)
			 {
				 return str;
			 } 
			 else if(dic.get(str+"e")!=null)//*ing -> *e (VING)
			 {
				 return str+"e";
			 }
			 else if(pos>4&&wordchar[pos-4]=='y')//*ying -> *ie (VING)
			 {
				 char a=wordchar[pos-4];
				 char b=wordchar[pos-5];
				 wordchar[pos-4]='i';
				 wordchar[pos-3]='e';
				 String tmpstr=charArraytoString(wordchar,pos-2);
				 if(dic.get(tmpstr)!=null)
				 {
					 return tmpstr;
				 }
				 wordchar[pos-4]=a;
				 wordchar[pos-3]=b;
			 }
			 else if(pos>=6&&wordchar[pos-4]==wordchar[pos-5])//*??ing -> *? (VING)
			 {
				 String tmpstr=charArraytoString(wordchar,pos-4);
				 if(dic.get(tmpstr)!=null)
				 {
					 return tmpstr;
				 }
			 }
		 }
		 else if(pos>=3&&wordchar[pos-1]=='d'&&wordchar[pos-2]=='e')
		 {
			 String str=charArraytoString(wordchar,pos-2);
			 if(dic.get(str)!=null)//*ed -> * (PAST)(VEN)
			 {
				 return str;
			 }
			 else if(dic.get(str+"e")!=null)//*ed -> *e (PAST)(VEN)
			 {
				 return str+"e";
			 }
			 else if(pos>3&&wordchar[pos-3]=='i')//*ied -> *y (PAST)(VEN)
			 {
				 char c= wordchar[pos-3];
				 wordchar[pos-3]='y';
				 if(dic.get(charArraytoString(wordchar,pos-2))!=null)
				 {
					 return charArraytoString(wordchar,pos-2);
				 }
				 wordchar[pos-3]=c;
			 }
			 else if(pos>=5&&wordchar[pos-3]==wordchar[pos-4])//*??ed -> *? (PAST)(VEN)
			 {
				 if(dic.get(charArraytoString(wordchar,pos-3))!=null)
				 {
					 return dic.get(charArraytoString(wordchar,pos-3));
				 }
			 }
		 }
		 return null;
	 }
	 
	 
	 public String charArraytoString(char[] chararray,int pos)
	 {
		 char[] tmp=new char[pos];
		 for(int i=0;i<pos;i++)
		 {
			 tmp[i]=chararray[i];
		 }
		 return String.valueOf(tmp);
	 }
}
