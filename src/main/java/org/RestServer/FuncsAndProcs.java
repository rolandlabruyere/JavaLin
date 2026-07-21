package org.restserver;

import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Base64;
//import static java.lang.Math.abs;

public class FuncsAndProcs {

    public static String thisEnvironment;
    int issues;
    public static final String logHeader = "=".repeat(100);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public String iniPath = "src/RestServer.ini";
    public String tempLogName;


    public static String decodeBase64(String encodedString){
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }


    public String getLogPath() throws Throwable{
        File iniFile = new File(iniPath);
        Ini ini = new Ini(iniFile);
        return ini.get("CommonSettings", "TempLog");
    }
    public String getIniValue(String section, String iniKey) throws Throwable{
        File iniFile = new File(iniPath);
        Ini ini = new Ini(iniFile);
        return ini.get(section, iniKey);
    }
    public List<String> getIniKeysInSection(String thisSection,int valuesSwitch) throws Throwable{
        File iniFile = new File(iniPath);
        List<String> result = new ArrayList<>();
        Ini ini = new Ini(iniFile);

        Set<Entry<String, Section>> sections = ini.entrySet();

        for (Entry<String, Section> entry : sections) {
            Section section = entry.getValue();

            if (section.getName().equalsIgnoreCase(thisSection)) {
                Set<Entry<String, String>> values = section.entrySet();
                for (Entry<String, String> subEntry : values) {
                    switch (valuesSwitch){
                        case 0 -> result.add(subEntry.getKey());
                        case 1 -> result.add(subEntry.getValue());
                        case 2 -> result.add(subEntry.getKey() + "," + subEntry.getValue());
                    }
                }
            }
        }
        return result;
    }

    public void saveIniValue(String section, String iniKey, String value) throws Throwable{
        File iniFile = new File(iniPath);
        Ini ini = new Ini(iniFile);
        ini.put(section, iniKey, value);
        ini.store();
        if (! value.isEmpty()) {
            writeToLog("Ini file updated. Changed the value of key \"" + iniKey + "\" into value \"" + value + "\" in section [" + section + "].");
        }
    }
    public void splitIniKey(String section, String iniKey1, String iniKey2) throws Throwable {
        String[] values = getIniValue(section, iniKey1).split(",");
        //        removeIniSection(section);
        saveIniValue(section, iniKey1, values[1]);
        saveIniValue(section, iniKey2, values[0]);
    }
    public void removeIniSection(String section) throws Throwable{
        File iniFile = new File(iniPath);
        Ini ini = new Ini(iniFile);
        writeToLog("Removing temporary section [" + section + "] from ini file.");
        ini.remove(section);
        ini.store();
    }

    public String getFinalLogPath() throws Throwable{
        File iniFile = new File(iniPath);
        Ini ini = new Ini(iniFile);
        return ini.get("CommonSettings", "FinalLog");
    }

    public void pauseExecution(Long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
    public void pauseExecution(int millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    public boolean binarySearch(String[] arr, String x)
    {
        boolean found = false;

        int l = 0, r = arr.length - 1;

        while (l <= r) {
            int m = l + (r - l) / 2;
            int res = x.compareTo(arr[m]);

            if (res == 0) {
                found = true;
                System.out.println("The given number is listed as restricted, this testcase will probably not match the expected result." + "\n");
            }
            if (res > 0)
                l = m + 1;
            else
                r = m - 1;
        }

        return found;
    }

    public String firstCharToUpper(String inputString){
        String hulp;
        hulp = inputString.substring(0, 1).toUpperCase() + inputString.substring(1).toLowerCase();
        hulp = hulp.replace("plus", "Plus");

        return hulp;
    }

    public String left(String wholeString, Integer cutLength){
        return wholeString.substring(0, cutLength);
    }

    public String createRndHexStr(Integer length) {
        StringBuilder rndNum = new StringBuilder();
        int min = 0;
        int max = 15;
        for (int i = 1; i <= length; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
            switch (randomNum) {
                case 10 ->  rndNum.append("a");
                case 11 ->  rndNum.append("b");
                case 12 ->  rndNum.append("c");
                case 13 ->  rndNum.append("d");
                case 14 ->  rndNum.append("e");
                case 15 ->  rndNum.append("f");
                default ->  rndNum.append(Integer.toString(randomNum));
            }
        }
        return rndNum.toString();
    }
    public void writeToFile(String logPath, String namePart1, String namePart2, String namePart3, String textToWrite) throws Throwable{
        String fileName = namePart1 + "_" + namePart2 + "_" + namePart3 + ".json";

        OutputStream outputFile = new FileOutputStream(logPath + fileName);
        byte[] bytes = textToWrite.getBytes();
        outputFile.write(bytes);
        writeToLog("[" + fileName + "]\n");
        outputFile.close();
    }
    public void deleteFile(String filename) throws IOException {
        Path fileToDeletePath = Paths.get(filename);
        Files.delete(fileToDeletePath);
    }

    public void writeToLog(String logText)throws Throwable {
        createSubfolder(getLogPath());

        if(logText.equals("Test run started")){
            logText = logHeader + "\n" + logText + "\n" + logHeader;

        }
        logText = depositTimestamp(0) + "\n" + logText;
        logText += "\n";
        tempLogName = "RestServer_" + left(depositTimestamp(0), 10) + ".log";
        Files.writeString(Path.of(getLogPath(), tempLogName), replaceEscapeSequence(logText) , StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        //System.out.println(logText);
    }
    public void writeToHtmlLog(String outputFolder, String scenarioName, String scenarioLine, String uniqueId, String htmlString)throws Throwable {
        String fullName = outputFolder + scenarioName.replace(" ", "") + scenarioLine + "_" + uniqueId + ".html";
        Files.writeString(Path.of(fullName), htmlString , StandardOpenOption.CREATE);
    }


    public void writeToTextFile(String namePart1, String namePart2, String namePart3, String textToWrite)
    {
        try {
            OutputStream outputFile = new FileOutputStream("src/output/response_" + namePart1 + "_" + namePart2 + "_" + namePart3 + ".json");
            byte[] bytes = textToWrite.getBytes();
            outputFile.write(bytes);
            outputFile.close(); 
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeToAnyFile(String fullPath, String textToWrite){
        try {
            Files.writeString(Path.of(fullPath), textToWrite , StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String depositTimestamp(Integer xDaysAgo) {
        //create test Timestamp
        xDaysAgo = xDaysAgo * -1;
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        Date depositDate = subtractDay(Calendar.getInstance().getTime(), xDaysAgo);
        Integer milliSecs = Calendar.getInstance().get(Calendar.MILLISECOND);
        String yyyyMMdd = sdfDate.format(depositDate);
        String hhmmss = sdfTime.format(depositDate);
        return yyyyMMdd + "T" + hhmmss + "." + formatMilliSecs(milliSecs);
    }

    public Long epochTime(String timestamp) throws ParseException {
        timestamp = timestamp.replace("T", " ");
        //timestamp = left(timestamp,20);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = df.parse(left(timestamp, timestamp.length() - 1));
        return date.getTime();
    }

    public String transformTimestamp(String timestamp)  {
        String[] splitTimestamp = timestamp.split(" ");
        String[] splitDate = splitTimestamp[0].split("-");
        String result = splitDate[2] + "-" + splitDate[1] + "-00" + splitDate[0] + "T" + left(splitTimestamp[1], 5);
        return result;
    }

    public void createSubfolder(String relPathFolder) {
        File myDir = new File(relPathFolder);

        if (!myDir.exists()){
            myDir.mkdir();
        }
    }
    public String createTestCaseLogfolder(String subFolder) throws Throwable {
        File myDir = new File(getFinalLogPath() + subFolder);

        if (!myDir.exists()){
            myDir.mkdir();
        }
        return myDir.toString() + "\\";
    }
    public String formatNumber(Integer num) {
        String hulp = num.toString();
        switch (hulp.length()) {
            case 3 -> hulp = "0" + hulp;
            case 2 -> hulp = "00" + hulp;
            case 1 -> hulp = "000" + hulp;
        }
        return hulp;
    }
    public String formatTimePart(Integer num) {
        String hulp = num.toString();
        switch (hulp.length()) {
            case 1 -> hulp = "0" + hulp;
        }
        return hulp;
    }

    public String findLastDateInMonth(String bookYear, String endMonth){
        String dateSection = "";
        switch (endMonth){
            case "02" -> dateSection = "28";
            case "04", "06", "09", "11" -> dateSection = "30";
            default -> dateSection = "31";
        }
        return bookYear + "-" + endMonth + "-" + dateSection;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //fps.getEndpointIndex(endpointName[], endpointIndex[], useNext)
    public String getEndpointIndex(String[] endpointName, Integer[] endpointIndex, String item){
        int ubound = endpointName.length;
        for (int i = 0; i < ubound; i++ ){
            if(endpointName[i].equals(item)){
                return endpointIndex[i].toString();
            }
        }
        return "-1";
    }
    public Integer getInwardIndex(String filterOptions){
        int index;

        switch (filterOptions.toLowerCase()) {
            case "requestforwork" -> index = 2;
            case "event" -> index = 3;
            case "measurementdata" -> index = 4;
            case "endpoints" -> index = 5;
            case "alive" -> index = 6;
            case "updatefirmware" -> index = 7;
            case "config" -> index = 8;
            case "gatewaykeys" -> index = 9;
            case "devices" -> index = 10;
            case "networkconfig" -> index = 11;
            case "status" -> index = 12;
            case "requestfortime" -> index = 13;
            default -> index = 1;
        }
        return index;
    }
    public Integer getOutwardIndex(String filterOptions){
        int index;
        switch (filterOptions.toLowerCase()) {
            case "requestfortime" -> index = 2;
            case "sendendpoints" -> index = 3;
            case "sendlastmeasurementdata" -> index = 4;
            case "updateconfig" -> index = 5;
            case "updateendpoints" -> index = 6;
            case "updatefirmware" -> index = 7;
            case "sendalive" -> index = 8;
            case "sendconfig" -> index = 9;
            case "sendgatewaykeys" -> index = 10;
            case "updategatewaykeys" -> index = 11;
            case "generategatewaykeys" -> index = 12;
            case "updatekeys" -> index = 13;
            case "senddevices" -> index = 14;
            case "sendnetworkconfig" -> index = 15;
            case "updatenetworkconfig" -> index = 16;
            case "requestremotereboot" -> index = 17;
            case "sendstatus" -> index = 18;
            default -> index = 1;
        }
        return index;
    }

    public Integer getRequestIntervalIndex(String filterOptions){
        int index = 0;
        switch (filterOptions.toLowerCase()) {
            case "minute" -> index = 1;
            case "five_minutes" -> index = 2;
            case "fifteen_minutes" -> index = 3;
            case "hour" -> index = 4;
            case "four_hours" -> index = 5;
            case "twenty_four_hours" -> index = 6;
            case "day" -> index = 7;
        }
        System.out.println("found: " + index);
        return index;
    }

    public Integer getCommonIntervalIndex(String filterOptions){
        int index = 0;
        switch (filterOptions.toLowerCase()) {
            case "none" -> index = 1;
            case "five_seconds" -> index = 2;
            case "minute" -> index = 3;
            case "five_minutes" -> index = 4;
            case "fifteen_minutes" -> index = 5;
            case "hour" -> index = 6;
            case "four_hours" -> index = 7;
            case "twenty_four_hours" -> index = 8;
            case "day" -> index = 9;
            case "week" -> index = 10;
            case "month" -> index = 11;
            case "annual_quarter" -> index = 12;
            case "year" -> index = 13;
        }
        return index;
    }

    public void convertYmlToIni(String fullFileName) throws Throwable {
        String ymlContent = readFile(fullFileName, StandardCharsets.UTF_8);
        String iniContent = ymlContent
                .replace("Warmtelink", "[Warmtelink")
                .replace(".bin:", ".bin]")
                .replace(": '", " = ")
                .replace("'", "");
        fullFileName = fullFileName.replace(".yml", ".ini");
        writeToAnyFile(fullFileName, iniContent);
        saveIniValue("FirmwareFiles", "ini", fullFileName);
    }


    public Integer getLogLevelIndex(String filterOptions){
        int index = 0;
        switch (filterOptions.toLowerCase()) {
            case "emergency" -> index = 1;
            case "alert" -> index = 2;
            case "critical" -> index = 3;
            case "error" -> index = 4;
            case "warning" -> index = 5;
            case "notice" -> index = 6;
            case "info" -> index = 7;
            case "debug" -> index = 8;
        }
        return index;
    }

    public boolean checkErrors() throws Throwable {
        boolean foundErrors = false;
        String runError = getIniValue("RunErrors", "emptyQueueError");
        if(!runError.isEmpty()){
            writeToLog(runError);
            foundErrors = true;
        }
        runError = getIniValue("RunErrors", "loadedQueueError");
        if(!runError.isEmpty()){
            writeToLog(runError);
            foundErrors = true;
        }
        runError = getIniValue("RunErrors", "fwVersionAssertError");
        if(!runError.equals("0")){
            writeToLog("Detected " + runError + " assertion errors on the installed firmware version.");
            foundErrors = true;
        }
        return foundErrors;
    }

    public void resetErrors() throws Throwable {
        saveIniValue("RunErrors", "emptyQueueError", "");
        saveIniValue("RunErrors", "loadedQueueError", "");
        saveIniValue("RunErrors", "fwVersionAssertError", "0");
    }

    public void storeUser(String user) throws Throwable {
        String[] userParts = user.trim().split(" ");
        saveIniValue("ActiveUser", "user", user);
        saveIniValue("ActiveUser", "formattedUser", userParts[1] + ", " + userParts[0]);
    }

    public String determineFwBinFile(String installVersion) throws Throwable {
        String lastVersion = getIniValue("ToggleFwVersion", "currentVersion");
        String downloadVersion = getIniValue("ToggleFwVersion", "downloadVersion");
        String filename = "";

        if (left(lastVersion, 5).equals(left(downloadVersion, 5))
                && left(lastVersion, 5).equals(left(installVersion, 5))){
            if (downloadVersion.contains("-test")){
                filename = getIniValue("FirmwareFiles", "bintest");
            } else{
                filename = getIniValue("FirmwareFiles", "bin");
            }
        } else{
            filename = getIniValue("FirmwareFiles", "bin");
            saveIniValue("ToggleFwVersion", "downloadVersion", installVersion);
        }
        writeToLog("Download file: " + filename);
        return filename;
    }

    public String determineFwTargetModule() throws Throwable {
        String curVersion = getIniValue("ToggleFwVersion", "currentVersion");
        String targetModule = "1";

        if (curVersion.contains("2.0") || curVersion.contains("2.1")){
            targetModule = "2";
        }
        return targetModule;
    }

    public String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get((path)));
        return new String(encoded, encoding);
    }

    public String[] returnDistinctValues(String testCases, String regex){
        String[] result = testCases.split(regex);
        String hulp = "";
        String newResult = "";

        for (int t = 0; t < result.length; t++){
            if (! result[t].equalsIgnoreCase(hulp)){
                newResult = newResult + result[t] + ";";
            }
            hulp = result[t];
        }
        newResult = left(newResult, newResult.length() - 1);

        return newResult.split(";");
    }
    public String[] returnDistinctValues(String[] testCases){
        String newResult = "";
        String hulp = "";
        for (int t = 0; t < testCases.length; t++){
            if (! testCases[t].equalsIgnoreCase(hulp)){
                hulp = testCases[t];
                newResult += testCases[t] + ";";
            } else{
                hulp = testCases[t];
            }
        }
        newResult = left(newResult, newResult.length() - 1);

        return newResult.split(";");
    }
    public List<String> returnDistinctValues(List<String> items){
        List<String> newResult = new ArrayList<>();
        String hulp = "";

        quickSort(items, 0, items.size() - 1 );

        for (String item : items) {
            if (!item.equalsIgnoreCase(hulp)) {
                hulp = item;
                newResult.add(item);
            } else {
                hulp = item;
            }
        }
        return newResult;
    }

    public void quickSort(String[] array, int start, int end) {
        int i = start;
        int k = end;
        if (end - start >= 1) {
            String pivot = array[start];
            while (k > i) {
                while (array[i].compareTo(pivot) <= 0 && i <= end && k > i)
                    i++;
                while (array[k].compareTo(pivot) > 0 && k >= start && k >= i)
                    k--;
                if (k > i)
                    swap(array, i, k);
            }
            swap(array, start, k);
            quickSort(array, start, k - 1);
            quickSort(array, k + 1, end);
        } else { return; }
    }
    public void quickSort(List<String> array, int start, int end) {
        int i = start;
        int k = end;
        if (end - start >= 1) {
            String pivot = array.get(start);
            while (k > i) {
                while (array.get(i).compareTo(pivot) <= 0 && i <= end && k > i)
                    i++;
                while (array.get(k).compareTo(pivot) > 0 && k >= start && k >= i)
                    k--;
                if (k > i)
                    swap(array, i, k);
            }
            swap(array, start, k);
            quickSort(array, start, k - 1);
            quickSort(array, k + 1, end);
        } else { return; }
    }

    public String convertFloatToTime(Float input){
        int wholeSecs = (int) (input + 0.5);
        int hrs = (int) (input / 3600);
        int sec =  wholeSecs - (hrs * 3600);
        int min = (int) sec / 60;
        sec = sec - (min * 60);
        return formatTimePart(hrs) + ":" + formatTimePart(min)+ ":" + formatTimePart(sec);
    }

    /******************************************************************************
     ***   private functions
     ******************************************************************************/
    private void swap(String[] array, int index1, int index2) {
        String temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
    private void swap(List<String> array, int index1, int index2) {
        String temp = array.get(index1);
        array.set(index1, array.get(index2));
        array.set(index2, temp);
    }
    private Date subtractDay(Date date, Integer daysToSubstract) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, daysToSubstract);
        return cal.getTime();
    }
    private String formatMilliSecs(Integer milliSecs) {
        String hulp = milliSecs.toString();
        switch (hulp.length()) {
            case 2 -> hulp = "0" + hulp;
            case 1 -> hulp = "00" + hulp;
            case 0 -> hulp = "000" + hulp;
        }
        return hulp;
    }

    private String replaceEscapeSequence(String thisString){
        if (thisString.contains(ANSI_RESET)){
            thisString = thisString.replace(ANSI_RESET,"");
        }
        if (thisString.contains(ANSI_RED)){
            thisString = thisString.replace(ANSI_RED,"(Assert error) ==> ");
        }
        return thisString;
    }


}
