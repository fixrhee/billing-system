package org.billing.api.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.billing.api.data.Account;
import org.billing.api.data.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class AccountRepository {
	private JdbcTemplate jdbcTemplate;

	public Account getAccountByID(Object id) {
		try {
			Account acc = this.jdbcTemplate.queryForObject(
					"SELECT id, name, system_account, created_date FROM account WHERE id = ?;", new Object[] { id },
					new RowMapper<Account>() {
						public Account mapRow(ResultSet rs, int arg1) throws SQLException {
							Account acc = new Account();
							acc.setId(rs.getInt("id"));
							acc.setCreatedDate(rs.getTimestamp("created_date"));
							acc.setName(rs.getString("name"));
							acc.setSystemAccount(rs.getBoolean("system_account"));
							return acc;
						}
					});
			return acc;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer validateAccountPermission(Object accountID, Integer groupID) {
		try {
			Integer id = this.jdbcTemplate.queryForObject(
					"SELECT id from account_permission WHERE account_id = ? AND group_id = ?;",
					new Object[] { accountID, groupID }, Integer.class);
			return id;
		} catch (EmptyResultDataAccessException ex) {
			return 0;
		}
	}

	public Map<Integer, Account> getAccountInMap(List<Integer> ids) {
		String sql = "select * from account where id in (:accountID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("accountID", ids);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		Map<Integer, Account> mapRet = template.query(sql, paramMap, new ResultSetExtractor<Map<Integer, Account>>() {
			@Override
			public Map<Integer, Account> extractData(ResultSet rs) throws SQLException, DataAccessException {
				HashMap<Integer, Account> mapRet = new HashMap<Integer, Account>();
				while (rs.next()) {
					Account acc = new Account();
					acc.setCreatedDate(rs.getTimestamp("created_date"));
					acc.setId(rs.getInt("id"));
					acc.setName(rs.getString("name"));
					acc.setSystemAccount(rs.getBoolean("system_account"));
					mapRet.put(rs.getInt("id"), acc);
				}
				return mapRet;
			}
		});
		return mapRet;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
