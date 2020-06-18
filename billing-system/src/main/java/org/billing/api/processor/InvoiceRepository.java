package org.billing.api.processor;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.billing.api.data.Billing;
import org.billing.api.data.Invoice;
import org.billing.api.data.Member;
import org.billing.api.data.PublishInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class InvoiceRepository {

	private JdbcTemplate jdbcTemplate;

	public List<Invoice> getAllInvoice(int pageSize, int rowNum, int billerID) {
		try {
			List<Invoice> inv = this.jdbcTemplate.query(
					"SELECT id, billing_id, biller_id, member_id, invoice_number, amount, description, active, created_date FROM invoice WHERE biller_id = ? ORDER BY id DESC LIMIT ?,?;",
					new Object[] { billerID, pageSize, rowNum }, new RowMapper<Invoice>() {
						public Invoice mapRow(ResultSet rs, int arg1) throws SQLException {
							Invoice inv = new Invoice();
							inv.setId(rs.getInt("id"));
							Billing bill = new Billing();
							bill.setId(rs.getInt("billing_id"));
							Member biller = new Member();
							biller.setId(rs.getInt("biller_id"));
							Member member = new Member();
							member.setId(rs.getInt("member_id"));
							inv.setBilling(bill);
							inv.setBiller(biller);
							inv.setMember(member);
							inv.setInvoiceNumber(rs.getString("invoice_number"));
							inv.setAmount(rs.getBigDecimal("amount"));
							inv.setDescription(rs.getString("description"));
							inv.setActive(rs.getBoolean("active"));
							inv.setCreatedDate(rs.getTimestamp("created_date"));
							return inv;
						}
					});
			return inv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<PublishInvoice> getAllPublishInvoice(int pageSize, int rowNum, int billerID) {
		try {
			List<PublishInvoice> inv = this.jdbcTemplate.query(
					"SELECT id, billing_id, biller_id, member_id, invoice_number, payment_code, amount, description, status, created_date FROM invoice_published WHERE biller_id = ? ORDER BY id DESC LIMIT ?,?;",
					new Object[] { billerID, pageSize, rowNum }, new RowMapper<PublishInvoice>() {
						public PublishInvoice mapRow(ResultSet rs, int arg1) throws SQLException {
							PublishInvoice inv = new PublishInvoice();
							Billing bill = new Billing();
							bill.setId(rs.getInt("billing_id"));
							Member biller = new Member();
							biller.setId(rs.getInt("biller_id"));
							Member member = new Member();
							member.setId(rs.getInt("member_id"));
							inv.setId(rs.getInt("id"));
							inv.setBilling(bill);
							inv.setBiller(biller);
							inv.setMember(member);
							inv.setInvoiceNumber(rs.getString("invoice_number"));
							inv.setAmount(rs.getBigDecimal("amount"));
							inv.setDescription(rs.getString("description"));
							inv.setPaymentCode(rs.getString("payment_code"));
							inv.setStatus(rs.getString("status"));
							inv.setCreatedDate(rs.getTimestamp("created_date"));
							return inv;
						}
					});
			return inv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<PublishInvoice> loadPublishInvoiceByBilling(int pageSize, int rowNum, int billerID, int billingID) {
		try {
			List<PublishInvoice> inv = this.jdbcTemplate.query(
					"SELECT id, billing_id, biller_id, member_id, invoice_number, payment_code, amount, description, status, created_date FROM invoice_published WHERE biller_id = ?  AND billingID = ? ORDER BY id DESC LIMIT ?,?;",
					new Object[] { billerID, pageSize, rowNum }, new RowMapper<PublishInvoice>() {
						public PublishInvoice mapRow(ResultSet rs, int arg1) throws SQLException {
							PublishInvoice inv = new PublishInvoice();
							Billing bill = new Billing();
							bill.setId(rs.getInt("billing_id"));
							Member biller = new Member();
							biller.setId(rs.getInt("biller_id"));
							Member member = new Member();
							member.setId(rs.getInt("member_id"));
							inv.setId(rs.getInt("id"));
							inv.setBilling(bill);
							inv.setBiller(biller);
							inv.setMember(member);
							inv.setInvoiceNumber(rs.getString("invoice_number"));
							inv.setAmount(rs.getBigDecimal("amount"));
							inv.setDescription(rs.getString("description"));
							inv.setPaymentCode(rs.getString("payment_code"));
							inv.setStatus(rs.getString("status"));
							inv.setCreatedDate(rs.getTimestamp("created_date"));
							return inv;
						}
					});
			return inv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public PublishInvoice getPublishInvoiceByInvoice(int invoiceID, int billerID) {
		try {
			PublishInvoice inv = this.jdbcTemplate.queryForObject(
					"SELECT id, billing_id, biller_id, member_id, invoice_number, payment_code, amount, description, status, created_date FROM invoice_published WHERE biller_id = ? AND invoice_id = ?;",
					new Object[] { billerID, invoiceID }, new RowMapper<PublishInvoice>() {
						public PublishInvoice mapRow(ResultSet rs, int arg1) throws SQLException {
							PublishInvoice inv = new PublishInvoice();
							Billing bill = new Billing();
							bill.setId(rs.getInt("billing_id"));
							Member biller = new Member();
							biller.setId(rs.getInt("biller_id"));
							Member member = new Member();
							member.setId(rs.getInt("member_id"));
							inv.setId(rs.getInt("id"));
							inv.setBilling(bill);
							inv.setBiller(biller);
							inv.setMember(member);
							inv.setInvoiceNumber(rs.getString("invoice_number"));
							inv.setAmount(rs.getBigDecimal("amount"));
							inv.setDescription(rs.getString("description"));
							inv.setPaymentCode(rs.getString("payment_code"));
							inv.setStatus(rs.getString("status"));
							inv.setCreatedDate(rs.getTimestamp("created_date"));
							return inv;
						}
					});
			return inv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<PublishInvoice> publishInvoiceBillingStatus(String start, String end, int currentPage, int pageSize,
			int billerID, String billingID, String status) {
		try {
			List<PublishInvoice> inv = this.jdbcTemplate.query(
					"SELECT id, billing_id, biller_id, member_id, invoice_number, payment_code, amount, description, status, created_date FROM invoice_published WHERE biller_id = ? AND billing_id = ? AND status = ? AND (DATE(created_date) BETWEEN ? AND ?) ORDER BY id DESC LIMIT ?,?;",
					new Object[] { billerID, billingID, status, start, end, currentPage, pageSize },
					new RowMapper<PublishInvoice>() {
						public PublishInvoice mapRow(ResultSet rs, int arg1) throws SQLException {
							PublishInvoice inv = new PublishInvoice();
							Billing bill = new Billing();
							bill.setId(rs.getInt("billing_id"));
							Member biller = new Member();
							biller.setId(rs.getInt("biller_id"));
							Member member = new Member();
							member.setId(rs.getInt("member_id"));
							inv.setId(rs.getInt("id"));
							inv.setBilling(bill);
							inv.setBiller(biller);
							inv.setMember(member);
							inv.setInvoiceNumber(rs.getString("invoice_number"));
							inv.setAmount(rs.getBigDecimal("amount"));
							inv.setDescription(rs.getString("description"));
							inv.setPaymentCode(rs.getString("payment_code"));
							inv.setStatus(rs.getString("status"));
							inv.setCreatedDate(rs.getTimestamp("created_date"));
							return inv;
						}
					});
			return inv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<PublishInvoice> publishInvoiceBilling(String start, String end, int currentPage, int pageSize,
			Integer billerID, String billingID) {
		try {
			List<PublishInvoice> inv = this.jdbcTemplate.query(
					"SELECT id, billing_id, biller_id, member_id, invoice_number, payment_code, amount, description, status, created_date FROM invoice_published WHERE biller_id = ? AND billing_id = ? AND (DATE(created_date) BETWEEN ? AND ?) ORDER BY id DESC LIMIT ?,?;",
					new Object[] { billerID, billingID, start, end, currentPage, pageSize },
					new RowMapper<PublishInvoice>() {
						public PublishInvoice mapRow(ResultSet rs, int arg1) throws SQLException {
							PublishInvoice inv = new PublishInvoice();
							Billing bill = new Billing();
							bill.setId(rs.getInt("billing_id"));
							Member biller = new Member();
							biller.setId(rs.getInt("biller_id"));
							Member member = new Member();
							member.setId(rs.getInt("member_id"));
							inv.setId(rs.getInt("id"));
							inv.setBilling(bill);
							inv.setBiller(biller);
							inv.setMember(member);
							inv.setInvoiceNumber(rs.getString("invoice_number"));
							inv.setAmount(rs.getBigDecimal("amount"));
							inv.setDescription(rs.getString("description"));
							inv.setPaymentCode(rs.getString("payment_code"));
							inv.setStatus(rs.getString("status"));
							inv.setCreatedDate(rs.getTimestamp("created_date"));
							return inv;
						}
					});
			return inv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Map<Integer, PublishInvoice> getPublishInvoiceInMap(List<Integer> ids) {
		String sql = "select * from invoice_published where invoice_id in (:invoiceID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("invoiceID", ids);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		Map<Integer, PublishInvoice> mapRet = template.query(sql, paramMap,
				new ResultSetExtractor<Map<Integer, PublishInvoice>>() {
					@Override
					public Map<Integer, PublishInvoice> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						HashMap<Integer, PublishInvoice> mapRet = new HashMap<Integer, PublishInvoice>();
						while (rs.next()) {
							PublishInvoice inv = new PublishInvoice();
							Billing bill = new Billing();
							bill.setId(rs.getInt("billing_id"));
							Member biller = new Member();
							biller.setId(rs.getInt("biller_id"));
							Member member = new Member();
							member.setId(rs.getInt("member_id"));
							inv.setId(rs.getInt("id"));
							inv.setInvoiceID(rs.getInt("invoice_id"));
							inv.setBilling(bill);
							inv.setBiller(biller);
							inv.setMember(member);
							inv.setInvoiceNumber(rs.getString("invoice_number"));
							inv.setAmount(rs.getBigDecimal("amount"));
							inv.setDescription(rs.getString("description"));
							inv.setPaymentCode(rs.getString("payment_code"));
							inv.setStatus(rs.getString("status"));
							inv.setCreatedDate(rs.getTimestamp("created_date"));
							mapRet.put(rs.getInt("invoice_id"), inv);
						}
						return mapRet;
					}
				});
		return mapRet;
	}

	public Map<Integer, PublishInvoice> getPublishInvoiceDateInMap(int month, List<Integer> ids) {
		String sql = "select * from invoice_published where invoice_id in (:invoiceID) and (MONTH(created_date) = "
				+ month + ");";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("invoiceID", ids);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		Map<Integer, PublishInvoice> mapRet = template.query(sql, paramMap,
				new ResultSetExtractor<Map<Integer, PublishInvoice>>() {
					@Override
					public Map<Integer, PublishInvoice> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						HashMap<Integer, PublishInvoice> mapRet = new HashMap<Integer, PublishInvoice>();
						while (rs.next()) {
							PublishInvoice inv = new PublishInvoice();
							Billing bill = new Billing();
							bill.setId(rs.getInt("billing_id"));
							Member biller = new Member();
							biller.setId(rs.getInt("biller_id"));
							Member member = new Member();
							member.setId(rs.getInt("member_id"));
							inv.setId(rs.getInt("id"));
							inv.setInvoiceID(rs.getInt("invoice_id"));
							inv.setBilling(bill);
							inv.setBiller(biller);
							inv.setMember(member);
							inv.setInvoiceNumber(rs.getString("invoice_number"));
							inv.setAmount(rs.getBigDecimal("amount"));
							inv.setDescription(rs.getString("description"));
							inv.setPaymentCode(rs.getString("payment_code"));
							inv.setStatus(rs.getString("status"));
							inv.setCreatedDate(rs.getTimestamp("created_date"));
							mapRet.put(rs.getInt("invoice_id"), inv);
						}
						return mapRet;
					}
				});
		return mapRet;
	}

	public Invoice getInvoiceByID(Object id, int billerID) {
		try {
			Invoice inv = this.jdbcTemplate.queryForObject(
					"SELECT id, billing_id, biller_id, member_id, invoice_number, amount, description, active, created_date FROM invoice WHERE biller_id = ? AND id = ?;",
					new Object[] { billerID, id }, new RowMapper<Invoice>() {
						public Invoice mapRow(ResultSet rs, int arg1) throws SQLException {
							Invoice inv = new Invoice();
							inv.setId(rs.getInt("id"));
							Billing bill = new Billing();
							bill.setId(rs.getInt("billing_id"));
							Member biller = new Member();
							biller.setId(rs.getInt("biller_id"));
							Member member = new Member();
							member.setId(rs.getInt("member_id"));
							inv.setBilling(bill);
							inv.setBiller(biller);
							inv.setMember(member);
							inv.setInvoiceNumber(rs.getString("invoice_number"));
							inv.setAmount(rs.getBigDecimal("amount"));
							inv.setDescription(rs.getString("description"));
							inv.setActive(rs.getBoolean("active"));
							inv.setCreatedDate(rs.getTimestamp("created_date"));
							return inv;
						}
					});
			return inv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Invoice getInvoiceByNo(String no, int billerID) {
		try {
			Invoice inv = this.jdbcTemplate.queryForObject(
					"SELECT id, billing_id, biller_id, member_id, invoice_number, amount, description, active, created_date FROM invoice WHERE biller_id = ? AND invoice_number = ?;",
					new Object[] { billerID, no }, new RowMapper<Invoice>() {
						public Invoice mapRow(ResultSet rs, int arg1) throws SQLException {
							Invoice inv = new Invoice();
							inv.setId(rs.getInt("id"));
							Billing bill = new Billing();
							bill.setId(rs.getInt("billing_id"));
							Member biller = new Member();
							biller.setId(rs.getInt("biller_id"));
							Member member = new Member();
							member.setId(rs.getInt("member_id"));
							inv.setBilling(bill);
							inv.setBiller(biller);
							inv.setMember(member);
							inv.setInvoiceNumber(rs.getString("invoice_number"));
							inv.setAmount(rs.getBigDecimal("amount"));
							inv.setDescription(rs.getString("description"));
							inv.setActive(rs.getBoolean("active"));
							inv.setCreatedDate(rs.getTimestamp("created_date"));
							return inv;
						}
					});
			return inv;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void createInvoice(Invoice inv, int id) {
		jdbcTemplate.update(
				"insert into invoice (billing_id, biller_id, member_id, invoice_number, amount, description) values (?, ?, ?, ?, ?, ?)",
				inv.getBilling().getId(), inv.getBiller().getId(), inv.getMember().getId(), inv.getInvoiceNumber(),
				inv.getAmount(), inv.getDescription());
	}

	public void createBatchInvoice(int billingID, int billerID, String invoiceNo, String amount, String desc,
			String[] memberID) {
		jdbcTemplate.batchUpdate(
				"insert into invoice (billing_id, biller_id, member_id, invoice_number, amount, description) values (?, ?, ?, ?, ?, ?)",
				new BatchPreparedStatementSetter() {

					@Override
					public int getBatchSize() {
						return memberID.length;
					}

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setLong(1, billingID);
						ps.setLong(2, billerID);
						ps.setLong(3, Long.parseLong(memberID[i]));
						ps.setString(4, invoiceNo);
						ps.setBigDecimal(5, new BigDecimal(amount));
						ps.setString(6, desc);
					}
				});
	}

	public void updateInvoice(BigDecimal amount, Boolean active, String id) {
		jdbcTemplate.update("update invoice set amount = ?, active = ? where id = ?", amount, active, id);
	}

	public void deleteInvoice(String id) {
		jdbcTemplate.update("delete from invoice where id = ?", id);
	}

	public Integer countMemberInvoice(int billerID, int memberID) {
		Integer count = this.jdbcTemplate.queryForObject(
				"SELECT COUNT(id) from invoice WHERE biller_id = ? AND member_id = ?;",
				new Object[] { billerID, memberID }, Integer.class);
		return count;
	}

	public Integer totalInvoice(int billerID) {
		Integer count = this.jdbcTemplate.queryForObject("SELECT COUNT(id) from invoice WHERE biller_id = ?;",
				new Object[] { billerID }, Integer.class);
		return count;
	}

	public Integer totalPublishInvoice(int billerID) {
		Integer count = this.jdbcTemplate.queryForObject("SELECT COUNT(id) from invoice_published WHERE biller_id = ?;",
				new Object[] { billerID }, Integer.class);
		return count;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
