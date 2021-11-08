package com.portal.av.util;

import java.util.Comparator;

import com.portal.av.models.UserProfile;

public class ProfileComparator implements Comparator<UserProfile> {

	@Override
	public int compare(UserProfile p1, UserProfile p2) {
		int lastnameCompare = p1.getLastName().compareTo(p2.getLastName());
		int firstnameCompare = p1.getFirstName().compareTo(p2.getLastName());
		return (lastnameCompare == 0) ? lastnameCompare : firstnameCompare;
	}

}
