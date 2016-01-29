package EnvironmentConfiguration.model;

import EnvironmentConfiguration.controller.HandleConfigFunctions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class RegisterList {

	static int MAX_SIZE = 32; //default is 32 bit registers

	private static final int NAME = 0;
	private static final int SOURCE = 1;
	private static final int SIZE = 2;
	private static final int TYPE = 3;
	private static final int START = 4;
	private static final int END = 5;

	private HashMap<String, Register> map;
	private ArrayList<String[]> lookup;
	private ErrorLogger errorLogger = new ErrorLogger(new ArrayList<ErrorMessageList>());

	public RegisterList(String csvFile) {
		this.map = new HashMap<String, Register>();
		this.lookup = new ArrayList<String[]>();

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int lineCounter = 0;

		ArrayList<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			//initialize the errormessage here


			while ((line = br.readLine()) != null) {
				boolean isSkipped = false;
				// use comma as separator
				//String[] reg = line.split(cvsSplitBy);
				line.replaceAll("\\s+","");
				ArrayList<String> stringProcessed = HandleConfigFunctions.generateFromSplittingStrings(line);
				String []reg = new String[stringProcessed.size()];
				stringProcessed.toArray(reg);
//				System.out.println(reg.length + "dafuq");
//				System.out.println(reg[0] + " " +reg[1] + " " +reg[2] + " " + reg[3] + " "   + reg[4] + " " + reg[5]);
//				System.out.println(reg.length + "dafuq");

////				String[] temp = line.split(cvsSplitBy);
//				if(temp.length < 6){
//					isSkipped = true;
//				}
				// trim every row just in case
//				for (int i = 0; i < reg.length; i++){
//					reg[i] = reg[i].trim();
//				}


				ArrayList<String> missingParametersRegister = HandleConfigFunctions.checkifMissing(reg);
				ArrayList<String> invalidParametersRegister = HandleConfigFunctions.checkForInvalidInput(reg);
				if(missingParametersRegister.size() > 0){
					isSkipped = true;
					errorMessages.add(new ErrorMessage(
							Types.registerShouldNotBeEmpty,
							missingParametersRegister,
							Integer.toString(lineCounter)));
				}

				if(invalidParametersRegister.size() > 0 && lineCounter != 0){
					isSkipped = true;
					errorMessages.add(new ErrorMessage(
							Types.registerShouldNotBeInvalid,
							invalidParametersRegister,
							Integer.toString(lineCounter)));
				}

//				for(int x = 0; x < lookupCopy.size(); x++){
//					System.out.println((lookupCopy.get(x))[0]);
//				}

//				System.out.println("EnvironmentConfiguration.model.Register [name= " + reg[0]
//	                                 + " , source=" + reg[1]
//	                                 + " , size=" + reg[2]
//	                                 + " , type=" + reg[3]
//	                                 + " , start=" + reg[4]
//	                                 + " , end=" + reg[5]+ "]");

				// we need to get the max register size listed in the csv file
				if(!isSkipped) {
					int regSize = 0;
					// don't get the table headers
					if (lineCounter != 0) {

						regSize = Integer.parseInt(reg[SIZE]);
						if (MAX_SIZE < regSize)
							MAX_SIZE = regSize;

						int startIndex = Integer.parseInt(reg[START]);
						int endIndex = Integer.parseInt(reg[END]);

						if ((startIndex == 0 && endIndex + 1 != regSize) || (startIndex > 0 && endIndex - startIndex + 1 != regSize)) {
							errorMessages.add(new ErrorMessage(
									Types.registerInvalidSizeFormat,
									HandleConfigFunctions.generateArrayListString(reg[NAME]),
									Integer.toString(lineCounter)));
							//						reg = HandleConfigFunctions.adjustAnArray(reg, 1);
							//						reg[reg.length - 1] = "0";
						}

						//					//check if length >= 5;
						//					if(reg.length >= 5){
						//						errorMessages.add(new ErrorMessage(
						//								Types.registerShouldNotBeEmpty,
						//								HandleConfigFunctions.generateArrayListString(toCatchError),
						//								Integer.toString(lineCounter)));
						//						reg = HandleConfigFunctions.adjustAnArray(reg, 1);
						//						reg[reg.length - 1] = "0";
						//					}
						//					else if(!HandleConfigFunctions.isInteger(reg[END], 10) || !HandleConfigFunctions.isInteger(reg[START], 10))
						//						errorMessages.add(new ErrorMessage(
						//								Types.registerShouldNumber,
						//								HandleConfigFunctions.generateArrayListString(toCatchError),
						//								Integer.toString(lineCounter)));
						//
						//
						//					else {
						//
						//						endIndex = Integer.parseInt(reg[END]);
						//					}
						// System.out.println(source.getValue().substring(startIndex, endIndex + 1));

						// need to convert start and end indices into hex equivalents
						endIndex = ((endIndex + 1) / 4) - 1;
						startIndex = startIndex / 4;

						// reverse order of indices
						endIndex = ((RegisterList.MAX_SIZE / 4) - 1) - endIndex;
						startIndex = ((RegisterList.MAX_SIZE / 4) - 1) - startIndex;
						reg[START] = String.valueOf(endIndex);
						reg[END] = String.valueOf(startIndex);
						reg[NAME] = reg[NAME].toUpperCase();
						// add csv row to lookup table
						this.lookup.add(reg);
					}

					// if a register is the source register itself
					if (reg[NAME].equals(reg[SOURCE])) {
						switch (reg[TYPE]) {
							case "1":
							case "2":
								Register g = new Register(reg[NAME], regSize);
								this.map.put(reg[NAME], g);
								break;
							case "4":
								EFlags e = new EFlags(reg[NAME], regSize);
								this.map.put(reg[NAME], e);
								break;
							/*to fix:*/
							default:
								errorMessages.add(
										new ErrorMessage(Types.invalidRegister,
												HandleConfigFunctions.generateArrayListString(reg[TYPE]),
												Integer.toString(lineCounter + 1)));
								reg[TYPE] = "0";
								break;
						}
					}
				}
				lineCounter++;
			}

			// should check for register configuration errors...
			int index = 1;
			boolean isFoundError = true;
			for (String[] lookupEntry : this.lookup){

				// if all source (mother) registers are existent
				if ( !this.map.containsKey(lookupEntry[1]) ){
					System.out.println("Register Configuration Error: " + lookupEntry[1] + "does not exist at line " + index);
					errorMessages.add(new ErrorMessage(
							Types.doesNotExist, HandleConfigFunctions.generateArrayListString(lookupEntry[1]),
							Integer.toString(index)));
				}
				index++;
			}

		}
// catch (NumberFormatException e) {
////			errorMessages.add(new ErrorMessage(
////					Types.registerShouldNumber, HandleConfigFunctions.generateArrayListString(toCatchError),
////					Integer.toString(lineCounter)));
//		}
//		catch (IndexOutOfBoundsException index){
////			errorMessages.add(new ErrorMessage(Types.registerShouldNotBeEmpty,
////					new ArrayList<String>(), Integer.toString(lineCounter)));
//		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		ErrorMessageList registerErrorList = new ErrorMessageList(Types.registerFile, errorMessages);
		errorLogger.add(registerErrorList);
	}

	public Iterator<String[]> getRegisterList(){
		return this.lookup.iterator();
	}

	/*
		getRegisterKeys() is used for getting all register names to be highlighted
	 */
	public Iterator<String> getRegisterKeys(){
		List registerKeys = new ArrayList<>();
		Iterator<String[]> iterator = getRegisterList();
		while(iterator.hasNext()){
			String regName = iterator.next()[0];
			registerKeys.add(regName);
		}
		return registerKeys.iterator();
	}

	public EFlags getEFlags(){
		return (EFlags) this.map.get("EFLAGS");
	}

	public boolean isExisting(String registerName){
		return (find(registerName) != null) ;
	}

	public int getSize(String registerName){
		return getSize(new Token(Token.reg, registerName));
	}

	public int getSize(Token a){
		String key = a.getValue();
		String size;
		size = find(key)[SIZE];
		return Integer.parseInt(size);
	}

	public String get(String registerName){
		return get(new Token(Token.reg, registerName));
	}

	public String get(Token a){
		ArrayList<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
		// find the mother/source register
		String key = a.getValue();
		String[] register = find(key);

		// get the mother register
		if ( isExisting(key) ) {
			Register mother = this.map.get(register[SOURCE]);
			int startIndex = Integer.parseInt(register[START]);
			int endIndex = Integer.parseInt(register[END]);
			// System.out.println(source.getValue().substring(startIndex, endIndex + 1));
			// return the indicated child register value
			return mother.getValue().substring(startIndex, endIndex + 1);
		} else {
			System.out.println("ERROR: EnvironmentConfiguration.model.Register " + key + " does not exist.");
			errorMessages.add(new ErrorMessage(Types.doesNotExist,
					HandleConfigFunctions.generateArrayListString(key), ""));
			errorLogger.get(0).add(errorMessages);
		}
		return null;
	}

	public void set(String registerName, String hexString){
		set(new Token(Token.reg, registerName), hexString);
	}

	public void set(Token a, String hexString){
		String key = a.getValue();
		String[] register = find(key);
		ArrayList<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
		// get the mother register

		Register mother = this.map.get(register[SOURCE]);
		int startIndex = Integer.parseInt(register[START]);
		int endIndex = Integer.parseInt(register[END]);

		//check for mismatch between parameter hexString and child register value		
		String child = mother.getValue().substring(startIndex, endIndex + 1);

		if ( child.length() > hexString.length() ){
			int differenceInLength = child.length() - hexString.length();
			for (int i = 0; i < differenceInLength; i++){
				hexString = "0" + hexString;
			}
		}

		if ( child.length() == hexString.length() ){
			String newValue = mother.getValue();
			char[] val = newValue.toCharArray();
			for( int i = startIndex; i <= endIndex; i++){
				val[i] = hexString.charAt(i-startIndex);
			}
			newValue = new String(val);
//			System.out.println("new: " + newValue);
			mother.setValue(newValue.toUpperCase());
		}
		else {
			if ( hexString.equals("") ) {
				System.out.println("Writing to register failed.");
				errorMessages.add(new ErrorMessage(Types.writeRegisterFailed,
						new ArrayList<String>(), ""));
			}
			else {
				System.out.println("Data type mismatch between "
						+ register[0] + ":" + child + " and " + hexString);
				errorMessages.add(new ErrorMessage(Types.dataTypeMismatch,
						HandleConfigFunctions.generateArrayListString(register[0], child, hexString), ""));

			}
			errorLogger.get(0).add(errorMessages);
		}
	}

	public void clear(){
		Iterator<String> keys = this.map.keySet().iterator();
		while (keys.hasNext()){
			this.map.get(keys.next()).initializeValue();
		}
	}

	public void print(){
		Map<String, Register> sortedMap = new TreeMap<String, Register>(map);
		Iterator<Map.Entry<String, Register>> ite = sortedMap.entrySet().iterator();
		while (ite.hasNext()){
			Map.Entry<String, Register> x = ite.next();
			System.out.println(x);
		}
	}

	public String[] find(String registerName){
		for (String[] x : this.lookup){
			if ( x[0].equals(registerName) ){
				return x;
			}
		}
		return null;
	}

	public Map getRegisterMap(){
		return this.map;
	}

	public ErrorLogger getErrorLogger(){
		if(errorLogger.get(0).getSizeofErrorMessages() == 0)
			return new ErrorLogger(new ArrayList<ErrorMessageList>());
		else
			return errorLogger;
	}
}
