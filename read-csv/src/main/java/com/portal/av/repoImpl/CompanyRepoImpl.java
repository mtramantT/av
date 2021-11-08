package com.portal.av.repoImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import com.portal.av.models.UserProfile;

public class CompanyRepoImpl {

	public Map<String, List<UserProfile>> mapProfileToCompany(
			Queue<String> companies, 
			Queue<UserProfile> profiles) {
		
		//Get output map created
		Map<String, List<UserProfile>> companyProfileMap = new HashMap<>();
		while(!companies.isEmpty()) {
			companyProfileMap.put(companies.poll(), new ArrayList<>());
		}
		// Sort profiles
		while(!profiles.isEmpty()) {
			String key = profiles.peek().getInsuranceCompany();
			if(companyProfileMap.containsKey(key)) {
				companyProfileMap.get(key).add(profiles.poll());
			}else {
				List<UserProfile> newProfileList = new ArrayList<>();
				newProfileList.add(profiles.poll());
				companyProfileMap.put(key, newProfileList);
			}
		}
		
		return companyProfileMap;
	}
	
	public Queue<String> getCompanies(List<UserProfile> profiles) {
		LinkedList<String> companyQueue = new LinkedList<>();
		Set<String> uniqueSet = new HashSet<>();
		for(UserProfile profile : profiles) {
			if(uniqueSet.add(profile.getInsuranceCompany())) {
				companyQueue.add(profile.getInsuranceCompany());
			}
		}
		return companyQueue;
	}
	
	public Queue<UserProfile> getProfileQueue(Map<String, UserProfile> profiles) {
		return profiles.values().stream()
				.collect(Collectors.toCollection(LinkedList::new));
	}
	

	public List<UserProfile> combineProfileLists(
			List<UserProfile> newProfileList, Map<String, UserProfile> profileMap) {
		
		for(UserProfile newProfile : newProfileList) {
			if(profileMap.containsKey(newProfile.getUserId())) {
				UserProfile latest = getLatestVersion(
						profileMap.get(newProfile.getUserId()),
						newProfile);
				profileMap.put(latest.getUserId(), latest);
			}else {
				profileMap.put(newProfile.getUserId(), newProfile);
			}
		}
		return profileMap.values().stream().collect(Collectors.toList());
	}
	
	private UserProfile getLatestVersion(UserProfile p1, UserProfile p2) {
		if (p1.getVersion() > p2.getVersion()) return p1;
		return p2;
	}
}
