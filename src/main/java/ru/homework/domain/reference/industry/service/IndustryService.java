package ru.homework.domain.reference.industry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework.domain.reference.industry.dto.IndustryDto;
import ru.homework.domain.reference.industry.mapper.IndustryMapper;
import ru.homework.domain.reference.industry.model.Industry;
import ru.homework.domain.reference.industry.repository.IndustryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndustryService {
	private final IndustryRepository industryRepository;
	private final IndustryMapper industryMapper;

	public List<IndustryDto> getAllActiveIndustries() {
		return industryRepository.findByIsActiveTrue().stream()
				.map(industryMapper::toDto)
				.collect(Collectors.toList());
	}

	public Optional<IndustryDto> getIndustryById(Integer id) {
		return industryRepository.findById(id)
				.map(industryMapper::toDto);
	}

	public IndustryDto saveIndustry(IndustryDto dto) {
		Industry industry = industryMapper.toEntity(dto);
		industry.setActive(true);

		Industry saved = industryRepository.save(industry);
		return industryMapper.toDto(saved);
	}

	public void deactivateIndustry(Integer id) {
		industryRepository.updateActiveStatus(id, false);
	}

	public List<Industry> findByIsActiveTrue() {
		return industryRepository.findByIsActiveTrue();
	}
}
