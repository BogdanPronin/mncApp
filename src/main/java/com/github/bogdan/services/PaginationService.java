package com.github.bogdan.services;

import com.github.bogdan.models.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

public class PaginationService {
    public static <T> ArrayList<T> getPage(Dao<T,Integer> dao,int page, int size) throws SQLException {
        ArrayList<T> list = new ArrayList();
        long startRow = (page-1)*size;
        long maxRows = startRow+size;
        QueryBuilder<T, Integer> queryBuilder = dao.queryBuilder();
        queryBuilder.offset(startRow).limit(maxRows);
        list = (ArrayList<T>) dao.query(queryBuilder.prepare());
        return list;
    }
}
