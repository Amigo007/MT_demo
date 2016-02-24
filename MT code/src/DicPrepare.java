import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
 
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
 


public class DicPrepare {
	
	/**
	 * fixedPhrase  固定的词组，一般不可以改变 the Great Wall
	 * fixedPhraseCombination 固定的组合，一般也不变，例如so.beautiful.that...，as.well.as
	 * nounsurfaceSemantic 一些非常基础的词性，例如sb，sth之类的
	 */
	 public HashMap<String, String> fixedPhrase=new HashMap<String, String>();
	 public Set<String> fixedPhraseCombination=new HashSet<String>();
	 public HashMap<String, String> nounsurfaceSemantic=new HashMap<String, String>();
	 		
	public void readTxtFile(String filePath,String code,int type){
	 try {
	     String encoding=code;
	     File file=new File(filePath); 
	     if(file.isFile() && file.exists()){ //判断txt文件是否存在
	         InputStreamReader read = new InputStreamReader(new FileInputStream(file),encoding);//考虑到编码格式
	         BufferedReader bufferedReader = new BufferedReader(read);
	         String lineTxt = "";
	         int cnt=0;
	         while((lineTxt = bufferedReader.readLine()) != null)
	         {
	        	 cnt++;
	        	 if(cnt==1)
	        		 continue;
	             
	             
	             if(type==1)
	             {
	            	 String[] name=lineTxt.split(" ");
	            	 fixedPhrase.put(name[0], name[1]);
	             }
	             else if(type==2){
	            	 fixedPhraseCombination.add(lineTxt); 
				} 
	             else if(type==3){
	            	 String[] name=lineTxt.split(" ");
	            	 nounsurfaceSemantic.put(name[0], name[1]); 
				} 
	         }
	         read.close();
	     }
	     }
	     catch (Exception e) { 
	            System.out.println("读取文件内容出错");
	            e.printStackTrace();
	        }
	    }
		
}
