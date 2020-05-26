package org.billing.api.processor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.billing.api.data.Journal;
import org.billing.api.data.Member;
import org.billing.api.data.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class TransactionRepository {

	private JdbcTemplate jdbcTemplate;

	public BigDecimal getBalanceInquiry(int billerID) {
		return jdbcTemplate.queryForObject(
				"select sum(amount) as balance from journal where member_id = ? and status = 'PROCESSED'",
				BigDecimal.class, billerID);
	}

	public BigDecimal getPendingBalance(int billerID) {
		return jdbcTemplate.queryForObject(
				"select sum(amount) as balance from journal where member_id = ? and status = 'PENDING'",
				BigDecimal.class, billerID);
	}

	public List<Journal> loadTransactionHistory(int pageSize, int rowNum, int id) {
		try {
			List<Journal> biller = this.jdbcTemplate.query(
					"SELECT id, member_id, transfer_type_id, reference_no, amount, description, status, transaction_number, transaction_date FROM journal WHERE member_id = ? ORDER BY id DESC LIMIT ?,?;",
					new Object[] { id, pageSize, rowNum }, new RowMapper<Journal>() {
						public Journal mapRow(ResultSet rs, int arg1) throws SQLException {
							Journal journal = new Journal();
							Member biller = new Member();
							biller.setId(rs.getInt("member_id"));
							TransferType trfType = new TransferType();
							trfType.setId(rs.getInt("transfer_type_id"));
							journal.setMember(biller);
							journal.setTransferType(trfType);
							journal.setAmount(rs.getBigDecimal("amount"));
							journal.setDescription(rs.getString("description"));
							journal.setId(rs.getInt("id"));
							journal.setReferenceNo(rs.getString("reference_no"));
							journal.setStatus(rs.getString("status"));
							journal.setTransactionDate(rs.getTimestamp("transaction_date"));
							journal.setTransactionNumber(rs.getString("transaction_number"));
							return journal;
						}
					});
			return biller;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer totalTransaction(int billerID) {
		Integer count = this.jdbcTemplate.queryForObject("SELECT COUNT(id) from journal WHERE member_id = ?;",
				new Object[] { billerID }, Integer.class);
		return count;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
