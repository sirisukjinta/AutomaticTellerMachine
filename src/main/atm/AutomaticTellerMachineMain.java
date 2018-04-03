package main.atm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AutomaticTellerMachineMain {
	static TreeMap<Integer, Integer> availableNoteMapForUpdate = getAvailableNotes();
	static boolean finished = false;
	
	public static void main(String[] arg) throws Exception{
		String amountString = readConsoleData();
		System.out.println("amountString " + amountString);
		
		int amountInt = 0;
		try{
			amountInt = Integer.parseInt(amountString);
		}catch(Exception e){
			System.out.println("Unable to parse amount, " + e);
			throw e;
		}
		
		if(amountInt < 0){
			System.out.println("Invalid input (negative)");
			throw new Exception("Invalid input (negative)");
		}
		
		if(amountInt%10 != 0){
			System.out.println("Invalid input (coin)");
			throw new Exception("Invalid input (coin)");
		}
		
		int maxDispense = 20000;
		if(amountInt > maxDispense){
			System.out.println("Invalid input (more than "+maxDispense+")");
			throw new Exception("Balance is not enough to dispense");
		}
		
		int balance = checkBalance();
		if(amountInt > balance){
			System.out.println("Balance is not enough to dispense");
			throw new Exception("Balance is not enough to dispense");
		}

		int[] possibleNotes = {1000, 500, 100, 50, 20};
		int[] impossibleValues = new int[]{10, 30};
		List<Integer> impossibleValueList = Arrays.stream(impossibleValues).boxed().collect(Collectors.toList());
		
		if(impossibleValueList.contains(amountInt)){
			System.out.println("Invalid input (not possible to dispense)");
			throw new Exception("Invalid input (not possible to dispense)");
		}
		
		int noteAmount = 0;
		int amountCalculation = 0;
		int amountCalculation2 = 0;
		int[] noteAmounts = new int[5];
		int i = 0;
		if(impossibleValueList.contains(amountInt%100)){
			amountCalculation = amountInt - amountInt%100 - 100;
			for(int possibleNote : possibleNotes){
				noteAmount = getNote(possibleNote, amountCalculation, false);
				amountCalculation = amountCalculation - (possibleNote * noteAmount);
				noteAmounts[i] = noteAmount;
				i++;
			}
			
			i = 0;
			amountCalculation2 = amountInt%100 + 100;
			for(int possibleNote : possibleNotes){
				if(possibleNote > 50){
					i++;
					continue;
				}
				noteAmount = getNote(possibleNote, amountCalculation2, true);
				amountCalculation2 = amountCalculation2 - (possibleNote * noteAmount);
				noteAmounts[i] = noteAmounts[i] + noteAmount;
				i++;
			}
		}else{
			amountCalculation = amountInt;
			for(int possibleNote : possibleNotes){
				noteAmount = getNote(possibleNote, amountCalculation, false);
				amountCalculation = amountCalculation - (possibleNote * noteAmount);
				noteAmounts[i] = noteAmount;
				i++;
			}
		}
		
		if(amountCalculation != 0){
			noteAmounts = new int[5];
			advanceCalculate(possibleNotes, noteAmounts, amountInt, 0);
		}else{
			TreeMap<Integer, Integer> balanceNoteMap = null;
			for (int j = 0; j < noteAmounts.length; j++) {
				System.out.println("Note Type : " + possibleNotes[j] + " Note Amount : " + noteAmounts[j]);
				balanceNoteMap = updateAvailableNotes(possibleNotes[j], noteAmounts[j]);
			}
			for (HashMap.Entry<Integer, Integer> entry : balanceNoteMap.entrySet()) {
				System.out.println("Balance --> Note Type : " + entry.getKey() + " Note Amount : " + entry.getValue());
			}
		}
	}
	
	private static String readConsoleData() throws Exception{
		String dataString = "";
		String inputLine = "";
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try{
			 inputLine = in.readLine();
			 dataString = dataString + inputLine;
		} catch(IOException e) {
			System.out.println("Unable to read buffer, " + e);
			throw e;
		} finally {
			try {
				if(in != null) in.close();
			} catch (IOException e) {
				System.out.println("Unable to close BufferedReader, " + e);
			}
		}
		
		return dataString;
	}
	
	private static int getNote(int noteType, int amount, boolean useOddFifty) throws Exception{
		int noteAmount = 0;
		int availableNoteAmount = 0;
		
		try{
			if(useOddFifty && noteType == 50){
				noteAmount = Math.floorDiv(amount, noteType);
				if(noteAmount%2 == 0){
					noteAmount = noteAmount - 1;
				}
				availableNoteAmount = getAvailableNote(noteType, noteAmount);
			}else{
				noteAmount = Math.floorDiv(amount, noteType);
				availableNoteAmount = getAvailableNote(noteType, noteAmount);
			}
		} catch(Exception e) {
			System.out.println("Unable to trunc note, " + e);
			throw e;
		}
		
		return availableNoteAmount;
	}
	
	private static int getAvailableNote(int noteType, int noteAmount) throws Exception{
		int availableNoteAmount = 0;

		TreeMap<Integer, Integer> availableNoteMap = getAvailableNotes();
		availableNoteAmount = availableNoteMap.get(noteType);
		if(noteAmount <= availableNoteAmount){
			availableNoteMap.put(noteType, (availableNoteAmount-noteAmount));
			return noteAmount;
		}else{
			return availableNoteAmount;
		}
	}

	public static List<int[]> advanceCalculate(int[] possibleNotes, int[] noteAmounts, int amountInt, int position){
		List<int[]> possibleResultList = new ArrayList<>();
		int value = getSumAmount(possibleNotes, noteAmounts);
		if (value < amountInt){
			for (int i = position; i < possibleNotes.length; i++) {
				int[] noteAmountsTemp = noteAmounts.clone();
				noteAmountsTemp[i]++;
				if(!finished){
					List<int[]> newList = advanceCalculate(possibleNotes, noteAmountsTemp, amountInt, i);
					if (newList != null){
						possibleResultList.addAll(newList);
					}
				}else{
					break;
				}
			}
		} else if (value == amountInt) {
			TreeMap<Integer, Integer> availableNoteMap = getAvailableNotes();
			boolean allNotesPossible = true;
			for (int i = 0; i < noteAmounts.length; i++) {
				if (noteAmounts[i] > availableNoteMap.get(possibleNotes[i])){
					allNotesPossible = false;
				}
			}
			if(allNotesPossible){
				TreeMap<Integer, Integer> balanceNoteMap = null;
				System.out.println("Advance Calculate");
				for (int i = 0; i < noteAmounts.length; i++) {
					System.out.println("Note Type : " + possibleNotes[i] + " Note Amount : " + noteAmounts[i]);
					balanceNoteMap = updateAvailableNotes(possibleNotes[i], noteAmounts[i]);
				}
				for (HashMap.Entry<Integer, Integer> entry : balanceNoteMap.entrySet()) {
					System.out.println("Balance --> Note Type : " + entry.getKey() + " Note Amount : " + entry.getValue());
				}
				finished = true;
			}
		}
		return possibleResultList;
	}

	public static int getSumAmount(int[] possibleNotes, int[] noteAmounts){
		int ret = 0;
		for (int i = 0; i < noteAmounts.length; i++) {
			ret += possibleNotes[i] * noteAmounts[i];
		}
		return ret;
	}

	public static TreeMap<Integer, Integer> getAvailableNotes(){
		TreeMap<Integer, Integer> availableNoteMap = new TreeMap<Integer, Integer>(Collections.reverseOrder());
		availableNoteMap.put(1000, 100);
		availableNoteMap.put(500, 100);
		availableNoteMap.put(100, 100);
		availableNoteMap.put(50, 100);
		availableNoteMap.put(20, 100);
		return availableNoteMap;
	}

	public static int checkBalance(){
		int balance = 0;
		TreeMap<Integer, Integer> availableNoteMap = getAvailableNotes();
		for (HashMap.Entry<Integer, Integer> entry : availableNoteMap.entrySet()) {
			balance += (entry.getKey() * entry.getValue());
		}
		return balance;
	}

	public static TreeMap<Integer, Integer> updateAvailableNotes(int noteType, int noteAmount){
		availableNoteMapForUpdate.put(noteType, availableNoteMapForUpdate.get(noteType) - noteAmount);
		return availableNoteMapForUpdate;
	}
}