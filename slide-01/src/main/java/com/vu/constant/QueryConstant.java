package com.vu.constant;

public class QueryConstant {

	public static final String FIND_ROLE_NAMES_BY_USER_ID = "select r.name from public.roles r "
			+ "inner join public.users u on r.id = u.role_id " + "where u.id = (:userId);";

	public static final String FIND_USER_BY_EMAIL = "select * from public.users u " + "where u.email = (:email);";

	public static final String FIND_ALL_CUSTOMERS_WITH_FULLNAME = "select * " + "from public.users u "
			+ "where u.fullname like (:fullname);";
}