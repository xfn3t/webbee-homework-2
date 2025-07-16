package ru.homework.domain.reference.orgform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.homework.domain.reference.orgform.dto.OrgFormDto;
import ru.homework.domain.reference.orgform.mapper.OrgFormMapper;
import ru.homework.domain.reference.orgform.model.OrgForm;
import ru.homework.domain.reference.orgform.repository.OrgFormRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrgFormService {
	private final OrgFormRepository orgFormRepository;
	private final OrgFormMapper orgFormMapper;

	public List<OrgFormDto> getAllActiveOrgForms() {
		return orgFormRepository.findByIsActiveTrue().stream()
				.map(orgFormMapper::toDto)
				.collect(Collectors.toList());
	}

	public Optional<OrgFormDto> getOrgFormById(Integer id) {
		return orgFormRepository.findById(id)
				.map(orgFormMapper::toDto);
	}

	public OrgFormDto saveOrgForm(OrgFormDto dto) {
		OrgForm orgForm = orgFormMapper.toEntity(dto);
		orgForm.setActive(true);
		OrgForm saved = orgFormRepository.save(orgForm);
		return orgFormMapper.toDto(saved);
	}

	public void deactivateOrgForm(Integer id) {
		orgFormRepository.updateActiveStatus(id, false);
	}
}