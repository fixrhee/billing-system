package org.billing.api.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.billing.api.data.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class TransferTypeRepository {
	private JdbcTemplate jdbcTemplate;

	public Map<Integer, TransferType> getTransferTypeInMap(List<Integer> ids) {
		String sql = "select * from transfer_type where id in (:trfTypeID)";
		Map<String, List<Integer>> paramMap = Collections.singletonMap("trfTypeID", ids);
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(this.jdbcTemplate.getDataSource());
		Map<Integer, TransferType> mapRet = template.query(sql, paramMap,
				new ResultSetExtractor<Map<Integer, TransferType>>() {
					@Override
					public Map<Integer, TransferType> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						HashMap<Integer, TransferType> mapRet = new HashMap<Integer, TransferType>();
						while (rs.next()) {
							TransferType trfType = new TransferType();
							trfType.setId(rs.getInt("id"));
							trfType.setName(rs.getString("name"));
							mapRet.put(rs.getInt("id"), trfType);
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
