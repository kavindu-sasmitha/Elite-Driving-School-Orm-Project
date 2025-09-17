package edu.lk.ijse.bo.custom.impl;

import edu.lk.ijse.bo.custom.InstructorBo;
import edu.lk.ijse.bo.util.EntityDtoConvertor;
import edu.lk.ijse.dao.custom.InstructorDao;
import edu.lk.ijse.dao.custom.impl.InstructorDaoImpl;
import edu.lk.ijse.dto.InstructorDto;
import edu.lk.ijse.entity.Instructor;

import java.util.List;
import java.util.stream.Collectors;

public class InstructorBoImpl implements InstructorBo {

    private final InstructorDao instructorDao = new InstructorDaoImpl();
    private final EntityDtoConvertor convertor = new EntityDtoConvertor();

    @Override
    public void addInstructor(InstructorDto instructorDto) {
        instructorDao.save(convertor.mapToEntity(instructorDto));
    }

    @Override
    public InstructorDto getInstructorById(Integer id) {
        return instructorDao.findById(id).map(convertor::mapToDto).orElse(null);
    }

    @Override
    public List<InstructorDto> getAllInstructors() {
        return instructorDao.findAll().stream().map(convertor::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void updateInstructor(InstructorDto instructorDto) {
        Instructor instructor = convertor.mapToEntity(instructorDto);
        instructor.setInstructorId(instructorDto.getInstructorId());
        instructorDao.update(instructor);
    }

    @Override
    public void deleteInstructor(Integer id) {
        instructorDao.findById(id).ifPresent(instructorDao::delete);
    }

    @Override
    public List<InstructorDto> getInstructorsBySpecialization(String specialization) {
        return instructorDao.findAll().stream()
                .filter(i -> specialization.equals(i.getSpecialization()))
                .map(convertor::mapToDto)
                .collect(Collectors.toList());
    }
}