package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.data.po.Honor;
import org.fatmansoft.teach.data.po.LeaveInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface HonorRepository extends JpaRepository<Honor,Integer> {
    List<Honor> findAll();
    Optional<Honor> findHonorByStudentStudentId(Integer studentId);
    @Query(value = "from Honor where ?1=''")
    List<Honor> findHonorListByStudentId(String StudentId);

}
