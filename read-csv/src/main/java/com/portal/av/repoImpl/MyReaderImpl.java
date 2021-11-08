package com.portal.av.repoImpl;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.portal.av.models.UserProfile;

public class MyReaderImpl {

	public List<String[]> parseCsv(Reader reader) throws IOException, CsvException {
			CSVReader csvReader = new CSVReader(reader);
			List<String[]> list = new ArrayList<>();
			list = csvReader.readAll();
			reader.close();
			csvReader.close();
			return list;
	}
	
	public Reader openCsv(String filePath) throws IOException, URISyntaxException { 
		return Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(filePath).toURI()));
	}
	
	public Map<String, UserProfile> getUserProfiles(List<String[]> parsedCsv) {
		Map<String, UserProfile> profileMap = new HashMap<>();
		
		for (int i=1; i<parsedCsv.size(); i++) {
			UserProfile newProfile = UserProfile.builder()
					.userId(parsedCsv.get(i)[0])
					.firstName(parsedCsv.get(i)[1])
					.lastName(parsedCsv.get(i)[2])
					.version(Integer.parseInt(parsedCsv.get(i)[3]))
					.insuranceCompany(parsedCsv.get(i)[4])
					.build();
			if (!profileMap.containsKey(newProfile.getUserId())) {
				profileMap.put(newProfile.getUserId(), newProfile);
			}else {
				UserProfile latest = getLatestVersion(
						profileMap.get(newProfile.getUserId()),
						newProfile);
				profileMap.put(latest.getUserId(), latest);
			}
					
		}
		
		return profileMap;
	}
	
	public UserProfile getLatestVersion(UserProfile p1, UserProfile p2) {
		if (p1.getVersion() > p2.getVersion()) return p1;
		return p2;
	}

}
