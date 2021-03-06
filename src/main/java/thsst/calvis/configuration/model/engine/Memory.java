package thsst.calvis.configuration.model.engine;

import thsst.calvis.configuration.model.exceptions.DuplicateVariableException;
import thsst.calvis.configuration.model.exceptions.MemoryAlignmentException;
import thsst.calvis.configuration.model.exceptions.MemoryReadException;
import thsst.calvis.configuration.model.exceptions.MemoryWriteException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Memory view:    Protected Mode Flat Model
 * address size:    32-bit
 * starts from:     0x0000 0000
 * ends at:         0x0000 FFFF
 */
public class Memory {

    public static int MAX_ADDRESS_SIZE;
    public static int DEFAULT_RELATIVE_SIZE;
    public static final int SIZE_DIRECTIVE_NAME = 0;
    public static final int SIZE_DIRECTIVE_PREFIX = 1;
    public static final int SIZE_DIRECTIVE_SIZE = 2;

    private TreeMap<String, String> mem;
    private ArrayList<String[]> lookup;
    private HashMap<String, String> labelMap;
    private HashMap<String, String> variableMap;
    private String variablePointer;

    private boolean isAligned;
    private boolean isInitialized;
    private String variablePointerStartingAddress;

    public Memory(int bitSize, int bitEndLength, String csvFile) {
        MAX_ADDRESS_SIZE = bitSize;
        DEFAULT_RELATIVE_SIZE = bitEndLength;
        this.mem = new TreeMap<>(Collections.reverseOrder());
        this.labelMap = new HashMap<>();
        this.variableMap = new HashMap<>();

        this.isAligned = false;
        this.isInitialized = false;
        this.variablePointerStartingAddress = MemoryAddressCalculator.extend("0000", Memory.MAX_ADDRESS_SIZE, "0");

        resetVariablePointer();

        String lastAddress = MemoryAddressCalculator.extend("F", bitEndLength, "F");
        int end = Integer.parseInt(lastAddress, 16);
        for ( int i = 0x0; i <= end; i++ ) {
            String address = MemoryAddressCalculator.extend(Integer.toHexString(i), bitSize, "0");
            if ( isInitialized ) {
                mem.put(address, "" + address.charAt(address.length() - 1) + address.charAt(address.length() - 1));
            } else {
                mem.put(address, "00");
            }
        }

        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        int lineCounter = 0;
        this.lookup = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(new File(csvFile)));
            while ( (line = br.readLine()) != null ) {
                // use comma as separator
                String[] row = line.split(cvsSplitBy);
                // trim every row just in case
                for ( int i = 0; i < row.length; i++ ) {
                    row[i] = row[i].trim();
                }
                if ( lineCounter != 0 ) {
                    this.lookup.add(row);

//		                    "], Prefix = [" + row[Memory.SIZE_DIRECTIVE_PREFIX] +
//		                    "], Size = [" + row[Memory.SIZE_DIRECTIVE_SIZE] + "]");
                }
                lineCounter++;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            if ( br != null ) {
                try {
                    br.close();
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map getMemoryMap() {
        return this.mem;
    }

    public String getVariablePointer() {
        return variablePointer;
    }

    public void incrementVariablePointer(int prefixSize) {
        int byteOffset = prefixSize / 8; // byte addressable memory
        Integer startingAddress = Integer.parseInt(variablePointer, 16);
        startingAddress += byteOffset;
        variablePointer = reformatAddress(Integer.toHexString(startingAddress));
    }

    public static String reformatAddress(String add) {
        return MemoryAddressCalculator.extend(add, Memory.MAX_ADDRESS_SIZE, "0");
    }

    public void write(Token baseAddressToken, String value, Token des)
            throws MemoryWriteException, MemoryAlignmentException {
        write(baseAddressToken.getValue(), value, des);
    }

    public void write(Token baseAddressToken, String value, int offset)
            throws MemoryWriteException, MemoryAlignmentException {
        write(baseAddressToken.getValue(), value, offset);
    }

    public void write(String wholeMemoryString, String value, Token des)
            throws MemoryWriteException, MemoryAlignmentException {
        // des contains our offset.
        String desValue = des.getValue();
        String[] memoryArray = desValue.split("/");
//		String sizeDirective = memoryArray[0];
        String baseAddress = memoryArray[1];

        int offset = getBitSize(des);
        String reformattedValue = MemoryAddressCalculator.extend(value, offset, "0");
        write(baseAddress, reformattedValue, offset);
    }

    public void write(String baseAddress, String value, int offset)
            throws MemoryWriteException, MemoryAlignmentException {
        String memoryBaseAddress = baseAddress;
        if ( baseAddress.contains("/") ) {
            memoryBaseAddress = baseAddress.split("/")[1];
        }
        if ( this.mem.containsKey(memoryBaseAddress) ) {
            Integer inc = Integer.parseInt(memoryBaseAddress, 16);
            if ( isAligned ) {
                int memoryBlocks = offset / 8;
                if ( inc % memoryBlocks == 0 ) {
                    writeToMap(baseAddress, value, offset, inc);
                } else {
                    throw new MemoryAlignmentException(memoryBaseAddress);
                }
            } else {
                writeToMap(baseAddress, value, offset, inc);
            }
        } else {
            throw new MemoryWriteException(memoryBaseAddress);
        }
    }

    // Used by PUSH instructions
    public void writeToStack(String baseAddress, String value, int offset)
            throws MemoryWriteException, MemoryAlignmentException {
        String memoryBaseAddress = baseAddress;
        if ( baseAddress.contains("/") ) {
            memoryBaseAddress = baseAddress.split("/")[1];
        }
        if ( this.mem.containsKey(memoryBaseAddress) ) {
            Integer inc = Integer.parseInt(memoryBaseAddress, 16);
            writeToMap(baseAddress, value, offset, inc);
        } else {
            throw new MemoryWriteException(memoryBaseAddress);
        }
    }

    private void writeToMap(String baseAddress, String value, int offset, int inc) throws MemoryWriteException {
        int offsetHex = offset / 4;
        value = value.toUpperCase();
        value = MemoryAddressCalculator.extend(value, offset, "0");
        if ( value.length() > offsetHex ) {
            throw new MemoryWriteException(baseAddress, value);
        }
        for ( int i = 0; i < offsetHex / 2; i++ ) {
            String succeedingAddress = Memory.reformatAddress(Integer.toHexString(inc));
            if ( this.mem.containsKey(succeedingAddress) ) {
                this.mem.put(succeedingAddress,
                        value.substring((value.length() - 2) - (i * 2), value.length() - (i * 2)));
                inc++;
            } else {
                throw new MemoryWriteException(succeedingAddress);
            }
        }
        
    }

    public String read(String address) {
        return this.mem.get(address);
    }

    public String read(Token baseAddressToken, Token src) throws MemoryReadException {
        return read(baseAddressToken.getValue(), src);
    }

    public String read(Token baseAddressToken, int offset) throws MemoryReadException {
        return read(baseAddressToken.getValue(), offset);
    }

    public String read(String wholeMemoryString, Token src) throws MemoryReadException {
        String entireMemoryToken = src.getValue();
        String[] memoryArray = entireMemoryToken.split("/");

        String sizeDirective = memoryArray[0];
        String baseAddress = memoryArray[1];

        int offset = 0; //default offset = 0;
        for ( String[] x : this.lookup ) {
            if ( sizeDirective.equalsIgnoreCase(x[Memory.SIZE_DIRECTIVE_NAME]) ) {
                offset = Integer.valueOf(x[Memory.SIZE_DIRECTIVE_SIZE]);
                break;
            }
        }
        return read(baseAddress, offset);
    }

    public String read(String baseAddress, int offset) throws MemoryReadException {
        String result = "";
        Integer inc;

        String checkSizeDirective = baseAddress;
        if ( checkSizeDirective.contains("/") ) {
            checkSizeDirective = baseAddress.split("/")[1];
        }
        
        inc = Integer.parseInt(checkSizeDirective, 16);
        int offsetHex = offset / 4;
        for ( int i = 0; i < offsetHex / 2; i++ ) {
            result = read(reformatAddress(Integer.toHexString(inc))) + result;
            inc++;
        }

        if ( result.contains("null") ) {
            throw new MemoryReadException(baseAddress, offset);
        }
        return result;
    }

    public boolean isExisting(String sizeDirective) {
        for ( String[] x : this.lookup ) {
            if ( x[Memory.SIZE_DIRECTIVE_NAME].equals(sizeDirective) ) {
                return true;
            }
        }
        return false;
    }

    public void print(String start, String end) {
        Map<String, String> map = new TreeMap<>(mem).descendingMap();
        Iterator<Map.Entry<String, String>> ite = map.entrySet().iterator();
        while ( ite.hasNext() ) {
            Map.Entry<String, String> x = ite.next();
            if ( Integer.parseInt(x.getKey(), 16) >= Integer.parseInt(start, 16)
                    && Integer.parseInt(x.getKey(), 16) <= Integer.parseInt(end, 16) ) {
                
            }
        }
    }

    public void clear() {
        Iterator<String> keys = this.mem.keySet().iterator();
        while ( keys.hasNext() ) {
            if ( isInitialized ) {
                String address = keys.next();
                this.mem.put(address, "" + address.charAt(address.length() - 1) + address.charAt(address.length() - 1));
            } else {
                this.mem.put((keys.next()), "00" );
            }
        }
        this.labelMap.clear();
        this.variableMap.clear();
        resetVariablePointer();
    }

    public Iterator<String[]> getLookup() {
        return lookup.iterator();
    }

    public ArrayList<String[]> getLookupTable(){
        return this.lookup;
    }

    public Iterator<String> getMemoryKeys() {
        List memoryKeys = new ArrayList<>();
        Iterator<String[]> iterator = getLookup();
        while ( iterator.hasNext() ) {
            String[] arr = iterator.next();
            String sizeDirective = arr[Memory.SIZE_DIRECTIVE_NAME];
            String sizePrefix = arr[Memory.SIZE_DIRECTIVE_PREFIX];
            memoryKeys.add(sizeDirective);
            memoryKeys.add(sizePrefix);
        }
        return memoryKeys.iterator();
    }

    public int getBitSize(Token a) {
        String key = a.getValue();

        String checkSizeDirective = key;
        if ( checkSizeDirective.contains("/") ) {
            key = checkSizeDirective.split("/")[0];
        }

        String[] arr = find(key, Memory.SIZE_DIRECTIVE_NAME);
        if ( arr != null ) {
            return Integer.parseInt(arr[Memory.SIZE_DIRECTIVE_SIZE]);
        }
        return 0;
    }

    public int getHexSize(Token a) {
        return getBitSize(a) / 4;
    }

    public String[] find(String sizeDirective, int index) {
        for ( String[] x : this.lookup ) {
            if ( x[index].equalsIgnoreCase(sizeDirective) ) {
                return x;
            }
        }
        return null;
    }

    public int getPrefixBitSize(Token prefix) {
        String key = prefix.getValue();
        String[] arr = find(key, Memory.SIZE_DIRECTIVE_PREFIX);
        if ( arr != null ) {
            return Integer.parseInt(arr[Memory.SIZE_DIRECTIVE_SIZE]);
        }
        return 0;
    }

    public int getPrefixHexSize(Token prefix) {
        return getPrefixBitSize(prefix) / 4;
    }

    /**
     * getFromLabelMap(String) is used by control transfer instructions
     * indicated in the BSH files
     *
     * @param key is the name of the label
     * @return the address pointed to by the label
     * @throws NullPointerException, in case the label does not exist
     */
    public String getFromLabelMap(String key) throws NullPointerException {
        if ( labelMap.get(key) != null ) {
            return labelMap.get(key);
        } else {
            throw new NullPointerException("Label: " + key + " does not exist.");
        }
    }

    public void putToLabelMap(String key, String address) {
        labelMap.put(key, address);
    }

    public String getFromVariableMap(String key) throws NullPointerException {
        if ( variableMap.get(key) != null ) {
            return variableMap.get(key);
        } else {
            throw new NullPointerException("Variable " + key + " does not exist.");
        }
    }

    public String getCorrespondingLabel(String addressKey) {
        for ( Map.Entry<String, String> entry : variableMap.entrySet() ) {
            if ( Objects.equals(addressKey, entry.getValue()) ) {
                return entry.getKey();
            }
        }
        return "";
    }

    public void putToVariableMap(String key, String size) throws DuplicateVariableException {
        if ( variableMap.containsKey(key) ) {
            throw new DuplicateVariableException(key);
        } else {
            variableMap.put(key, variablePointer);
        }
    }

    public static void setDefaultRelativeSize(int defaultRelativeSize) {
        DEFAULT_RELATIVE_SIZE = defaultRelativeSize;
    }

    public boolean containsLabel(String label) {
        return labelMap.containsKey(label);
    }

    /**
     * Used by LEA instruction
     *
     * @param memoryAddressingMode
     * @return
     */
    public String removeSizeDirectives(String memoryAddressingMode) {
        String result = memoryAddressingMode;
        if ( result.contains("/") ) {
            result = result.split("/")[1];
        }
        return result;
    }

    private void resetVariablePointer() {
        this.variablePointer = variablePointerStartingAddress;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public boolean isAligned() {
        return isAligned;
    }

    public void setAligned(boolean aligned) {
        isAligned = aligned;
    }
}
