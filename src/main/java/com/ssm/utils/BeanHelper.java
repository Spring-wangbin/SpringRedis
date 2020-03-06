package com.ssm.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public final class BeanHelper {
	private static final Log logger = LogFactory.getLog(BeanHelper.class);
	private static ApplicationContext appCtx;

	public static boolean initialized = false;

	public static void setApplicationContext(
			ApplicationContext applicationContext) {
		appCtx = applicationContext;
		initialized = true;
	}

	public static ApplicationContext getApplicationContext(){
		return appCtx;
	}

	public static Object findBean(String beanId) {
		Object service = null;
		try {
			service = appCtx.getBean(beanId);
		} catch (NoSuchBeanDefinitionException ex) {
			throw new RuntimeException("no such bean for[" + beanId + "]", ex);
		} catch (BeansException ex) {
			throw new RuntimeException("bean exception for[" + beanId + "]", ex);
		}
		return service;
	}

	@SuppressWarnings(value = "all")
	public static Object findBeanByClass(Class clz) throws RuntimeException {
		if (logger.isDebugEnabled()) {
			logger.debug("findBeanOfType="
					+ (clz == null ? "Empty Class Name" : clz.getName()));
		}
		if (clz == null) {
			return null;
		}
		Object service = null;
		try {
			Map<String, Object> serviceMap = appCtx.getBeansOfType(clz);
			Iterator<String> beanNames = serviceMap.keySet().iterator();
			while (beanNames.hasNext()) {
				Object instance = serviceMap.get(beanNames.next());
				if (instance.getClass().equals(clz)) {
					service = instance;
				} else if (AopUtils.isAopProxy(instance)) {
					service = instance;
					break;
				}
			}
		} catch (NoSuchBeanDefinitionException ex) {
			throw new RuntimeException("no such bean for[" + clz + "]", ex);
		} catch (BeansException ex) {
			throw new RuntimeException("bean exception for[" + clz + "]", ex);
		}

		return service;
	}

	public static String getMessage(String key, Object[] params, Locale locale) {
		if (locale == null) {
			locale = new Locale("zh_CN");
		}
		if (!initialized || appCtx == null) {
			StringBuffer sb = new StringBuffer("Message(");
			sb.append(key);
			sb.append(" Params: ");
			if (params != null)
				for (int i = 0; i < params.length; i++) {
					sb.append(params[i] + ",");
				}
			return sb.toString();
		}
		String i18n = "";
		try {
			i18n = appCtx.getMessage(key, params, locale);
		} catch (NoSuchMessageException e) {
			logger.error("i18n definition for [" + key
					+ "] not found in properties file.", e);
		}
		return i18n;
	}

	public static String getMessageDirect(String key, Object[] params,
			Locale locale) {
		if (locale == null) {
			locale = new Locale("zh_CN");
		}
		return appCtx.getMessage(key, params, locale);
	}
}
