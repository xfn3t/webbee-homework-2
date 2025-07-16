package ru.homework.deal.domain.service;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.homework.deal.domain.dto.DealSearchRequest;
import ru.homework.deal.domain.model.Deal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class DealSpecification {

	private DealSpecification() { }

	public static Specification<Deal> byCriteria(DealSearchRequest req) {
		return (root, query, cb) -> {
			// включаем distinct один раз
			Objects.requireNonNull(query).distinct(true);

			List<Predicate> predicates = new ArrayList<>();
			// isActive = true
			predicates.add(cb.isTrue(root.get("isActive")));

			// фильтры по запросу
			if (req.getDealId() != null) {
				predicates.add(cb.equal(root.get("id"), UUID.fromString(req.getDealId())));
			}
			if (req.getDescription() != null) {
				predicates.add(cb.like(root.get("description"), req.getDescription()));
			}
			if (req.getAgreementNumber() != null) {
				predicates.add(cb.like(root.get("agreementNumber"), "%" + req.getAgreementNumber() + "%"));
			}
			if (req.getAgreementDateFrom() != null || req.getAgreementDateTo() != null) {
				LocalDate from = req.getAgreementDateFrom();
				LocalDate to   = req.getAgreementDateTo();
				if (from != null && to != null) {
					predicates.add(cb.between(root.get("agreementDate"), from, to));
				} else if (from != null) {
					predicates.add(cb.greaterThanOrEqualTo(root.get("agreementDate"), from));
				} else {
					predicates.add(cb.lessThanOrEqualTo(root.get("agreementDate"), to));
				}
			}
			if (req.getAvailabilityDateFrom() != null || req.getAvailabilityDateTo() != null) {
				LocalDate from = req.getAvailabilityDateFrom();
				LocalDate to   = req.getAvailabilityDateTo();
				if (from != null && to != null) {
					predicates.add(cb.between(root.get("availabilityDate"), from, to));
				} else if (from != null) {
					predicates.add(cb.greaterThanOrEqualTo(root.get("availabilityDate"), from));
				} else {
					predicates.add(cb.lessThanOrEqualTo(root.get("availabilityDate"), to));
				}
			}
			if (req.getTypes() != null && !req.getTypes().isEmpty()) {
				predicates.add(root.get("type").as(String.class).in(req.getTypes()));
			}
			if (req.getStatuses() != null && !req.getStatuses().isEmpty()) {
				predicates.add(root.get("status").as(String.class).in(req.getStatuses()));
			}
			if (req.getCloseDtFrom() != null || req.getCloseDtTo() != null) {
				LocalDateTime from = req.getCloseDtFrom();
				LocalDateTime to   = req.getCloseDtTo();
				if (from != null && to != null) {
					predicates.add(cb.between(root.get("closeDt"), from, to));
				} else if (from != null) {
					predicates.add(cb.greaterThanOrEqualTo(root.get("closeDt"), from));
				} else {
					predicates.add(cb.lessThanOrEqualTo(root.get("closeDt"), to));
				}
			}
			if (req.getBorrowerSearch() != null) {
				var joinC = root.join("contractors", JoinType.LEFT);
				var joinR = joinC.join("roles", JoinType.LEFT);
				Predicate byCategory = cb.equal(joinR.get("category"), "BORROWER");
				Predicate byAny = cb.or(
						cb.like(joinC.get("contractorId"), "%" + req.getBorrowerSearch() + "%"),
						cb.like(joinC.get("name"),         "%" + req.getBorrowerSearch() + "%"),
						cb.like(joinC.get("inn"),          "%" + req.getBorrowerSearch() + "%")
				);
				predicates.add(cb.and(byCategory, byAny));
			}
			if (req.getWarrantySearch() != null) {
				var joinC = root.join("contractors", JoinType.LEFT);
				var joinR = joinC.join("roles", JoinType.LEFT);
				Predicate byCategory = cb.equal(joinR.get("category"), "WARRANTY");
				Predicate byAny = cb.or(
						cb.like(joinC.get("contractorId"), "%" + req.getWarrantySearch() + "%"),
						cb.like(joinC.get("name"),         "%" + req.getWarrantySearch() + "%"),
						cb.like(joinC.get("inn"),          "%" + req.getWarrantySearch() + "%")
				);
				predicates.add(cb.and(byCategory, byAny));
			}
			if (req.getSum() != null) {
				var joinS = root.join("sums", JoinType.LEFT);
				// только главная сумма
				predicates.add(cb.isTrue(joinS.get("isMain")));
				BigDecimal val = req.getSum().getValue();
				String curr   = req.getSum().getCurrency();
				if (val != null) {
					predicates.add(cb.equal(joinS.get("sum"), val));
				}
				if (curr != null) {
					predicates.add(cb.equal(joinS.get("currency"), curr));
				}
			}

			// собираем все в одно AND‑условие
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}

	public static Specification<Deal> isActive() {
		return (root, query, cb) -> cb.isTrue(root.get("isActive"));
	}

	public static Specification<Deal> hasId(String id) {
		return (root, query, cb) -> cb.equal(root.get("id"), UUID.fromString(id));
	}

	public static Specification<Deal> hasDescription(String desc) {
		return (root, query, cb) -> cb.equal(root.get("description"), desc);
	}

	public static Specification<Deal> likeAgreementNumber(String num) {
		return (root, query, cb) -> cb.like(root.get("agreementNumber"), "%" + num + "%");
	}

	public static Specification<Deal> betweenAgreementDate(LocalDate from, LocalDate to) {
		return (root, query, cb) -> {
			if (from != null && to != null) {
				return cb.between(root.get("agreementDate"), from, to);
			} else if (from != null) {
				return cb.greaterThanOrEqualTo(root.get("agreementDate"), from);
			} else {
				return cb.lessThanOrEqualTo(root.get("agreementDate"), to);
			}
		};
	}

	public static Specification<Deal> betweenAvailabilityDate(LocalDate from, LocalDate to) {
		return (root, query, cb) -> {
			if (from != null && to != null) {
				return cb.between(root.get("availabilityDate"), from, to);
			} else if (from != null) {
				return cb.greaterThanOrEqualTo(root.get("availabilityDate"), from);
			} else {
				return cb.lessThanOrEqualTo(root.get("availabilityDate"), to);
			}
		};
	}

	public static Specification<Deal> typeIn(java.util.List<String> types) {
		return (root, query, cb) -> root.get("type").in(types);
	}

	public static Specification<Deal> statusIn(java.util.List<String> statuses) {
		return (root, query, cb) -> root.get("status").in(statuses);
	}

	public static Specification<Deal> betweenCloseDt(LocalDateTime from, LocalDateTime to) {
		return (root, query, cb) -> {
			if (from != null && to != null) {
				return cb.between(root.get("closeDt"), from, to);
			} else if (from != null) {
				return cb.greaterThanOrEqualTo(root.get("closeDt"), from);
			} else {
				return cb.lessThanOrEqualTo(root.get("closeDt"), to);
			}
		};
	}

	private static Specification<Deal> hasContractorLike(String roleCategory, String keyword) {
		return (root, query, cb) -> {
			var joinC = root.join("contractors", JoinType.LEFT);
			var joinR = joinC.join("roles", JoinType.LEFT);
			var byCategory = cb.equal(joinR.get("category"), roleCategory);
			var byId       = cb.like(joinC.get("contractorId"), "%" + keyword + "%");
			var byName     = cb.like(joinC.get("name"),         "%" + keyword + "%");
			var byInn      = cb.like(joinC.get("inn"),          "%" + keyword + "%");
			return cb.and(byCategory, cb.or(byId, byName, byInn));
		};
	}

	private static Specification<Deal> hasMainSum(BigDecimal value, String currency) {
		return (root, query, cb) -> {
			var joinS = root.join("sums", JoinType.LEFT);
			var conj = cb.conjunction();
			conj.getExpressions().add(cb.isTrue(joinS.get("isMain")));
			if (value != null) {
				conj.getExpressions().add(cb.equal(joinS.get("sum"), value));
			}
			if (currency != null) {
				conj.getExpressions().add(cb.equal(joinS.get("currency"), currency));
			}
			return conj;
		};
	}
}