package com.portal.av;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import com.opencsv.exceptions.CsvException;
import com.portal.av.models.UserProfile;
import com.portal.av.repoImpl.CompanyRepoImpl;
import com.portal.av.repoImpl.MyReaderImpl;
import com.portal.av.repoImpl.MyWriterImpl;

/*
 * Requirements:
 * 1) Create a file for each insurance company
 * 2) For each user profile, organize them into their respective insurance company file
 * 3) ADDITIONAL REQ:
 * 		1) No duplicate users (replace with highest version)
 * 		2) all files should be sorted asc by last and first name
 */
public class Driver {

	private static final String inputFilePath = "csv/sample.csv";
	public static void main(String[] args) {

		try {
			// Get Inputs
			MyReaderImpl myReader = new MyReaderImpl();
			List<String[]> inputLines = myReader.parseCsv(myReader.openCsv(inputFilePath));
			Map<String, UserProfile> inputProfiles = myReader.getUserProfiles(inputLines);
			
			
			// Map Inputs to companies
			MyWriterImpl myWriter = new MyWriterImpl();
			CompanyRepoImpl companyRepo = new CompanyRepoImpl();
			Queue<String> companies = companyRepo.getCompanies(
					inputProfiles.values().stream().collect(Collectors.toList()));
			Queue<UserProfile> profiles = companyRepo.getProfileQueue(inputProfiles);
			Map<String, List<UserProfile>> newCompanyProfileMap = 
					companyRepo.mapProfileToCompany(new LinkedList<>(companies), new LinkedList<>(profiles));
			
			Map<String, List<UserProfile>> companyProfileMap = new HashMap<>();
			while(!companies.isEmpty()) {
				String key = companies.poll();
				String filepath = "csv/Company/" + key;
				List<UserProfile> newCompanyProfileList = newCompanyProfileMap.get(key);
				if(myWriter.fileExists(filepath)) {
					List<String[]> companyProfileLines = myReader.parseCsv(myReader.openCsv(filepath));
					Map<String, UserProfile> oldProfilesMap = myReader.getUserProfiles(companyProfileLines);
					companyProfileMap.put(key, companyRepo.combineProfileLists(newCompanyProfileList, oldProfilesMap));
				}else {
					companyProfileMap.put(key, newCompanyProfileList);
				}
			}
			
			// Write to outputs
			myWriter.csvWriter(companyProfileMap);
			

			
			System.out.println("Complete");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
