package edu.lk.ijse.bo.custom;

import edu.lk.ijse.bo.SuperBO;
import edu.lk.ijse.dto.InstructorDto;
import java.util.List;

public interface InstructorBo extends SuperBO {
    void addInstructor(InstructorDto instructorDto);
    InstructorDto getInstructorById(Integer id);
    List<InstructorDto> getAllInstructors();
    void updateInstructor(InstructorDto instructorDto);
    void deleteInstructor(Integer id);
    List<InstructorDto> getInstructorsBySpecialization(String specialization);
}