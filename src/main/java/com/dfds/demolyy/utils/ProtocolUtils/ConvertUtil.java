package com.dfds.demolyy.utils.ProtocolUtils;
import java.io.ByteArrayInputStream;  
import java.io.ByteArrayOutputStream;  
import java.io.IOException;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.io.Serializable;  
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 进制转换工具类
 */
public class ConvertUtil {
	 	public final static char[] BToA = "0123456789abcdef".toCharArray();  
	  
	    private ConvertUtil() {  
	  
	    }  
	    
	    public static String getNowDate(){
	    	try {
	    		java.util.Calendar c=java.util.Calendar.getInstance();   
			    java.text.SimpleDateFormat f=new java.text.SimpleDateFormat("MM月dd日  HH:mm");   
			    return f.format(c.getTime()); 
			} catch (Exception e) {
				throw e;
			}
	    }
	    
	    /**
	     * int值转换为4位的十六进制
	     */
	    public static String format4Hex(int n){
	    	try {
	    		String str=Integer.toHexString(n);
		    	int len=str.length();
		    	String hexStr = "";
		    	for(int i=len;i<4;i++){
				       if(i==len)
				    	   hexStr="0";
				       else
				    	   hexStr=hexStr+"0";
			    }
		    	return hexStr+str.toUpperCase();
			} catch (Exception e) {
				throw e;
			}
	  }
	    
	    /**
	     * 二进制转换成十六进制字符串
	     */
	    public static String BINTOHEX(String binstr){
	    	try {
	    		 int len = binstr.length() / 4;
				  String res = "";
				  for(int i = 0;i < len;i++){
				   char[] ch = new char[4];
				   binstr.getChars(i * 4, i * 4 + 4, ch, 0);
				   res = res + switchS(trans1(ch));
				  }
				  return res;
			} catch (Exception e) {
				throw e;
			}
		}
			 
		public static int trans1(char[] ch){
			try {
				int sum = 0;
				for(int i = 0;i < 4;i++){
				int y = 8;
				   if(ch[i] == '1'){
					    for(int j = 1;j <= i;j++){
					     y = y / 2;
					    }
					    sum = sum + y;
				   }
			    }
				return sum;
			} catch (Exception e) {
				throw e;
			}
		 }
			 
		 public static String switchS(int i){
			 try {
				 String s = "";
				  switch(i){
				   case 10:
				    s = "A";
				    break;
				   case 11:
				    s = "B";
				    break;
				   case 12:
				    s = "C";
				    break;
				   case 13:
				    s = "D";
				    break;
				   case 14:
				    s = "E";
				    break;
				   case 15:
				    s = "F";
				   default:
				    s = "" + i; 
				  }
				  return s;
			} catch (Exception e) {
				throw e;
			}
		 }
		 
		 /**
		  * 十六进制转换成二进制
		  * @param str
		  * @return
		  */
	    public static  String HEXTOBIN(String str)
	    {
	    	try {
	    		String resultStr = "";
		        String str2 = "";
		        for (int i = 0; i < str.length(); i++)
		        {
		            switch (str.substring(i, i+1).toUpperCase())
		            {
		                case "0":
		                    str2 = "0000";
		                    break;
		                case "1":
		                    str2 = "0001";
		                    break;
		                case "2":
		                    str2 = "0010";
		                    break;
		                case "3":
		                    str2 = "0011";
		                    break;
		                case "4":
		                    str2 = "0100";
		                    break;
		                case "5":
		                    str2 = "0101";
		                    break;
		                case "6":
		                    str2 = "0110";
		                    break;
		                case "7":
		                    str2 = "0111";
		                    break;
		                case "8":
		                    str2 = "1000";
		                    break;
		                case "9":
		                    str2 = "1001";
		                    break;
		                case "A":
		                    str2 = "1010";
		                    break;
		                case "B":
		                    str2 = "1011";
		                    break;
		                case "C":
		                    str2 = "1100";
		                    break;
		                case "D":
		                    str2 = "1101";
		                    break;
		                case "E":
		                    str2 = "1110";
		                    break;
		                case "F":
		                    str2 = "1111";
		                    break;
		            }
		            resultStr += str2;
		        }
		        return resultStr;
			} catch (Exception e) {
				throw e;
			}
	    }
		 
		 /**
		  * ASCII码转换为十六进制
		  * @param ch
		  * @return
		  */
		 public static String ASCTOHEX(String ch){
			 try {
				 char[] chararr = ch.toCharArray();
					String str = "";
					for(int i = 0;i<chararr.length;i++){
						int a=(int)chararr[i];     //ASCII   
				  		str +=Integer.toString(a,16);   //十六进制  
					}
						
				  	return str.toUpperCase();
			} catch (Exception e) {
				throw e;
			}
		}
		 
		 /**
		  * 十六进制字符串转换为ASCII
		  */
		 public static String HEXTOASC(String hex){
			 try {
				 StringBuilder ascStr = new StringBuilder();
				  StringBuilder temp = new StringBuilder();
				  //每2个为一组转换为ASCII字符
				  for( int i=0; i<hex.length()-1; i+=2 ){
				      String output = hex.substring(i, (i + 2));
				      int decimal = Integer.parseInt(output, 16);
				      ascStr.append((char)decimal);
				      temp.append(decimal);
				  }
				  return ascStr.toString();
			} catch (Exception e) {
				throw e;
			}
		 }
		
		 /**
		  * 汉字转换为十六进制
		  * @param content
		  * @return
		  */
		 public static String GBTOHEX(String content){//将汉字转换为16进制数
			 try {
				 String enUnicode=null;
				  for(int i=0;i<content.length();i++){
				   if(i==0){
				       	  enUnicode=getHexString(Integer.toHexString(content.charAt(i)).toUpperCase())+"20";
				      }else{
				    	  enUnicode=enUnicode+getHexString(Integer.toHexString(content.charAt(i)).toUpperCase())+"20";
				      }
				  }
				  return enUnicode;
			} catch (Exception e) {
				throw e;
			}
		 }
		 
		 /**
		  * 16进制数转换为汉字
		  * @param strHex
		  * @return
		  */
		 public static String HEXTOGB(String strHex){
			 try {
				 String AA = "";
		         strHex = strHex.replace("00", "");
		         String[] s = strHex.split("20");
		         for (int i = 0; i < s.length; i++)
		         {
		             AA += ZF16ToZF(s[i]);
		         }
		         return AA;
			} catch (Exception e) {
				throw e;
			}
		 }
		 
		public  static String ZF16ToZF(String str16){
			try {
				 String ZFStr = "";
		         if(str16 == "")
		         {
		             return"";
		         }
		         if (str16.length() % 2 != 0)
		         {
		             str16 += "20";
		         }
		         byte[] bi = new byte[str16.length() / 2];
		         for (int i = 0; i < bi.length; i ++)
		         {
		             bi[i] = hexStringToBytes(str16.substring(i*2, i*2+2));
		         }
		         try {
					ZFStr = new String(bi,"GBK");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
		         return ZFStr;
			} catch (Exception e) {
				throw e;
			}
		 }
		
		public static byte hexStringToBytes(String hexString) {  
			try {
				hexString = hexString.toUpperCase();  
			    char[] hexChars = hexString.toCharArray();  
			    byte d;  
			    d = (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));  
			    return d;  
			} catch (Exception e) {
				throw e;
			}
		}  
		
		public  static byte charToByte(char c) {  
			 return (byte) "0123456789ABCDEF".indexOf(c);  
		}  
		 
		 /**
		  * 字符串转换为4位字符串  14----0014
		  * @param hexString
		  * @return
		  */
		 public static String getHexString(String hexString){
			 try {
				 String hexStr="";
			      for(int i=hexString.length();i<4;i++){
				       if(i==hexString.length())
				    	   hexStr="0";
				       else
				    	   hexStr=hexStr+"0";
			      }
			      return hexStr+hexString;
			} catch (Exception e) {
				throw e;
			}
		 }
		 
	  
	    /** 
	     * 把16进制字符串转换成字节数组 byte[]
	     * @param hex 
	     * @return 
	     */  
	    public static byte[] hexStringToByte(String hex) { 
	    	try {
	    		   int len = (hex.length() / 2);  
	   	        byte[] result = new byte[len];  
	   	        char[] achar = hex.toCharArray();  
	   	        for (int i = 0; i < len; i++) {  
	   	            int pos = i * 2;  
	   	            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));  
	   	        }  
	   	        return result;  
			} catch (Exception e) {
				throw e;
			}
	    }  
	    
	  
	  
	    private static byte toByte(char c) {  
	        byte b = (byte) "0123456789ABCDEF".indexOf(c);  
	        return b;  
	    }  
	  
	    /** 
	     * 把字节数组byte[]转换成16进制字符串 
	     * @param bArray 
	     * @return 
	     */  
	    public static final String bytesToHexString(byte[] bArray) {  
	    	try {
	    		if(bArray == null )  
		        {  
		            return "";  
		        }  
		        StringBuffer sb = new StringBuffer(bArray.length);  
		        String sTemp;  
		        for (int i = 0; i < bArray.length; i++) {  
		            sTemp = Integer.toHexString(0xFF & bArray[i]);  
		            if (sTemp.length() < 2)  
		                sb.append(0);  
		            sb.append(sTemp.toUpperCase());  
		        }  
		        return sb.toString();  
			} catch (Exception e) {
				throw e;
			}
	    }  
	    
	    /**
	     * 从一个byte[]数组中截取一部分
	     * @param src
	     * @param begin
	     * @param count
	     * @return
	     */
	    public static byte[] subBytes(byte[] src, int begin, int count) {
	        byte[] bs = new byte[count];
	        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
	        return bs;
	    }
	  
	    /** 
	     * 把字节数组转换为对象 
	     *  
	     * @param bytes 
	     * @return 
	     * @throws IOException 
	     * @throws ClassNotFoundException 
	     */  
	    public static final Object bytesToObject(byte[] bytes) throws IOException,  
	            ClassNotFoundException {  
	        ByteArrayInputStream in = new ByteArrayInputStream(bytes);  
	        ObjectInputStream oi = new ObjectInputStream(in);  
	        Object o = oi.readObject();  
	        oi.close();  
	        return o;  
	    }  
	  
	    /** 
	     * 把可序列化对象转换成字节数组 
	     *  
	     * @param s 
	     * @return 
	     * @throws IOException 
	     */  
	    public static final byte[] objectToBytes(Serializable s) throws IOException {  
	        ByteArrayOutputStream out = new ByteArrayOutputStream();  
	        ObjectOutputStream ot = new ObjectOutputStream(out);  
	        ot.writeObject(s);  
	        ot.flush();  
	        ot.close();  
	        return out.toByteArray();  
	    }  
	  
	    public static final String objectToHexString(Serializable s)  
	            throws IOException {  
	        return bytesToHexString(objectToBytes(s));  
	    }  
	  
	    public static final Object hexStringToObject(String hex)  
	            throws IOException, ClassNotFoundException {  
	        return bytesToObject(hexStringToByte(hex));  
	    }  
	  
	    /** 
	     * @函数功能: BCD码转为10进制串(阿拉伯数据) 
	     * @输入参数: BCD码 
	     * @输出结果: 10进制串 
	     */  
	    public static String bcd2Str(byte[] bytes) {  
	        StringBuffer temp = new StringBuffer(bytes.length * 2);  
	  
	        for (int i = 0; i < bytes.length; i++) {  
	            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));  
	            temp.append((byte) (bytes[i] & 0x0f));  
	        }  
	        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp  
	                .toString().substring(1) : temp.toString();  
	    }  
	  
	    /** 
	     * @函数功能: 10进制串转为BCD码 
	     * @输入参数: 10进制串 
	     * @输出结果: BCD码 
	     */  
	    public static byte[] str2Bcd(String asc) {  
	        int len = asc.length();  
	        int mod = len % 2;  
	  
	        if (mod != 0) {  
	            asc = "0" + asc;  
	            len = asc.length();  
	        }  
	  
	        byte abt[] = new byte[len];  
	        if (len >= 2) {  
	            len = len / 2;  
	        }  
	  
	        byte bbt[] = new byte[len];  
	        abt = asc.getBytes();  
	        int j, k;  
	  
	        for (int p = 0; p < asc.length() / 2; p++) {  
	            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {  
	                j = abt[2 * p] - '0';  
	            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {  
	                j = abt[2 * p] - 'a' + 0x0a;  
	            } else {  
	                j = abt[2 * p] - 'A' + 0x0a;  
	            }  
	  
	            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {  
	                k = abt[2 * p + 1] - '0';  
	            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {  
	                k = abt[2 * p + 1] - 'a' + 0x0a;  
	            } else {  
	                k = abt[2 * p + 1] - 'A' + 0x0a;  
	            }  
	  
	            int a = (j << 4) + k;  
	            byte b = (byte) a;  
	            bbt[p] = b;  
	        }  
	        return bbt;  
	    }  
	  
	    public static String BCD2ASC(byte[] bytes) { 
	    	try {
	    		 StringBuffer temp = new StringBuffer(bytes.length * 2);  
	    		  
	 	        for (int i = 0; i < bytes.length; i++) {  
	 	            int h = ((bytes[i] & 0xf0) >>> 4);  
	 	            int l = (bytes[i] & 0x0f);  
	 	            temp.append(BToA[h]).append(BToA[l]);  
	 	        }  
	 	        return temp.toString(); 
			} catch (Exception e) {
				throw e;
			}
	    }  
	  
	    /** 
	     * 两字符数组异或 
	     */  
	    public static byte[] byteArrXor(byte[] arr1, byte[] arr2, int len){  
	    	try {
	    		 byte[] dest = new byte[len];  
	 	        if((arr1.length < len) || (arr2.length < len)){  
	 	            return null;  
	 	        }  
	 	        for(int i = 0;i < len;i++){  
	 	            dest[i] = (byte)(arr1[i] ^ arr2[i]);  
	 	        }  
	 	        return dest;  
			} catch (Exception e) {
				throw e;
			}
	    }  
	    
	    /**
	     * 获取明天的日期
	     * @return
	     */
	    @SuppressWarnings("static-access")
		public static String getTomorrowDate() {
			Date date=new Date();//取时间
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
			date=calendar.getTime(); //这个时间就是日期往后推一天的结果
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateString = formatter.format(date);
 
			return dateString;
		}
	    
	    public static void main(String[] args) {
//			String s = "4130";
//			System.out.println(HEXTOASC(s));
	    	System.out.println(getNowDate());
		}
	      
}