package lexicoCalc;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author 202010557@uesb.edu.br
 */

/*
 * Aceita espacos entre os operandos e operadores?
 * 	R: sim, uma vez que a leitura de caracteres de espaco nao implicam em uma mudanca do estado atual
 * 
 * Qual o alfabeto?
 * 	R: [0-9], ., +, *, (, ), =
 * 
 * Tokens necessarios:
 * 	R:
 *-------|-------------------|---------------------------------------------------------------------------|
 * TOKEN |  LEXEMAS EXEMPLO  | 						 DESCRICAO INFORMAL DO PADRAO                        |
 * ------|-------------------|---------------------------------------------------------------------------|
 * bracks|		  (, )		 |  							parenteses                                   |  
 *  int  |	  1, 38274, 9    |    			numero inteiro ou parte inteira de um numero				 |
 *  dec  |      ., .123      | parte decimal de um numero, podendo ou nao ser sucedida da parte inteira  |
 *  op   |       +, *        |            		 operadores de soma e multiplicacao               		 |
 * equals|         =         | 		caractere final, =. necessario para reconhecimento da cadeia 		 |
 * ------|-------------------|---------------------------------------------------------------------------|
 * 
 * obs: o analisador lexico faz o papel do analisador sintatico na parte decimal, onde subcadeias de '.'
 * sucessivos nao sao reconhecidas
 * 
 * ex: "12..2 + 3 =" nao eh aceito no analisador lexico, embora teoricamente isso nao eh da competencia dele
 */

public class lexicoCalc {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Estado estadoAtual = Estado.BEGIN;
        StringBuilder intBuilder = new StringBuilder();
        StringBuilder decBuilder = new StringBuilder();
        
        ArrayList <Character> BRACKS = new ArrayList<>();
        ArrayList <String> INT = new ArrayList<>();
        ArrayList <String> DEC = new ArrayList<>();
        ArrayList <Character> OP = new ArrayList<>();
        ArrayList <Character> EQUALS = new ArrayList<>();
        
        System.out.println("Introduza uma expressao:\n");
        String input = scanner.nextLine();
        char c;
        boolean erro = false;
        
        for (int i = 0; i < input.length(); i++) {
        	c = input.charAt(i);
	        switch (estadoAtual) {
	            case BEGIN:
	            	if (isNumeric(String.valueOf(c))) {
	            		estadoAtual = Estado.INT;
	            		intBuilder.append(c);
	            	} else {
	            		switch (c) {
		            		case '=':
		            			estadoAtual = Estado.EQUALS;
		            			EQUALS.add(c);
		            			break;
		            			
		            		case '.':
		            			estadoAtual = Estado.DEC;
		            			decBuilder.append(c);
		            			break;
		            			
		            		case ' ':
		            			estadoAtual = Estado.BEGIN;	// permanece no mesmo estado (nem precisava dessa linha?)
		            			break;
		            			
		            		case '(':
		            		case ')':
		            			estadoAtual = Estado.BRACKS;
		            			BRACKS.add(c);
		            			break;
		            			
		            		default:
		            			erro = true;
	            		} // switch  (c)
	            	} // else (c nao numerico)
	            	
	            	break; // switch (estadoAtual) case BEGIN
	            		
	            case BRACKS:
	            	if (isNumeric(String.valueOf(c))) {
	            		estadoAtual = Estado.INT;
	            		intBuilder.append(c);
	            	} else {
	            		switch (c) {
		            		case '=':
		            			estadoAtual = Estado.EQUALS;
		            			EQUALS.add(c);
		            			break;
		            			
		            		case '.':
		            			estadoAtual = Estado.DEC;
		            			decBuilder.append(c);
		            			break;
		            			
		            		case '(':
		            		case ')':
		            			estadoAtual = Estado.BRACKS;
		            			BRACKS.add(c);
		            			break;
		            			
		            		case ' ':
		            			estadoAtual = Estado.BRACKS;
		            			break;
		            			
		            		case '+':
		            		case '*':
		            			estadoAtual = Estado.OP;
		            			OP.add(c);
		            			break;
		            			
		            		default:
		            			erro = true;
	            		} // switch  (c)
	            	} // else (c nao numerico)
	            	
	            	break; // switch (estadoAtual) case BRACKS
	                
	            case INT:
	            	if (isNumeric(String.valueOf(c))) {
	            		estadoAtual = Estado.INT;
	            		intBuilder.append(c);	
	            	} else {
	            		INT.add(intBuilder.toString());
	            		intBuilder.setLength(0);	// zerar o stringbuilder
	            		switch (c) {
		            		case '=':
		            			estadoAtual = Estado.EQUALS;
		            			EQUALS.add(c);
		            			break;
		            			
		            		case '.':
		            			estadoAtual = Estado.DEC;
		            			decBuilder.append(c);
		            			break;
		            			
		            		case ' ':
		            			estadoAtual = Estado.INT;
		            			intBuilder.append(c);
		            			break;
		            			
		            		case '(':
		            		case ')':
		            			estadoAtual = Estado.BRACKS;
		            			BRACKS.add(c);
		            			break;
		            		
		            		case '+':
		            		case '*':
		            			estadoAtual = Estado.OP;
		            			OP.add(c);
		            			break;
		            			
		            		default:
		            			erro = true;
	            		} // switch  (c)
	            	} // else (c nao numerico)
	            	
	            	break; // switch (estadoAtual) case INT
	                
	            case DEC:
	            	if (isNumeric(String.valueOf(c))) {
	            		estadoAtual = Estado.DEC;
	            		decBuilder.append(c);
	        		} else {
	        			DEC.add(decBuilder.toString());
	        			decBuilder.setLength(0);
	            		switch (c) {
		            		case '=':
		            			estadoAtual = Estado.EQUALS;
		            			EQUALS.add(c);
		            			break;
		            			
		            		case ' ':
		            			estadoAtual = Estado.DEC;
		            			break;
		            			
		            		case '(':
		            		case ')':
		            			estadoAtual = Estado.BRACKS;
		            			BRACKS.add(c);
		            			break;
		            			
		            		case '+':
		            		case '*':
		            			estadoAtual = Estado.OP;
		            			OP.add(c);
		            			break;
		            			
		            		default:
		            			erro = true;
	            		} // switch  (c)
	            	} // else (c nao numerico)
	            	
	            	break; // switch (estadoAtual) case DEC
	            	
	            case OP:
	            	if (isNumeric(String.valueOf(c))) {
	            		estadoAtual = Estado.INT;
	            		intBuilder.append(c);
	            	} else {
	            		switch (c) {
		            		case '=':
		            			estadoAtual = Estado.EQUALS;
		            			EQUALS.add(c);
		            			break;
		            			
		            		case '.':
		            			estadoAtual = Estado.DEC;
		            			decBuilder.append(c);
		            			break;
		            			
		            		case '+':
		            		case '*':
		            			estadoAtual = Estado.OP;
		            			OP.add(c);
		            			break;	
		            			
		            		case ' ':
		            			estadoAtual = Estado.OP;
		            			break;
		            			
		            		case '(':
		            		case ')':
		            			estadoAtual = Estado.BRACKS;
		            			BRACKS.add(c);
		            			break;
		            			
		            		default:
		            			erro = true;
	            		} // switch  (c)
	            	} // else (c nao numerico)
	            	
	            	break; // switch (estadoAtual) case OP
	
            } // switch (estadoAtual)
        } // for (int i = 0; i < input.length(); i++)
        
      if (estadoAtual == Estado.EQUALS && !erro) {
        System.out.println(" :) \t O string foi reconhecido");
        
        System.out.println("\nbrackets |" + BRACKS.toString());
        System.out.println("int \t |" + INT.toString());
        System.out.println("dec \t |" + DEC.toString());
        System.out.println("op \t |" + OP.toString());
        System.out.println("equals   |" + EQUALS.toString());
        
      } else
        System.out.println(" :( \t O string NAO foi reconhecido");
      
      scanner.close();

    }
    
    
    /**
     * @see <a href="https://www.baeldung.com/java-check-string-number#regex>Check If a String Is Numeric in Java</a>
     */
    
    private static Pattern pattern = Pattern.compile("[0-9]");
    
    public static boolean isNumeric(String string) {
    	return pattern.matcher(string).matches();
    }
    
}
