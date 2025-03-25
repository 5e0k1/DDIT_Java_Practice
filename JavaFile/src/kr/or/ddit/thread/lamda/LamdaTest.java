package kr.or.ddit.thread.lamda;

import kr.or.ddit.util.ScanUtil;

public class LamdaTest {
	public static void main(String[] args) {
		// 람다식을 이용해서 입력받은 문자 길이를 출력 하시오.
		Lamdas lam1 = str -> System.out.println("입력한 문자의 길이 : "+str.length());
		
		// 람다식을 이용해서 1~n까지의 짝수만 출력
		Lamdas lam2 = str -> {
			int n = Integer.valueOf(str);
			System.out.println("==입력한 숫자까지 짝수들 모음==");
			for(int i=2; i<=n; i+=2) {
				System.out.println(i);
			}
		};
		// 람다식을 이용해서 1~n까지의 홀수 값의 합을 리턴.
		Sum lam3 = n -> {
			int sum=0;
			System.out.println("==입력한 숫자까지 홀수의 합==");
			for(int i=1; i<=n; i+=2) {
				sum += i;
			}
			return sum;
		};
		
		/////////////////////실행///////////////////////////
		
		lam1.print(ScanUtil.nextLine("문자열입력 : "));
		lam2.print(ScanUtil.nextLine("정수 n 입력 : "));
		int oddSum = lam3.sum(ScanUtil.nextInt("정수 n 입력 : "));
		System.out.println("입력한 숫자까지 홀수의 합 : "+ oddSum);
	}
}
interface Lamdas{
	void print(String str);
}
interface Sum{
	int sum(int n);
}

