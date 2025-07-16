package ru.homework.domain.contractor.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.homework.domain.contractor.dto.ContractorFilter;
import ru.homework.domain.contractor.model.Contractor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContractorRepository {

	private final JdbcTemplate jdbcTemplate;

	public void save(Contractor contractor) {
		if (contractor.getId() != null && existsById(contractor.getId())) {
			update(contractor);
		} else {
			insert(contractor);
		}
	}

	public boolean existsById(String id) {
		String sql = "SELECT COUNT(*) FROM contractor WHERE id = ?";
		Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
		return count > 0;
	}

	private void insert(Contractor contractor) {
		String sql = "INSERT INTO contractor (id, parent_id, name, name_full, inn, ogrn, country, industry, org_form, " +
				"create_date, modify_date, create_user_id, modify_user_id, is_active) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, contractor.getId());
			ps.setString(2, contractor.getParentId());
			ps.setString(3, contractor.getName());
			ps.setString(4, contractor.getNameFull());
			ps.setString(5, contractor.getInn());
			ps.setString(6, contractor.getOgrn());
			ps.setString(7, contractor.getCountry());
			ps.setObject(8, contractor.getIndustry());
			ps.setObject(9, contractor.getOrgForm());
			ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
			ps.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
			ps.setString(12, "system");
			ps.setString(13, "system");
			ps.setBoolean(14, contractor.isActive());
			return ps;
		});
	}

	private void update(Contractor contractor) {
		String sql = "UPDATE contractor SET parent_id = ?, name = ?, name_full = ?, inn = ?, ogrn = ?, " +
				"country = ?, industry = ?, org_form = ?, modify_date = ?, modify_user_id = ? " +
				"WHERE id = ?";

		jdbcTemplate.update(sql,
				contractor.getParentId(),
				contractor.getName(),
				contractor.getNameFull(),
				contractor.getInn(),
				contractor.getOgrn(),
				contractor.getCountry(),
				contractor.getIndustry(),
				contractor.getOrgForm(),
				Timestamp.valueOf(LocalDateTime.now()),
				"system",
				contractor.getId()
		);
	}

	public Optional<Contractor> findById(String id) {
		String sql = "SELECT * FROM contractor WHERE id = ? AND is_active = true";
		try {
			return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
				Contractor c = new Contractor();
				c.setId(rs.getString("id"));
				c.setParentId(rs.getString("parent_id"));
				c.setName(rs.getString("name"));
				c.setNameFull(rs.getString("name_full"));
				c.setInn(rs.getString("inn"));
				c.setOgrn(rs.getString("ogrn"));
				c.setCountry(rs.getString("country"));
				c.setIndustry(rs.getObject("industry", Integer.class));
				c.setOrgForm(rs.getObject("org_form", Integer.class));
				c.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime());
				c.setModifyDate(rs.getTimestamp("modify_date").toLocalDateTime());
				c.setCreateUserId(rs.getString("create_user_id"));
				c.setModifyUserId(rs.getString("modify_user_id"));
				c.setActive(rs.getBoolean("is_active"));
				return c;
			}, id));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public void deactivateById(String id) {
		String sql = "UPDATE contractor SET is_active = false WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

	public List<Contractor> search(ContractorFilter filter, int limit, int offset) {
		StringBuilder sqlBuilder = new StringBuilder("""
            SELECT c.id, c.parent_id, c.name, c.name_full, c.inn, c.ogrn,
                   c.country, c.industry, c.org_form, c.is_active
            FROM contractor c
            LEFT JOIN country ct ON c.country = ct.id
            LEFT JOIN org_form of ON c.org_form = of.id
            WHERE c.is_active = true
            """);

		List<Object> params = buildWhereClause(filter, sqlBuilder);

		sqlBuilder.append(" LIMIT ? OFFSET ?");
		params.add(limit);
		params.add(offset);

		return jdbcTemplate.query(
				sqlBuilder.toString(),
				this::mapToContractor,
				params.toArray()
		);
	}

	private Contractor mapToContractor(ResultSet rs, int rowNum) throws SQLException {
		Contractor contractor = new Contractor();
		contractor.setId(rs.getString("id"));
		contractor.setParentId(rs.getString("parent_id"));
		contractor.setName(rs.getString("name"));
		contractor.setNameFull(rs.getString("name_full"));
		contractor.setInn(rs.getString("inn"));
		contractor.setOgrn(rs.getString("ogrn"));
		contractor.setCountry(rs.getString("country"));
		contractor.setIndustry(rs.getObject("industry", Integer.class));
		contractor.setOrgForm(rs.getObject("org_form", Integer.class));
		contractor.setActive(rs.getBoolean("is_active"));
		return contractor;
	}

	private List<Object> buildWhereClause(
			ContractorFilter filter,
			StringBuilder sqlBuilder
	) {

		List<Object> params = new ArrayList<>();

		final String contractorId = filter.getContractorId();
		final String parentId = filter.getParentId();
		final String searchTerm = filter.getSearchTerm();
		final String country = filter.getCountry();
		final Integer industry = filter.getIndustry();
		final String orgForm = filter.getOrgForm();

		if (contractorId != null) {
			sqlBuilder.append(" AND c.id = ?");
			params.add(contractorId);
		}

		if (parentId != null) {
			sqlBuilder.append(" AND c.parent_id = ?");
			params.add(parentId);
		}

		if (searchTerm != null) {
			sqlBuilder.append(" AND (c.name ILIKE ? OR c.name_full ILIKE ? OR c.inn ILIKE ? OR c.ogrn ILIKE ?)");
			String likeTerm = "%" + searchTerm + "%";
			params.add(likeTerm);
			params.add(likeTerm);
			params.add(likeTerm);
			params.add(likeTerm);
		}

		if (country != null) {
			sqlBuilder.append(" AND ct.name ILIKE ?");
			params.add("%" + country + "%");
		}

		if (industry != null) {
			sqlBuilder.append(" AND c.industry = ?");
			params.add(industry);
		}

		if (orgForm != null) {
			sqlBuilder.append(" AND of.name ILIKE ?");
			params.add("%" + orgForm + "%");
		}

		return params;
	}


	public int countSearchResults(ContractorFilter filter) {
		StringBuilder sqlBuilder = new StringBuilder(
				"SELECT COUNT(*) " +
						"FROM contractor c " +
						"LEFT JOIN country ct ON c.country = ct.id " +
						"LEFT JOIN org_form of ON c.org_form = of.id " +
						"WHERE c.is_active = true"
		);

		List<Object> params = buildWhereClause(filter, sqlBuilder);

		return jdbcTemplate.queryForObject(
				sqlBuilder.toString(),
				Integer.class,
				params.toArray()
		);
	}

	public void deleteById(String id) {
		String sql = "DELETE FROM contractor WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

	public void deleteAll() {
		String sql = "DELETE FROM contractor";
		jdbcTemplate.update(sql);
	}
}