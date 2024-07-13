package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;

import edu.ncsu.csc316.cleaning.dsa.Algorithm;
import edu.ncsu.csc316.cleaning.dsa.DSAFactory;
import edu.ncsu.csc316.cleaning.dsa.DataStructure;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.Map.Entry;

import edu.ncsu.csc316.dsa.sorter.Sorter;

/**
 *
 * Class that uses methods to generate different reports including the 
 * frequency of rooms, vacuum bag status report and events by room
 * 
 * @author Lalitha Edupuganti
 *
*/
public class ReportManager {

	/** Private cleaningManager field that holds a cleaningManager necessary to 
	 * access different method associated with the class*/
    private CleaningManager manager = null;

    /**
     * The constructor that takes in a roomFile, cleaning log file, and the specified
     * mapType
     * @param pathToRoomFile the file that holds the list of rooms
     * @param pathToLogFile the file that holds the list of cleaning events
     * @param mapType the type of map datastructure to hold the entries of room and cleaning events
     * @throws FileNotFoundException if file cannot be found
    */
    public ReportManager(String pathToRoomFile, String pathToLogFile, DataStructure mapType) throws FileNotFoundException {
    	manager = new CleaningManager(pathToRoomFile, pathToLogFile, mapType);
        DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        DSAFactory.setNonComparisonSorterType(Algorithm.COUNTING_SORT);
        DSAFactory.setMapType(mapType);
        
       
    }
    
    /**
     * The constructor that takes in a roomFile, cleaning log file, and the default
     * mapType
     * @param pathToRoomFile the file that holds the list of rooms
     * @param pathToLogFile the file that holds the list of cleaning events
     * @throws FileNotFoundException if file cannot be found
    */
    public ReportManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {
        this(pathToRoomFile, pathToLogFile, DataStructure.SKIPLIST);
    }

    /**
     * The method that prints a string report on the current vacuum bag capacity
     * This method will either tell how much space before a replacement is needed or
     * if a replacement is needed
     * @param timestamp used to calculate and report how many square feet are remaining
     * @return vaccumString.toString the string report on vaccum bag capacity
    */
    public String getVacuumBagReport(String timestamp) {
    	int total = 5280;
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    	try {
	        LocalDateTime lastReplaced = LocalDateTime.parse(timestamp, formatter);
	        
	        int remainingSquareFeet = total - manager.getCoverageSince(lastReplaced);
	        
	        if (remainingSquareFeet <= 0) {
	        	StringBuilder vaccumString = new StringBuilder("Vacuum Bag Report (last replaced ").append(timestamp).append(") [\n");
	        	vaccumString.append("   Bag is overdue for replacement!\n]");
	        	return vaccumString.toString();
	        }
	        
	    	StringBuilder vaccumString = new StringBuilder("Vacuum Bag Report (last replaced ").append(timestamp).append(") [");
	    				vaccumString.append("\n" + "   Bag is due for replacement in " + remainingSquareFeet + " SQ FT\n]"); 
	
	        return vaccumString.toString();
	        
    	} catch (DateTimeParseException e) {
    		
    		String parseError = "Date & time must be in the format: MM/DD/YYYY HH:MM:SS";
    		return parseError;
    	}
    }

    /**
     * The method that prints a string report on frequency report of the list of provided rooms
     * This method will provide a report on the rooms in decending order of how frequently
     * they were cleaned
     * @param number the number of rooms to be reported
     * @return frequencyLine the string report on the frequency the rooms
    */
    public String getFrequencyReport(int number) {
    	String frequencyLine = "";
    	
    	if (number <= 0) {
    		frequencyLine = "Number of rooms must be greater than 0.";
    		return frequencyLine;
    	} else {
    		frequencyLine = "Frequency of Cleanings [\n";
    	}
    	
    	Map<String, List<CleaningLogEntry>> getEventByRoomMap = DSAFactory.getMap(null);
    	getEventByRoomMap = manager.getEventsByRoom();
    	
    	if (getEventByRoomMap == null) {
    		return "No rooms have been cleaned.";
    	}
    	
    	@SuppressWarnings("unchecked")
		Map.Entry<String, List<CleaningLogEntry>>[] sortedEntries =  new Map.Entry[getEventByRoomMap.size()];
    	
    	sortedEntries = sortByParam(getEventByRoomMap, true, false);
    	
    	Integer [] count = new Integer[getEventByRoomMap.size()];
    	
    	int index = 0;
    	
    	for (Map.Entry<String, List<CleaningLogEntry>> entry : sortedEntries) {
    		count[index] = entry.getValue().size();
    		index++;
    	}
    	
    	index = 0;
    	
    	for (Map.Entry<String, List<CleaningLogEntry>> entry : sortedEntries) {
    		if (index >= number) {
                break; // Stop when the requested number of rooms has been processed
            }
    		String room = entry.getKey();
    		frequencyLine += "   " + room + " has been cleaned " + count[index] + " times\n";
    		index++;		
    		
    	}
    	
    	
    	frequencyLine += "]";
    	
        return frequencyLine;
    }

    /**
     * This method provides a report on the listed rooms and their cleaning events
     * where the rooms appear in alphabetical order and the corresponding cleaning events
     * are from most recent to least recent
     *
     * @return roomReport.toString the string report the rooms
    */
    public String getRoomReport() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    	
    	Map<String, List<CleaningLogEntry>> getEventByRoomMap = DSAFactory.getMap(null);
    	getEventByRoomMap = manager.getEventsByRoom();
    	
    	if (getEventByRoomMap == null) {
    		return "No rooms have been cleaned.";
    	}
    	
    	@SuppressWarnings("unchecked")
		Map.Entry<String, List<CleaningLogEntry>>[] sortedEntries =  new Map.Entry[getEventByRoomMap.size()];
    	
    	sortedEntries = sortByParam(getEventByRoomMap, false, true);
    	
    	StringBuilder roomReport = new StringBuilder("Room Report [\n");
    	
    	for (Map.Entry<String, List<CleaningLogEntry>> entry : sortedEntries) {
	        String room = entry.getKey();
	        List<CleaningLogEntry> cleaningEvents = entry.getValue();

	        roomReport.append("   ").append(room).append(" was cleaned on [");

	        if (cleaningEvents.isEmpty()) {
	            roomReport.append("\n      (never cleaned)");
	        } else {
	            for (CleaningLogEntry cleaningEvent : cleaningEvents) {
	                roomReport.append("\n      " + cleaningEvent.getTimestamp().format(formatter));
	            }
	        }

	        roomReport.append("\n   ]\n");
	    }
    	
    	roomReport.append("]");

	    return roomReport.toString();
	    
    }
    
    /**
     * Private helper method that is used to sort through a list of map entries in format String, List -> CleaningLogEntry
     * The method either sorts the input Map, by either count (frequency of rooms cleaned) or to sort out the rooms alphabetically
     * depending on which param is true and false
     *
     * @param getEventByRoomMap the map to be sorted
     * @param isFrequency the comparator boolean to specify sorting
     * @param isRoomComp the comparator boolean to specify sorting
     * 
     * @return entryArray the array of sorted entries
    */
    private Map.Entry<String, List<CleaningLogEntry>>[] sortByParam(Map<String, List<CleaningLogEntry>> getEventByRoomMap, Boolean isFrequency, Boolean isRoomComp) {
    			
    	@SuppressWarnings("unchecked")
		Map.Entry<String, List<CleaningLogEntry>>[] entryArray = new Map.Entry[getEventByRoomMap.size()];
    	
    	
    	int i = 0;
    	for (Map.Entry<String, List<CleaningLogEntry>> entry : getEventByRoomMap.entrySet()) {
    		 entryArray[i] = entry; // Add the current entry to the array
    	     i++;
    	}
    	
    	if (isFrequency) {
    		Sorter<Entry<String, List<CleaningLogEntry>>> cleaningSorter = DSAFactory.getComparisonSorter(new FrequencyComparator());
        	
        	cleaningSorter.sort(entryArray);
    	}
    	
    	if (isRoomComp) {
    		Sorter<Entry<String, List<CleaningLogEntry>>> cleaningSorter = DSAFactory.getComparisonSorter(new RoomComparator());
        	
        	cleaningSorter.sort(entryArray);
    	}
    	
    	

    	return entryArray;
    	
    }
    
    
    /**
     * Private class that holds the compareTo method to compare map entries of type
     * Entry based on each entries's cleaning log size
     * 
     * @author Lalitha Edupuganti
    */
    private class FrequencyComparator implements Comparator<Entry<String, List<CleaningLogEntry>>> {

    	/**
         * Compare to method that compares two entries based on their cleaning events' list size
         * for frequency, the comparator helps sort entries in descending order of frequency
         * 
         * @param entry1 the first entry to be compared
         * @param entry2 the second entry to be compared
         * @return int the int returned from comparing the two entries
        */
    	public int compare(Entry<String, List<CleaningLogEntry>> entry1, Entry<String, List<CleaningLogEntry>> entry2) {
			if (entry2.getValue().size() > entry1.getValue().size()) {
				return 1;
			} else if (entry2.getValue().size() < entry1.getValue().size()) {
				return -1;
			} else  {
				return entry1.compareTo(entry2);
			}
		}


    	
    }
    
    /**
     * Private class that holds the compareTo method to compare map entries of type
     * Entry based on each entries's key (room name string) for alphabetical sorting
     * 
     * @author Lalitha Edupuganti
    */
    private class RoomComparator implements Comparator<Entry<String, List<CleaningLogEntry>>> {

    	/**
         * Compare to method that compares two entries based on their key value that is the room
         * name string which is compared to sort the entries in alphabetical order
         * 
         * @param entry1 the first entry to be compared
         * @param entry2 the second entry to be compared
         * @return int the int returned from comparing the two entries
        */
    	public int compare(Entry<String, List<CleaningLogEntry>> entry1, Entry<String, List<CleaningLogEntry>> entry2) {
			if (entry1.getKey().compareTo(entry2.getKey()) > 0) {
				return 1;
			} else if (entry1.getKey().compareTo(entry2.getKey()) < 0) {
				return -1;
			} else  {
				return entry1.compareTo(entry2);
			}
		}


    	
    }

}