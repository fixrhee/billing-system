package org.billing.api.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.billing.api.data.Billing;
import org.billing.api.data.Member;
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
public class BillingRepository {

	private JdbcTemplate jdbcTemplate;

	public List<Billing> getAllBilling(int pageSize, int rowNum, int billerID) {
		try {
			List<Billing> bill = this.jdbcTemplate.query(
					"SELECT id, member_id, name, description, billing_cycle, outstanding, created_date FROM billing WHERE member_id = ? ORDER BY id DESC LIMIT ?,?;",
					new Object[] { billerID, pageSize, rowNum }, new RowMapper<Billing>() {
						public Billing mapRow(ResultSet rs, int arg1) throws SQLException {
							Billing bill = new Billing();
							Member biller = new Member();
							biller.setId(rs.getInt("member_id"));
							bill.setMember(biller);
							bill.setId(rs.getInt("id"));
							bill.setName(rs.getString("name"));
							bill.setDescription(rs.getString("description"));
							bill.setBillingCycle(rs.getInt("billing_cycle"));
							bill.setOutstanding(rs.getBoolean("outstanding"));
							bill.setCreatedDate(rs.getTimestamp("created_date"));
							return bill;
						}
					});
			return bill;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Billing getBillingByID(Object id, int billerID) {
		try {
			Billing member = this.jdbcTemplate.queryForObject(
					"SELECT id, member_id, name, description, billing_cycle, outstanding, created_date FROM billing WHERE member_id = ? AND id = ?;",
					new Object[] { billerID, id }, new RowMapper<Billing>() {
						public Billing mapRow(ResultSet rs, int arg1) throws SQLException {
							Billing bill = new Billing();
							Member biller = new Member();
							biller.setId(rs.getInt("member_id"));
							bill.setMember(biller);
							bill.setId(rs.getInt("id"));
							bill.setName(rs.getString("name"));
							bill.setDescription(rs.getString("description"));
							bill.setBillingCycle(rs.getInt("billing_cycle"));
							bill.setOutstanding(rs.getBoolean("outstanding"));
							bill.setCreatedDate(rs.getTimestamp("created_date"));
							return bill;
						}
					});
			return member;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Map<Integer, Billing> getBillingInMap(List<Integer> ids) {
		String sql = "select * from billing where id in (:billingID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("billingID", ids);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		Map<Integer, Billing> mapRet = template.query(sql, paramMap, new ResultSetExtractor<Map<Integer, Billing>>() {
			@Override
			public Map<Integer, Billing> extractData(ResultSet rs) throws SQLException, DataAccessException {
				HashMap<Integer, Billing> mapRet = new HashMap<Integer, Billing>();
				while (rs.next()) {
					Billing bill = new Billing();
					Member biller = new Member();
					biller.setId(rs.getInt("member_id"));
					bill.setMember(biller);
					bill.setId(rs.getInt("id"));
					bill.setName(rs.getString("name"));
					bill.setDescription(rs.getString("description"));
					bill.setBillingCycle(rs.getInt("billing_cycle"));
					bill.setOutstanding(rs.getBoolean("outstanding"));
					bill.setCreatedDate(rs.getTimestamp("created_date"));
					mapRet.put(rs.getInt("id"), bill);
				}
				return mapRet;
			}
		});
		return mapRet;
	}

	public void createBilling(Billing billing, int id) {
		jdbcTemplate.update(
				"insert into billing (member_id, name, description, billing_cycle, outstanding) values (?, ?, ?, ?, ?)",
				id, billing.getName(), billing.getDescription(), billing.getBillingCycle(), billing.getOutstanding());
	}

	public void updateBilling(Billing billing, String id) {
		jdbcTemplate.update(
				"update billing set name = ?, description = ?, billing_cycle = ?, outstanding = ?  WHERE id = ?",
				billing.getName(), billing.getDescription(), billing.getBillingCycle(), billing.getOutstanding(), id);
	}

	public void deleteBilling(String id) {
		jdbcTemplate.update("delete from billing WHERE id = ?", id);
	}

	public Integer totalBilling(int billerID) {
		Integer count = this.jdbcTemplate.queryForObject("SELECT COUNT(id) from billing WHERE member_id = ?;",
				new Object[] { billerID }, Integer.class);
		return count;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
