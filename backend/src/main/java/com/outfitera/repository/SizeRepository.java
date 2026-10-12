package com.outfitera.repository;

import com.outfitera.entity.Size;
import com.outfitera.enums.SizeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

    Optional<Size> findByNameAndSizeType(String name, SizeType sizeType);

    List<Size> findAllByOrderBySizeTypeAscNameAsc();
}
