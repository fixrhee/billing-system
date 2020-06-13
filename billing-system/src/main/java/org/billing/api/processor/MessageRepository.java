package org.billing.api.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.billing.api.data.Member;
import org.billing.api.data.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class MessageRepository {

	private JdbcTemplate jdbcTemplate;

	public List<Message> getAllMessage(int pageSize, int rowNum, int id) {
		try {
			List<Message> msg = this.jdbcTemplate.query(
					"SELECT m.id, m.from_member_id, m.to_member_id, m.subject, m.body, m.read, m.tag, m.archive, m.created_date, m.modified_date FROM message m WHERE m.to_member_id = ? AND m.archive = false ORDER BY m.id DESC LIMIT ?,?;",
					new Object[] { id, pageSize, rowNum }, new RowMapper<Message>() {
						public Message mapRow(ResultSet rs, int arg1) throws SQLException {
							Message msg = new Message();
							Member from = new Member();
							from.setId(rs.getInt("from_member_id"));
							Member to = new Member();
							to.setId(rs.getInt("to_member_id"));
							msg.setFromMember(from);
							msg.setToMember(to);
							msg.setId(rs.getInt("id"));
							msg.setArchive(rs.getBoolean("archive"));
							msg.setBody(rs.getString("body"));
							msg.setRead(rs.getBoolean("read"));
							msg.setTag(rs.getString("tag"));
							msg.setCreatedDate(rs.getTimestamp("created_date"));
							msg.setModifiedDate(rs.getTimestamp("modified_date"));
							return msg;
						}
					});
			return msg;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Message getMessageByID(Object id) {
		try {
			Message msg = this.jdbcTemplate.queryForObject(
					"SELECT m.id, m.from_member_id, m.to_member_id, m.subject, m.body, m.read, m.tag, m.archive, m.created_date, m.modified_date FROM message m WHERE m.id = ?",
					new Object[] { id }, new RowMapper<Message>() {
						public Message mapRow(ResultSet rs, int arg1) throws SQLException {
							Message msg = new Message();
							Member from = new Member();
							from.setId(rs.getInt("from_member_id"));
							Member to = new Member();
							to.setId(rs.getInt("to_member_id"));
							msg.setFromMember(from);
							msg.setToMember(to);
							msg.setId(rs.getInt("id"));
							msg.setArchive(rs.getBoolean("archive"));
							msg.setBody(rs.getString("body"));
							msg.setRead(rs.getBoolean("read"));
							msg.setTag(rs.getString("tag"));
							msg.setCreatedDate(rs.getTimestamp("created_date"));
							msg.setModifiedDate(rs.getTimestamp("modified_date"));
							return msg;
						}
					});
			return msg;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public void readMessage(String id) {
		jdbcTemplate.update("update message set read = true where id = ?;", id);
	}

	public void createMessage(int from, int to, String subject, String body) {
		jdbcTemplate.update("insert into message (from_member_id, to_member_id, subject, body) values (?, ?, ?, ?)",
				from, to, subject, body);
	}

	public void deleteMessage(String id) {
		jdbcTemplate.update("update message set archive = true where id = ?;", id);
	}

	public Integer totalMessage(int memberID) {
		Integer count = this.jdbcTemplate.queryForObject("SELECT COUNT(id) from message WHERE to_member_id = ?;",
				new Object[] { memberID }, Integer.class);
		return count;
	}

	public Integer totalUnreadMessage(int memberID) {
		Integer count = this.jdbcTemplate.queryForObject(
				"SELECT COUNT(id) from message WHERE to_member_id = ? and read = false;", new Object[] { memberID },
				Integer.class);
		return count;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
