package com.oxigeno.portal.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.sql.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	// Alfabeto que incluye mayúsculas, minúsculas, números y algunos símbolos
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
			//+ "abcdefghijklmnopqrstuvwxyz"
			+ "0123456789"
			+ "!@#$%^&*-_=+.";

	private static final SecureRandom random = new SecureRandom();

	public static boolean validarCuit(String sCUIT) {
		String aMult = "6789456789"; 
		int iResult = 0; 


		if (sCUIT.length () == 11) 
		{ 
			// La suma de los productos 
			for(int i = 0; i <= 9; i++) 
			{ 
				iResult += Integer.parseInt("" + sCUIT.charAt (i)) * Integer.parseInt("" + aMult.charAt(i)); 
			} 
			// El modulo de 11 
			iResult = (iResult % 11); 

			// Se compara el resultado con el digito verificador 
			return (iResult == Integer.parseInt (""+sCUIT.charAt (10))); 
		}     
		return false; 
	} 

	public static boolean isCreditCardValid(String cardNumber) { 
		String digitsOnly = cardNumber.replaceAll("[^\\d]", "");

		//Chequeamos que el numero entrado tenga formato num�rico...
		if (digitsOnly.matches("^?[0-9]+(\\.[0-9]+)")) {
			//alert("El n�mero de la tarjeta de cr�dito no tiene formato num�rico.");
			//campo.focus();
			return false;
		}
		//digitsOnly = getDigitsOnly(cardNumber); 
		int sum = 0; 
		int digit = 0; 
		int addend = 0; 
		boolean timesTwo = false; 

		for (int i = digitsOnly.length() - 1; i >= 0; i--) { 
			digit = Integer.parseInt(digitsOnly.substring(i, i + 1)); 
			if (timesTwo) { 
				addend = digit * 2; 
				if (addend > 9) { 
					addend -= 9; 
				} 
			} else { 
				addend = digit; 
			} 
			sum += addend; 
			timesTwo = !timesTwo; 
		} 

		int modulus = sum % 10; 
		return modulus == 0; 

	} 

	public static boolean validarCBU(String Dato){
		/* Clave Bancaria Uniforme

	 Copyright (c) 2003 Ernesto De Spirito
	 Adaptada por Jos� Luis Gonzalez para funcion Excel
	 www. latiumsoftware.com/es/index.php
	 www. latiumsoftware.com/es/pascal/0049.php
	 Determina si un n�mero de CBU es v�lido. ARGENTINA
	 La cadena que se pasa como par�metro debe estar formada
	 por 22 d�gitos num�ricos (no se admiten guiones, barras
	 ni espacios como parte de la cadena con el CBU).
	 El d�gito verificador est� en las posiciones 8 y 22
 	usando clave 10 con ponderador 9713
	--------------------------------------
		 */

		int [] Peso = new int [4];
		int j;
		int Suma, digito;
		Peso[0] = 3;
		Peso[1] = 1;
		Peso[2] = 7;
		Peso[3] = 9;

		//Verifica longitud
		if (Dato.length() != 22)
			return false;


		//Verifica que son todo n�meros
		for (int k = 0; k < Dato.length (); k++) {
			if (Dato.substring (k, k+1).charAt(0) < '0' || Dato.substring (k, k+1).charAt (0) > '9')
				return false;
		}

		//Verifica 8� D�gito
		Suma = 0;
		j = 0;
		for (int i = 7; i >= 1; i--) {
			Suma = Suma + (Integer.parseInt(Dato.substring(i-1, i)) * Peso[j % 4]);
			j = j + 1;
		} 
		digito = (10 - Suma % 10) % 10;
		if (Integer.parseInt(Dato.substring(7, 8)) != digito)
			return false;

		// Verifica 22� D�gito
		Suma = 0;
		j = 0;
		for (int i = 21; i >=9; i--) {
			Suma = Suma + (Integer.parseInt(Dato.substring(i-1, i)) *  Peso[j % 4]);
			j = j + 1;
		}
		digito = (10 - Suma % 10) % 10;
		if (Integer.parseInt(Dato.substring(21, 22)) != digito)
			return false;	
		return true;
	}


	public static boolean validateDateFormat(String fechadenacimiento) {
		return validateDateFormat(fechadenacimiento, false);
	}

	public static boolean validateDateFormat(String fechadenacimiento, boolean required ) {
		if (!required && (fechadenacimiento == null || fechadenacimiento.isEmpty()))
			return true;
		SimpleDateFormat sdf = new SimpleDateFormat ("dd/MM/yyyy");
		try {
			Object d = sdf.parse(fechadenacimiento);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public static boolean validateRange (Integer anio, int desde, int hasta) {
		return anio >= desde && anio <= hasta;
	}

	public static boolean validateData(String value, int size, boolean required, String regex) {
		value = value == null ? "": value;
		if ((value.isEmpty()) && required) 
			return false;

		if (value.length () > size) 
			return false;

		boolean b = true;
		if (!value.isEmpty() && regex != null) {
			b = value.matches(regex);
		}

		return b;
	}

	public static boolean validateData(String value, int size) {
		return validateData (value, size, false, null);
	}

	public static boolean isPatenteMoto (String sPatente) {
		//Checks for email addresses starting with
		//inappropriate symbols like dots or @ signs.
		sPatente = sPatente.toUpperCase();
		Pattern p = Pattern.compile("[0-9][0-9][0-9][A-Z][A-Z][A-Z]");
		Matcher m = p.matcher(sPatente);
		//Pattern p1 = Pattern.compile("[A-Z][A-Z][0-9][0-9][0-9][A-Z][A-Z]");
		//Matcher m1 = p1.matcher(sPatente);
		Pattern p2 = Pattern.compile("[A-Z][0-9][0-9][0-9][A-Z][A-Z][A-Z]");
		Matcher m2 = p2.matcher(sPatente);
		return m.matches() || 
				//m1.matches() || 
				m2.matches();
	}

	public static boolean isPatenteAuto (String sPatente) {
		//Checks for email addresses starting with
		//inappropriate symbols like dots or @ signs.
		sPatente = sPatente.toUpperCase();
		Pattern p = Pattern.compile("[A-Z][A-Z][A-Z][0-9][0-9][0-9]");
		Matcher m = p.matcher(sPatente);
		//Pattern p1 = Pattern.compile("[A-Z][A-Z][0-9][0-9][0-9][A-Z][A-Z]");
		//Matcher m1 = p1.matcher(sPatente);
		Pattern p2 = Pattern.compile("[A-Z][A-Z][0-9][0-9][0-9][A-Z][A-Z]");
		Matcher m2 = p2.matcher(sPatente);
		return m.matches() || 
				//m1.matches() || 
				m2.matches();
	}

	/*
	 * Oculta la tarjeta de credito
	 */
	public static String hideCard(String text, int tohide) {
		if (text == null)
			return null;
		StringBuffer sb = new StringBuffer ();
		for (int i = 0; i < text.length(); i++) {
			if (i< text.length()-tohide)
				sb.append("*");
			else
				sb.append(text.substring(i, i+1));
		}
		return sb.toString();
	}

	/*
	 * Oculta la tarjeta de credito
	 */
	public static String hideCard (String text) {
		return hideCard (text, 4);
	}

	/*
	 * Oculta la tarjeta de credito
	 */
	public static String hideAll(String text) {
		return hideCard (text, 0);
	}

	public static Integer vnull(Integer id) {
		if (id == null)
			return 0;
		return id;
	}

	/*
	 * Copia los attributos de un objeto a otro usando Reflection.
	 * TODO: revisar la copia de atributos, getter y setters no implementados
	 */
	public static HashMap Merge (HashMap oDest, HashMap oSource) throws IllegalArgumentException {
		return Merge (oDest, oSource, false);
	}
	public static HashMap Merge (HashMap<String, Object> oDest, HashMap<String, Object> oSource, boolean overwrite) throws IllegalArgumentException {
		for (Map.Entry entry: oSource.entrySet()) {
			if (entry.getValue() != null || overwrite)
				oDest.put((String) entry.getKey(), entry.getValue ());
		}
		return oDest;
	}

	public static ArrayList<Field> getAllFields(Class clazz, Integer modifier) {
		if (clazz == null) {
			return new ArrayList<Field> ();
		}

		ArrayList<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass(), modifier));
		ArrayList<Field> filteredFields = (ArrayList<Field>) Arrays.stream(clazz.getDeclaredFields())
				.filter(f -> (modifier == null || (f.getModifiers() & modifier) != 0))
				.collect(Collectors.toList());
		result.addAll(filteredFields);
		return result;
	}

	public static ArrayList<Method> getAllGetters(Class clazz) {
		if (clazz == null) {
			return new ArrayList<Method> ();
		}

		ArrayList<Method> result = new ArrayList<>(getAllGetters(clazz.getSuperclass()));
		ArrayList<Method> filteredFields = (ArrayList<Method>) Arrays.stream(clazz.getDeclaredMethods())
				.filter(f -> (f.getModifiers() & java.lang.reflect.Modifier.PUBLIC) != 0 && (
						f.getName().startsWith("get") ||
						f.getName().startsWith("is")
						) && f.getParameterCount() == 0
						)
				.collect(Collectors.toList());
		result.addAll(filteredFields);
		return result;
	}

	public static Object merge (Object oDest, Object oSource, boolean overwrite) throws IllegalArgumentException {
		try {
			if (oSource == null) 
				return oDest;
			Class clazz = oDest.getClass();
			//Field[] field = clazz.getDeclaredFields() ; 
			//Class superclazz = clazz.getSuperclass();
			//if (superclazz != null) field = Arrays.addAll(field, superclazz.getSuperclass().getDeclaredFields());
			ArrayList<Field> fieldsSource= getAllFields (oSource.getClass(), java.lang.reflect.Modifier.PRIVATE);
			ArrayList<Field> fieldsDest = getAllFields (clazz, java.lang.reflect.Modifier.PRIVATE);
			for (int i = 0; i < fieldsDest.size (); i++) {
				Object o1;
				Field fdest = fieldsDest.get(i);
				fdest.setAccessible(true);
				Class type = fieldsDest.get(i).getType();
				/*if (type.equals(String.class)) {
				} else if (type.equals(Integer.class)) {

				} else if (type.equals(Float.class)) {

				}else if (type.equals(Float.class)) {

				}else if (type.equals(Long.class)) {

				}else if (type.equals(Boolean.class)) {

				} else if (type.equals(String.class)) {

				} else {
				}*/
				for (Field fsource: fieldsSource) {
					fsource.setAccessible(true);
					if (fsource.getName().equals (fdest.getName ())) {
						o1 = fsource.get(oSource);
						if (o1 != null || overwrite) // si es null no lo copia
							fdest.set(oDest, o1);						
						break;
					}
				}

			}
			return oDest;
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new IllegalArgumentException (e);
		}
	}

	//	public static void objectDiferences (Object oSource, Object oDest, List<Values_> result, List<Object> ids) throws IllegalArgumentException, InvocationTargetException {
	//		try {
	//			if (oSource == null) 
	//				return;
	//			Class clazz = oDest.getClass();
	//			Class clazzSource = oSource.getClass();
	//			//Field[] field = clazz.getDeclaredFields() ; 
	//			//Class superclazz = clazz.getSuperclass();
	//			//if (superclazz != null) field = Arrays.addAll(field, superclazz.getSuperclass().getDeclaredFields());
	//			ArrayList<Field> fieldsSource= Util.getAllFields (clazzSource, java.lang.reflect.Modifier.PRIVATE);
	//			ArrayList<Field> fieldsDest = Util.getAllFields (clazz, java.lang.reflect.Modifier.PRIVATE);
	//			for (int i = 0; i < fieldsDest.size (); i++) {
	//				Object o1;
	//				Field fdest = fieldsDest.get(i);
	//				if ((fdest.getModifiers() & java.lang.reflect.Modifier.PRIVATE) != 0) {
	//					//Class type = fieldsDest.get(i).getType();
	//
	//					for (Field fsource: fieldsSource) {
	//						String name = fsource.getName(); 
	//						if (name.equals (fdest.getName ())) {
	//							fsource.setAccessible(true);
	//							fdest.setAccessible(true);
	//							Object vsource = fsource.get (oSource);
	//							Object vtarget = fdest.get (oDest);
	//							Values_ v = new Values_ ();
	//							v.setFieldname(fsource.getName());
	//							v.setSourceValue (vsource);
	//							v.setTargetValue (vtarget);
	//							if (isId (fdest))
	//								ids.add (vtarget);
	//							if (
	//									(
	//											(v.getSourceValue() == null || v.getTargetValue() == null) && 
	//											v.getSourceValue() != v.getTargetValue())
	//									||
	//									(v.getSourceValue() == null && v.getTargetValue() == null) 
	//									||
	//										(v.getSourceValue() != null && v.getTargetValue() == null) 
	//										||
	//									!v.getSourceValue().equals(v.getTargetValue())
	//								)
	//								result.add (v);
	//							break;
	//						}
	//					}
	//				}
	//
	//			}
	//		} catch (IllegalAccessException | IllegalArgumentException e) {
	//			throw new IllegalArgumentException (e);
	//		}
	//	}
	public static String getPKColumnName(Class<?> pojo) {

		if (pojo == null) 
			return null;

		String name = null;

		for (Field f : pojo.getDeclaredFields()) {

			Id id = null;
			Column column = null;

			Annotation[] as = f.getAnnotations();
			for (Annotation a : as) {
				if (a.annotationType() == Id.class) 
					id = (Id) a;
				else if (a.annotationType() == Column.class) 
					column = (Column) a;
			}

			if (id != null && column != null){
				name = column.name();
				break;
			}
		}

		if (name == null && pojo.getSuperclass() != Object.class)
			name = getPKColumnName(pojo.getSuperclass());

		return name;
	}

	private static boolean isId (Field f) {
		Id id = null; 
		Column column = null;
		Annotation[] as = f.getAnnotations();
		for (Annotation a : as) {
			if (a.annotationType() == Id.class) 
				id = (Id) a;
			else if (a.annotationType() == Column.class) 
				column = (Column) a;
		}
		return (id != null && column != null);
	}

	public static String getTableName(Class<?> c) {
		Table id = null; 
		Annotation[] as = c.getAnnotations();
		for (Annotation a : as) {
			if (a.annotationType() == Table.class) 
				id = (Table) a;
		}
		return id.name();
	}

	public static String renderToString(Object oValue, boolean bCleanBinary) {
		// Convierte el objeto a un formato HTML conveniente para la grilla
		String s = null;
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setGroupingUsed(false);
		if (oValue == null) {
			return "";
		}
		if (oValue instanceof String) {
			s = (String) oValue;
		}
		else if (oValue instanceof Integer) {
			s = ( (Integer) oValue).toString();
		}
		else if (oValue instanceof Boolean) {
			s = ( (Boolean) oValue).booleanValue() ? "1" : "0";
		}
		else if (oValue instanceof java.util.Date) {
			s = DateFormat.getDateInstance(DateFormat.SHORT).
					format( (java.util.Date) oValue);

		}
		else if (oValue instanceof Float) {
			s = nf.format(oValue);
		}
		else if (oValue instanceof Double) {
			s = nf.format(oValue);
		}
		else if (oValue instanceof Long) {
			s = nf.format(oValue);
		}
		else if (oValue instanceof Short) {
			s = nf.format(oValue);
		}
		else if (oValue instanceof Object && oValue != null) {
			s = oValue.toString();
		}
		else {
			throw new IllegalArgumentException  ("Tipo de datos de columna inválido: " +
					oValue.getClass().getName());
		}
		if (bCleanBinary) return cleanBinary (s);
		return s;
	}

	private static String cleanBinary(String s) {
		StringBuffer sb = new StringBuffer ();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >=27) sb.append(c);
		}
		return sb.toString();
	}

	public static Object clonar(Object obj) throws InstantiationException, IllegalAccessException {
		Class<? extends Object> clazz = obj.getClass();
		Object newObj = clazz.newInstance();
		return merge (newObj, obj, true);
	}

	/*
	 * Determina si es 0km
	 */
	public static boolean es0Km(int anio) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date (System.currentTimeMillis()));
		return (c.get(Calendar.YEAR) - anio <=1);
	}

	public static Integer changeTo0Km(int anio) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date (System.currentTimeMillis()));
		if (c.get(Calendar.YEAR) - anio <=1)
			return c.get(Calendar.YEAR);
		return anio;
	}

	public static boolean esMotoAuto(Integer idtipodeasistencia) {
		return (idtipodeasistencia == 1 || idtipodeasistencia == 2);

	}


	/*
	 * Convierte los atributos del objeto a Uppercase
	 */
	public static Object uppercase (Object oSource) throws IllegalArgumentException {
		try {
			if (oSource == null) 
				return oSource;
			Class clazz = oSource.getClass();
			ArrayList<Field> fieldsSource= getAllFields (clazz, java.lang.reflect.Modifier.PRIVATE);
			for (int i = 0; i < fieldsSource.size (); i++) {
				Object o1;
				Field fsource = fieldsSource.get(i);
				fsource.setAccessible(true);
				o1 = fsource.get(oSource);
				if (o1 != null && o1 instanceof String) // si es null no lo copia
					fsource.set(oSource, ((String) o1).toUpperCase());						
			}
			return oSource;
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new IllegalArgumentException (e);
		}
	}


	public static int converSiNoToInt(String valor) {
		if (valor.equalsIgnoreCase("S")) {
			return 0;
		} else {
			return 1;
		}
	}

	public static double convertToDouble(String valor) {
		if (valor != null && !valor.trim().isEmpty()) {
			return Double.parseDouble(valor);
		} else {
			return 0.0;
		}

	}


	public static String generateRandomKey(int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("La longitud debe ser mayor a 0");
		}

		StringBuilder key = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(CHARACTERS.length());
			key.append(CHARACTERS.charAt(index));
		}
		return key.toString();
	}

}

