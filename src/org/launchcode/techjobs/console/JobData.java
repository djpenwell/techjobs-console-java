package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.*;  // Needed to add this utility in order to utilize PATTERN functionality. https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }
	
	public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

	/**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
	public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);
				//  https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
				//  https://stackoverflow.com/questions/15409296/what-is-the-use-of-pattern-quote-method
				//  The Pattern.quote method quotes part of a regex pattern to make regex interpret it as string literals.
                //  Say you have some user input in your search program, and you want to regex for it. 
				//  But this input may have unsafe characters so you can use
                //  Pattern pattern = Pattern.compile(Pattern.quote(userInput));
				//
            if (Pattern.compile(Pattern.quote(value), Pattern.CASE_INSENSITIVE).matcher(aValue).find()) {
                jobs.add(row);
            }
        }

        // Return Search Results
        if (jobs.size() == 0){
            String search_area = column;
            search_area = search_area.toUpperCase();
            System.out.println("Sorry, are no matches for \"" + value + "\" in the \"" + search_area + "\" data field.");
        }
        return jobs;
    }
	 
    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

    public static ArrayList<HashMap<String, String>> findAllColumns(String value) {
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            ArrayList<String> keys = new ArrayList<>(row.keySet());
            for (String column : keys) {
                String aValue = row.get(column);
				//  https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
				//  https://stackoverflow.com/questions/15409296/what-is-the-use-of-pattern-quote-method
				//  The Pattern.quote method quotes part of a regex pattern to make regex interpret it as string literals.
                //  Say you have some user input in your search program, and you want to regex for it. 
				//  But this input may have unsafe characters so you can use
                //  Pattern pattern = Pattern.compile(Pattern.quote(userInput));
				//
                if (Pattern.compile(Pattern.quote(value), Pattern.CASE_INSENSITIVE).matcher(aValue).find()) {
                    jobs.add(row);
                    break;
                }
            }

        }

        // Return Search Results
        if (jobs.size() == 0){
            System.out.println("Sorry, are no matches for \"" + value + "\" in any of the data fields.");
        }
        return jobs;
    }



}