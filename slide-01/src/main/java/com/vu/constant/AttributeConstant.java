package com.vu.constant;

public class AttributeConstant {

	// -- Search Attribute
	public static final String B2C_SEARCH_DTO_ATTRIBUTE = "searchDto";

	// --- Spring Security Attributes
	public static final String REQUIRED_LOGIN_ANY_ROLES_ATTRIBUTE = "hasAnyRole('ROLE_USER', 'ROLE_ADMIN')";
	public static final String REQUIRED_LOGIN_ADMIN_ROLE_ATTRIBUTE = "hasRole('ROLE_ADMIN')";

	// -- User Attributes
	public static final String USER_LIST_ATTRIBUTE = "users";
	public static final String USER_DETAIL_ATTRIBUTE = "user";
	public static final String USER_DTO_ATTRIBUTE = "userDto";
	public static final String USER_DTOS_ATTRIBUTE = "userDtos";
	public static final String USER_DELETED_ATTRIBUTE = "userDeleted";
}