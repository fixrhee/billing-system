package org.billing.api.processor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.billing.api.data.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class MemberRepository {

	private JdbcTemplate jdbcTemplate;

	public Member validateAccess(String username, String secret) {
		try {
			Member member = this.jdbcTemplate.queryForObject(
					"SELECT id, username, name, email, password, msisdn, address, id_card, active, created_date FROM member WHERE username = ? AND password = MD5(?);",
					new Object[] { username, secret }, new RowMapper<Member>() {
						public Member mapRow(ResultSet rs, int arg1) throws SQLException {
							Member member = new Member();
							member.setId(rs.getInt("id"));
							member.setName(rs.getString("name"));
							member.setEmail(rs.getString("email"));
							member.setUsername(rs.getString("username"));
							member.setPassword(rs.getString("password"));
							member.setMsisdn(rs.getString("msisdn"));
							member.setAddress(rs.getString("address"));
							member.setIdCard(rs.getString("id_card"));
							member.setActive(rs.getBoolean("active"));
							member.setCreatedDate(rs.getTimestamp("created_date"));
							return member;
						}
					});
			return member;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Member getMemberByID(Object id) {
		try {
			Member member = this.jdbcTemplate.queryForObject(
					"SELECT id, username, name, email, password, msisdn, address, id_card, active, created_date FROM member WHERE id = ?;",
					new Object[] { id }, new RowMapper<Member>() {
						public Member mapRow(ResultSet rs, int arg1) throws SQLException {
							Member member = new Member();
							member.setId(rs.getInt("id"));
							member.setName(rs.getString("name"));
							member.setEmail(rs.getString("email"));
							member.setUsername(rs.getString("username"));
							member.setPassword(rs.getString("password"));
							member.setMsisdn(rs.getString("msisdn"));
							member.setAddress(rs.getString("address"));
							member.setIdCard(rs.getString("id_card"));
							member.setActive(rs.getBoolean("active"));
							member.setCreatedDate(rs.getTimestamp("created_date"));
							return member;
						}
					});
			return member;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Member getMemberByUsername(String username) {
		try {
			Member member = this.jdbcTemplate.queryForObject(
					"SELECT id, username, name, email, password, msisdn, address, id_card, active, created_date FROM member WHERE username = ?;",
					new Object[] { username }, new RowMapper<Member>() {
						public Member mapRow(ResultSet rs, int arg1) throws SQLException {
							Member member = new Member();
							member.setId(rs.getInt("id"));
							member.setName(rs.getString("name"));
							member.setEmail(rs.getString("email"));
							member.setUsername(rs.getString("username"));
							member.setPassword(rs.getString("password"));
							member.setAddress(rs.getString("address"));
							member.setIdCard(rs.getString("id_card"));
							member.setMsisdn(rs.getString("msisdn"));
							member.setActive(rs.getBoolean("active"));
							member.setCreatedDate(rs.getTimestamp("created_date"));
							return member;
						}
					});
			return member;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Integer> getAllMember(int pageSize, int rowNum, int id) {
		try {
			List<Integer> mid = this.jdbcTemplate.query(
					"SELECT member_id FROM membership WHERE parent_id = ? ORDER BY id DESC LIMIT ?,?;",
					new Object[] { id, pageSize, rowNum }, new RowMapper<Integer>() {
						public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
							return rs.getInt("member_id");
						}
					});
			return mid;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Map<Integer, Member> getMemberInMap(List<Integer> ids) {
		String sql = "select * from member where id in (:memberID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("memberID", ids);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		Map<Integer, Member> mapRet = template.query(sql, paramMap, new ResultSetExtractor<Map<Integer, Member>>() {
			@Override
			public Map<Integer, Member> extractData(ResultSet rs) throws SQLException, DataAccessException {
				HashMap<Integer, Member> mapRet = new HashMap<Integer, Member>();
				while (rs.next()) {
					Member member = new Member();
					member.setId(rs.getInt("id"));
					member.setName(rs.getString("name"));
					member.setEmail(rs.getString("email"));
					member.setUsername(rs.getString("username"));
					member.setPassword(rs.getString("password"));
					member.setMsisdn(rs.getString("msisdn"));
					member.setAddress(rs.getString("address"));
					member.setIdCard(rs.getString("id_card"));
					member.setActive(rs.getBoolean("active"));
					member.setCreatedDate(rs.getTimestamp("created_date"));
					mapRet.put(rs.getInt("id"), member);
				}
				return mapRet;
			}
		});
		return mapRet;
	}

	public List<Member> getMemberInList(List<Integer> ids) {
		String sql = "select * from member where id in (:memberID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("memberID", ids);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		List<Member> mapRet = template.query(sql, paramMap, new ResultSetExtractor<List<Member>>() {
			@Override
			public List<Member> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Member> listRet = new LinkedList<Member>();
				while (rs.next()) {
					Member member = new Member();
					member.setId(rs.getInt("id"));
					member.setName(rs.getString("name"));
					member.setEmail(rs.getString("email"));
					member.setUsername(rs.getString("username"));
					member.setPassword(rs.getString("password"));
					member.setMsisdn(rs.getString("msisdn"));
					member.setAddress(rs.getString("address"));
					member.setIdCard(rs.getString("id_card"));
					member.setActive(rs.getBoolean("active"));
					member.setCreatedDate(rs.getTimestamp("created_date"));
					listRet.add(member);
				}
				return listRet;
			}
		});
		return mapRet;
	}

	public Integer createMember(Member member) {
		String sql = "insert into member (group_id, username, name, email, password, msisdn, address, id_card) values (?, ?, ?, ?, MD5(?), ?, ?, ?)";
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, member.getGroup().getId());
				ps.setString(2, member.getUsername());
				ps.setString(3, member.getName());
				ps.setString(4, member.getEmail());
				ps.setString(5, member.getPassword());
				ps.setString(6, member.getMsisdn());
				ps.setString(7, member.getAddress());
				ps.setString(8, member.getIdCard());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	public void createMembership(int memberID, int billerID, int sequence) {
		jdbcTemplate.update("insert into membership (parent_id, member_id, sequence) values (?, ?, ?)", billerID,
				memberID, sequence);
	}

	public void updateMember(String id, Member member) {
		jdbcTemplate.update(
				"update member set name = ?, email = ?, password = MD5(?), msisdn = ?, address = ?, id_card = ? where id = ?",
				member.getName(), member.getEmail(), member.getPassword(), member.getMsisdn(), member.getAddress(),
				member.getIdCard(), id);
	}

	public void deleteMembership(String memberID, int billerID) {
		jdbcTemplate.update("delete from membership  where member_id = ? and parent_id = ?", memberID, billerID);
	}

	public Integer getMembershipID(Object memberID, int billerID) {
		try {
			Integer id = this.jdbcTemplate.queryForObject(
					"SELECT id from membership WHERE member_id = ? AND parent_id = ?;",
					new Object[] { memberID, billerID }, Integer.class);
			return id;
		} catch (EmptyResultDataAccessException ex) {
			return 0;
		}
	}

	public Integer totalMember(int billerID) {
		Integer count = this.jdbcTemplate.queryForObject("SELECT COUNT(id) from membership WHERE parent_id = ?;",
				new Object[] { billerID }, Integer.class);
		return count;
	}

	public Integer countMembership(int memberID) {
		Integer count = this.jdbcTemplate.queryForObject("SELECT COUNT(id) from membership WHERE member_id = ?;",
				new Object[] { memberID }, Integer.class);
		return count;
	}

	public Integer getMembershipSequence(int memberID, int billerID) {
		Integer seq = this.jdbcTemplate.queryForObject(
				"SELECT sequence from membership WHERE member_id = ? AND parent_id = ?;",
				new Object[] { memberID, billerID }, Integer.class);
		return seq;
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
