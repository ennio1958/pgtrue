package com.azserve.paginegialle;

public class Test {

	public static void main(String[] args) {
		String p = "(VE)";
		System.out.println(p.replaceAll("\\(", "").replaceAll("\\)",""));
	}
}
