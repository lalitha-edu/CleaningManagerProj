package edu.ncsu.csc316.cleaning.manager;

import java.io.FileNotFoundException;
import edu.ncsu.csc316.cleaning.data.CleaningLogEntry;
import edu.ncsu.csc316.cleaning.data.RoomRecord;
import edu.ncsu.csc316.cleaning.dsa.Algorithm;
import edu.ncsu.csc316.cleaning.dsa.DSAFactory;
import edu.ncsu.csc316.cleaning.dsa.DataStructure;
import edu.ncsu.csc316.cleaning.io.InputReader;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.sorter.Sorter;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
*
* Class that uses methods to generate the lists and values used by the ReportManager
* class to generate reports on frequency, vacuum bag status and room report
* 
* @author Lalitha Edupuganti
*
*/
public class CleaningManager {
	/** A generic list that holds a list of entries of type CleaningLogEntry */
	private List<CleaningLogEntry> newList = null;
	/** A generic map that holds entries of type String, RoomRecord */
	private Map<String, RoomRecord> roomList = null;
	/** A generic list that holds a list of entries of type RoomRecord */
	private List<RoomRecord> roomRecords = null;
	
	/**
     * The constructor that takes in a roomFile, cleaning log file, and the specified
     * mapType
     * @param pathToRoomFile the file that holds the list of rooms
     * @param pathToLogFile the file that holds the list of cleaning events
     * @param mapType the type of map datastructure to hold the entries of room and cleaning events
     * @throws FileNotFoundException if file cannot be found
    */
    public CleaningManager(String pathToRoomFile, String pathToLogFile, DataStructure mapType) throws FileNotFoundException {
        DSAFactory.setListType(DataStructure.ARRAYBASEDLIST);
        DSAFactory.setComparisonSorterType(Algorithm.MERGESORT);
        DSAFactory.setNonComparisonSorterType(Algorithm.COUNTING_SORT);
        DSAFactory.setMapType(mapType);
        
        
        newList = DSAFactory.getIndexedList();
    	
    	newList = InputReader.readLogFile(pathToLogFile);
    	
    	roomList = DSAFactory.getMap(null);
    	
    	roomRecords = InputReader.readRoomFile(pathToRoomFile);
    	
    	for (RoomRecord roomRecord : roomRecords) {
    	    // Assuming getRoomId() returns the String key for the SkipListMap
    	    String roomId = roomRecord.getRoomID();
    	    roomList.put(roomId, roomRecord);
    	}
        
    }
    
    /**
     * The constructor that takes in a roomFile, cleaning log file, and the default
     * mapType
     * @param pathToRoomFile the file that holds the list of rooms
     * @param pathToLogFile the file that holds the list of cleaning events
     * @throws FileNotFoundException if file cannot be found
    */
    public CleaningManager(String pathToRoomFile, String pathToLogFile) throws FileNotFoundException {
        this(pathToRoomFile, pathToLogFile, DataStructure.SKIPLIST);
    }

    /**
     * This method returns a Map that holds a entries of type String, List of CleaningLogEntry
     * The method takes the listed rooms and their cleaning events and stores them into a map
     *
     * @return getEventByRoom the map created and filled by entries of the room name and corresponding 
     * cleaning events
    */
    public Map<String, List<CleaningLogEntry>> getEventsByRoom() {
    	
    	Map<String, List<CleaningLogEntry>> getEventByRoomMap = DSAFactory.getMap(null);
    	
    	if (roomList.isEmpty() || newList.isEmpty()) {
    		return null;
    	}
    	
    	// Initialize the map with all room IDs and empty lists
        for (RoomRecord roomToRead : roomRecords) {
            String roomString = roomToRead.getRoomID();
            getEventByRoomMap.put(roomString, DSAFactory.getIndexedList());
        }

        // Populate the map with the cleaning events
        for (CleaningLogEntry cleaningEvent : newList) {
            String roomString = cleaningEvent.getRoomID();
            List<CleaningLogEntry> eventList = getEventByRoomMap.get(roomString);

            // Add the current cleaning event to the list
            eventList.addFirst(cleaningEvent);
        }

        // Sort the lists by time
        for (List<CleaningLogEntry> eventList : getEventByRoomMap.values()) {
            sortByTime(eventList);
        }

        return getEventByRoomMap;
    }

    /**
     * This method provides an integer value the square feet that was cleaned so far 
     * from the provided time stamp
     *
     * @param time the time from which coverage should be calculated
     * @return int the total squarefeet covered and cleaned from the input time
    */
    public int getCoverageSince(LocalDateTime time) {
    	
    	// Value for if the last bag change time has passed in traversal of C
    	boolean bagChanged = false;

    	// Variable to track the position in the list of cleaning events
    	int index = 0;

    	// Total square footage cleaned
    	int sqrFeet = 0;

    	//sorted the input list C into descending order by Time
    	sortByTime(newList);

    	//while loop runs until bagChanged variable does not turn to true
    	while (index < newList.size() && !bagChanged) {
    	
    			CleaningLogEntry event = newList.get(index);
            	//check if the specific event’s time is earlier than the param time
            	if (event.getTimestamp().compareTo(time) >= 0) {
            	      //this means event occurred after bag change time
            	      //get specific event’s room
    	        	RoomRecord room = roomList.get(event.getRoomID());
    	        	//calculate and update sqrFeet to hold number of square feet cleaned
    	        	Double percentCompleteDouble = (double) event.getPercentCompleted() / 100.0;
    	        	sqrFeet += (room.getLength() * room.getWidth()) * percentCompleteDouble;
    	        	//update index
    	        	index++;
            	} else {
            		bagChanged = true;
            	}
    	}

    	return sqrFeet;


    }
    
    /**
     * Private helper method that is used to sort through a list of entries of tyep CleaningLogEntry
     * The method sorts the input generic list by time in descending order where the 
     * most recent cleaning event comes first
     *
     * @param newEntry the list of cleaning events to sort
    */
    private void sortByTime(List<CleaningLogEntry> newEntry) {
    	int originalSize = newEntry.size();
        
    	CleaningLogEntry[] sortingList = new CleaningLogEntry[originalSize];
   
    	int i = 0;
    	for(i = 0; i < originalSize - 1; i++) {
    		sortingList[i] = newEntry.removeLast();
    	}
    	if (originalSize > 0) {
    		sortingList[i] = newEntry.last();
    	}
    	
    	Sorter<CleaningLogEntry> cleaningSorter = DSAFactory.getComparisonSorter(new TimeComparator());
    	
    	cleaningSorter.sort(sortingList);
    	
    	int j = 0;
    	while(j < originalSize) {
    		newEntry.addLast(sortingList[j]);
    		j++;
    	}
    	
    	if (originalSize > 0) {
        	newEntry.removeFirst();
    	}

    	
    }
    
    /**
     * Private class that holds the compareTo method to compare list entries of type
     * CleaningLogEntry based on each entry's timestamp where the more recent time should
     * come first
     * 
     * @author Lalitha Edupuganti
    */
    private class TimeComparator implements Comparator<CleaningLogEntry> {

    	/**
         * Compare to method that compares two entries based on time 
         * to sort the entries in order of most recent to least recent time
         * 
         * @param entry1 the first entry to be compared
         * @param entry2 the second entry to be compared
         * @return int the int returned from comparing the two entries
        */
    	public int compare(CleaningLogEntry entry1, CleaningLogEntry entry2) {
			if (entry2.getTimestamp().compareTo(entry1.getTimestamp()) > 0) {
				return 1;
			} else if (entry2.getTimestamp().compareTo(entry1.getTimestamp()) < 0) {
				return -1;
			} else  {
				return 0;
			}
		}


    	
    }
    

}