package com.cbsinc.cms.publisher.controllers;

/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko.
 * Konstantin Grabko is Owner and author this code. You can not use it and you
 * cannot change it without written permission from Konstantin Grabko Email:
 * konstantin.grabko@yahoo.com or konstantin.grabko@gmail.com
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002-2014
 * </p>
 * <p>
 * Company: CENTER BUSINESS SOLUTIONS INC
 * </p>
 * 
 * @author Konstantin Grabko
 * @version 1.0
 */
public interface SpecialCatalog {

	// Вывод служебных страниц
	public static long FOR_EXTERNAL_PAGE = -11;

	// вывод наиболее посещаемых страниц
	public static long OUTPUT_PAGES_SORT_BY_VISIT_STATISTICS = -10;

	// вывод всех с сортировкой по ID
	public static long OUTPUT_PAGES_SORT_BY_SOFT_ID = -2;

	// вывод всех с сортировкой по дате создания
	public static long OUTPUT_PAGES_SORT_BY_CREATED_DATE = -6;

	// вывод новинок которые ввел один пользователь из своего кабинета
	public static long OUTPUT_PAGES_NEW_USER_CONTENT_FOR_APROVEMENT_SORT_BY_DATE = -9;

	// вывод всех по рейтингу
	public static long OUTPUT_PAGES_SORT_BY_RATING = -7;

	// forum mess
	public static long OUTPUT_PAGES_AREA_FROM_USERSITE_TO_MAIN_SITE = -3;

	// posted message is for aprove admin
	public static long CONTENT_WAITING_FOR_APROVEMENT = -4;

	// posted message is no aprove admin
	public static long CONTENT_REJECTED_PUBLICATION = -5;

	public static long OUTPUT_PAGES_FROM_NEWS_CATALOG = -1;

	public static long ROOT_CATALOG = 0;

}
