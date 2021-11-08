package com.portal.av.repoImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.opencsv.CSVWriter;
import com.portal.av.models.UserProfile;
import com.portal.av.util.ProfileComparator;

public class MyWriterImpl {

	public boolean fileExists(String filepath) {
		File tempFile = new File(filepath);
		return tempFile.exists();
	}
	
	public void csvWriter(Map<String, List<UserProfile>> companyProfileMap) throws URISyntaxException, IOException {
		for(Entry<String, List<UserProfile>> companyProfileEntry : companyProfileMap.entrySet()) {
			String filename = "csv/Company/" + companyProfileEntry.getKey() + ".csv";
			
			Path path = null;
			if(!fileExists(filename)) {
				path = Paths.get(new File("src/main/resources/" + filename).toURI());
			} else {
				path = Paths.get(ClassLoader.getSystemResource(filename).toURI());
			}
			List<String[]> lines = getCsvArray(companyProfileEntry.getValue());
			CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));
			writer.writeAll(lines);
			writer.close();
		}
	}
	
	private List<String[]> getCsvArray(List<UserProfile> userProfiles) {
		List<String[]> output = new ArrayList<>();
		String[] headers = {"User Id", "First Name", "Last Name", "Version", "Insurance Company"};
		output.add(headers);
		sortByName(userProfiles);
		for(int i=0; i<userProfiles.size(); i++) {
			String[] line = new String[5];
			line[0] = userProfiles.get(i).getUserId();
			line[1] = userProfiles.get(i).getFirstName();
			line[2] = userProfiles.get(i).getLastName();
			line[3] = "" + userProfiles.get(i).getVersion();
			line[4] = userProfiles.get(i).getInsuranceCompany();
			output.add(line);
		}
		return output;
	}
	
	public String mkDir(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdir();
		}
		return file.getAbsolutePath();
	}
	
	private void sortByName(List<UserProfile> profiles) {
		Collections.sort(profiles, new ProfileComparator());
	}
}
