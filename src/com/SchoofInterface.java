package com;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
public class SchoofInterface {
	public static String Directory = "";
	public static String FileName = "schoof.exe";
	
	@SuppressWarnings("empty-statement")
	public static BigInteger getNP(BigInteger A, BigInteger B, BigInteger P)
	{
		BigInteger NP = null;
		
		try
		{
			Process Proc = Runtime.getRuntime().exec(Directory + FileName + " " + P + " " + A + " " + B);
			BufferedReader BR = new BufferedReader(new InputStreamReader(Proc.getInputStream()));

			String Line = null;
			while ((Line = BR.readLine()) != null)
			{
				if (!Line.startsWith("NP= "))
					continue;

				int Stop = 0;
				for (Stop = 4; Stop < Line.length() && Character.isDigit(Line.charAt(Stop)); Stop++);

				NP = new BigInteger(Line.substring(4, Stop));
				break;
			}
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
		
		return NP;
	}
}
