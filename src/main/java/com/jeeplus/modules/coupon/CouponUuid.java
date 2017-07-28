package com.jeeplus.modules.coupon;

import java.util.Random;

public class CouponUuid {
	
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',  
        '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',  
        'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',  
        'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',  
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',  
        'Z' }; 
	
	/** 
	 * 随机ID生成器，由数字、小写字母和大写字母组成 
	 *  
	 * @param size 
	 * @return 
	 */  
	public static String getCouponNo(int size) {  
	    Random random = new Random();  
	    char[] cs = new char[size];  
	    for (int i = 0; i < cs.length; i++) {  
	    	if(i == 4 || i == 9 || i == 14){
	    		cs[i]='-';
	    	}else{
	    		cs[i] = digits[random.nextInt(digits.length)];  
	    	}
	    }  
	    return new String(cs);  
	}
	
    public static void main(String[] args) {
        System.out.println("-----"+getCouponNo(19).toUpperCase());
    }
}
