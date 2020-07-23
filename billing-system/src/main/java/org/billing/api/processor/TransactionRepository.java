package org.billing.api.processor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;

import org.billing.api.data.Account;
import org.billing.api.data.Journal;
import org.billing.api.data.Member;
import org.billing.api.data.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class TransactionRepository {

	private JdbcTemplate jdbcTemplate;

	public BigDecimal getBalanceInquiry(int memberID, Object accountID) {
		BigDecimal bal = jdbcTemplate.queryForObject(
				"select sum(journal) as balance from (select sum(-amount) as journal from journal where from_member_id = ? and from_account_id = ? and transaction_state = 'PROCESSED' union all select sum(amount) as journal from journal where to_member_id = ? and to_account_id = ? and transaction_state = 'PROCESSED') t1;",
				BigDecimal.class, memberID, accountID, memberID, accountID);
		if (bal == null) {
			return BigDecimal.ZERO;
		}
		return bal;
	}

	public BigDecimal getReservedAmount(int memberID, Object accountID) {
		return jdbcTemplate.queryForObject(
				"select sum(journal) as balance from (select sum(-amount) as journal from journal where from_member_id = ? and from_account_id = ? and transaction_state = 'PENDING' union all select sum(amount) as journal from journal where to_member_id = ? and to_account_id = ? and transaction_state = 'PENDING') t1;",
				BigDecimal.class, memberID, accountID, memberID, accountID);
	}

	public List<Journal> loadTransactionHistory(String start, String end, int pageSize, int rowNum, int fromMemberID) {
		try {
			List<Journal> biller = this.jdbcTemplate.query(
					"SELECT id, main_transaction_id, from_member_id, to_member_id, from_account_id, to_account_id, transfer_type_id, trace_number, transaction_number, request_token, amount, description, reference_number, transaction_state, transaction_date, modified_date FROM journal where from_member_id = ? AND (DATE(transaction_date) BETWEEN '2020-07-12' AND '2020-07-14') UNION ALL SELECT id, main_transaction_id, from_member_id, to_member_id, from_account_id, to_account_id, transfer_type_id, trace_number, transaction_number, request_token, amount, description, reference_number, transaction_state, transaction_date, modified_date FROM journal where to_member_id = ? AND (DATE(transaction_date) BETWEEN ? AND ?) ORDER BY id LIMIT ?,?;",
					new Object[] { fromMemberID, fromMemberID, start, end, pageSize, rowNum },
					new RowMapper<Journal>() {
						public Journal mapRow(ResultSet rs, int arg1) throws SQLException {
							Journal journal = new Journal();
							Member fromMember = new Member();
							fromMember.setId(rs.getInt("from_member_id"));
							Member toMember = new Member();
							toMember.setId(rs.getInt("to_member_id"));
							Account fromAccount = new Account();
							fromAccount.setId(rs.getInt("from_account_id"));
							Account toAccount = new Account();
							toAccount.setId(rs.getInt("to_account_id"));
							TransferType trfType = new TransferType();
							trfType.setId(rs.getInt("transfer_type_id"));
							journal.setId(rs.getInt("id"));
							journal.setFromMember(fromMember);
							journal.setToMember(toMember);
							journal.setFromAccount(fromAccount);
							journal.setToAccount(toAccount);
							journal.setTransferType(trfType);
							journal.setAmount(rs.getBigDecimal("amount"));
							journal.setDescription(rs.getString("description"));
							journal.setReferenceNo("reference_number");
							journal.setTraceNo(rs.getString("trace_number"));
							journal.setTransactionDate(rs.getTimestamp("transaction_date"));
							journal.setTransactionNumber(rs.getString("transaction_number"));
							journal.setTransactionState(rs.getString("transaction_state"));
							return journal;
						}
					});
			return biller;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Integer createPayment(String traceNo, String description, String refNo, BigDecimal amount, Integer trxTypeID,
			Integer fromAccountID, Integer toAccountID, Integer fromMemberID, Integer toMemberID, String trxNo,
			String trxState) {

		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(
						"insert into journal (from_member_id, to_member_id, from_account_id, to_account_id,  transfer_type_id, trace_number, transaction_number, amount, description, reference_number, transaction_state) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, fromMemberID);
				statement.setInt(2, toMemberID);
				statement.setInt(3, fromAccountID);
				statement.setInt(4, toAccountID);
				statement.setInt(5, trxTypeID);
				statement.setString(6, traceNo);
				statement.setString(7, trxNo);
				statement.setBigDecimal(8, amount);
				statement.setString(9, description);
				statement.setString(10, refNo);
				statement.setString(11, trxState);
				return statement;
			}
		}, holder);

		Integer primaryKey = holder.getKey().intValue();
		return primaryKey;
	}

	public Integer totalTransaction(int memberID) {
		Integer count = this.jdbcTemplate.queryForObject(
				"select sum(trx) as history_count from (SELECT COUNT(id) as trx from journal WHERE from_member_id = ? union all SELECT COUNT(id) as trx from journal WHERE to_member_id = ?) t1;",
				new Object[] { memberID, memberID }, Integer.class);
		return count;
	}

	public Integer validateTraceNo(String traceNo) {
		try {
			Integer id = this.jdbcTemplate.queryForObject(
					"select id from journal where trace_number = ? and (DATE(transaction_date)) = (DATE(NOW()));",
					new Object[] { traceNo }, Integer.class);
			return id;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
