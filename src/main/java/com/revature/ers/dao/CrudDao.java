package com.revature.ers.dao;

import java.util.List;

public interface CrudDao<T>{	
	//CREATE
	public T insert(T t);
	
	//READ
	public T selectById(int id);
	public List<T> selectAll();
	
	//UPDATE
	public boolean update(T t);
	
	//DELETE
	public boolean delete(T t);
}
