package com.ssm.utils.database;

import com.ssm.pojo.User;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.ArrayList;

public class DataSourceExchange implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		DataSource dataSource = invocation.getMethod().getAnnotation(DataSource.class);
		DataSourceHolder.setDataSource(dataSource.name());
		try {
			return invocation.proceed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}