package com.sectong.util;

import java.util.Date;


public class TEST {
public static void main(String[] args){
	
	System.out.println("a:"+MD5Tools.getMD5StrFormTime("16" + "18086628278"
			+ "$2a$10$OB3Ni.NElsl5i1q6Acj8sOBAoMtoM3wmHjubaX/CZrddC5y5wfQje"));
 
	String[] ls=MD5Tools.getMD5StrFormTimes("16" + "18086628278"
			+ "$2a$10$OB3Ni.NElsl5i1q6Acj8sOBAoMtoM3wmHjubaX/CZrddC5y5wfQje");
	for(String s:ls){
		System.out.println(s);
	}
}
}
