package edu.ncsu.csc316.cleaning.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc316.cleaning.dsa.DataStructure;

//Because the ReportManager class calls the CleaningManager class, and uses those
//methods' funtionalities to write the reports returned by the report manager's methods
//I have directly tested the report manager 
//I have also directly tested the getVaccumBag from cleaning manager

/**
*
* Tests the methods from report manager directly, to see how reports
* on frequently cleaned rooms, get events by room and vaccum bag status are reported
* 
* @author Lalitha Edupuganti
*
*/
public class ReportManagerTest {
	
	/** New cleaning manager instance*/
	private CleaningManager newCleaningManager;
	
	/** New report manager instance*/
	private ReportManager newReportManager;
	
//	/** New report manager instance*/
//	private ReportManager newEmptyReportManager;
	
	/** New report manager instance with specified maptype*/
	private ReportManager mapTypeReportManager;
	
	/** New cleaning events loge*/
	private final String cleaningTestFile = "input/text_testCEOne.txt";
	/** New rooms list*/
	private final String roomTestFile = "input/text_testRoomOne.txt";
	
	/** New cleaning events loge*/
//	private final String emptyCETestFile = "input/text_testCEEmpty.txt";
//	/** New rooms list*/
//	private final String emptyRoomTestFile = "input/text_testRoomEmpty.txt";
//	
	/**
	 *
	 * Sets the new instances of each cleaning manger and report manager
	 *
	*/
	@Before
	public void setUp() throws FileNotFoundException {
		newCleaningManager = new CleaningManager(roomTestFile, cleaningTestFile);
		newReportManager = new ReportManager(roomTestFile, cleaningTestFile);
		mapTypeReportManager = new ReportManager(roomTestFile, cleaningTestFile, DataStructure.UNORDEREDLINKEDMAP);
//		newEmptyReportManager = new ReportManager(emptyRoomTestFile, emptyCETestFile);
		
	}

	/**
	 *
	 * Tests the frequency report functionality for a valid test request
	 *
	*/
	@Test
	public void getFrequencyReport() {
		String expectedFrequencyReport = "Frequency of Cleanings [\n   Living Room has been cleaned 6 times\n" +
		        "   Dining Room has been cleaned 3 times\n" +
		        "   Guest Bathroom has been cleaned 2 times\n]";
		assertEquals(expectedFrequencyReport, newReportManager.getFrequencyReport(3));
	}
	
//	/**
//	 *
//	 * Tests the frequency report functionality for a invalid test request
//	 *
//	*/
//	@Test
//	public void getFrequencyInvalidReport() {
//		String expectedFrequencyReport = "Number of rooms must be greater than 0.";
//		assertEquals(expectedFrequencyReport, newReportManager.getFrequencyReport(-6));
//	}
//	
//	/**
//	 *
//	 * Tests the frequency report functionality for a empty test request
//	 *
//	*/
//	@Test
//	public void getFrequencyEmptyReport() {
//		String expectedFrequencyReport = "No rooms have been cleaned.";
//		assertEquals(expectedFrequencyReport, newEmptyReportManager.getFrequencyReport(6));
//	}
	
	/**
	 *
	 * Tests the get events by room report functionality for a valid test request
	 *
	*/
	@Test
	public void getRoomReport() {
		String expectedFrequencyReport = "Room Report [\n"
				+ "   Dining Room was cleaned on [\n"
				+ "      05/31/2021 09:27:45\n"
				+ "      05/23/2021 18:22:11\n"
				+ "      05/21/2021 09:16:33\n"
				+ "   ]\n"
				+ "   Foyer was cleaned on [\n"
				+ "      05/01/2021 10:03:11\n"
				+ "   ]\n"
				+ "   Guest Bathroom was cleaned on [\n"
				+ "      05/17/2021 04:37:31\n"
				+ "      05/08/2021 07:01:51\n"
				+ "   ]\n"
				+ "   Guest Bedroom was cleaned on [\n"
				+ "      05/23/2021 11:51:19\n"
				+ "      05/13/2021 22:20:34\n"
				+ "   ]\n"
				+ "   Kitchen was cleaned on [\n"
				+ "      (never cleaned)\n"
				+ "   ]\n"
				+ "   Living Room was cleaned on [\n"
				+ "      05/30/2021 10:14:41\n"
				+ "      05/28/2021 17:22:52\n"
				+ "      05/12/2021 18:59:12\n"
				+ "      05/11/2021 19:00:12\n"
				+ "      05/09/2021 18:44:23\n"
				+ "      05/03/2021 17:22:52\n"
				+ "   ]\n"
				+ "   Office was cleaned on [\n"
				+ "      06/01/2021 13:39:01\n"
				+ "   ]\n"
				+ "]";
		assertEquals(expectedFrequencyReport, newReportManager.getRoomReport());
		
	}
	

	/**
	 *
	 * Tests the get events by room report functionality for a valid test request
	 * with a specific or a requested map type
	 *
	*/
	@Test
	public void getUnoreredLinkedMapRoomReport() {
		String expectedFrequencyReport = "Room Report [\n"
				+ "   Dining Room was cleaned on [\n"
				+ "      05/31/2021 09:27:45\n"
				+ "      05/23/2021 18:22:11\n"
				+ "      05/21/2021 09:16:33\n"
				+ "   ]\n"
				+ "   Foyer was cleaned on [\n"
				+ "      05/01/2021 10:03:11\n"
				+ "   ]\n"
				+ "   Guest Bathroom was cleaned on [\n"
				+ "      05/17/2021 04:37:31\n"
				+ "      05/08/2021 07:01:51\n"
				+ "   ]\n"
				+ "   Guest Bedroom was cleaned on [\n"
				+ "      05/23/2021 11:51:19\n"
				+ "      05/13/2021 22:20:34\n"
				+ "   ]\n"
				+ "   Kitchen was cleaned on [\n"
				+ "      (never cleaned)\n"
				+ "   ]\n"
				+ "   Living Room was cleaned on [\n"
				+ "      05/30/2021 10:14:41\n"
				+ "      05/28/2021 17:22:52\n"
				+ "      05/12/2021 18:59:12\n"
				+ "      05/11/2021 19:00:12\n"
				+ "      05/09/2021 18:44:23\n"
				+ "      05/03/2021 17:22:52\n"
				+ "   ]\n"
				+ "   Office was cleaned on [\n"
				+ "      06/01/2021 13:39:01\n"
				+ "   ]\n"
				+ "]";
		assertEquals(expectedFrequencyReport, mapTypeReportManager.getRoomReport());
		
	}
	
	/**
	 *
	 * Tests the getVacuumBagReport functionality from the ReportManager class
	 * for a valid test, where the time leads to a bag not in the need for replacement
	 * but is a valid request
	 *
	*/
	@Test
	public void getVaccumBagReport() throws FileNotFoundException  {
		String expectedFrequencyReport = "Vacuum Bag Report (last replaced 05/28/2021 14:15:02) [\n"
				+ "   Bag is due for replacement in 3742 SQ FT\n"
				+ "]";
		assertEquals(expectedFrequencyReport, newReportManager.getVacuumBagReport("05/28/2021 14:15:02"));
		
	}
	
	/**
	 *
	 * Tests the getVacuumBag functionality from the CleaningManager class
	 * for a valid test, checks if the correct square feet are returned
	 *
	*/
	@Test
	public void getVaccumBag() throws FileNotFoundException  {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
    	
	    LocalDateTime lastReplaced = LocalDateTime.parse("05/28/2021 14:15:02", formatter);
	   

		assertEquals(1538, newCleaningManager.getCoverageSince(lastReplaced));
		
	}
	
	/**
	 *
	 * Tests the getVacuumBagReport functionality from the ReportManager class
	 * for a valid test, where the time since is the earliest time of the list
	 * and bag must be replaced
	 *
	*/
	@Test
	public void getFullVaccumBagReport() throws FileNotFoundException  {
		String expectedFrequencyReport = "Vacuum Bag Report (last replaced 05/01/2021 00:00:00) [\n"
				+ "   Bag is overdue for replacement!\n"
				+ "]";
		assertEquals(expectedFrequencyReport, newReportManager.getVacuumBagReport("05/01/2021 00:00:00"));
		
	}
	

}
