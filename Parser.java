

import java.io.*;
import java.util.*;


public class Parser {
	
	public static void main(String[] args) throws IOException
	{
		FileReader fr = new FileReader("color.json");
		int i;
		char c;
		
		ArrayList<Character> jsonList = new ArrayList<Character>();
		while ((i = fr.read()) != -1) {
			c = (char) i;
			if (!Character.isWhitespace(c)) {
				
				jsonList.add(c);
			}
		}
		
		fr.close();
		try{
		Start s = new Start();
		
		int index = s.matchStart(jsonList, 0);
		
		if (index+1 == jsonList.size())
			System.out.println("Valid JSON");
		else
			System.out.println("Invalid JSON");
		}
		catch(ParseException e)
		{
			System.out.println(e);
		}
	}
}
class ParseException extends Exception{
	ParseException(String s)
	{
		super(s);
	}
}

class Start{
	
	public int matchStart(ArrayList<Character> jsonString,int index) throws ParseException
	{ 
		if(jsonString.get(index)=='[')
		{    
			   index++;
			index=matchArray(jsonString,index);
		}
		else if(jsonString.get(index)=='{')
		{   index++;
			index=matchObject(jsonString,index);
		}
		
	   
		return index;
	}
	public int matchObject(ArrayList<Character> jsonString,int index) throws ParseException
	{
		if(jsonString.get(index)!='}')
		{
			index=matchPair(jsonString,index);
			while(jsonString.get(index)!='}'){
				if(jsonString.get(index)!=','){
					throw new ParseException("Invalid JSON \n Missing , or } in object at location "+index);
				}
				index++;
				if (index >= jsonString.size()) {
					throw new ParseException("Invalid JSON \n Missing } in object at location "+index);
				}
				index=matchPair(jsonString,index);
			}
			
		}
		if (jsonString.get(index) != '}') {
			throw new ParseException("Invalid JSON \n Missing } for object at location "+index);
		}
		
		return index;
	}
	
	
	public int matchPair(ArrayList<Character> jsonString,int index) throws ParseException
	{
	   if(jsonString.get(index)!='"')
		  throw new ParseException("Invalid JSON \n expected string in object at location "+index);
	   index++;
	   index=matchString(jsonString,index);
	   if(jsonString.get(index)!=':')
		   throw new ParseException("Invalid JSON \n expected : in object at location "+index);
	   index++;
	   index=matchValue(jsonString,index);
	   return index;
	}
	public int matchArray(ArrayList<Character> jsonString,int index) throws ParseException
	{    
		if(jsonString.get(index)!=']')
		{
			index=matchValue(jsonString,index);
			while(jsonString.get(index)!=']'){
				if(jsonString.get(index)!=','){
					throw new ParseException("Invalid JSON \n Missing , or ] in array at location "+index);
				}
				index++;
				if (index >= jsonString.size()) {
					throw new ParseException("Invalid JSON \n Missing ] for array at location "+index);
				}
				index=matchValue(jsonString,index);
			}
			
		}
		if (jsonString.get(index) != ']') {
			throw new ParseException("Invalid JSON \n Missing ] for array at location "+index);
		}
		/*if(jsonString.get(index)==']')
			index++;*/
		return index;
	}
	
	
	public int matchValue(ArrayList<Character> jsonString,int index) throws ParseException
	{
		if(jsonString.get(index)>='0'&&jsonString.get(index)<='9')
			index=matchNumber(jsonString,index);
		//System.out.println(index);	
		else if(jsonString.get(index)=='n')
			index=matchNull(jsonString,index);
		else if(jsonString.get(index)=='t')
			index=matchTrue(jsonString,index);
		else if(jsonString.get(index)=='f')
				index=matchFalse(jsonString,index);
		
		else if(jsonString.get(index)=='[')
		{ 
		index=matchArray(jsonString,++index);
		index++;
		if (index >= jsonString.size()) {
			throw new ParseException("Invalid JSON \n Missing ] for array at location "+index);
		}
		}
		
		else if(jsonString.get(index)=='{')
		{ 
		index=matchObject(jsonString,++index);
		index++;
		if (index >= jsonString.size()) {
			throw new ParseException("Invalid JSON \n Missing } for object at location "+index);
		}
	    }
		
		else if(jsonString.get(index)=='"')
		{
			index=matchString(jsonString,++index);
			
			
		}
		return index;
	}
	
	public int matchString(ArrayList<Character> jsonString,int index) throws ParseException
	{
		while(jsonString.get(index)>='!'&&jsonString.get(index)<='~' )
			{ 
			   
				if(jsonString.get(index)=='\\')
				{ index++;
					if(jsonString.get(index)=='"'||jsonString.get(index)=='\\'||jsonString.get(index)=='/'||jsonString.get(index)=='b'||jsonString.get(index)=='f'||jsonString.get(index)=='n'||jsonString.get(index)=='r'||jsonString.get(index)=='t')
					{
						index++;
						continue;
					}
					else
					{
						throw new ParseException("Invalid JSON \n invalid character at location "+index);
					}
				}
				if(jsonString.get(index)=='"')
				{ 
					index++;
					//System.out.println(index);
					break;
				}
				index++;
				  if (index >= jsonString.size()) {
						throw new ParseException("Invalid JSON \n Missing \" for String at location "+index);
					}
				
			}
		
		return index;
	}
	
	
	
	public int matchNumber(ArrayList<Character> jsonString,int index)  throws ParseException
	{   
		if(jsonString.get(index)>='0'&&jsonString.get(index)<='9')
		{
			while(jsonString.get(index)>='0'&&jsonString.get(index)<='9')
				index++;
			if(jsonString.get(index)=='.')
			{   
				index++;
				if(!(jsonString.get(index)>='0'&&jsonString.get(index)<='9'))
					throw new ParseException("Invalid JSON \n expected digits after dot at "+index);
				index=matchDigits(jsonString,index);
			}
			if(jsonString.get(index)=='e'||jsonString.get(index)=='E')
			{
				index++;
				if(!(jsonString.get(index)>='0'&&jsonString.get(index)<='9'))
					throw new ParseException("Invalid JSON \n expected digits after exponent at "+index);
				index=matchExponent(jsonString,index);
			}
			
		}
		return index;
	}
	
	public int matchExponent(ArrayList<Character> jsonString,int index)
	{
		if(jsonString.get(index)=='+'||jsonString.get(index)=='-'||(jsonString.get(index)>='0'&&jsonString.get(index)<='9'))
		{
			index++;
			index=matchDigits(jsonString,index);
			
		}
		return index;
	}
	
	public int matchDigits(ArrayList<Character> jsonString,int index)
	{
		while(jsonString.get(index)>='0'&&jsonString.get(index)<='9')
			index++;
		return index;
	}
	
	public int matchNull(ArrayList<Character> jsonString,int index)
	{
		if(jsonString.get(index)=='n')
		{	index++;
			if(jsonString.get(index)=='u')
			{ index++;
			if(jsonString.get(index)=='l')
				{index++;
				if(jsonString.get(index)=='l')
					index++;
				}
			}
		}
		return index;
	}
	public int matchTrue(ArrayList<Character> jsonString,int index)
	{
		if(jsonString.get(index)=='t')
		{	index++;
			if(jsonString.get(index)=='r')
			{ index++;
			if(jsonString.get(index)=='u')
				{index++;
				if(jsonString.get(index)=='e')
					index++;
				}
			}
		}
		return index;
	}
	public int matchFalse(ArrayList<Character> jsonString,int index)
	{
		if(jsonString.get(index)=='f')
		{	index++;
			if(jsonString.get(index)=='a')
			{ index++;
			if(jsonString.get(index)=='l')
				{index++;
					if(jsonString.get(index)=='s')
					{
						index++;
						if(jsonString.get(index)=='e')
							index++;
					}
				}
			}
		}
		return index;
	}
}
